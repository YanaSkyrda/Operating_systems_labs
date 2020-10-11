import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ClassInfoPrinter {
    String filePath;
    Class loadedClass;

    public ClassInfoPrinter(String filePath) {
        MyClassLoader classLoader = new MyClassLoader();
        this.filePath = filePath;
        this.loadedClass = classLoader.findClass(filePath);
    }
    public void printInfo() {
        System.out.println("Class from: " + filePath);
        printMainInfo();
        printInterfaces();
        printClassType();
        printFields();
        printClasses();
    }

    public void printMainInfo() {
        System.out.println("Class from package:  " + loadedClass.getPackage().getName());
        System.out.println(Modifier.toString(loadedClass.getModifiers()) + " " + loadedClass.getSimpleName());
        if (loadedClass.getSuperclass() != null) {
            System.out.println("Extends " + loadedClass.getSuperclass());
        } else {
            System.out.println("Given class is not an extension of any other class");
        }
    }
    public void printInterfaces() {
        Class[] classes = loadedClass.getInterfaces();
        if (classes.length == 0) {
            System.out.println("Given class doesn't implement interfaces.");
            return;
        } else {
            System.out.println("Interfaces implemented in given class: ");
        }
        for (Class currClass : classes) {
            System.out.println(Modifier.toString(currClass.getModifiers())
                    + " " + currClass.getName());
        }
    }

    public void printFields() {
        Field[] fields = loadedClass.getDeclaredFields();
        if (fields.length == 0) {
            System.out.println("No fields declared in given class.");
            return;
        } else {
            System.out.println("Fields in given class: ");
        }
        for (Field field : fields) {
            System.out.println(Modifier.toString(field.getModifiers())
                    + " " + field.getType().getName()
                    + " " + field.getName());
        }
    }

    public void printClasses() {
        Class[] classes = loadedClass.getDeclaredClasses();
        if (classes.length == 0) {
            System.out.println("No subclasses declared in given class.");
            return;
        } else {
            System.out.println("Subclasses in given class: ");
        }
        for (Class currClass : classes) {
            System.out.println(Modifier.toString(currClass.getModifiers())
                    + " " + currClass.getName());
        }
    }

    public void printClassType() {
        System.out.println("Is interface: " + loadedClass.isInterface());
        System.out.println("Is enum: " + loadedClass.isEnum());
    }
}
