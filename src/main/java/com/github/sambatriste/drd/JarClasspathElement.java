package com.github.sambatriste.drd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * {@link ClasspathElement}のjarファイル実装クラス。
 */
class JarClasspathElement implements ClasspathElement {

    /** Jarファイル */
    private final File jarFile;

    /** クラスパス要素 */
    private final String source;

    /**
     * コンストラクタ。
     *
     * @param classpathElement クラスパス要素
     */
    public JarClasspathElement(String classpathElement) {
        this.source = classpathElement;
        this.jarFile = new File(classpathElement);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getContents() {
        return tvf(jarFile);
    }

    /**
     * 引数で与えられたファイルをzipファイルとして読み込み、
     * そのエントリを取得する。
     *
     * @param file 対象となるファイル
     * @return zipファイルのエントリ一覧
     */
    private static List<String> tvf(File file) {
        ZipFile jar = toZipFile(file);
        Enumeration<? extends ZipEntry> entries = jar.entries();
        List<String> content = new ArrayList<>(jar.size());
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.isDirectory()) {
                content.add(entry.getName());
            }
        }
        return content;
    }

    /**
     * ファイルをzipファイルとしてオープンする。
     *
     * @param jar 対象ファイル
     * @return zipファイル
     */
    private static ZipFile toZipFile(File jar) {
        assert jar.exists();
        try {
            return new ZipFile(jar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return source;
    }
}
