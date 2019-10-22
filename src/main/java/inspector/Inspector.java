package inspector;

import utils.Console;
import utils.IndentedOutput;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static utils.Utils.join;
import static utils.Console.leftpad;

public class Inspector {
    private InspectorResult inspectorResult;

    public InspectorResult inspectObject(Object obj, boolean isRecursive) {
        Class c = obj.getClass();
        int depth = 0;
        inspectorResult = new InspectorResult(obj);

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

        if (aClass.isArray()) {
            inspectArray(aClass, obj, recursive, depth);
        }
    }

    private void inspectSuperclass(Class childClass, Object obj, boolean recursive, int depth) {
        if (childClass.getSuperclass() != null) {
            Class superclass = childClass.getSuperclass();
            String name = superclass.getName();
            inspectorResult.addSuperclass(childClass.getName(), superclass);

            indentOutput(depth, indentation -> {
                leftpad("SUPERCLASS: " + name, indentation);
                inspectClass(superclass, obj, recursive, indentation + 2);
            });
        }
    }

    private void inspectInterfaces(Class c, Object obj, boolean recursive, int depth) {
        List<Class> interfaces = Arrays.asList(c.getInterfaces());
        if (!interfaces.isEmpty()) {
            inspectorResult.addInterfaces(new ArrayList<>(interfaces));

            indentOutput(depth, indentation -> {
                for (Class i : interfaces) {
                    String name = i.getSimpleName();
                    leftpad("INTERFACE", indentation);
                    leftpad("name: " + name, indentation + 2);
                    inspectMethods(c, indentation + 2);
                    inspectFields(c, obj, recursive, indentation + 2);
                }
            });
        }
    }

    private void inspectConstructors(Class c, int depth) {
        List<Constructor> constructors = Arrays.asList(c.getConstructors());
        if (!constructors.isEmpty()) {
            inspectorResult.addConstructors(c.getName(), new ArrayList<>(constructors));
            indentOutput(depth, indentation -> {
                for (Constructor constructor : constructors) {
                    leftpad("CONSTRUCTOR", indentation);
                    leftpad("name: " + constructor.getName(), indentation + 2);

                    String params = Arrays.stream(constructor.getParameterTypes())
                        .map(Class::getTypeName)
                        .collect(Collectors.joining(", "));

                    if (!params.isEmpty()) {
                        leftpad("parameters: [" + params + "]", indentation + 2);
                    }

                    String modifier = Modifier.toString(constructor.getModifiers());
                    leftpad("modifiers: " + modifier, indentation + 2);
                }
            });
        }
    }

    private void inspectMethods(Class c, int depth) {
        List<Method> methods = Arrays.asList(c.getDeclaredMethods());
        if (!methods.isEmpty()) {
            inspectorResult.addMethods(new ArrayList<>(methods));
            indentOutput(depth, indentation -> {
                for (Method method : methods) {
                    leftpad("METHOD", indentation);
                    String name = method.getName();
                    String exceptions = join(method.getExceptionTypes(), Class::getName);
                    String paramTypes = join(method.getParameterTypes(), Class::getTypeName);
                    String returnType = method.getReturnType().getName();
                    String modifier = Modifier.toString(method.getModifiers());

                    leftpad("method: " + name, indentation + 2);
                    leftpad("returnType: " + returnType, indentation + 2);
                    leftpad("modifier: " + modifier, indentation + 2);

                    if (!exceptions.isEmpty()) {
                        leftpad("exceptions: [" + exceptions + "]", indentation + 2);
                    }

                    if (!paramTypes.isEmpty()) {
                        leftpad("parameterTypes: [" + paramTypes + "]", indentation + 2);
                    }
                }
            });
        }
    }

    private void inspectFields(Class c, Object obj, boolean recursive, int depth) {
        List<Field> fields = Arrays.asList(c.getDeclaredFields());
        if (!fields.isEmpty()) {
            inspectorResult.addFields(c.getName(), new ArrayList<Field>(fields));
            indentOutput(depth, indentation -> {
                for (Field field : fields) {
                    leftpad("FIELD", indentation);
                    field.setAccessible(true);

                    String name = field.getName();
                    Class typeClass = field.getType();
                    String modifiers = Modifier.toString(field.getModifiers());

                    leftpad("name: " + name, indentation + 2);
                    leftpad("type: " + typeClass.getSimpleName(), indentation + 2);
                    leftpad("modifiers: " + modifiers, indentation + 2);

                    try {
                        Object valueObj = field.get(obj);
                        inspectValues(typeClass, valueObj, indentation, recursive);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void inspectArray(Class c, Object obj, boolean recursive, int depth) {
        indentOutput(depth, indentation -> {
            int length = Array.getLength(obj);

            Class componentType = c.getComponentType();

            leftpad("ARRAY", indentation);
            leftpad("componentType: " + componentType.getName(), indentation + 2);
            leftpad("length: " + length, indentation + 2);
            leftpad("contents: [", indentation + 2, length > 0 ? "\n" : "");

            forEach(obj, element -> {
                inspectorResult.addArray(element);
                inspectValues(componentType, element, indentation + 2, recursive);
            });

            leftpad("]", length > 0 ? indentation : 0);
        });
    }

    private void inspectValues(Class valueClass, Object valueObject, int indentation, boolean recursive) {
        if (valueClass.isPrimitive()) {
            leftpad("value: " + valueObject.toString(), indentation + 2);
        } else if (valueClass.isArray()) {
            leftpad("value: ", indentation + 2);
            inspectArray(valueClass, valueObject, recursive, indentation + 2);
        } else if (valueObject == null) {
            leftpad("value: null", indentation + 2);
        } else {
            if (!recursive) {
                leftpad("referenceValue: " + getReferenceFormat(valueObject), indentation + 2);
            } else {
                leftpad("value: ", indentation + 2);
                inspectClass(valueObject.getClass(), valueObject, true, indentation + 2);
            }
        }
    }

    private void indentOutput(int depth, IndentedOutput indentedOutput) {
        int indentation = depth + 2;
        indentedOutput.indent(indentation);
    }

    private void forEach(Object obj, Consumer action) {
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object element = Array.get(obj, i);
                action.accept(element);
            }
        }
    }

    private String getReferenceFormat(Object o) {
        return o.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(o));
    }

    public void setHasOutput(boolean hasOutput) {
        Console.enableOutput = hasOutput;
    }
}
