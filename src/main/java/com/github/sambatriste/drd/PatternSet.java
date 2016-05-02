package com.github.sambatriste.drd;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * {@link Pattern}の集合
 */
class PatternSet {

    /** {@link Pattern}の集合 */
    private final Set<Pattern> patterns;

    /**
     * コンストラクタ。
     *
     * @param patterns パターン文字列
     */
    PatternSet(Collection<String> patterns) {
        this.patterns = createPatterns(patterns);
    }

    /**
     * パターン文字列から{@link Pattern}の集合を生成する。
     *
     * @param patternStrings パターン文字列
     * @return {@link Pattern}の集合
     */
    private static Set<Pattern> createPatterns(Collection<String> patternStrings) {
        if (patternStrings == null) {
            return Collections.emptySet();
        }
        Set<Pattern> excludes = new HashSet<>();
        for (String patternString : patternStrings) {
            excludes.add(Pattern.compile(patternString));
        }
        return Collections.unmodifiableSet(excludes);
    }

    /**
     * 引数で与えられた文字列が、包含するパターンのいずれかにマッチするか判定する。
     * @param target 対象文字列
     * @return マッチする場合、真
     */
    boolean any(String target) {
        for (Pattern pattern : patterns) {
            boolean matches = pattern.matcher(target).matches();
            if (matches) {
                return true;
            }
        }
        return false;
    }
}
