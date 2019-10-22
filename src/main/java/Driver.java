
import data.ClassA;
import data.ClassB;
import data.ClassD;
import inspector.Inspector;

import java.io.*;

public class Driver {

    public static void main(String[] args) throws Exception {
        boolean isRecursive = true;
        boolean useStdOut = true;

        runTest("script1.txt", new ClassA(), isRecursive, useStdOut);
        runTest("script2.txt", new ClassA(12), isRecursive, useStdOut);
        runTest("script3.txt", new ClassB(), isRecursive, useStdOut);
        runTest("script4.txt", new ClassD(32), isRecursive, useStdOut);
        runTest("script5.txt", new ClassD(), isRecursive, useStdOut);
        runTest("script6.txt", new ClassB[12], isRecursive, useStdOut);
        runTest("script7.txt", new ClassB[12][12], isRecursive, useStdOut);
        runTest("script8.txt", "Test String", isRecursive, useStdOut);
    }

    private static void runTest(String filename, Object testObj, boolean recursive, boolean useStdOut) {
        if (!useStdOut) {
            runTest(filename, testObj, recursive);
            return;
        }

        try {
            System.out.println();
            runInspect(null, testObj, recursive);
            System.out.println();

        } catch (Exception e) {
            System.err.println("Unable to compleatly run test: " + testObj);
            e.printStackTrace();
        }
    }

    public static void runTest(String filename, Object testObj, boolean recursive) {
        try {
            PrintStream stdOut = System.out;
            File file = new File(filename);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            PrintStream filePrintStream = new PrintStream(fileOutputStream);

            System.setOut(filePrintStream);
            runInspect(filename, testObj, recursive);

            filePrintStream.flush();
            fileOutputStream.flush();
            filePrintStream.close();
            fileOutputStream.close();

            System.setOut(stdOut);
        } catch (IOException ioe) {
            System.err.println("Unable to open file: " + filename);
        } catch (Exception e) {
            System.err.println("Unable to compleatly run test: " + testObj);
            e.printStackTrace();
        }
    }

    private static void runInspect(String filename, Object testObj, boolean recursive) {
        System.out.println("======================================================");
        if (filename != null) {
            System.out.println("Filename: " + filename);
        }
        System.out.println("Running Test: " + testObj);
        System.out.println("Recursive: " + recursive);
        new Inspector().inspect(testObj, recursive);
        System.out.println("======================================================");
    }
}
