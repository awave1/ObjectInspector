package testclasses;

public class ClassWithMethods implements SomeInterface {
    public void foo() {
        String bar = "bar";
    }

    private String coolString() {
        return "ayo";
    }

    protected int number() {
        return 42;
    }

    boolean isFalse() {
        return true;
    }

    @Override
    public void doStuff() {

    }
}
