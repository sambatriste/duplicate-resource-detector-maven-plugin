package com.github.sambatriste.drd;

import org.junit.Test;


public class MainForCLITest {

    private String home = System.getProperty("user.home");

    private String m2Repo = home + "/.m2/repository";
    @Test
    public void test() {
        String cp = m2Repo + "/org/slf4j/slf4j-simple/1.7.7/slf4j-simple-1.7.7.jar" + ";" +
                m2Repo + "/org/slf4j/slf4j-api/1.7.7/slf4j-api-1.7.7.jar" + ";";

        MainForCLI.main(cp, cp, ".*Logger.*");
    }
}