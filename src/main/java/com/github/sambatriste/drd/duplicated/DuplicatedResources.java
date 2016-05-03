package com.github.sambatriste.drd.duplicated;


import com.github.sambatriste.drd.classpath.ClasspathElement;
import com.github.sambatriste.drd.classpath.ResourceFilter.ExcludedResource;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 重複したリソース。
 */
public class DuplicatedResources implements Iterable<Entry<String, Collection<ClasspathElement>>> {

    /**
     * 重複したリソース。
     * キー:リソース名
     * 値:そのリソースを含んでいるクラスパス要素
     */
    private final Map<String, Collection<ClasspathElement>> duplicated;

    /** 実際に除外されたリソース */
    private final Set<ExcludedResource> excluded;

    /**
     * コンストラクタ
     *
     * @param duplicated 重複したリソース
     * @param excluded 除外されたリソース
     */
     public DuplicatedResources(Map<String, Collection<ClasspathElement>> duplicated,
                        Set<ExcludedResource> excluded) {
        this.duplicated = Collections.unmodifiableMap(duplicated);
        this.excluded = Collections.unmodifiableSet(excluded);
    }
    /**
     *  実際に除外されたリソースを取得する。
     *
     *  @return 実際に除外されたリソース
     */
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
    public Iterator<Entry<String, Collection<ClasspathElement>>> iterator() {
        return duplicated.entrySet().iterator();
    }
}
