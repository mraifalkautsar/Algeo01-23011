package utils;

import javafx.scene.control.TextArea;
import matrix.Matrix;

public class OutputFunctions {

    // Display matrix in output terminal (TextArea)
    public static void displayMatrix(Matrix matrix, TextArea outputTerminal) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.rowEff; i++) {
            for (int j = 0; j < matrix.colEff; j++) {
                sb.append(matrix.data[i][j]);
                if (j < matrix.colEff - 1) {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        outputTerminal.appendText(sb.toString());
    }

    // Print an array to the output terminal (TextArea)
    public static void printArray(double[] array, String arrayName, TextArea outputTerminal) {
        StringBuilder sb = new StringBuilder(arrayName + ":\n");
        for (int i = 0; i < array.length; i++) {
            sb.append("Element [").append(i + 1).append("] = ").append(array[i]).append("\n");
        }
        outputTerminal.appendText(sb.toString());
    }

    // Print solution of a system of linear equations to the output terminal (TextArea)
    public static void printCoefficients(double[] solution, boolean alpha, TextArea outputTerminal) {
        if (solution == null) {
            outputTerminal.appendText("No solution found.\n");
        } else {
            StringBuilder sb = new StringBuilder("Solution:\n");
            if (alpha) {
                for (int i = 0; i < solution.length; i++) {
                    sb.append("a").append(i + 1).append(" = ").append(solution[i]).append("\n");
                }
            } else {
                for (int i = 0; i < solution.length; i++) {
                    sb.append("b").append(i + 1).append(" = ").append(solution[i]).append("\n");
                }
            }
            outputTerminal.appendText(sb.toString());
        }
    }

    // Print regression equation to the output terminal (TextArea)
    public static void printRegressionEquation(double[] regression, TextArea outputTerminal) {
        String result = "y = " + regression[0] + " * x + " + regression[1] + "\n";
        outputTerminal.appendText(result);
    }
}
