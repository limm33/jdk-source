/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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
package jdk.javadoc.internal.doclets.toolkit;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

import com.sun.source.util.DocTreePath;
import jdk.javadoc.doclet.Reporter;

import static javax.tools.Diagnostic.Kind.*;

/**
 * Provides standardized access to the diagnostic reporting facilities
 * for a doclet.
 *
 * Messages are specified by resource keys to be found in the doclet's
 * {@link Resources resources}.  Values can be substituted into the
 * strings obtained from the resource files.
 *
 * Messages are reported to the doclet's {@link Reporter reporter}.
 */
public class Messages {
    private final Configuration configuration;
    private final Resources resources;
    private Reporter reporter;

    /**
     * Creates a {@code Messages} object to provide standardized access to
     * the doclet's diagnostic reporting mechanisms.
     *
     * @param configuration the doclet's configuration, used to access
     *  the doclet's resources, reporter, and additional methods and state
     *  used to filter out messages, if any, which should be suppressed.
     */
    public Messages(Configuration configuration) {
        this.configuration = configuration;
        resources = configuration.getResources();
    }

    // ***** Errors *****

    /**
     * Reports an error message to the doclet's reporter.
     *
     * @param key the name of a resource containing the message to be printed
     * @param args optional arguments to be replaced in the message.
     */
    public void error(String key, Object... args) {
        report(ERROR, resources.getText(key, args));
    }

    /**
     * Reports an error message to the doclet's reporter.
     *
     * @param path a path identifying the position to be included with
     *  the message
     * @param key the name of a resource containing the message to be printed
     * @param args optional arguments to be replaced in the message.
     */
    public void error(DocTreePath path, String key, Object... args) {
        report(ERROR, path, resources.getText(key, args));
    }

    // ***** Warnings *****

    /**
     * Reports a warning message to the doclet's reporter.
     *
     * @param key the name of a resource containing the message to be printed
     * @param args optional arguments to be replaced in the message.
     */
    public void warning(String key, Object... args) {
        report(WARNING, resources.getText(key, args));
    }

    /**
     * Reports a warning message to the doclet's reporter.
     *
     * @param path a path identifying the position to be included with
     *  the message
     * @param key the name of a resource containing the message to be printed
     * @param args optional arguments to be replaced in the message.
     */
    public void warning(DocTreePath path, String key, Object... args) {
        if (configuration.showMessage(path, key))
            report(WARNING, path, resources.getText(key, args));
    }

    /**
     * Reports a warning message to the doclet's reporter.
     *
     * @param e an element identifying the declaration whose position should
     *  to be included with the message
     * @param key the name of a resource containing the message to be printed
     * @param args optional arguments to be replaced in the message.
     */
    public void warning(Element e, String key, Object... args) {
        if (configuration.showMessage(e, key)) {
            report(WARNING, e, resources.getText(key, args));
        }
    }

    // ***** Notices *****

    /**
     * Reports an informational notice to the doclet's reporter.
     *
     * @param key the name of a resource containing the message to be printed
     * @param args optional arguments to be replaced in the message.
     */
    public void notice(String key, Object... args) {
        if (!configuration.quiet) {
            report(NOTE, resources.getText(key, args));
        }
    }

    // ***** Internal support *****

    private void report(Diagnostic.Kind k, String msg) {
        initReporter();
        reporter.print(k, msg);
    }

    private void report(Diagnostic.Kind k, DocTreePath p, String msg) {
        initReporter();
        reporter.print(k, p, msg);
    }

    private void report(Diagnostic.Kind k, Element e, String msg) {
        initReporter();
        reporter.print(k, e, msg);
    }

    // Lazy init the reporter for now, until we can fix/improve
    // the init of ConfigurationImpl in HtmlDoclet (and similar.)
    private void initReporter() {
        if (reporter == null) {
            reporter = configuration.reporter;
        }
    }
}
