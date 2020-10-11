
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyClassLoaderTests {
    @Test
    void loadExampleClass() {
        MyClassLoader classLoader = new MyClassLoader();
        Class loadedClass = classLoader.findClass("exampleclasses.Cat");
        assertEquals("exampleclasses.Cat", loadedClass.getName());
    }

    @Test
    void loadSelfClass() {
        MyClassLoader classLoader = new MyClassLoader();
        Class loadedClass = classLoader.findClass("MyClassLoader");
        assertEquals("MyClassLoader", loadedClass.getName());
    }
}
