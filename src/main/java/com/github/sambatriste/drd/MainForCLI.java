package com.github.sambatriste.drd;

import com.github.sambatriste.drd.duplicated.Controller;
import com.github.sambatriste.drd.util.Printer;
import com.github.sambatriste.drd.util.Printer.StdOutPrinter;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * CLI用メインクラス。
 * プログラム引数は以下のとおり。
 * <ol>
 * <li>ランタイムのクラスパス要素</li>
 * <li>テスト時のクラスパス要素</li>
 * <li>除外対象リソースのパターン</li>
 * </ol>
 */
public class MainForCLI {

    /**
     * メインメソッド。
     *
     * @param args プログラム引数
     */
    public static void main(String... args) {
        new MainForCLI(args).execute();
    }

    private List<String> runtimeScoped;
    private List<String> testScoped = Collections.emptyList();
    private List<String> excludes = Collections.emptyList();

    private final Printer printer = new StdOutPrinter();

    /**
     * コンストラクタ。
     * @param args プログラム引数
     */
    @SuppressWarnings("fallthrough")
    private MainForCLI(String[] args) {
        switch (args.length) {
            case 3:
                excludes = split(args[2], ",");
                // fallthrough
            case 2:
                testScoped = split(args[1], File.pathSeparator);
                // fallthrough
            case 1:
                runtimeScoped = split(args[0],File.pathSeparator);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * 文字列を分割する。
     * @param s 文字列
     * @param delimiter 区切り文字
     * @return 分割後の文字列
     */
    private static List<String> split(String s, String delimiter) {
        return Arrays.asList(s.split(delimiter));
    }

    /** 実行する。*/
    private void execute() {
        Controller controller = new Controller(
                runtimeScoped,
                testScoped,
                excludes,
                printer
        );
        controller.execute();
    }

}
