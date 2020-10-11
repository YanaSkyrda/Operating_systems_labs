package exampleclasses;

import java.io.Serializable;

public class Cat implements Animal, Serializable {
    private String name;
    private int age;

    public Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private void meow() {
        System.out.print("meow");
    }

    public void voice() {
        meow();
    }

    public void sleep() throws InterruptedException {
        Thread.sleep(5000);
    }

    @Override
    public String toString() {
        return "cat's name: "+ name + ", cat's age: " + age + ".";
    }
}