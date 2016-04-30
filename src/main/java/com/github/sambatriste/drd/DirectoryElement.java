package com.github.sambatriste.drd;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ClasspathElement}のディレクトリ実装クラス。
 */
class DirectoryElement implements ClasspathElement {

    /** ディレクトリ */
    private final File root;

    /**
     * コンストラクタ。
     *
     * @param dir ディレクトリ
     */
    DirectoryElement(File dir) {
        this.root = dir;
    }


    private static class Walker extends SimpleFileVisitor<Path> {

        private final List<String> resourcesInDir = new ArrayList<>();

        private final Path root;

        private Walker(Path root) {
            this.root = root;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Path relative = root.relativize(file);
            resourcesInDir.add(relative.toString());
            return FileVisitResult.CONTINUE;
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getContents() {

        Path start = Paths.get(root.toURI());
        Walker walker = new Walker(start);
        try {
            Files.walkFileTree(start, walker);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return walker.resourcesInDir;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return root.getPath();
    }
}
