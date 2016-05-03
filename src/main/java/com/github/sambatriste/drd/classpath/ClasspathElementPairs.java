package com.github.sambatriste.drd.classpath;


import com.github.sambatriste.drd.duplicated.DuplicatedResources;
import com.github.sambatriste.drd.util.PatternSet;

import java.util.ArrayList;
import java.util.List;

/**
 * クラスパス要素の全ペア。
 */
public class ClasspathElementPairs {

    /** クラスパス要素のペア */
    private final List<ClasspathElementPair> pairs;

    /**
     * コンストラクタ。
     * 与えられたクラスパスから、クラスパス要素のペアを作成する。
     *
     * @param classpathElements クラスパス要素
     */
    public ClasspathElementPairs(List<String> classpathElements) {
        this.pairs = new PairListBuilder(classpathElements).createPairs();
    }

    /**
     * クラスパス要素ペア間で重複したリソースを検出する。
     *
     * @param excluded 除外パターン
     * @return 重複した要素
     */
    public DuplicatedResources detectDuplicated(PatternSet excluded) {
        DuplicatedResourceContext ctx = new DuplicatedResourceContext(excluded);
        for (ClasspathElementPair pair : pairs) {
            pair.appendDuplicatedResourcesTo(ctx);
        }
        return ctx.getResult();
    }

    /**
     * クラスパス要素から{@link ClasspathElementPair}のリストを作成するクラス。
     */
    private static class PairListBuilder {

        /** 作成されたペア */
        private final List<ClasspathElementPair> pairs;

        /** クラスパス要素 */
        private final List<String> classpathElements;

        /** {@link ClasspathElement}のファクトリ */
        private final ClasspathElementFactory elementFactory = new ClasspathElementFactory();

        /**
         * コンストラクタ。
         * @param classpathElements クラスパス要素
         */
        private PairListBuilder(List<String> classpathElements) {
            this.classpathElements = classpathElements;
            this.pairs = new ArrayList<>(classpathElements.size());
        }

        /**
         * クラスパス要素の文字列表現から、{@link ClasspathElement}のリストを作成する。
         *
         * @return {@link ClasspathElement}のリスト
         */
        private List<ClasspathElementPair> createPairs() {
            pairs.clear();
            for (int i = 0; i < classpathElements.size() - 1; i++) {
                for (int j = i + 1; j < classpathElements.size(); j++) {
                    ClasspathElementPair pair = createPairFrom(i, j);
                    pairs.add(pair);
                }
            }
            return pairs;
        }

        /**
         * ペアを作成する。
         *
         * @param i 一方の添字
         * @param j もう一方の添字
         * @return 組み合わされたペア
         */
        private ClasspathElementPair createPairFrom(int i, int j) {
            ClasspathElement one = elementFactory.create(classpathElements.get(i));
            ClasspathElement another = elementFactory.create(classpathElements.get(j));
            return new ClasspathElementPair(one, another);
        }
    }
}
