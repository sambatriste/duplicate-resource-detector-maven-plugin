package com.github.sambatriste.drd.classpath;

import com.github.sambatriste.drd.duplicated.DuplicatedResources;
import com.github.sambatriste.drd.util.MultiValueMapWrapper;
import com.github.sambatriste.drd.util.PatternSet;

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
                duplicated.unwrap(),
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

}
