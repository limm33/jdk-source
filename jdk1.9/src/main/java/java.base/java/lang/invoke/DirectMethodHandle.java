/*
 * Copyright (c) 2008, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang.invoke;

import jdk.internal.misc.Unsafe;
import jdk.internal.vm.annotation.ForceInline;
import jdk.internal.vm.annotation.Stable;
import sun.invoke.util.ValueConversions;
import sun.invoke.util.VerifyAccess;
import sun.invoke.util.VerifyType;
import sun.invoke.util.Wrapper;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.invoke.LambdaForm.*;
import static java.lang.invoke.LambdaForm.Kind.*;
import static java.lang.invoke.MethodHandleNatives.Constants.*;
import static java.lang.invoke.MethodHandleStatics.UNSAFE;
import static java.lang.invoke.MethodHandleStatics.newInternalError;
import static java.lang.invoke.MethodTypeForm.*;

/**
 * The flavor of method handle which implements a constant reference
 * to a class member.
 * @author jrose
 */
class DirectMethodHandle extends MethodHandle {
    final MemberName member;

    // Constructors and factory methods in this class *must* be package scoped or private.
    private DirectMethodHandle(MethodType mtype, LambdaForm form, MemberName member) {
        super(mtype, form);
        if (!member.isResolved())  throw new InternalError();

        if (member.getDeclaringClass().isInterface() &&
                member.isMethod() && !member.isAbstract()) {
            // Check for corner case: invokeinterface of Object method
            MemberName m = new MemberName(Object.class, member.getName(), member.getMethodType(), member.getReferenceKind());
            m = MemberName.getFactory().resolveOrNull(m.getReferenceKind(), m, null);
            if (m != null && m.isPublic()) {
                assert(member.getReferenceKind() == m.getReferenceKind());  // else this.form is wrong
                member = m;
            }
        }

        this.member = member;
    }

    // Factory methods:
    static DirectMethodHandle make(byte refKind, Class<?> receiver, MemberName member) {
        MethodType mtype = member.getMethodOrFieldType();
        if (!member.isStatic()) {
            if (!member.getDeclaringClass().isAssignableFrom(receiver) || member.isConstructor())
                throw new InternalError(member.toString());
            mtype = mtype.insertParameterTypes(0, receiver);
        }
        if (!member.isField()) {
            if (refKind == REF_invokeSpecial) {
                member = member.asSpecial();
                LambdaForm lform = preparedLambdaForm(member);
                return new Special(mtype, lform, member);
            } else {
                LambdaForm lform = preparedLambdaForm(member);
                return new DirectMethodHandle(mtype, lform, member);
            }
        } else {
            LambdaForm lform = preparedFieldLambdaForm(member);
            if (member.isStatic()) {
                long offset = MethodHandleNatives.staticFieldOffset(member);
                Object base = MethodHandleNatives.staticFieldBase(member);
                return new StaticAccessor(mtype, lform, member, base, offset);
            } else {
                long offset = MethodHandleNatives.objectFieldOffset(member);
                assert(offset == (int)offset);
                return new Accessor(mtype, lform, member, (int)offset);
            }
        }
    }
    static DirectMethodHandle make(Class<?> receiver, MemberName member) {
        byte refKind = member.getReferenceKind();
        if (refKind == REF_invokeSpecial)
            refKind =  REF_invokeVirtual;
        return make(refKind, receiver, member);
    }
    static DirectMethodHandle make(MemberName member) {
        if (member.isConstructor())
            return makeAllocator(member);
        return make(member.getDeclaringClass(), member);
    }
    private static DirectMethodHandle makeAllocator(MemberName ctor) {
        assert(ctor.isConstructor() && ctor.getName().equals("<init>"));
        Class<?> instanceClass = ctor.getDeclaringClass();
        ctor = ctor.asConstructor();
        assert(ctor.isConstructor() && ctor.getReferenceKind() == REF_newInvokeSpecial) : ctor;
        MethodType mtype = ctor.getMethodType().changeReturnType(instanceClass);
        LambdaForm lform = preparedLambdaForm(ctor);
        MemberName init = ctor.asSpecial();
        assert(init.getMethodType().returnType() == void.class);
        return new Constructor(mtype, lform, ctor, init, instanceClass);
    }

    @Override
    BoundMethodHandle rebind() {
        return BoundMethodHandle.makeReinvoker(this);
    }

    @Override
    MethodHandle copyWith(MethodType mt, LambdaForm lf) {
        assert(this.getClass() == DirectMethodHandle.class);  // must override in subclasses
        return new DirectMethodHandle(mt, lf, member);
    }

    @Override
    String internalProperties() {
        return "\n& DMH.MN="+internalMemberName();
    }

    //// Implementation methods.
    @Override
    @ForceInline
    MemberName internalMemberName() {
        return member;
    }

    private static final MemberName.Factory IMPL_NAMES = MemberName.getFactory();

    /**
     * Create a LF which can invoke the given method.
     * Cache and share this structure among all methods with
     * the same basicType and refKind.
     */
    private static LambdaForm preparedLambdaForm(MemberName m) {
        assert(m.isInvocable()) : m;  // call preparedFieldLambdaForm instead
        MethodType mtype = m.getInvocationType().basicType();
        assert(!m.isMethodHandleInvoke()) : m;
        int which;
        switch (m.getReferenceKind()) {
        case REF_invokeVirtual:    which = LF_INVVIRTUAL;    break;
        case REF_invokeStatic:     which = LF_INVSTATIC;     break;
        case REF_invokeSpecial:    which = LF_INVSPECIAL;    break;
        case REF_invokeInterface:  which = LF_INVINTERFACE;  break;
        case REF_newInvokeSpecial: which = LF_NEWINVSPECIAL; break;
        default:  throw new InternalError(m.toString());
        }
        if (which == LF_INVSTATIC && shouldBeInitialized(m)) {
            // precompute the barrier-free version:
            preparedLambdaForm(mtype, which);
            which = LF_INVSTATIC_INIT;
        }
        LambdaForm lform = preparedLambdaForm(mtype, which);
        maybeCompile(lform, m);
        assert(lform.methodType().dropParameterTypes(0, 1)
                .equals(m.getInvocationType().basicType()))
                : Arrays.asList(m, m.getInvocationType().basicType(), lform, lform.methodType());
        return lform;
    }

    private static LambdaForm preparedLambdaForm(MethodType mtype, int which) {
        LambdaForm lform = mtype.form().cachedLambdaForm(which);
        if (lform != null)  return lform;
        lform = makePreparedLambdaForm(mtype, which);
        return mtype.form().setCachedLambdaForm(which, lform);
    }

    static LambdaForm makePreparedLambdaForm(MethodType mtype, int which) {
        boolean needsInit = (which == LF_INVSTATIC_INIT);
        boolean doesAlloc = (which == LF_NEWINVSPECIAL);
        String linkerName;
        LambdaForm.Kind kind;
        switch (which) {
        case LF_INVVIRTUAL:    linkerName = "linkToVirtual";   kind = DIRECT_INVOKE_VIRTUAL;     break;
        case LF_INVSTATIC:     linkerName = "linkToStatic";    kind = DIRECT_INVOKE_STATIC;      break;
        case LF_INVSTATIC_INIT:linkerName = "linkToStatic";    kind = DIRECT_INVOKE_STATIC_INIT; break;
        case LF_INVSPECIAL:    linkerName = "linkToSpecial";   kind = DIRECT_INVOKE_SPECIAL;     break;
        case LF_INVINTERFACE:  linkerName = "linkToInterface"; kind = DIRECT_INVOKE_INTERFACE;   break;
        case LF_NEWINVSPECIAL: linkerName = "linkToSpecial";   kind = DIRECT_NEW_INVOKE_SPECIAL; break;
        default:  throw new InternalError("which="+which);
        }

        MethodType mtypeWithArg = mtype.appendParameterTypes(MemberName.class);
        if (doesAlloc)
            mtypeWithArg = mtypeWithArg
                    .insertParameterTypes(0, Object.class)  // insert newly allocated obj
                    .changeReturnType(void.class);          // <init> returns void
        MemberName linker = new MemberName(MethodHandle.class, linkerName, mtypeWithArg, REF_invokeStatic);
        try {
            linker = IMPL_NAMES.resolveOrFail(REF_invokeStatic, linker, null, NoSuchMethodException.class);
        } catch (ReflectiveOperationException ex) {
            throw newInternalError(ex);
        }
        final int DMH_THIS    = 0;
        final int ARG_BASE    = 1;
        final int ARG_LIMIT   = ARG_BASE + mtype.parameterCount();
        int nameCursor = ARG_LIMIT;
        final int NEW_OBJ     = (doesAlloc ? nameCursor++ : -1);
        final int GET_MEMBER  = nameCursor++;
        final int LINKER_CALL = nameCursor++;
        Name[] names = arguments(nameCursor - ARG_LIMIT, mtype.invokerType());
        assert(names.length == nameCursor);
        if (doesAlloc) {
            // names = { argx,y,z,... new C, init method }
            names[NEW_OBJ] = new Name(NF_allocateInstance, names[DMH_THIS]);
            names[GET_MEMBER] = new Name(NF_constructorMethod, names[DMH_THIS]);
        } else if (needsInit) {
            names[GET_MEMBER] = new Name(NF_internalMemberNameEnsureInit, names[DMH_THIS]);
        } else {
            names[GET_MEMBER] = new Name(NF_internalMemberName, names[DMH_THIS]);
        }
        assert(findDirectMethodHandle(names[GET_MEMBER]) == names[DMH_THIS]);
        Object[] outArgs = Arrays.copyOfRange(names, ARG_BASE, GET_MEMBER+1, Object[].class);
        assert(outArgs[outArgs.length-1] == names[GET_MEMBER]);  // look, shifted args!
        int result = LAST_RESULT;
        if (doesAlloc) {
            assert(outArgs[outArgs.length-2] == names[NEW_OBJ]);  // got to move this one
            System.arraycopy(outArgs, 0, outArgs, 1, outArgs.length-2);
            outArgs[0] = names[NEW_OBJ];
            result = NEW_OBJ;
        }
        names[LINKER_CALL] = new Name(linker, outArgs);
        String lambdaName = kind.defaultLambdaName + "_" + shortenSignature(basicTypeSignature(mtype));
        LambdaForm lform = new LambdaForm(lambdaName, ARG_LIMIT, names, result, kind);

        // This is a tricky bit of code.  Don't send it through the LF interpreter.
        lform.compileToBytecode();
        return lform;
    }

    static Object findDirectMethodHandle(Name name) {
        if (name.function == NF_internalMemberName ||
            name.function == NF_internalMemberNameEnsureInit ||
            name.function == NF_constructorMethod) {
            assert(name.arguments.length == 1);
            return name.arguments[0];
        }
        return null;
    }

    private static void maybeCompile(LambdaForm lform, MemberName m) {
        if (lform.vmentry == null && VerifyAccess.isSamePackage(m.getDeclaringClass(), MethodHandle.class))
            // Help along bootstrapping...
            lform.compileToBytecode();
    }

    /** Static wrapper for DirectMethodHandle.internalMemberName. */
    @ForceInline
    /*non-public*/ static Object internalMemberName(Object mh) {
        return ((DirectMethodHandle)mh).member;
    }

    /** Static wrapper for DirectMethodHandle.internalMemberName.
     * This one also forces initialization.
     */
    /*non-public*/ static Object internalMemberNameEnsureInit(Object mh) {
        DirectMethodHandle dmh = (DirectMethodHandle)mh;
        dmh.ensureInitialized();
        return dmh.member;
    }

    /*non-public*/ static
    boolean shouldBeInitialized(MemberName member) {
        switch (member.getReferenceKind()) {
        case REF_invokeStatic:
        case REF_getStatic:
        case REF_putStatic:
        case REF_newInvokeSpecial:
            break;
        default:
            // No need to initialize the class on this kind of member.
            return false;
        }
        Class<?> cls = member.getDeclaringClass();
        if (cls == ValueConversions.class ||
            cls == MethodHandleImpl.class ||
            cls == Invokers.class) {
            // These guys have lots of <clinit> DMH creation but we know
            // the MHs will not be used until the system is booted.
            return false;
        }
        if (VerifyAccess.isSamePackage(MethodHandle.class, cls) ||
            VerifyAccess.isSamePackage(ValueConversions.class, cls)) {
            // It is a system class.  It is probably in the process of
            // being initialized, but we will help it along just to be safe.
            if (UNSAFE.shouldBeInitialized(cls)) {
                UNSAFE.ensureClassInitialized(cls);
            }
            return false;
        }
        return UNSAFE.shouldBeInitialized(cls);
    }

    private static class EnsureInitialized extends ClassValue<WeakReference<Thread>> {
        @Override
        protected WeakReference<Thread> computeValue(Class<?> type) {
            UNSAFE.ensureClassInitialized(type);
            if (UNSAFE.shouldBeInitialized(type))
                // If the previous call didn't block, this can happen.
                // We are executing inside <clinit>.
                return new WeakReference<>(Thread.currentThread());
            return null;
        }
        static final EnsureInitialized INSTANCE = new EnsureInitialized();
    }

    private void ensureInitialized() {
        if (checkInitialized(member)) {
            // The coast is clear.  Delete the <clinit> barrier.
            if (member.isField())
                updateForm(preparedFieldLambdaForm(member));
            else
                updateForm(preparedLambdaForm(member));
        }
    }
    private static boolean checkInitialized(MemberName member) {
        Class<?> defc = member.getDeclaringClass();
        WeakReference<Thread> ref = EnsureInitialized.INSTANCE.get(defc);
        if (ref == null) {
            return true;  // the final state
        }
        Thread clinitThread = ref.get();
        // Somebody may still be running defc.<clinit>.
        if (clinitThread == Thread.currentThread()) {
            // If anybody is running defc.<clinit>, it is this thread.
            if (UNSAFE.shouldBeInitialized(defc))
                // Yes, we are running it; keep the barrier for now.
                return false;
        } else {
            // We are in a random thread.  Block.
            UNSAFE.ensureClassInitialized(defc);
        }
        assert(!UNSAFE.shouldBeInitialized(defc));
        // put it into the final state
        EnsureInitialized.INSTANCE.remove(defc);
        return true;
    }

    /*non-public*/ static void ensureInitialized(Object mh) {
        ((DirectMethodHandle)mh).ensureInitialized();
    }

    /** This subclass represents invokespecial instructions. */
    static class Special extends DirectMethodHandle {
        private Special(MethodType mtype, LambdaForm form, MemberName member) {
            super(mtype, form, member);
        }
        @Override
        boolean isInvokeSpecial() {
            return true;
        }
        @Override
        MethodHandle copyWith(MethodType mt, LambdaForm lf) {
            return new Special(mt, lf, member);
        }
    }

    /** This subclass handles constructor references. */
    static class Constructor extends DirectMethodHandle {
        final MemberName initMethod;
        final Class<?>   instanceClass;

        private Constructor(MethodType mtype, LambdaForm form, MemberName constructor,
                            MemberName initMethod, Class<?> instanceClass) {
            super(mtype, form, constructor);
            this.initMethod = initMethod;
            this.instanceClass = instanceClass;
            assert(initMethod.isResolved());
        }
        @Override
        MethodHandle copyWith(MethodType mt, LambdaForm lf) {
            return new Constructor(mt, lf, member, initMethod, instanceClass);
        }
    }

    /*non-public*/ static Object constructorMethod(Object mh) {
        Constructor dmh = (Constructor)mh;
        return dmh.initMethod;
    }

    /*non-public*/ static Object allocateInstance(Object mh) throws InstantiationException {
        Constructor dmh = (Constructor)mh;
        return UNSAFE.allocateInstance(dmh.instanceClass);
    }

    /** This subclass handles non-static field references. */
    static class Accessor extends DirectMethodHandle {
        final Class<?> fieldType;
        final int      fieldOffset;
        private Accessor(MethodType mtype, LambdaForm form, MemberName member,
                         int fieldOffset) {
            super(mtype, form, member);
            this.fieldType   = member.getFieldType();
            this.fieldOffset = fieldOffset;
        }

        @Override Object checkCast(Object obj) {
            return fieldType.cast(obj);
        }
        @Override
        MethodHandle copyWith(MethodType mt, LambdaForm lf) {
            return new Accessor(mt, lf, member, fieldOffset);
        }
    }

    @ForceInline
    /*non-public*/ static long fieldOffset(Object accessorObj) {
        // Note: We return a long because that is what Unsafe.getObject likes.
        // We store a plain int because it is more compact.
        return ((Accessor)accessorObj).fieldOffset;
    }

    @ForceInline
    /*non-public*/ static Object checkBase(Object obj) {
        // Note that the object's class has already been verified,
        // since the parameter type of the Accessor method handle
        // is either member.getDeclaringClass or a subclass.
        // This was verified in DirectMethodHandle.make.
        // Therefore, the only remaining check is for null.
        // Since this check is *not* guaranteed by Unsafe.getInt
        // and its siblings, we need to make an explicit one here.
        return Objects.requireNonNull(obj);
    }

    /** This subclass handles static field references. */
    static class StaticAccessor extends DirectMethodHandle {
        private final Class<?> fieldType;
        private final Object   staticBase;
        private final long     staticOffset;

        private StaticAccessor(MethodType mtype, LambdaForm form, MemberName member,
                               Object staticBase, long staticOffset) {
            super(mtype, form, member);
            this.fieldType    = member.getFieldType();
            this.staticBase   = staticBase;
            this.staticOffset = staticOffset;
        }

        @Override Object checkCast(Object obj) {
            return fieldType.cast(obj);
        }
        @Override
        MethodHandle copyWith(MethodType mt, LambdaForm lf) {
            return new StaticAccessor(mt, lf, member, staticBase, staticOffset);
        }
    }

    @ForceInline
    /*non-public*/ static Object nullCheck(Object obj) {
        return Objects.requireNonNull(obj);
    }

    @ForceInline
    /*non-public*/ static Object staticBase(Object accessorObj) {
        return ((StaticAccessor)accessorObj).staticBase;
    }

    @ForceInline
    /*non-public*/ static long staticOffset(Object accessorObj) {
        return ((StaticAccessor)accessorObj).staticOffset;
    }

    @ForceInline
    /*non-public*/ static Object checkCast(Object mh, Object obj) {
        return ((DirectMethodHandle) mh).checkCast(obj);
    }

    Object checkCast(Object obj) {
        return member.getReturnType().cast(obj);
    }

    // Caching machinery for field accessors:
    static final byte
            AF_GETFIELD        = 0,
            AF_PUTFIELD        = 1,
            AF_GETSTATIC       = 2,
            AF_PUTSTATIC       = 3,
            AF_GETSTATIC_INIT  = 4,
            AF_PUTSTATIC_INIT  = 5,
            AF_LIMIT           = 6;
    // Enumerate the different field kinds using Wrapper,
    // with an extra case added for checked references.
    static final int
            FT_LAST_WRAPPER    = Wrapper.COUNT-1,
            FT_UNCHECKED_REF   = Wrapper.OBJECT.ordinal(),
            FT_CHECKED_REF     = FT_LAST_WRAPPER+1,
            FT_LIMIT           = FT_LAST_WRAPPER+2;
    private static int afIndex(byte formOp, boolean isVolatile, int ftypeKind) {
        return ((formOp * FT_LIMIT * 2)
                + (isVolatile ? FT_LIMIT : 0)
                + ftypeKind);
    }
    @Stable
    private static final LambdaForm[] ACCESSOR_FORMS
            = new LambdaForm[afIndex(AF_LIMIT, false, 0)];
    static int ftypeKind(Class<?> ftype) {
        if (ftype.isPrimitive())
            return Wrapper.forPrimitiveType(ftype).ordinal();
        else if (VerifyType.isNullReferenceConversion(Object.class, ftype))
            return FT_UNCHECKED_REF;
        else
            return FT_CHECKED_REF;
    }

    /**
     * Create a LF which can access the given field.
     * Cache and share this structure among all fields with
     * the same basicType and refKind.
     */
    private static LambdaForm preparedFieldLambdaForm(MemberName m) {
        Class<?> ftype = m.getFieldType();
        boolean isVolatile = m.isVolatile();
        byte formOp;
        switch (m.getReferenceKind()) {
        case REF_getField:      formOp = AF_GETFIELD;    break;
        case REF_putField:      formOp = AF_PUTFIELD;    break;
        case REF_getStatic:     formOp = AF_GETSTATIC;   break;
        case REF_putStatic:     formOp = AF_PUTSTATIC;   break;
        default:  throw new InternalError(m.toString());
        }
        if (shouldBeInitialized(m)) {
            // precompute the barrier-free version:
            preparedFieldLambdaForm(formOp, isVolatile, ftype);
            assert((AF_GETSTATIC_INIT - AF_GETSTATIC) ==
                   (AF_PUTSTATIC_INIT - AF_PUTSTATIC));
            formOp += (AF_GETSTATIC_INIT - AF_GETSTATIC);
        }
        LambdaForm lform = preparedFieldLambdaForm(formOp, isVolatile, ftype);
        maybeCompile(lform, m);
        assert(lform.methodType().dropParameterTypes(0, 1)
                .equals(m.getInvocationType().basicType()))
                : Arrays.asList(m, m.getInvocationType().basicType(), lform, lform.methodType());
        return lform;
    }
    private static LambdaForm preparedFieldLambdaForm(byte formOp, boolean isVolatile, Class<?> ftype) {
        int ftypeKind = ftypeKind(ftype);
        int afIndex = afIndex(formOp, isVolatile, ftypeKind);
        LambdaForm lform = ACCESSOR_FORMS[afIndex];
        if (lform != null)  return lform;
        lform = makePreparedFieldLambdaForm(formOp, isVolatile, ftypeKind);
        ACCESSOR_FORMS[afIndex] = lform;  // don't bother with a CAS
        return lform;
    }

    private static final Wrapper[] ALL_WRAPPERS = Wrapper.values();

    private static Kind getFieldKind(boolean isGetter, boolean isVolatile, Wrapper wrapper) {
        if (isGetter) {
            if (isVolatile) {
                switch (wrapper) {
                    case BOOLEAN: return GET_BOOLEAN_VOLATILE;
                    case BYTE:    return GET_BYTE_VOLATILE;
                    case SHORT:   return GET_SHORT_VOLATILE;
                    case CHAR:    return GET_CHAR_VOLATILE;
                    case INT:     return GET_INT_VOLATILE;
                    case LONG:    return GET_LONG_VOLATILE;
                    case FLOAT:   return GET_FLOAT_VOLATILE;
                    case DOUBLE:  return GET_DOUBLE_VOLATILE;
                    case OBJECT:  return GET_OBJECT_VOLATILE;
                }
            } else {
                switch (wrapper) {
                    case BOOLEAN: return GET_BOOLEAN;
                    case BYTE:    return GET_BYTE;
                    case SHORT:   return GET_SHORT;
                    case CHAR:    return GET_CHAR;
                    case INT:     return GET_INT;
                    case LONG:    return GET_LONG;
                    case FLOAT:   return GET_FLOAT;
                    case DOUBLE:  return GET_DOUBLE;
                    case OBJECT:  return GET_OBJECT;
                }
            }
        } else {
            if (isVolatile) {
                switch (wrapper) {
                    case BOOLEAN: return PUT_BOOLEAN_VOLATILE;
                    case BYTE:    return PUT_BYTE_VOLATILE;
                    case SHORT:   return PUT_SHORT_VOLATILE;
                    case CHAR:    return PUT_CHAR_VOLATILE;
                    case INT:     return PUT_INT_VOLATILE;
                    case LONG:    return PUT_LONG_VOLATILE;
                    case FLOAT:   return PUT_FLOAT_VOLATILE;
                    case DOUBLE:  return PUT_DOUBLE_VOLATILE;
                    case OBJECT:  return PUT_OBJECT_VOLATILE;
                }
            } else {
                switch (wrapper) {
                    case BOOLEAN: return PUT_BOOLEAN;
                    case BYTE:    return PUT_BYTE;
                    case SHORT:   return PUT_SHORT;
                    case CHAR:    return PUT_CHAR;
                    case INT:     return PUT_INT;
                    case LONG:    return PUT_LONG;
                    case FLOAT:   return PUT_FLOAT;
                    case DOUBLE:  return PUT_DOUBLE;
                    case OBJECT:  return PUT_OBJECT;
                }
            }
        }
        throw new AssertionError("Invalid arguments");
    }

    static LambdaForm makePreparedFieldLambdaForm(byte formOp, boolean isVolatile, int ftypeKind) {
        boolean isGetter  = (formOp & 1) == (AF_GETFIELD & 1);
        boolean isStatic  = (formOp >= AF_GETSTATIC);
        boolean needsInit = (formOp >= AF_GETSTATIC_INIT);
        boolean needsCast = (ftypeKind == FT_CHECKED_REF);
        Wrapper fw = (needsCast ? Wrapper.OBJECT : ALL_WRAPPERS[ftypeKind]);
        Class<?> ft = fw.primitiveType();
        assert(ftypeKind(needsCast ? String.class : ft) == ftypeKind);

        // getObject, putIntVolatile, etc.
        Kind kind = getFieldKind(isGetter, isVolatile, fw);

        MethodType linkerType;
        if (isGetter)
            linkerType = MethodType.methodType(ft, Object.class, long.class);
        else
            linkerType = MethodType.methodType(void.class, Object.class, long.class, ft);
        MemberName linker = new MemberName(Unsafe.class, kind.methodName, linkerType, REF_invokeVirtual);
        try {
            linker = IMPL_NAMES.resolveOrFail(REF_invokeVirtual, linker, null, NoSuchMethodException.class);
        } catch (ReflectiveOperationException ex) {
            throw newInternalError(ex);
        }

        // What is the external type of the lambda form?
        MethodType mtype;
        if (isGetter)
            mtype = MethodType.methodType(ft);
        else
            mtype = MethodType.methodType(void.class, ft);
        mtype = mtype.basicType();  // erase short to int, etc.
        if (!isStatic)
            mtype = mtype.insertParameterTypes(0, Object.class);
        final int DMH_THIS  = 0;
        final int ARG_BASE  = 1;
        final int ARG_LIMIT = ARG_BASE + mtype.parameterCount();
        // if this is for non-static access, the base pointer is stored at this index:
        final int OBJ_BASE  = isStatic ? -1 : ARG_BASE;
        // if this is for write access, the value to be written is stored at this index:
        final int SET_VALUE  = isGetter ? -1 : ARG_LIMIT - 1;
        int nameCursor = ARG_LIMIT;
        final int F_HOLDER  = (isStatic ? nameCursor++ : -1);  // static base if any
        final int F_OFFSET  = nameCursor++;  // Either static offset or field offset.
        final int OBJ_CHECK = (OBJ_BASE >= 0 ? nameCursor++ : -1);
        final int U_HOLDER  = nameCursor++;  // UNSAFE holder
        final int INIT_BAR  = (needsInit ? nameCursor++ : -1);
        final int PRE_CAST  = (needsCast && !isGetter ? nameCursor++ : -1);
        final int LINKER_CALL = nameCursor++;
        final int POST_CAST = (needsCast && isGetter ? nameCursor++ : -1);
        final int RESULT    = nameCursor-1;  // either the call or the cast
        Name[] names = arguments(nameCursor - ARG_LIMIT, mtype.invokerType());
        if (needsInit)
            names[INIT_BAR] = new Name(NF_ensureInitialized, names[DMH_THIS]);
        if (needsCast && !isGetter)
            names[PRE_CAST] = new Name(NF_checkCast, names[DMH_THIS], names[SET_VALUE]);
        Object[] outArgs = new Object[1 + linkerType.parameterCount()];
        assert(outArgs.length == (isGetter ? 3 : 4));
        outArgs[0] = names[U_HOLDER] = new Name(NF_UNSAFE);
        if (isStatic) {
            outArgs[1] = names[F_HOLDER]  = new Name(NF_staticBase, names[DMH_THIS]);
            outArgs[2] = names[F_OFFSET]  = new Name(NF_staticOffset, names[DMH_THIS]);
        } else {
            outArgs[1] = names[OBJ_CHECK] = new Name(NF_checkBase, names[OBJ_BASE]);
            outArgs[2] = names[F_OFFSET]  = new Name(NF_fieldOffset, names[DMH_THIS]);
        }
        if (!isGetter) {
            outArgs[3] = (needsCast ? names[PRE_CAST] : names[SET_VALUE]);
        }
        for (Object a : outArgs)  assert(a != null);
        names[LINKER_CALL] = new Name(linker, outArgs);
        if (needsCast && isGetter)
            names[POST_CAST] = new Name(NF_checkCast, names[DMH_THIS], names[LINKER_CALL]);
        for (Name n : names)  assert(n != null);
        // add some detail to the lambdaForm debugname,
        // significant only for debugging
        StringBuilder nameBuilder = new StringBuilder(kind.methodName);
        if (isStatic) {
            nameBuilder.append("Static");
        } else {
            nameBuilder.append("Field");
        }
        if (needsCast)  nameBuilder.append("Cast");
        if (needsInit)  nameBuilder.append("Init");
        if (needsCast || needsInit) {
            // can't use the pre-generated form when casting and/or initializing
            return new LambdaForm(nameBuilder.toString(), ARG_LIMIT, names, RESULT);
        } else {
            return new LambdaForm(nameBuilder.toString(), ARG_LIMIT, names, RESULT, kind);
        }
    }

    /**
     * Pre-initialized NamedFunctions for bootstrapping purposes.
     * Factored in an inner class to delay initialization until first usage.
     */
    static final NamedFunction
            NF_internalMemberName,
            NF_internalMemberNameEnsureInit,
            NF_ensureInitialized,
            NF_fieldOffset,
            NF_checkBase,
            NF_staticBase,
            NF_staticOffset,
            NF_checkCast,
            NF_allocateInstance,
            NF_constructorMethod,
            NF_UNSAFE;
    static {
        try {
            NamedFunction nfs[] = {
                    NF_internalMemberName = new NamedFunction(DirectMethodHandle.class
                            .getDeclaredMethod("internalMemberName", Object.class)),
                    NF_internalMemberNameEnsureInit = new NamedFunction(DirectMethodHandle.class
                            .getDeclaredMethod("internalMemberNameEnsureInit", Object.class)),
                    NF_ensureInitialized = new NamedFunction(DirectMethodHandle.class
                            .getDeclaredMethod("ensureInitialized", Object.class)),
                    NF_fieldOffset = new NamedFunction(DirectMethodHandle.class
                            .getDeclaredMethod("fieldOffset", Object.class)),
                    NF_checkBase = new NamedFunction(DirectMethodHandle.class
                            .getDeclaredMethod("checkBase", Object.class)),
                    NF_staticBase = new NamedFunction(DirectMethodHandle.class
                            .getDeclaredMethod("staticBase", Object.class)),
                    NF_staticOffset = new NamedFunction(DirectMethodHandle.class
                            .getDeclaredMethod("staticOffset", Object.class)),
                    NF_checkCast = new NamedFunction(DirectMethodHandle.class
                            .getDeclaredMethod("checkCast", Object.class, Object.class)),
                    NF_allocateInstance = new NamedFunction(DirectMethodHandle.class
                            .getDeclaredMethod("allocateInstance", Object.class)),
                    NF_constructorMethod = new NamedFunction(DirectMethodHandle.class
                            .getDeclaredMethod("constructorMethod", Object.class)),
                    NF_UNSAFE = new NamedFunction(new MemberName(MethodHandleStatics.class
                            .getDeclaredField("UNSAFE")))
            };
            // Each nf must be statically invocable or we get tied up in our bootstraps.
            assert(InvokerBytecodeGenerator.isStaticallyInvocable(nfs));
        } catch (ReflectiveOperationException ex) {
            throw newInternalError(ex);
        }
    }

    static {
        // The Holder class will contain pre-generated DirectMethodHandles resolved
        // speculatively using MemberName.getFactory().resolveOrNull. However, that
        // doesn't initialize the class, which subtly breaks inlining etc. By forcing
        // initialization of the Holder class we avoid these issues.
        UNSAFE.ensureClassInitialized(Holder.class);
    }

    /* Placeholder class for DirectMethodHandles generated ahead of time */
    final class Holder {}
}
