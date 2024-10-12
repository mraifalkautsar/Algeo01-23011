package utils;

public class OutputFunctions {
    // Metode untuk mencetak sebuah matriks
    public static void printMatrix(double[][] matrix) {
        System.out.println("Matrix:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "\t");
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
    public static void printSolution(double[] solution) {
        if (solution == null) {
            System.out.println("No solution found.");
        } else {
            System.out.println("Solution:");
            for (int i = 0; i < solution.length; i++) {
                System.out.println("x" + (i + 1) + " = " + solution[i]);
            }
        }
    }

    // Metode untuk mengetak persamaan regresi
    public static void printRegressionEquation(double[] regression) {
        System.out.println("y = " + regression[0] + " * x + " + regression[1]);
    }
}
