package com.github.sambatriste.drd.classpath;

import java.io.File;

/**
 * {@link ClasspathElement}のファクトリ。
 */
class ClasspathElementFactory {

    /**
     * クラスパス要素の文字列から{@link ClasspathElement}実装クラスの
     * インスタンスを生成する。
     *
     * @param classpathElement クラスパス要素の文字列
     * @return {@link ClasspathElement}実装クラスのインスタンス
     */
    ClasspathElement create(String classpathElement) {
        File f = new File(classpathElement);
        if (!f.exists()) {
            throw new IllegalArgumentException(classpathElement + " does not exist.");
        }
        if (f.getName().endsWith(".jar")) {
            return new JarClasspathElement(classpathElement);
        }
        if (f.isDirectory()) {
            return new DirectoryClasspathElement(classpathElement);
        }
        throw new IllegalArgumentException("unknown type. " + classpathElement);
    }
}
