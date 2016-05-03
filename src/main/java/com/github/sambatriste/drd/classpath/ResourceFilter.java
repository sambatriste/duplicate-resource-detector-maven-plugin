package com.github.sambatriste.drd.classpath;

import com.github.sambatriste.drd.util.PatternSet;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ResourceFilter {

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

    boolean applyTo(String resourcePath) {
        Pattern matchedPattern = excludedResourcePatterns.getMatched(resourcePath);
        if (matchedPattern == null) {
            return false;
        }
        filtered.add(new ExcludedResource(resourcePath, matchedPattern));
        return true;
    }

    Set<ExcludedResource> getFilteredResources() {
        return filtered;
    }

    /**
     * 実際に除外対象となったリソース。
     */
    public static class ExcludedResource {

        /** リソースパス */
        private final String resourcePath;

        /** 適用されたパターン */
        private final Pattern appliedPattern;

        /**
         * コンストラクタ。
         * @param resourcePath リソースパス
         * @param appliedPattern 適用されたパターン
         */
        ExcludedResource(String resourcePath, Pattern appliedPattern) {
            this.resourcePath = resourcePath;
            this.appliedPattern = appliedPattern;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return resourcePath + " was excluded by pattern [" + appliedPattern.pattern() + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ExcludedResource that = (ExcludedResource) o;

            if (!resourcePath.equals(that.resourcePath)) return false;
            return appliedPattern.equals(that.appliedPattern);

        }

        @Override
        public int hashCode() {
            int result = resourcePath.hashCode();
            result = 31 * result + appliedPattern.hashCode();
            return result;
        }
    }

}
