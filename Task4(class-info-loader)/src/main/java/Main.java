public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassInfoPrinter classInfo = new ClassInfoPrinter("exampleclasses.Animal");
        classInfo.printInfo();
        classInfo = new ClassInfoPrinter("exampleclasses.Cat");
        classInfo.printInfo();
    }
}
