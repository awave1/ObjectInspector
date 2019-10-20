package inspector;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Utils.join;
import static utils.Utils.leftpad;

public class Inspector {
    private InspectorResult inspectorResult;

    public InspectorResult inspectObject(Object obj, boolean isRecursive) {
        Class c = obj.getClass();
        int depth = 0;
        inspectorResult = new InspectorResult(obj);

        if (c.isArray()) {
            inspectArray(c, obj, isRecursive, depth);
            return inspectorResult;
        }

        inspectClass(c, obj, isRecursive, depth);
        return inspectorResult;
    }

    public void inspect(Object obj, boolean recursive) {
        inspectObject(obj, recursive);
    }

    private void inspectClass(Class aClass, Object obj, boolean recursive, int depth) {
        String className = aClass.getSimpleName();

        leftpad("CLASS: " + className, depth);

        inspectSuperclass(aClass, obj, recursive, depth);
        inspectInterfaces(aClass, obj, recursive, depth);
        inspectConstructors(aClass, depth);
        inspectMethods(aClass, depth);
        inspectFields(aClass, obj, recursive, depth);

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

            inspectorResult.addSuperclass(superclass);
            inspectClass(superclass, obj, recursive, indentation + 1);
        }
    }

    private void inspectInterfaces(Class c, Object obj, boolean recursive, int depth) {
        List<Class> interfaces = Arrays.asList(c.getInterfaces());

        int indentation = depth + 1;

        if (!interfaces.isEmpty()) {
            inspectorResult.addInterfaces(new ArrayList<>(interfaces));

            for (Class i : interfaces) {
                leftpad("INTERFACE", indentation);
                String name = i.getSimpleName();
                leftpad("name: " + name, indentation + 1);
                inspectMethods(c, indentation + 1);
                inspectFields(c, obj, recursive, indentation + 1);
            }
        }
    }

    private void inspectConstructors(Class c, int depth) {
        List<Constructor> constructors = Arrays.asList(c.getConstructors());

        int indentation = depth + 1;

        if (!constructors.isEmpty()) {
            inspectorResult.addConstructors(new ArrayList<>());

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

    private void inspectMethods(Class c, int depth) {
        List<Method> methods = Arrays.asList(c.getMethods());

        int indentation = depth + 1;

        if (!methods.isEmpty()) {
            inspectorResult.addMethods(new ArrayList<>(methods));

            for (Method method : methods) {
                leftpad("METHOD", indentation);
                String name = method.getName();
                String exceptions = join(method.getExceptionTypes(), Class::getName);
                String paramTypes = join(method.getParameterTypes(), Class::getTypeName);
                String returnType = method.getReturnType().getName();
                String modifier = Modifier.toString(method.getModifiers());

                leftpad("method: " + name, indentation + 1);
                leftpad("returnType: " + returnType, indentation + 1);
                leftpad("modifier: " + modifier, indentation + 1);

                if (!exceptions.isEmpty()) {
                    leftpad("exceptions: [" + exceptions + "]", indentation + 1);
                }

                if (!paramTypes.isEmpty()) {
                    leftpad("parameterTypes: [" + paramTypes + "]", indentation + 1);
                }
            }
        }
    }

    private void inspectFields(Class c, Object obj, boolean recursive, int depth) {
        List<Field> fields = Arrays.asList(c.getDeclaredFields());

        int indentation = depth + 1;

        if (!fields.isEmpty()) {
            inspectorResult.addFields(c.getName(), new ArrayList<Field>(fields));
            for (Field field : fields) {
                leftpad("FIELD", indentation);
                field.setAccessible(true);

                String name = field.getName();
                Class typeClass = field.getType();
                String modifiers = Modifier.toString(field.getModifiers());

                leftpad("name: " + name, indentation + 1);
                leftpad("type: " + typeClass.getSimpleName(), indentation + 1);
                leftpad("modifiers: " + modifiers, indentation + 1);

                try {
                    Object valueObj = field.get(obj);

                    if (typeClass.isPrimitive()) {
                        leftpad("value: " + valueObj.toString(), indentation + 1);
                    } else if (typeClass.isArray()) {
                        leftpad("value: ", indentation + 1);
                        inspectArray(typeClass, valueObj, recursive, indentation + 2);
                    } else if (valueObj == null) {
                        leftpad("value: null", indentation + 1);
                    } else {
                        if (!recursive) {
                            leftpad("referenceValue: " + valueObj.getClass().getName() + "@" + valueObj.getClass().hashCode(), indentation + 1);
                        } else {
                            leftpad("value: ", indentation + 1);
                            inspectClass(typeClass, valueObj, true, indentation + 2);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void inspectArray(Class c, Object obj, boolean recursive, int depth) {
        int length = Array.getLength(obj);

        int indentation = depth + 1;

        leftpad("Array: [", indentation, length > 0 ? "\n" : "");

        Class componentType = c.getComponentType();

        for (int i = 0; i < length; i++) {
            Object storedObj = Array.get(obj, i);

            inspectorResult.addArray(storedObj);

            if (componentType.isPrimitive()) {
                leftpad(storedObj, indentation + 1);
            } else if (componentType.isArray()) {
                inspectArray(storedObj.getClass(), storedObj, recursive, indentation + 1);
            } else if (storedObj == null) {
                leftpad("null", indentation + 1);
            } else {
                if (recursive) {
                    inspectClass(storedObj.getClass(), storedObj, true, indentation + 1);
                } else {
                    leftpad("value: " + storedObj.getClass().getName() + "@" + storedObj.getClass().hashCode(), indentation + 1);
                }
            }
        }
        leftpad("]", length > 0 ? indentation : 0);
    }

    public InspectorResult getInspectorResult() {
        return inspectorResult;
    }
}
