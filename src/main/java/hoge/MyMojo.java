package hoge;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * Goal which touches a timestamp file.
 *
 */
@Mojo(

        name = "duplicate-resource-detector",
        threadSafe = true,
        defaultPhase = LifecyclePhase.COMPILE,
        requiresProject=true
)
@Execute(
        goal = "check",
        phase = LifecyclePhase.VERIFY)
public class MyMojo extends AbstractMojo {


    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;


    @Override
    public void execute() throws MojoExecutionException {
        // http://stackoverflow.com/questions/16249393/how-to-pass-maven-module-classes-to-a-maven-plugin
        List compileClasspathElements = null;
        try {
            compileClasspathElements = project.getCompileClasspathElements();
        } catch (DependencyResolutionRequiredException e) {
            e.printStackTrace();
        }
//        List dependencies = project.getDependencies();
//        for (int i = 0; i < dependencies.size(); i++) {
//            Dependency d = (Dependency) dependencies.get(i);
//
//        }

        for (Object e : compileClasspathElements) {
            getLog().info(e.toString());
        }


    }
}
