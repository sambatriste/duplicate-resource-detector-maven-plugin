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

    /**
     * クラスパス要素のペア。
     */
    private static class ClasspathElementPair {

        /** 片方のクラスパス要素 */
        private final ClasspathElement one;

        /** もう一方のクラスパス要素 */
        private final ClasspathElement another;

        /**
         * コンストラクタ。
         *
         * @param one 片方のクラスパス要素
         * @param another もう一方のクラスパス要素
         */
        private ClasspathElementPair(ClasspathElement one, ClasspathElement another) {
            this.one = one;
            this.another = another;
        }

        /**
         * ２つのクラスパス要素で重複したリソースを検出し、
         * 引数で与えられた{@link DuplicatedResourceContext}に追加する。
         *
         * @param builder 重複したリソース作成クラス
         */
        private void appendDuplicatedResourcesTo(DuplicatedResourceContext builder) {
            for (String resourcePath : detectDuplicatedResourcePaths()) {
                builder.add(resourcePath, one, another);
            }
        }

        /**
         * ２つのクラスパス要素から重複したリソースを抽出する。
         *
         * @return 重複したリソース全て
         */
        private List<String> detectDuplicatedResourcePaths() {
            List<String> duplicated = new ArrayList<>();
            duplicated.addAll(one.getContents());
            duplicated.retainAll(another.getContents());
            return duplicated;
        }

    }
}
