package inspector;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testclasses.ClassWithConstructors;
import testclasses.ClassWithMethods;
import testclasses.ClassWithOneParent;
import testclasses.SimpleClass;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InspectorTest {
    private static Inspector inspector = new Inspector();

    @BeforeAll
    static void beforeAll() {
        inspector.setHasOutput(false);
    }

    @AfterAll
    static void afterAll() {
        inspector.setHasOutput(true);
    }

    @Test
    void testInspectWithSimpleClass_noRecursion() {
        SimpleClass simpleClass = new SimpleClass();

        InspectorResult result = inspector.inspectObject(simpleClass, false);

        HashMap<String, ArrayList<Field>> objects = result.getFields();
        HashMap<String, Class> superclasses = result.getSuperclasses();

        assertEquals(1, superclasses.size());
        assertEquals(4, objects.get(result.getClassName()).size());

        assertTrue(result.findField("number").isPresent());
        assertEquals(result.findField("number").get().value, 0);

        assertTrue(result.findField("aBoolean").isPresent());
        assertFalse((Boolean) result.findField("aBoolean").get().value);

        assertTrue(result.findField("dummy").isPresent());
        assertNull(result.findField("dummy").get().value);

        assertTrue(result.findField("notDummy").isPresent());
        assertNotNull(result.findField("notDummy").get().value);
    }

    @Test
    void testInspectWithSimpleClass_withRecursion() {
        SimpleClass simpleClass = new SimpleClass();

        InspectorResult result = inspector.inspectObject(simpleClass, true);

        HashMap<String, ArrayList<Field>> objects = result.getFields();
        HashMap<String, Class> superclasses = result.getSuperclasses();

        assertEquals(1, superclasses.size());
        assertEquals(4, objects.get(result.getClassName()).size());

        assertTrue(result.findField("number").isPresent());
        assertEquals(result.findField("number").get().value, 0);

        assertTrue(result.findField("aBoolean").isPresent());
        assertFalse((Boolean) result.findField("aBoolean").get().value);

        assertTrue(result.findField("dummy").isPresent());
        assertNull(result.findField("dummy").get().value);

        assertTrue(result.findField("notDummy").isPresent());
        assertNotNull(result.findField("notDummy").get().value);
    }

    @Test
    void testClassWithOneParent_noRecursion() {
        ClassWithOneParent classWithOneParent = new ClassWithOneParent();
        InspectorResult result = inspector.inspectObject(classWithOneParent, false);

        HashMap<String, Class> superclasses = result.getSuperclasses();

        assertEquals(2, superclasses.size());
        assertEquals(
            classWithOneParent.getClass().getSuperclass().getName(),
            superclasses.get(classWithOneParent.getClass().getName()).getName()
        );
    }

    @Test
    void testClassWithOneParent_withRecursion() {
        ClassWithOneParent classWithOneParent = new ClassWithOneParent();
        InspectorResult result = inspector.inspectObject(classWithOneParent, true);

        HashMap<String, Class> superclasses = result.getSuperclasses();

        assertEquals(3, superclasses.size());
        assertEquals(
            classWithOneParent.getClass().getSuperclass().getName(),
            superclasses.get(classWithOneParent.getClass().getName()).getName()
        );
        assertEquals(
            superclasses.get(classWithOneParent.getClass().getName()).getSuperclass().getName(),
            Object.class.getName()
        );
    }

    @Test
    void testClassWithMethods_noRecursion() {
        ClassWithMethods classWithMethods = new ClassWithMethods();
        InspectorResult result = inspector.inspectObject(classWithMethods, false);

        HashMap<String, ArrayList<Method>> methods = result.getMethods();
        HashMap<String, ArrayList<Class>> interfaces = result.getInterfaces();

        ArrayList<Method> methodList = methods.get(classWithMethods.getClass().getName());
        ArrayList<String> actualMethodNames = new ArrayList<String>(){{
            add("foo");
            add("coolString");
            add("number");
            add("isFalse");
            add("doStuff");
        }};

        for (Method m : methodList) {
            assertTrue(actualMethodNames.contains(m.getName()));
        }

        assertFalse(interfaces.get(classWithMethods.getClass().getName()).isEmpty());
    }

    @Test
    void testClassWithMethods_withRecursion() {
        ClassWithMethods classWithMethods = new ClassWithMethods();
        InspectorResult result = inspector.inspectObject(classWithMethods, true);

        HashMap<String, ArrayList<Method>> methods = result.getMethods();
        HashMap<String, ArrayList<Class>> interfaces = result.getInterfaces();

        ArrayList<Method> methodList = methods.get(classWithMethods.getClass().getName());

        ArrayList<String> actualMethodNames = new ArrayList<String>(){{
            add("foo");
            add("coolString");
            add("number");
            add("isFalse");
            add("doStuff");
        }};

        for (Method m : methodList) {
            assertTrue(actualMethodNames.contains(m.getName()));
        }

        assertFalse(interfaces.get(classWithMethods.getClass().getName()).isEmpty());
    }

    @Test
    void testClassWithConstructors_noRecursion() {
        ClassWithConstructors classWithConstructors = new ClassWithConstructors(42);
        InspectorResult result = inspector.inspectObject(classWithConstructors, false);

        HashMap<String, ArrayList<Constructor>> constructors = result.getConstructors();
        ArrayList<Constructor> constructorList = constructors.get(classWithConstructors.getClass().getName());

        assertFalse(constructorList.isEmpty());
        assertEquals(2, constructorList.size());
    }

    @Test
    void testClassWithConstructors_withRecursion() {
        ClassWithConstructors classWithConstructors = new ClassWithConstructors(42);
        InspectorResult result = inspector.inspectObject(classWithConstructors, true);

        HashMap<String, ArrayList<Constructor>> constructors = result.getConstructors();
        ArrayList<Constructor> constructorList = constructors.get(classWithConstructors.getClass().getName());
        ArrayList<Constructor> parentConstructors = constructors.get(classWithConstructors.getClass().getSuperclass().getName());

        assertFalse(constructorList.isEmpty());
        assertFalse(parentConstructors.isEmpty());
        assertEquals(2, constructorList.size());
        assertEquals(2, parentConstructors.size());
    }

    @Test
    void testPrimitiveArray() {
        int[] arr = new int[]{ 1, 2, 3, 4, 5 };
        InspectorResult result = inspector.inspectObject(arr, false);
        assertArrays(arr, result);
    }

    @Test
    void testObjectArray_noRecursion() {
        String[] arr = new String[]{ "list", "of", "strings" };
        InspectorResult result = inspector.inspectObject(arr, false);
        assertArrays(arr, result);
    }

    @Test
    void testObjectArray_withRecursion() {
        SimpleClass[] arr = new SimpleClass[]{
            new SimpleClass(),
            new SimpleClass(),
            new SimpleClass()
        };
        InspectorResult result = inspector.inspectObject(arr, true);
        assertArrays(arr, result);
    }

    private static void assertArrays(Object arr, InspectorResult result) {
        HashMap<String, ArrayList<Object>> arrays = result.getArrays();

        for (Map.Entry<String, ArrayList<Object>> entry : arrays.entrySet()) {
            assertEquals(Array.getLength(arr), entry.getValue().size());

            for (int i = 0; i < Array.getLength(arr); i++) {
                assertEquals(Array.get(arr, i), entry.getValue().get(i));
            }
        }
    }
}