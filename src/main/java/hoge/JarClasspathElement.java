package hoge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by kawasaki on 2016/03/16.
 */
public class JarClasspathElement implements ClasspathElement {

    private final String container;

    private final List<String> content;


    public JarClasspathElement(String container) {
        this.container = container;
        this.content = tvf(container);
    }

    List<String> getContent() {
        return content;
    }

    @Override
    public List<String> retainAll(JarClasspathElement another) {
        List<String> duplicated = new ArrayList<String>(content);
        duplicated.retainAll(another.content);
        return duplicated;
    }

    private static List<String> tvf(String pathToArchive) {
        ZipFile jar = toZipFile(pathToArchive);
        Enumeration<? extends ZipEntry> entries = jar.entries();
        List<String> content = new ArrayList<String>(jar.size());
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            content.add(entry.getName());
        }
        return content;
    }

    private static ZipFile toZipFile(String pathToArchive) {
        File f = new File(pathToArchive);
        assert f.exists();
        try {
            return new ZipFile(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
