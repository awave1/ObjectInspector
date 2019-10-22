package loader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InspectorLoader extends ClassLoader {
    private static final String INSPECT_METHOD = "inspect";

    public InspectorLoader(ClassLoader loader) {
        super(loader);
    }


    public void invokeInspect(String inspectorClassPath, String classToInspectPath, boolean isRecursive) throws ReflectiveOperationException {
        Class inspectorClass = this.loadClass(inspectorClassPath);
        Constructor inspectorConstructor = inspectorClass.getConstructor();
        Object inspectorInstance = inspectorConstructor.newInstance();

        Class classToInspect = this.loadClass(classToInspectPath);
        Constructor classToInspectConstructor = classToInspect.getConstructor();

        Method inspectMethod = inspectorClass.getMethod(INSPECT_METHOD, Object.class, boolean.class);
        System.out.println("Recursive: " + isRecursive);
        inspectMethod.invoke(inspectorInstance, classToInspectConstructor.newInstance(), isRecursive);
    }
}
