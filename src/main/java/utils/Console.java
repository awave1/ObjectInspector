package utils;

import static utils.Utils.pad;

public class Console {
    public static boolean enableOutput = true;
    public static void leftpad(Object o, int depth) {
        if (enableOutput) {
            leftpad(String.valueOf(o), depth, "\n");
        }
    }

    public static void leftpad(Object o, int depth, String end) {
        if (enableOutput) {
            System.out.print(pad(String.valueOf(o), depth) + end);
        }
    }
}
