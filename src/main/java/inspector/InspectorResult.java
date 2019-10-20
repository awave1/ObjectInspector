package inspector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class InspectorResult {
    private Class classToInspect;
    private String className;

    private HashMap<String, Class> superclasses = new HashMap<>();

    private HashMap<String, ArrayList<Object>> fields = new HashMap<>();

    private HashMap<String, ArrayList<Method>> methods = new HashMap<>();

    private HashMap<String, ArrayList<Constructor>> constructors = new HashMap<>();

    private HashMap<String, ArrayList<Class>> interfaces = new HashMap<>();

    private HashMap<String, ArrayList<Object>> arrays = new HashMap<>();

    public InspectorResult(Class classToInspect) {
        this.classToInspect = classToInspect;
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

    public void addField(Object obj) {
        appendIfContains(obj, fields);
    }

    public void addArray(Object arrayObj) {
        appendIfContains(arrayObj, arrays);
    }

    private void appendIfContains(Object objToAdd, HashMap<String, ArrayList<Object>> map) {
        if (!map.containsKey(className)) {
            map.put(className, new ArrayList<Object>(){{ add(objToAdd); }});
        } else {
            map.get(className).add(objToAdd);
        }
    }

    public String getClassName() {
        return className;
    }

    public HashMap<String, Class> getSuperclasses() {
        return superclasses;
    }

    public HashMap<String, ArrayList<Object>> getFields() {
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
}
