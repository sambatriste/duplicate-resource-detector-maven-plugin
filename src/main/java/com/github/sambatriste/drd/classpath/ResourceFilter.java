package com.github.sambatriste.drd.classpath;

import com.github.sambatriste.drd.util.PatternSet;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

class ResourceFilter {

    /** 除外対象リソースのパターン */
    private final PatternSet excludedResourcePatterns;

    /** 実際に除外されたリソース */
    private final Set<ExcludedResource> filtered = new LinkedHashSet<>();

    /**
     * コンストラクタ。
     *
     * @param excludedResourcePatterns 除外リソースのパターン
     */
    ResourceFilter(PatternSet excludedResourcePatterns) {
        this.excludedResourcePatterns = excludedResourcePatterns;
    }

    /**
     * 引数で与えられたリソースに、本フィルターを適用する。
     * フィルターが適用された場合、その情報は本インスタンスに記録される。
     *
     * @param resourcePath 対象となるリソース
     * @return フィルターが適用された場合、真
     * @see #getFilteredResources()
     */
    boolean applyTo(String resourcePath) {
        Pattern matchedPattern = excludedResourcePatterns.getMatched(resourcePath);
        if (matchedPattern == null) {
            return false;
        }
        filtered.add(new ExcludedResource(resourcePath, matchedPattern));
        return true;
    }

    /**
     * 本フィルターにより除外されたリソースを取得する。
     *
     * @return 除外されたリソース
     */
    Set<ExcludedResource> getFilteredResources() {
        return filtered;
    }

}
