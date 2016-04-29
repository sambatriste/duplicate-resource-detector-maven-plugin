package hoge;

import java.util.List;

/**
 * Created by kawasaki on 2016/03/17.
 */
public class ClasspathSetting {

    List<ClasspathElement> classpathElements;

    public void detectDuplicate() {

        for (int i = 0; i < classpathElements.size(); i++) {
            ClasspathElement a = classpathElements.get(i);
            for (int j = i; j < classpathElements.size(); j++) {
                ClasspathElement b = classpathElements.get(j);
            }
        }
    }
}
