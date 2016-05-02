package com.github.sambatriste.drd;

import java.util.ArrayList;
import java.util.List;

/**
 * クラスパス要素のペア。
 */
class ClasspathElementPair {

    /** 片方のクラスパス要素 */
    private final ClasspathElement one;

    /** もう一方のクラスパス要素 */
    private final ClasspathElement another;

    /**
     * コンストラクタ。
     *
     * @param one 片方のクラスパス要素
     * @param another もう一方のクラスパス要素
     */
    ClasspathElementPair(ClasspathElement one, ClasspathElement another) {
        this.one = one;
        this.another = another;
    }

    /**
     * ２つのクラスパス要素で重複したリソースを検出し、
     * 引数で与えられた{@link DuplicatedResources.Builder}に追加する。
     *
     * @param builder 重複したリソース作成クラス
     */
    void appendDuplicatedResourcesTo(DuplicatedResources.Builder builder) {
        for (String resourcePath : detectDuplicatedResourcePaths()) {
            builder.add(resourcePath, one, another);
        }
    }

    /**
     * ２つのクラスパス要素から重複したリソースを抽出する。
     *
     * @return 重複したリソース全て
     */
    private List<String> detectDuplicatedResourcePaths() {
        List<String> duplicated = new ArrayList<>();
        duplicated.addAll(one.getContents());
        duplicated.retainAll(another.getContents());
        return duplicated;
    }


}
