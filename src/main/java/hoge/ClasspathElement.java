package hoge;

import java.util.List;

/**
 * Created by kawasaki on 2016/03/19.
 */
public interface ClasspathElement {
    public List<String> retainAll(JarClasspathElement another);
}
