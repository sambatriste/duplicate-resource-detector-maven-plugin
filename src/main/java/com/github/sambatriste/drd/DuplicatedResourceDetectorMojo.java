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

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(readonly = true)
    private List<String> excludedResources;

    /** 出力先 */
    private final Printer printer = new MavenLoggerPrinter(getLog());

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException {
        printer.println("start detecting.");
        printer.println("excluded resources=" + excludedResources);
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
        printer.println("end detecting.");

    }

    private void detectAndPrint(List<String> classpathElements, String scope) {
        DuplicateResourceDetector detector = new DuplicateResourceDetector(
                classpathElements,
                excludedResources,
                scope,
                printer
        );
        detector.printDuplicatedElements();
    }




}
