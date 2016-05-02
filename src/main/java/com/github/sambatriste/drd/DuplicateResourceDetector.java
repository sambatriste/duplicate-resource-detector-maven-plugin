package com.github.sambatriste.drd;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 重複したリソースを検出するクラス。
 */
class DuplicateResourceDetector {

    private final List<String> classpathElements;

    /** 除外パターン */
    private final PatternSet excludedResources;

    private final String scope;

    private final Printer printer;

    DuplicateResourceDetector(
            List<String> classpathElements,
            List<String> excludedResources,
            String scope,
            Printer printer) {
        this.classpathElements = classpathElements;
        this.excludedResources = new PatternSet(excludedResources);
        this.scope = scope;
        this.printer = printer;
    }

    /**
     * 重複したリソースを出力する。
     *
     */
    void printDuplicatedElements() {
        print(classpathElements, scope);
        DuplicatedResources duplicated = detect();
        print(duplicated);
    }

    private DuplicatedResources detect() {
        DuplicatedResources duplicated = new DuplicatedResources(excludedResources);
        ClasspathElementPairs pairs = new ClasspathElementPairs(classpathElements);
        pairs.appendDuplicatedResourcesTo(duplicated);
        return duplicated;
    }

    private void print(List<String> classpathElements, String scope) {
        printer.println(scope + " classpath [");
        for (String element : classpathElements) {
            printer.println("    " + element);
        }
        printer.println("]");

    }


    /**
     * 重複したリソースを出力する。
     *
     * @param duplicated 重複したリソース
     */
    private void print(DuplicatedResources duplicated) {
        if (duplicated.isEmpty()) {
            printer.println("No duplicated resource found.");
            return;
        }

        for (Entry<String, Set<ClasspathElement>> entry : duplicated) {
            print(entry);
        }
    }

    /**
     * 重複したリソース1件を出力する。
     *
     * @param entry 1件分のエントリ
     */
    private void print(Entry<String, Set<ClasspathElement>> entry) {
        printer.println("resource=" + entry.getKey());
        for (ClasspathElement classpathElement : entry.getValue()) {
            printer.println("    ", classpathElement);
        }
        printer.println("----------------------");
    }

}
