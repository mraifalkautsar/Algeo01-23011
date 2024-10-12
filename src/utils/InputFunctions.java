package utils;

import java.util.Scanner;

public class InputFunctions {
    private static Scanner scanner = new Scanner(System.in);

    // Metode untuk meminta integer dari pengguna
    public static int getInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Input tidak valid, masukkan integer.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    // Metode untuk meminta double dari pengguna
    public static double getDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Input tidak valid, masukkan integer.");
            scanner.next();
        }
        return scanner.nextDouble();
    }

    // Methode untuk meminta matriks dari pengguna
    public static double[][] getMatrix(int rows, int cols, String prompt) {
        System.out.println(prompt);
        double[][] matrix = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            System.out.println("Enter row " + (i + 1) + ":");
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = getDouble("Elemen baris ke-[" + (i + 1) + "] kolom ke-[" + (j + 1) + "]: ");
            }
        }
        return matrix;
    }

    // Metode untuk meminta array 1 dimensi (untuk konstanta, atau untuk nilai x dan y.)
    public static double[] getArray(int size, String prompt) {
        System.out.println(prompt);
        double[] array = new double[size];

        for (int i = 0; i < size; i++) {
            array[i] = getDouble("Element ke-[" + (i + 1) + "]: ");
        }
        return array;
    }
}
