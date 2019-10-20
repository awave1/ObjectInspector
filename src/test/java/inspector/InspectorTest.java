package inspector;

import org.junit.jupiter.api.Test;
import testclasses.ClassWithOneParent;
import testclasses.SimpleClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InspectorTest {
    private Inspector inspector = new Inspector();

    @Test
    void testInspectWithSimpleClass_noRecursion() {
        SimpleClass simpleClass = new SimpleClass();

        InspectorResult result = inspector.inspectObject(simpleClass, false);

        HashMap<String, ArrayList<Field>> objects = result.getFields();
        HashMap<String, Class> superclasses = result.getSuperclasses();

        System.out.println(superclasses);
        System.out.println(objects);

        assertEquals(1, superclasses.size());
        assertEquals(2, objects.get(result.getClassName()).size());

        assertTrue(result.findField("dummy").isPresent());
        assertNull(result.findField("dummy").get().value);

        assertTrue(result.findField("notDummy").isPresent());
        assertNotNull(result.findField("notDummy").get().value);
    }
}