package com.github.sambatriste.drd;

import org.junit.Test;

import java.io.File;

/**
 * Created by kawasaki on 2016/04/30.
 */
public class DirectoryElementTest {


    @Test
    public void test() {
        File file = new File("target/classes");
        assert file.exists();
        DirectoryElement sut = new DirectoryElement(file);
        
        System.out.println("sut.getContents() = " + sut.getContents());
    }
}