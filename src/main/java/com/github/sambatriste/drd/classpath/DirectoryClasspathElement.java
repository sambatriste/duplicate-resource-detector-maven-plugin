package com.github.sambatriste.drd.classpath;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
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
class DirectoryClasspathElement extends ClasspathElement.AbstractClasspathElement {

    /** ディレクトリ */
    private final File root;

    /**
     * コンストラクタ。
     *
     * @param classpathElement クラスパス要素
     */
    DirectoryClasspathElement(String classpathElement) {
        super(classpathElement);
        this.root = new File(classpathElement);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getContents() {

        Path start = Paths.get(root.toURI());
        RelativePathVisitor visitor = new RelativePathVisitor(start);
        try {
            Files.walkFileTree(start, visitor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return visitor.resourcesInDir;
    }

    /**
     * ディレクトリ配下のファイルを相対パスで探索する{@link FileVisitor}実装クラス。
     */
    private static class RelativePathVisitor extends SimpleFileVisitor<Path> {

        /** ディレクトリ配下のリソース */
        private final List<String> resourcesInDir = new ArrayList<>();

        /** 起点となるディレクトリ */
        private final Path root;

        /**
         * コンストラクタ。
         *
         * @param root 起点となるディレクトリ
         */
        private RelativePathVisitor(Path root) {
            this.root = root;
        }

        /** {@inheritDoc} */
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Path relative = root.relativize(file);
            resourcesInDir.add(normalize(relative));
            return FileVisitResult.CONTINUE;
        }

        /** パスを正規化する。 */
        private static String normalize(Path path) {
            return path.toString().replace('\\', '/');
        }
    }
}
