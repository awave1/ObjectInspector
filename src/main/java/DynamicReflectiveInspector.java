import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DynamicReflectiveInspector {
    private static final String INSPECT_METHOD = "inspect";

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: DynamicReflectiveInspector [package path to class with inspect method] [package path to class to inspect] [recursive]");
            System.out.println("Dynamically invoke a class with inspect method to inspect a specified class.");
            System.out.println("    - path to class with inspect(Object, boolean) method (e.g. inspector.Inspector) [required][string]");
            System.out.println("    - path to class to inspect (e.g. data.ClassA)                                   [required][string]");
            System.out.println("    - recursive mode                                                                [optional][boolean]");
            return;
        }

        String inspectorClassPath = args[0];
        String classToInspectPath = args[1];
        boolean isRecursive = args.length == 3 && args[2] != null && Boolean.parseBoolean(args[2]);

        ClassLoader classLoader = DynamicReflectiveInspector.class.getClassLoader();
        try {
            Class inspectorClass = classLoader.loadClass(inspectorClassPath);
            Constructor inspectorConstructor = inspectorClass.getConstructor();
            Object inspectorInstance = inspectorConstructor.newInstance();

            Class classToInspect = classLoader.loadClass(classToInspectPath);
            Constructor classToInspectConstructor = classToInspect.getConstructor();

            Method inspectMethod = inspectorClass.getMethod(INSPECT_METHOD, Object.class, boolean.class);
            System.out.println("Recursive: " + isRecursive);
            inspectMethod.invoke(inspectorInstance, classToInspectConstructor.newInstance(), isRecursive);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
