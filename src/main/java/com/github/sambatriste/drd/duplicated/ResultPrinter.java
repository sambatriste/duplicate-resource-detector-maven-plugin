package com.github.sambatriste.drd.duplicated;

import com.github.sambatriste.drd.classpath.ClasspathElement;
import com.github.sambatriste.drd.classpath.ClasspathElements;
import com.github.sambatriste.drd.classpath.ResourceFilter.ExcludedResource;
import com.github.sambatriste.drd.util.PatternSet;
import com.github.sambatriste.drd.util.Printer;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;


/**
 * 結果を出力するクラス。
 */
class ResultPrinter {

    /** タブ */
    private static final String TAB = "    ";

    /** hr */
    private static final String HR = "------------------------------";

    /** 出力先 */
    private final Printer printer;

    /**
     * コンストラクタ。
     * @param printer 出力先
     */
    ResultPrinter(Printer printer) {
        this.printer = printer;
    }

    /**
     * 出力する。
     *
     * @param classpathElements クラスパス要素
     * @param scopeName スコープ名
     */
    void print(ClasspathElements classpathElements, String scopeName) {
        printer.println(scopeName + " classpath [");
        for (String element : classpathElements) {
            printer.println(TAB, element);
        }
        printer.println("]");

    }

    /**
     * 重複したリソースを出力する。
     *
     * @param duplicated 重複したリソース
     */
    void print(DuplicatedResources duplicated) {
        print(duplicated.getExcludedResources());

        if (duplicated.isEmpty()) {
            printer.println("No duplicated resource found.");
            return;
        }


        for (Entry<String, Collection<ClasspathElement>> entry : duplicated) {
            print(entry);
        }
    }

    /**
     * 除外されたリソースを出力する。
     * @param excluded 除外されたリソース
     */
    private void print(Set<ExcludedResource> excluded) {
        if (excluded.isEmpty()) {
            printer.println("No resource excluded.");
            return;
        }

        printer.println("excluded resources ", HR);
        for (ExcludedResource e : excluded) {
            printer.println(TAB, e);
        }
        printer.println(HR);
    }

    /**
     * 重複したリソース1件を出力する。
     *
     * @param entry 1件分のエントリ
     */
    private void print(Entry<String, Collection<ClasspathElement>> entry) {
        printer.println("resource=" + entry.getKey());
        for (ClasspathElement classpathElement : entry.getValue()) {
            printer.println(TAB, classpathElement);
        }
        printer.println(HR);
    }

    void printExcludedResources(PatternSet excludedResources) {
        printer.println("excluded resource patterns=" + excludedResources);
    }

}
