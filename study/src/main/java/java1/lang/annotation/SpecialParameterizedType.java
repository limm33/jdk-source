package java1.lang.annotation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
public class SpecialParameterizedType implements ParameterizedType {

    private Class clz;

    private Class actualType;

    /**
     * 构造函数.
     * 
     * @param clz
     *            clz
     * @param actualType
     *            s
     */
    public SpecialParameterizedType(Class clz, Class actualType) {
        super();
        if (null == clz || null == actualType) {
            throw new RuntimeException("cla or actualType can not null");
        }
        this.clz = clz;
        this.actualType = actualType;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{actualType};
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Type getRawType() {
        return clz;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Type getOwnerType() {
        return null;
    }

}
