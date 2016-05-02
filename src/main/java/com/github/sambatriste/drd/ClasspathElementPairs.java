package com.github.sambatriste.drd;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * クラスパス要素の全ペア。
 */
class ClasspathElementPairs {

    /** クラスパス要素のペア */
    private final List<ClasspathElementPair> pairs;

    /**
     * コンストラクタ。
     * 与えられたクラスパスから、クラスパス要素のペアを作成する。
     *
     * @param classpathElements クラスパス要素
     */
    ClasspathElementPairs(List<String> classpathElements) {
        this.pairs = doPairing(classpathElements);
    }

    /**
     * クラスパス要素ペア間で重複したリソースを検出する。
     *
     * @param excluded 除外パターン
     * @return 重複した要素
     */
    DuplicatedResources detectDuplicated(PatternSet excluded) {
        DuplicatedResources.Builder builder = DuplicatedResources.startBuild(excluded);
        for (ClasspathElementPair pair : pairs) {
            pair.appendDuplicatedResourcesTo(builder);
        }
        return builder.build();
    }

    /**
     * クラスパス要素の文字列表現から、{@link ClasspathElement}のリストを作成する。
     *
     * @param classpathElements クラスパス要素の文字列表現
     * @return {@link ClasspathElement}のリスト
     */
    private static List<ClasspathElementPair> doPairing(List<String> classpathElements) {
        List<ClasspathElementPair> ret = new ArrayList<>(classpathElements.size());
        for (int i = 0; i < classpathElements.size() - 1; i++) {
            ClasspathElement one = toClasspathElement(classpathElements.get(i));
            for (int j = i + 1; j < classpathElements.size(); j++) {
                ClasspathElement another = toClasspathElement(classpathElements.get(j));
                ret.add(new ClasspathElementPair(one, another));
            }
        }
        return ret;
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
            return new DirectoryClasspathElement(f);
        }
        throw new IllegalArgumentException("unknown type. " + e);
    }
}
