package com.github.sambatriste.drd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tie301686 on 2016/05/02.
 */
class ClasspathElementPair {

    private final ClasspathElement one;

    private final ClasspathElement another;

    ClasspathElementPair(ClasspathElement one, ClasspathElement another) {
        this.one = one;
        this.another = another;
    }

    /**
     * ２つのクラスパス要素で重複したリソースを検出し、
     * 引数で与えられた{@link DuplicatedResources}に追加する。
     *
     * @param duplicatedResources 重複したリソース
     */
    void appendDuplicatedResourcesTo(DuplicatedResources duplicatedResources) {
        for (String resourcePath : detectDuplicatedResourcePaths()) {
            duplicatedResources.add(resourcePath, one, another);
        }
    }

    /**
     * ２つのクラスパス要素から重複したリソースを抽出する。
     *
     * @return 重複したリソース
     */
    private List<String> detectDuplicatedResourcePaths() {
        List<String> duplicated = new ArrayList<>();
        duplicated.addAll(one.getContents());
        duplicated.retainAll(another.getContents());
        return duplicated;
    }


}
