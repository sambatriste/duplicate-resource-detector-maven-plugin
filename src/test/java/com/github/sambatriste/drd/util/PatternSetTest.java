package com.github.sambatriste.drd.util;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * {@link PatternSet}のテスト
 */
public class PatternSetTest {

    @Test
    public void test() {

        List<String> p = Arrays.asList("META-INF/MANIFEST.MF", "");
        PatternSet sut = new PatternSet(p);
        assertThat(sut.any("META-INF/MANIFEST.MF"), is(true));

    }
}