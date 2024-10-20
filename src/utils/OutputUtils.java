package utils;

import matrix.Matrix;

public class OutputUtils {

    // Menampilkan matriks ke layar
    public static void displayMatrix(Matrix matrix) {
        for (int i = 0; i < matrix.rowEff; i++) {
            for (int j = 0; j < matrix.colEff; j++) {
                System.out.print(matrix.data[i][j]);
                if (j < matrix.colEff - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    // Metode untuk mencetak sebuah array
    public static void printArray(double[] array, String arrayName) {
        System.out.println(arrayName + ":");
        for (int i = 0; i < array.length; i++) {
            System.out.println("Element [" + (i + 1) + "] = " + array[i]);
        }
    }

    // Metode untuk mencetak solusi persamaan linier
    public static void printCoefficients(double[] solution, boolean alpha) {
        if (solution == null) {
            System.out.println("No solution found.");
        } else {
            System.out.println("Solution:");
            if (alpha) {
                for (int i = 0; i < solution.length; i++) {
                    System.out.println("a" + (i + 1) + " = " + solution[i]);
                }
            } else {
                for (int i = 0; i < solution.length; i++) {
                    System.out.println("b" + (i + 1) + " = " + solution[i]);
                }
            }
        }
    }

    // Metode untuk mengetak persamaan regresi
    public static void printRegressionEquation(double[] regression) {
        System.out.println("y = " + regression[0] + " * x + " + regression[1]);
    }
}