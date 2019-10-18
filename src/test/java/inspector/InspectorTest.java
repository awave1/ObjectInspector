package inspector;

import org.junit.jupiter.api.Test;
import testclasses.ClassWithOneParent;
import testclasses.SimpleClass;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InspectorTest {
    private Inspector inspector = new Inspector();

    @Test
    void testInspectWithSimpleClass_noRecursion() {
        SimpleClass simpleClass = new SimpleClass();

        inspector.inspect(simpleClass, false);

        HashMap<String, Object> objects = inspector.getClassObjects();
        HashMap<String, Object> superclasses = inspector.getSuperclassObjects();

        // TODO: Remove print statements.
        //       or not.
        System.out.println(superclasses);
        System.out.println(objects);

        assertEquals(1, superclasses.size());

        assertEquals(2, objects.size());
        assertNull(objects.get("dummy"));
        assertNotNull(objects.get("notDummy"));
    }

    @Test
    void testInspectWithClassWithOneParent_noRecursion() {
        ClassWithOneParent classWithOneParent = new ClassWithOneParent();

        inspector.inspect(classWithOneParent, false);

        HashMap<String, Object> objects = inspector.getClassObjects();
        HashMap<String, Object> superclasses = inspector.getSuperclassObjects();

        // TODO: Remove print statements.
        //       or not.
        System.out.println(objects);
        System.out.println(superclasses);

        assertEquals(2, superclasses.size());
        assertTrue(superclasses.containsKey("testclasses.ParentClass"));

        assertEquals(2, objects.size());
        assertNull(objects.get("simpleDummy"));
        assertNotNull(objects.get("simpleNotDummy"));
    }
}