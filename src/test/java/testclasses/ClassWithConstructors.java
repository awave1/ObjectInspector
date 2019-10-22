package testclasses;

public class ClassWithConstructors extends ParentClass {
    int num;

    public ClassWithConstructors(int number) {
        this.num = number;
    }

    public ClassWithConstructors(int number, boolean bool) {
        this.num = number;
    }
}
