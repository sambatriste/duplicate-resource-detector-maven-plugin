package com.github.sambatriste.drd;


import com.github.sambatriste.drd.ResourceFilter.ExcludedResource;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 重複したリソース。
 */
class DuplicatedResources implements Iterable<Entry<String, Set<ClasspathElement>>> {

    /**
     * 重複したリソース。
     * キー:リソース名
     * 値:そのリソースを含んでいるクラスパス要素
     */
    private final Map<String, Set<ClasspathElement>> duplicated;

    private final Set<ExcludedResource> excluded;

    /**
     * コンストラクタ
     *
     * @param duplicated 重複したリソース
     * @param excluded 除外されたリソース
     */
    DuplicatedResources(Map<String, Set<ClasspathElement>> duplicated,
                        Set<ExcludedResource> excluded) {
        this.duplicated = Collections.unmodifiableMap(duplicated);
        this.excluded = Collections.unmodifiableSet(excluded);
    }

    Set<ExcludedResource> getExcludedResources() {
        return excluded;
    }

    /**
     * 空であるか判定する。
     *
     * @return 重複したリソースが存在しない場合、真
     */
    boolean isEmpty() {
        return duplicated.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Entry<String, Set<ClasspathElement>>> iterator() {
        return duplicated.entrySet().iterator();
    }

    /**
     * オブジェクトの組み立てを開始する。
     *
     * @param excludedResourcePatterns 除外対象リソースのパターン
     * @return ビルダー
     */
    static DuplicatedResourceContext startBuild(PatternSet excludedResourcePatterns) {
        return new DuplicatedResourceContext(excludedResourcePatterns);
    }

}
