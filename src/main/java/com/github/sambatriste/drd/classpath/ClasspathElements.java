package com.github.sambatriste.drd.classpath;


import com.github.sambatriste.drd.duplicated.DuplicatedResources;
import com.github.sambatriste.drd.util.PatternSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * クラスパス要素の全ペア。
 */
public class ClasspathElements implements Iterable<String> {

    /** このインスタンスの元となったクラスパス要素 */
    private final List<String> source;

    /** クラスパス要素のペア */
    private final List<ClasspathElementPair> pairs;

    /**
     * コンストラクタ。
     * 与えられたクラスパスから、クラスパス要素のペア{@link ClasspathElementPair}を作成する。
     *
     * @param classpathElements クラスパス要素
     */
    public ClasspathElements(List<String> classpathElements) {
        this.source = new ArrayList<>(classpathElements);
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

    /** {@inheritDoc} */
    @Override
    public Iterator<String> iterator() {
        return source.iterator();
    }

    /** クラスパス要素から{@link ClasspathElementPair}のリストを作成するクラス。 */
    private static class PairListBuilder {

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
        }

        /**
         * クラスパス要素の文字列表現から、{@link ClasspathElement}のリストを作成する。
         *
         * @return {@link ClasspathElement}のリスト
         */
        private List<ClasspathElementPair> createPairs() {
            List<ClasspathElementPair> pairs = new ArrayList<>(classpathElements.size());
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
