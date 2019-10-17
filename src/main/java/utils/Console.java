package utils;

public class Console {
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
}
