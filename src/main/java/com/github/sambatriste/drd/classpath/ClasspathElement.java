package com.github.sambatriste.drd.classpath;

import java.util.List;

/**
 * クラスパス要素。
 */
public interface ClasspathElement {

    /**
     * このクラスパス要素に含まれる内容物を取得する。
     *
     * @return このクラスパス要素に含まれる内容物
     */
    List<String> getContents();

    /**
     * このインスタンスの元となったクラスパス要素の文字列表現を取得する。
     * 例
     * <ur>
     * <li>target/classes</li>
     * <li>~/.m2/repository/junit/junit/3.8.2/junit-3.8.2.jar</li>
     * </ur>
     *
     * @return このインスタンスの元となったクラスパス要素
     */
    String toString();

    /**
     * {@link ClasspathElement}の抽象クラス。
     *
     */
    abstract class AbstractClasspathElement implements ClasspathElement {

        /** 元となったクラスパス要素 */
        private final String source;

        /**
         * コンストラクタ
         * @param classpathElement クラスパス要素
         */
        AbstractClasspathElement(String classpathElement) {
            this.source = classpathElement;
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AbstractClasspathElement that = (AbstractClasspathElement) o;

            return source != null ? source.equals(that.source) : that.source == null;

        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return source != null ? source.hashCode() : 0;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return source;
        }
    }
}
