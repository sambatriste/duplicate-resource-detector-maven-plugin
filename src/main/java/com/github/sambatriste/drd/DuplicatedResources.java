package com.github.sambatriste.drd;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 重複したリソース。
 * ただし、META-INF/MANIFEST.MFは管理対象外とする。
 */
class DuplicatedResources implements Iterable<Entry<String, Set<ClasspathElement>>> {

    /**
     * 重複したリソース。
     * キー:リソース名
     * 値:そのリソースを含んでいるクラスパス要素
     */
    private final Map<String, Set<ClasspathElement>> duplicated = new LinkedHashMap<>();

    /**
     * 重複したリソースを登録する。
     *
     * @param resourcePath 重複したリソースのパス
     * @param elements     重複したリソースを保持していたクラスパス要素
     */
    void add(String resourcePath, ClasspathElement... elements) {
        if (isManifest(resourcePath)) {
            return;
        }
        Set<ClasspathElement> container = getContainer(resourcePath);
        container.addAll(Arrays.asList(elements));
        assert container.size() > 1;
    }

    /**
     * リソースがマニフェストファイルかどうか判定する。
     *
     * @param resourcePath リソースパス
     * @return マニフェストファイルの場合、真
     */
    private boolean isManifest(String resourcePath) {
        return resourcePath.equals("META-INF/MANIFEST.MF");
    }

    /**
     * リソースパスに対応するクラスパスの{@link Set}を取得する。
     *
     * @param resourcePath リソースパス
     * @return リソースパスに対応するクラスパスのSet
     */
    private Set<ClasspathElement> getContainer(String resourcePath) {
        Set<ClasspathElement> elements = duplicated.get(resourcePath);
        if (elements == null) {
            elements = new LinkedHashSet<>();
            duplicated.put(resourcePath, elements);
        }
        return elements;
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
}
