package com.github.sambatriste.drd;

import java.util.ArrayList;
import java.util.List;

/**
 * クラスパス要素。
 */
interface ClasspathElement {

    /**
     * このクラスパス要素に含まれる内容物を取得する。
     *
     * @return このクラスパス要素に含まれる内容物
     */
    List<String> getContents();

    /**
     * このインスタンスの元となったクラスパス要素の文字列表現を取得する。
     * 例
     * <ur>
     * <li>target/classes</li>
     * <li>~/.m2/repository/junit/junit/3.8.2/junit-3.8.2.jar</li>
     * </ur>
     *
     * @return このインスタンスの元となったクラスパス要素
     */
    String toString();

}
