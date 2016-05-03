package com.github.sambatriste.drd.duplicated;

import com.github.sambatriste.drd.util.Printer;

import java.util.List;

/**
 * 重複したリソース検出を実行するクラス。
 */
public class Controller {

    /** スコープruntimeのクラスパス要素 */
    private final List<String> runtimeScoped;

    /** スコープtestのクラスパス要素 */
    private final List<String> testScoped;

    /** 除外対象リソースのパターン */
    private final List<String> excludedResourcePatterns;

    /** 結果出力先 */
    private final Printer printer;

    /**
     * コンストラクタ。
     *
     * @param runtimeScoped            スコープruntimeのクラスパス要素
     * @param testScoped               スコープtestのクラスパス要素
     * @param excludedResourcePatterns 除外対象リソースのパターン
     * @param printer                  結果出力先
     */
    public Controller(List<String> runtimeScoped,
                      List<String> testScoped,
                      List<String> excludedResourcePatterns,
                      Printer printer) {
        this.runtimeScoped = runtimeScoped;
        this.testScoped = testScoped;
        this.excludedResourcePatterns = excludedResourcePatterns;
        this.printer = printer;
    }

    /**
     * 実行する。
     */
    public void execute() {
        detectAndPrint(runtimeScoped, "RUNTIME");
        detectAndPrint(testScoped, "TEST");
    }

    /**
     * 検出した重複を出力する。
     *
     * @param classpathElements クラスパス要素
     * @param scopeName         スコープ名
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
