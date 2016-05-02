package com.github.sambatriste.drd;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by tie301686 on 2016/05/02.
 */
class ClasspathElementPairs {

    private final List<ClasspathElementPair> pairs;

    ClasspathElementPairs(List<String> classpathElements) {
        this.pairs = doPairing(classpathElements);
    }

    /**
     * ２つのクラスパス要素で重複したリソースを検出し、
     * 引数で与えられた{@link DuplicatedResources}に追加する。
     *
     * @param duplicatedResources 重複したリソース
     */
    void appendDuplicatedResourcesTo(DuplicatedResources duplicatedResources) {
        for (ClasspathElementPair pair : pairs) {
            pair.appendDuplicatedResourcesTo(duplicatedResources);
        }
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
            return new DirectoryElement(f);
        }
        throw new IllegalArgumentException("unknown type. " + e);
    }
}
