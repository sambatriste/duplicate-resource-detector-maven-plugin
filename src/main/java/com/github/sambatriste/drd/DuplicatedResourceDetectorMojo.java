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

import java.util.Map.Entry;
import java.util.Set;

/**
 * クラスパス上に存在するリソースの重複を検出するプラグイン。
 */
@Mojo(
        name = "detect",
        requiresDependencyResolution = ResolutionScope.RUNTIME
)
public class DuplicatedResourceDetectorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    /** 出力先 */
    private final Printer printer = new MavenLoggerPrinter(getLog());

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {
        printer.println("start detecting.");

        // RUNTIME scope
        printDuplicatedElementsOf(getProjectRuntimeClasspathElements());
        // TEST scope
        printDuplicatedElementsOf(getProjectTestClasspathElements());

        printer.println("end detecting.");

    }

    /**
     * 重複したリソースを出力する。
     *
     * @param elements 調査対象となる{@link ClasspathElements}
     */
    private void printDuplicatedElementsOf(ClasspathElements elements) {
        printer.println(elements.scope + " classpath=" + elements.elements);
        DuplicatedResources duplicated = detect(elements);
        print(duplicated);

    }

    /**
     * リソースの重複を検出する。
     *
     * @param classpathElements 調査対象のクラスパス要素
     * @return 重複したリソース
     */
    private DuplicatedResources detect(ClasspathElements classpathElements) {
        DuplicateResourceDetector detector = new DuplicateResourceDetector(classpathElements);
        return detector.detect();
    }

    /**
     * 重複したリソースを出力する。
     *
     * @param duplicated 重複したリソース
     */
    private void print(DuplicatedResources duplicated) {
        if (duplicated.isEmpty()) {
            printer.println("no detected resource found.");
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


    /**
     * プロジェクトのRuntimeクラスパス要素を取得する。
     *
     * @return Runtimeクラスパス要素
     */
    @SuppressWarnings("unchecked")
    private ClasspathElements getProjectRuntimeClasspathElements() {
        try {
            return new ClasspathElements(project.getRuntimeClasspathElements(), Scope.RUNTIME);
        } catch (DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * プロジェクトのCompileクラスパス要素を取得する。
     *
     * @return Compileクラスパス要素
     */
    @SuppressWarnings("unchecked")
    private ClasspathElements getProjectTestClasspathElements() {
        try {
            return new ClasspathElements(project.getTestClasspathElements(), Scope.TEST);
        } catch (DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        }
    }
}
