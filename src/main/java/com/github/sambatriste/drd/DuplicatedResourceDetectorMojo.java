package com.github.sambatriste.drd;


import com.github.sambatriste.drd.duplicated.Executor;
import com.github.sambatriste.drd.util.Printer;
import com.github.sambatriste.drd.util.Printer.MavenLoggerPrinter;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.util.List;

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

    /**
     * 除外するリソースのパターン（正規表現）
     * @see java.util.regex.Pattern
     */
    @Parameter
    private List<String> excludedResources;

    /** 出力先 */
    private final Printer printer = new MavenLoggerPrinter(getLog());

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {
        try {
            detectAndPrint();
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException(
                    "artifact file has not been resolved.",
                    e);
        }
    }

    /**
     * 重複を検出し、出力する。
     * @throws DependencyResolutionRequiredException
     */
    private void detectAndPrint() throws DependencyResolutionRequiredException {

        // RUNTIME scope
        @SuppressWarnings("unchecked")
        List<String> runtimeScoped = project.getRuntimeClasspathElements();
        // TEST scope
        @SuppressWarnings("unchecked")
        List<String> testScoped = project.getTestClasspathElements();

        Executor executor = new Executor(
                runtimeScoped,
                testScoped,
                excludedResources,
                printer
        );
        executor.execute();

    }

}
