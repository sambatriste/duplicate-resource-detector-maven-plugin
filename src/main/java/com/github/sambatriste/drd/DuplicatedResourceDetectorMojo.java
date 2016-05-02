package com.github.sambatriste.drd;

import com.github.sambatriste.drd.ClasspathElements.Scope;
import com.github.sambatriste.drd.Printer.MavenLoggerPrinter;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * クラスパス上に存在するリソースの重複を検出するプラグイン。
 */
@Mojo(
        name = "detect",
        requiresDependencyResolution = ResolutionScope.TEST
)
public class DuplicatedResourceDetectorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(readonly = true)
    private List<String> excludedResources;

    /** 出力先 */
    private final Printer printer = new MavenLoggerPrinter(getLog());

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {
        printer.println("start detecting.");
        printer.println("excluded resources=" + excludedResources);
        try {
            // RUNTIME scope
            @SuppressWarnings("unchecked")
            List<String> rt = project.getRuntimeClasspathElements();
            printDuplicatedElementsOf(new ClasspathElements(rt, Scope.RUNTIME));

            // TEST scope
            @SuppressWarnings("unchecked")
            List<String> test = project.getTestClasspathElements();
            printDuplicatedElementsOf(new ClasspathElements(test, Scope.TEST));

        } catch (DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        }

        printer.println("end detecting.");

    }

    /**
     * 重複したリソースを出力する。
     *
     * @param elements 調査対象となる{@link ClasspathElements}
     */
    private void printDuplicatedElementsOf(ClasspathElements elements) {
        print(elements);
        DuplicatedResources duplicated = detect(elements);
        print(duplicated);

    }

    private void print(ClasspathElements elements) {
        printer.println(elements.scope + " classpath [");
        for (ClasspathElement element : elements) {
            printer.println("    " + element);
        }
        printer.println("]");

    }

    /**
     * リソースの重複を検出する。
     *
     * @param classpathElements 調査対象のクラスパス要素
     * @return 重複したリソース
     */
    private DuplicatedResources detect(ClasspathElements classpathElements) {
        DuplicateResourceDetector detector = new DuplicateResourceDetector(
                classpathElements,
                new PatternSet(excludedResources));
        return detector.detect();
    }

    /**
     * 重複したリソースを出力する。
     *
     * @param duplicated 重複したリソース
     */
    private void print(DuplicatedResources duplicated) {
        if (duplicated.isEmpty()) {
            printer.println("No duplicated resource found.");
            return;
        }

        for (Entry<String, Set<ClasspathElement>> entry : duplicated) {
            print(entry);
        }
    }

    /**
     * 重複したリソース1件を出力する。
     *
     * @param entry 1件分のエントリ
     */
    private void print(Entry<String, Set<ClasspathElement>> entry) {
        printer.println("resource=" + entry.getKey());
        for (ClasspathElement classpathElement : entry.getValue()) {
            printer.println("    ", classpathElement);
        }
        printer.println("----------------------");
    }


}
