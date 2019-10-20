package inspector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class InspectorResult {
    private Object objectToInspect;
    private Class classToInspect;
    private String className;

    private HashMap<String, Class> superclasses = new HashMap<>();

    private HashMap<String, ArrayList<Field>> fields = new HashMap<>();

    private HashMap<String, ArrayList<Method>> methods = new HashMap<>();

    private HashMap<String, ArrayList<Constructor>> constructors = new HashMap<>();

    private HashMap<String, ArrayList<Class>> interfaces = new HashMap<>();

    private HashMap<String, ArrayList<Object>> arrays = new HashMap<>();

    public InspectorResult(Object objectToInspect) {
        this.objectToInspect = objectToInspect;
        this.classToInspect = objectToInspect.getClass();
        this.className = classToInspect.getName();
    }

    public void addSuperclass(Class superclass) {
        superclasses.put(className, superclass);
    }

    public void addInterfaces(ArrayList<Class> interfaceObjects) {
        interfaces.put(className, interfaceObjects);
    }

    public void addConstructors(ArrayList<Constructor> constructorObjects) {
        constructors.put(className, constructorObjects);
    }

    public void addMethods(ArrayList<Method> methodObjects) {
        methods.put(className, methodObjects);
    }

    public void addFields(String className, ArrayList<Field> fieldObjects) {
        fields.put(className, fieldObjects);
    }

    public void addArray(Object arrayObj) {
        appendIfContains(className, arrayObj, arrays);
    }

    private void appendIfContains(String className, Object objToAdd, HashMap<String, ArrayList<Object>> map) {
        if (!map.containsKey(className)) {
            map.put(className, new ArrayList<Object>(){{ add(objToAdd); }});
        } else {
            map.get(className).add(objToAdd);
        }
    }

    public Optional<FieldPair> findField(String fieldName, String className) {
        return fields.get(className).stream()
            .filter(f ->  f.getName().equals(fieldName))
            .map(this::getFieldPair)
            .filter(Objects::nonNull)
            .findFirst();
    }

    public Optional<FieldPair> findField(String fieldName) {
        return findField(fieldName, className);
    }

    public String getClassName() {
        return className;
    }

    public HashMap<String, Class> getSuperclasses() {
        return superclasses;
    }

    public HashMap<String, ArrayList<Field>> getFields() {
        return fields;
    }

    public HashMap<String, ArrayList<Method>> getMethods() {
        return methods;
    }

    public HashMap<String, ArrayList<Constructor>> getConstructors() {
        return constructors;
    }

    public HashMap<String, ArrayList<Class>> getInterfaces() {
        return interfaces;
    }

    public HashMap<String, ArrayList<Object>> getArrays() {
        return arrays;
    }

    private FieldPair getFieldPair(Field field) {
        field.setAccessible(true);
        try {
            return new FieldPair(field, field.get(objectToInspect));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
