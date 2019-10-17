import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Console.leftpad;

public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class aClass, Object obj, boolean recursive, int depth) {
        String className = aClass.getSimpleName();

        leftpad("CLASS: " + className, depth);

        inspectSuperclass(aClass, obj, recursive, depth);
        inspectInterfaces(aClass, obj, recursive, depth);
        inspectConstructor(aClass, depth);

        System.out.println();
    }

    private void inspectSuperclass(Class childClass, Object obj, boolean recursive, int depth) {
        if (childClass.equals(Object.class)) {
            return;
        }

        int indentation = depth + 1;

        if (childClass.getSuperclass() != null) {
            Class superclass = childClass.getSuperclass();
            String name = superclass.getName();
            leftpad("SUPERCLASS: " + name, indentation);
            inspectClass(superclass, obj, recursive, indentation + 1);
        }
    }

    private void inspectInterfaces(Class c, Object obj, boolean recursive, int depth) {
        List<Class> interfaces = Arrays.asList(c.getInterfaces());

        int indentation = depth + 1;

        if (!interfaces.isEmpty()) {
            for (Class i : interfaces) {
                leftpad("INTERFACE", indentation);
                String name = i.getSimpleName();
                leftpad("name: " + name, indentation + 1);
                inspectClass(i, obj, recursive, indentation + 1);
            }
        }
    }

    private void inspectConstructor(Class c, int depth) {
        Constructor[] constructors = c.getConstructors();

        int indentation = depth + 1;

        if (constructors.length > 0) {
            for (Constructor constructor : constructors) {
                leftpad("CONSTRUCTOR", indentation);
                leftpad("name: " + constructor.getName(), indentation + 1);

                String params = Arrays.stream(constructor.getParameterTypes())
                    .map(Class::getTypeName)
                    .collect(Collectors.joining(", "));

                if (!params.isEmpty()) {
                    leftpad("parameters: [" + params + "]", indentation + 1);
                }

                String modifier = Modifier.toString(constructor.getModifiers());
                leftpad("modifiers: " + modifier, indentation + 1);
            }
        }
    }
}
