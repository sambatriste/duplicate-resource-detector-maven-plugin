package com.github.sambatriste.drd;

import java.util.ArrayList;
import java.util.List;

/**
 * 重複したリソースを検出するクラス。
 */
class DuplicateResourceDetector {

    private final ClasspathElements classpathElements;

    DuplicateResourceDetector(ClasspathElements classpathElements) {
        this.classpathElements = classpathElements;
    }

    /**
     * 重複を検出する。
     *
     * @return 検出された重複リソース
     */
    DuplicatedResources detect() {
        DuplicatedResources duplicated = new DuplicatedResources();
        for (int i = 0; i < classpathElements.size() - 1; i++) {
            ClasspathElement one = classpathElements.get(i);
            for (int j = i + 1; j < classpathElements.size(); j++) {
                ClasspathElement another = classpathElements.get(j);
                doDetect(one, another, duplicated);
            }
        }
        return duplicated;
    }

    private void doDetect(ClasspathElement one,
                          ClasspathElement another,
                          DuplicatedResources duplicatedResources) {
        for (String resourcePath : extractDuplicatedResourcePaths(one, another)) {
            duplicatedResources.add(resourcePath, one, another);
        }
    }

    private static List<String> extractDuplicatedResourcePaths(
            ClasspathElement one, ClasspathElement another) {
        List<String> duplicated = new ArrayList<>();
        duplicated.addAll(one.getContents());
        duplicated.retainAll(another.getContents());
        return duplicated;
    }

}
