package com.github.sambatriste.drd;


import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 重複したリソース。
 */
class DuplicatedResources implements Iterable<Entry<String, Set<ClasspathElement>>> {

    /**
     * 重複したリソース。
     * キー:リソース名
     * 値:そのリソースを含んでいるクラスパス要素
     */
    private final Map<String, Set<ClasspathElement>> duplicated;

    /**
     * コンストラクタ
     *
     * @param duplicated 重複したリソース
     */
    private DuplicatedResources(Map<String, Set<ClasspathElement>> duplicated) {
        this.duplicated = Collections.unmodifiableMap(duplicated);
    }

    /**
     * 空であるか判定する。
     *
     * @return 重複したリソースが存在しない場合、真
     */
    boolean isEmpty() {
        return duplicated.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Entry<String, Set<ClasspathElement>>> iterator() {
        return duplicated.entrySet().iterator();
    }

    /**
     * オブジェクトの組み立てを開始する。
     *
     * @param excludedResourcePatterns 除外対象リソースのパターン
     * @return ビルダー
     */
    static Builder startBuild(PatternSet excludedResourcePatterns) {
        return new Builder(excludedResourcePatterns);
    }

    /**
     * {@link DuplicatedResources}のビルダークラス。
     * META-INF配下は管理対象外とする。
     */
    static class Builder {

        /** 除外対象リソースのパターン */
        private final PatternSet excludedResources;

        /**
         * 重複したリソース。
         * キー:リソース名
         * 値:そのリソースを含んでいるクラスパス要素
         */
        private final Map<String, Set<ClasspathElement>> duplicated = new LinkedHashMap<>();

        /**
         * コンストラクタ。
         *
         * @param excludedResources 除外対象リソースのパターン
         */
        private Builder(PatternSet excludedResources) {
            this.excludedResources = excludedResources;
        }

        /**
         * 重複したリソースを登録する。
         *
         * @param resourcePath 重複したリソースのパス
         * @param elements 重複したリソースを保持していたクラスパス要素
         */
        void add(String resourcePath, ClasspathElement... elements) {
            if (isMetaInf(resourcePath)) {
                return;
            }
            if (excludedResources.any(resourcePath)) {
                return;
            }
            Set<ClasspathElement> values = getValueContainer(resourcePath);
            values.addAll(Arrays.asList(elements));
            assert values.size() > 1;
        }

        /**
         * {@link DuplicatedResources}を生成する。
         *
         * @return {@link DuplicatedResources}
         */
        DuplicatedResources build() {
            return new DuplicatedResources(duplicated);
        }


        /**
         * リソースがマニフェストファイルかどうか判定する。
         *
         * @param resourcePath リソースパス
         * @return マニフェストファイルの場合、真
         */
        private boolean isMetaInf(String resourcePath) {
            return resourcePath.startsWith("META-INF");
        }

        /**
         * リソースパスに対応するクラスパスの{@link Set}を取得する。
         *
         * @param resourcePath リソースパス
         * @return リソースパスに対応するクラスパスのSet
         */
        private Set<ClasspathElement> getValueContainer(String resourcePath) {
            Set<ClasspathElement> elements = duplicated.get(resourcePath);
            if (elements == null) {
                elements = new LinkedHashSet<>();
                duplicated.put(resourcePath, elements);
            }
            return elements;
        }
    }
}
