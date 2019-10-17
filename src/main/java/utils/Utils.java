package utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Utils {
    public static void leftpad(Object o, int depth) {
        leftpad(pad(String.valueOf(o), depth), depth, "\n");
    }

    public static void leftpad(Object o, int depth, String end) {
        System.out.print(pad(String.valueOf(o), depth) + end);
    }

    public static String pad(String string, int minLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < minLength; i++) {
            sb.append(" ");
        }
        sb.append(string);
        return sb.toString();
    }

    public static String join(List<String> objects, String delimiter) {
        return String.join(delimiter, objects);
    }

    public static String join(Class[] arr, Function<Class, String> mapper) {
        return join(Arrays.stream(arr).map(mapper).collect(Collectors.toList()), ", ");
    }

    public static String join(Class[] arr, String delimiter, Function<Class, String> mapper) {
        return join(Arrays.stream(arr).map(mapper).collect(Collectors.toList()), delimiter);
    }
}
