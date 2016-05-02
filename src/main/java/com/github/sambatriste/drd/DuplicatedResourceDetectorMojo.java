package com.github.sambatriste.drd;


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

    /** Mavenプロジェクト */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    /** 除外するリソースのパターン（正規表現）*/
    @Parameter(readonly = true)
    private List<String> excludedResources;

    /** 出力先 */
    private final ResultPrinter resultPrinter = new ResultPrinter(new MavenLoggerPrinter(getLog()));

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException {
        resultPrinter.printExcludedResources(excludedResources);
        try {
            // RUNTIME scope
            List<String> runtimeScoped = project.getRuntimeClasspathElements();
            detectAndPrint(runtimeScoped, "RUNTIME");

            // TEST scope
            List<String> testScoped = project.getTestClasspathElements();
            detectAndPrint(testScoped, "TEST");
        } catch (DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 検出した重複を出力する
     * @param classpathElements クラスパス要素
     * @param scopeName スコープ名
     */
    private void detectAndPrint(List<String> classpathElements, String scopeName) {
        DuplicateResourceDetector detector = new DuplicateResourceDetector(
                classpathElements,
                excludedResources,
                scopeName,
                resultPrinter
        );
        detector.printDuplicatedElements();
    }

}
