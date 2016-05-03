package com.github.sambatriste.drd.classpath;

import com.github.sambatriste.drd.duplicated.DuplicatedResources;
import com.github.sambatriste.drd.util.PatternSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * {@link DuplicatedResources}の中間結果を保持するクラス。
 * META-INF配下は管理対象外とする。
 */
class DuplicatedResourceContext {

    /** リソースフィルタ */
    private final ResourceFilter filter;

    /**
     * 重複したリソース。
     * キー:リソース名
     * 値:そのリソースを含んでいるクラスパス要素
     */
    private final MultiValueMapWrapper<String, ClasspathElement> duplicated = new MultiValueMapWrapper<>();

    /**
     * コンストラクタ。
     *
     * @param excludedResourcePatterns 除外対象リソースのパターン
     */
    DuplicatedResourceContext(PatternSet excludedResourcePatterns) {
        this.filter = new ResourceFilter(excludedResourcePatterns);
    }

    /**
     * 重複したリソースを登録する。
     *
     * @param resourcePath 重複したリソースのパス
     * @param elements     重複したリソースを保持していたクラスパス要素
     */
    void add(String resourcePath, ClasspathElement... elements) {
        if (isMetaInf(resourcePath) || filter.applyTo(resourcePath)) {
            return;
        }
        duplicated.add(resourcePath, elements);
    }

    /**
     * {@link DuplicatedResources}を生成する。
     *
     * @return {@link DuplicatedResources}
     */
    DuplicatedResources getResult() {
        return new DuplicatedResources(
                duplicated.map,
                filter.getFilteredResources());
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
     * 値に複数の要素を持つMapのラッパークラス。
     *
     * @param <K> キーの型
     * @param <V> 値の型
     */
    private static class MultiValueMapWrapper<K, V> {

        /** ラップ対象のMap */
        private final Map<K, Set<V>> map = new LinkedHashMap<>();

        /**
         * 重複したリソースを登録する。
         *
         * @param key         キー
         * @param valuesToAdd 追加対象の値
         */
        private void add(K key, V[] valuesToAdd) {
            add(key, Arrays.asList(valuesToAdd));
        }

        /**
         * 重複したリソースを登録する。
         *
         * @param key         キー
         * @param valuesToAdd 追加対象の値
         */
        private void add(K key, Collection<V> valuesToAdd) {
            Set<V> values = getValueContainer(key);
            values.addAll(valuesToAdd);
        }

        /**
         * キー対応する値のコレクションを取得する。
         *
         * @param key リソースパス
         * @return リソースパスに対応するクラスパスのSet
         */
        private Set<V> getValueContainer(K key) {
            Set<V> container = map.get(key);
            if (container == null) {
                container = new LinkedHashSet<>();
                map.put(key, container);
            }
            return container;
        }
    }
}
