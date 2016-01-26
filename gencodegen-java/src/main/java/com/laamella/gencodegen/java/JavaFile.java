package com.laamella.gencodegen.java;

import java.io.File;
import java.io.OutputStream;

import com.laamella.gencodegen.core.Block;
import com.laamella.gencodegen.core.io.OutputStreamFactory;

/**
 * The entry point for generating a Java file.
 */
public class JavaFile extends JavaBlock {
    public final Imports imports = new Imports();
    public final String packageName;
    public final String name;

    /**
     * Start a new Java file, or "Compilation Unit."
     */
    public JavaFile(final String packageName, final String fileName) {
        this.packageName = packageName;
        this.name = fileName;
        add("package %s;", packageName).ln();
        addObject(imports);
    }

    /**
     * Start a new class in this file.
     * @param opener the class declaration, like "public class Demo&ltX> extends Object"
     * @param args arguments in case you have placeholders in the opener.
     * @return a classbody with which the rest of the class can be defined.
     */
    public ClassBody class_(String opener, Object... args) {
        ln().open(opener, args);
        final ClassBody classBody = new ClassBody(getIndent(), imports);
        addObject(classBody);
        close();
        return classBody;
    }

    /**
     * Write the file to the file in the package directory where it belongs.
     */
    public void write(final OutputStreamFactory outputDir) throws Exception {
        checkIndentationMatches();
        final String packageDir = packageName.replace('.', File.separatorChar);
        outputDir.stream(packageDir, name + ".java", outputStream -> outputStream.write(JavaFile.this.toString().getBytes("utf-8")));
    }

}
