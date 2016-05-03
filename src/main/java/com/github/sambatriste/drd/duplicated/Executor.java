package com.github.sambatriste.drd.duplicated;

import com.github.sambatriste.drd.util.Printer;

import java.util.List;

/**
 *
 */
public class Executor {

    private final List<String> runtimeScoped;

    private final List<String> testScoped;

    private final List<String> excludedResourcePatterns;

    private final Printer printer;

    public Executor(List<String> runtimeScoped, List<String> testScoped, List<String> excludedResourcePatterns,
                    Printer printer) {
        this.runtimeScoped = runtimeScoped;
        this.testScoped = testScoped;
        this.excludedResourcePatterns = excludedResourcePatterns;
        this.printer = printer;
    }

    public void execute() {
        detectAndPrint(runtimeScoped, "RUNTIME");
        detectAndPrint(testScoped, "TEST");
    }

    /**
     * 検出した重複を出力する
     * @param classpathElements クラスパス要素
     * @param scopeName スコープ名
     */
    private void detectAndPrint(List<String> classpathElements, String scopeName) {
        DuplicateResourceDetector detector = new DuplicateResourceDetector(
                classpathElements,
                excludedResourcePatterns,
                scopeName,
                new ResultPrinter(printer)
        );
        detector.printDuplicatedElements();
    }
}
