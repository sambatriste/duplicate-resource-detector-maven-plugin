package com.github.sambatriste.drd;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * クラスパス要素の集まり。
 */
class ClasspathElements implements Iterable<ClasspathElement> {

    /** クラスパス要素の集まり。 */
    final List<ClasspathElement> elements;

    /** このクラスパス要素のスコープ */
    final Scope scope;

    /** スコープ */
    enum Scope {
        COMPILE,
        RUNTIME,
        TEST,
        PROVIDED
    }

    /**
     * コンストラクタ。
     *
     * @param classpathElements クラスパス
     */
    ClasspathElements(List<String> classpathElements, Scope scope) {
        this.scope = scope;
        List<ClasspathElement> elements = new ArrayList<>(classpathElements.size());
        for (String e : classpathElements) {
            elements.add(toClasspathElement(e));
        }
        this.elements = Collections.unmodifiableList(elements);
    }

    /**
     * 指定されたインデックスの要素を取得する。
     *
     * @param index インデックス
     * @return クラスパス要素
     */
    ClasspathElement get(int index) {
        return elements.get(index);
    }

    /**
     * 要素数を取得する。
     *
     * @return 要素数
     */
    int size() {
        return elements.size();
    }

    /**
     * クラスパス要素の文字列から{@link ClasspathElement}実装クラスの
     * インスタンスを生成する。
     *
     * @param e クラスパス要素の文字列
     * @return {@link ClasspathElement}実装クラスのインスタンス
     */
    private static ClasspathElement toClasspathElement(String e) {
        File f = new File(e);
        if (!f.exists()) {
            throw new IllegalArgumentException(e + " does not exist.");
        }
        if (f.getName().endsWith(".jar")) {
            return new JarClasspathElement(e);
        }
        if (f.isDirectory()) {
            return new DirectoryElement(f);
        }
        throw new IllegalArgumentException("unknown type. " + e);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<ClasspathElement> iterator() {
        return elements.iterator();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return elements.toString();
    }
}
