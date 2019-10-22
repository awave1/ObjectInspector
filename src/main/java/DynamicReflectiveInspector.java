import loader.InspectorLoader;

import java.lang.reflect.InvocationTargetException;

public class DynamicReflectiveInspector {

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

        try {
            InspectorLoader inspectorLoader = new InspectorLoader(DynamicReflectiveInspector.class.getClassLoader());
            inspectorLoader.invokeInspect(inspectorClassPath, classToInspectPath, isRecursive);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
