package com.github.sambatriste.drd.classpath;

import java.util.regex.Pattern;

/**
 * 実際に除外対象となったリソース。
 */
public class ExcludedResource {

    /** リソースパス */
    private final String resourcePath;

    /** 適用されたパターン */
    private final Pattern appliedPattern;

    /**
     * コンストラクタ。
     *
     * @param resourcePath   リソースパス
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
        return resourcePath.equals(that.resourcePath) &&
                appliedPattern.equals(that.appliedPattern);
    }

    @Override
    public int hashCode() {
        int result = resourcePath.hashCode();
        result = 31 * result + appliedPattern.hashCode();
        return result;
    }
}
