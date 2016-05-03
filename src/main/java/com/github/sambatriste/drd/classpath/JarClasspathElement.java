package com.github.sambatriste.drd.classpath;

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
    JarClasspathElement(String classpathElement) {
        this.source = classpathElement;
        this.jarFile = new File(classpathElement);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getContents() {
        return tvf(jarFile);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return source;
    }

    /**
     * 引数で与えられたファイルをzipファイルとして読み込み、そのエントリを取得する。
     * ただし、ディレクトリは除外する。
     *
     * @param file 対象となるファイル
     * @return zipファイルのエントリ一覧
     */
    private static List<String> tvf(File file) {
        ZipFile jar = toZipFile(file);
        List<String> contents = new ArrayList<>(jar.size());
        Enumeration<? extends ZipEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            addIfNotDirectory(entries.nextElement(), contents);
        }
        return contents;
    }

    /**
     * エントリがディレクトリでなければListに追加する。
     *
     * @param entry エントリ
     * @param content List
     */
    private static void addIfNotDirectory(ZipEntry entry, List<String> content) {
        if (!entry.isDirectory()) {
            content.add(entry.getName());
        }
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

}
