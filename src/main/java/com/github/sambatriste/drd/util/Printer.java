package com.github.sambatriste.drd.util;

import org.apache.maven.plugin.logging.Log;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * メッセージを出力するインタフェース。
 */
public interface Printer extends Closeable {

    /**
     * 出力する。
     *
     * @param objects 出力対象
     */
    void println(Object... objects);

    /**
     * {@link Printer}の抽象クラス。
     */
    abstract class AbstractPrinter implements Printer {
        /** {@inheritDoc} */
        @Override
        public final void println(Object... objects) {
            if (objects == null) return;
            for (Object e : objects) {
                print(e);
            }
            print("\n");
        }

        /**
         * １つのオブジェクトを出力する。
         *
         * @param object 出力対象
         */
        abstract void print(Object object);

        /** {@inheritDoc} */
        @Override
        public void close() throws IOException {
        }
    }

    /**
     * {@link Log}を出力先とする実装。
     */
    class MavenLoggerPrinter implements Printer {

        /** ログ */
        private final Log log;

        /**
         * コンストラクタ。
         *
         * @param log ログ
         */
        public MavenLoggerPrinter(Log log) {
            this.log = log;
        }

        /** {@inheritDoc} */
        @Override
        public void println(Object... objects) {
            StringBuilder s = new StringBuilder();
            for (Object object : objects) {
                s.append(object);
            }
            log.info(s.toString());
        }

        /** {@inheritDoc} */
        @Override
        public void close() throws IOException {
        }
    }

    /**
     * 標準出力を出力先とする実装。
     */
    class StdOutPrinter extends AbstractPrinter {

        /** {@inheritDoc} */
        @Override
        void print(Object object) {
            System.out.println(object);
        }
    }

    /**
     * ファイルを出力先とする実装。
     */
    class FilePrinter extends AbstractPrinter {

        /** ライター */
        private final BufferedWriter writer;

        /**
         * コンストラクタ。
         *
         * @param file 出力先ファイル
         */
        public FilePrinter(File file) {
            try {
                writer = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /** {@inheritDoc} */
        @Override
        void print(Object object) {
            try {
                writer.write(String.valueOf(object));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /** {@inheritDoc} */
        @Override
        public void close() throws IOException {
            writer.close();
        }
    }
}
