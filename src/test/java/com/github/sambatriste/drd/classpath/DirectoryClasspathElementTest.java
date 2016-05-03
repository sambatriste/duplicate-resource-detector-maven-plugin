package com.github.sambatriste.drd.classpath;

import com.github.sambatriste.drd.classpath.DirectoryClasspathElement;
import org.junit.Test;

import java.io.File;

/**
 * {@link DirectoryClasspathElement}のテスト
 */
public class DirectoryClasspathElementTest {


    @Test
    public void test() {
        File file = new File("target/classes");
        assert file.exists();
        DirectoryClasspathElement sut = new DirectoryClasspathElement(file);
        
        System.out.println("sut.getContents() = " + sut.getContents());
    }
}