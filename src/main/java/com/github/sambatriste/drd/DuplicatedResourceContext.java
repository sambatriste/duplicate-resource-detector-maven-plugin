package com.github.sambatriste.drd;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * {@link DuplicatedResources}の中間結果を保持するクラス。
 * META-INF配下は管理対象外とする。
 */
class DuplicatedResourceContext {

    private final ResourceFilter filter;

    /**
     * 重複したリソース。
     * キー:リソース名
     * 値:そのリソースを含んでいるクラスパス要素
     */
    private final Map<String, Set<ClasspathElement>> duplicated = new LinkedHashMap<>();

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
     * @param elements 重複したリソースを保持していたクラスパス要素
     */
    void add(String resourcePath, ClasspathElement... elements) {
        if (isMetaInf(resourcePath)) {
            return;
        }
        if (filter.applyTo(resourcePath)) {
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
        return new DuplicatedResources(duplicated, filter.getFilteredResources());
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
