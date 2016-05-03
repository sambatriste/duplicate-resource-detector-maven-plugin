package com.github.sambatriste.drd.classpath;

import org.junit.Test;

import java.util.List;

/**
 * Created by kawasaki on 2016/04/30.
 */
public class JarClasspathElementTest {

    @Test
    public void test() {
        JarClasspathElement sut =
                new JarClasspathElement("src/test/repository/junit/junit/3.8.1/junit-3.8.1.jar");
        List<String> contents = sut.getContents();
        System.out.println("contents = " + contents);

    }
}