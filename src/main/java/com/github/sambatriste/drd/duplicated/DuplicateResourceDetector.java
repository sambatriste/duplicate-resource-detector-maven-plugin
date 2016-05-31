package com.github.sambatriste.drd.duplicated;

import com.github.sambatriste.drd.classpath.ClasspathElements;
import com.github.sambatriste.drd.util.PatternSet;

import java.util.List;

/**
 * 重複したリソースを検出するクラス。
 */
class DuplicateResourceDetector {

    /** クラスパス要素 */
    private final ClasspathElements classpathElements;

    /** 除外パターン */
    private final PatternSet excludedResources;

    /** スコープ名 */
    private final String scopeName;

    /** 出力先 */
    private final ResultPrinter resultPrinter;

    /**
     * コンストラクタ。
     *
     * @param classpathElements クラスパス要素
     * @param excludedResources 除外対象リソースのパターン
     * @param scopeName スコープ名
     * @param resultPrinter 出力先
     */
    DuplicateResourceDetector(
            List<String> classpathElements,
            List<String> excludedResources,
            String scopeName,
            ResultPrinter resultPrinter) {
        this.classpathElements = new ClasspathElements(classpathElements);
        this.excludedResources = new PatternSet(excludedResources);
        this.scopeName = scopeName;
        this.resultPrinter = resultPrinter;
    }


    /**
     * 重複したリソースを出力する。
     */
    void printDuplicatedElements() {
        resultPrinter.print(classpathElements, scopeName);
        DuplicatedResources duplicated = detect();
        resultPrinter.print(duplicated);
    }

    /**
     * 重複したリソースを検出する。
     *
     * @return 重複したリソース
     */
    private DuplicatedResources detect() {
        return classpathElements.detectDuplicated(excludedResources);
    }
}
