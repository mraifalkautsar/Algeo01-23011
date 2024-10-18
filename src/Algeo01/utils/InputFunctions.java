package Algeo01.utils;

import Algeo01.matrix.Matrix;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputFunctions {
    private static Scanner scanner = new Scanner(System.in);

    public static Matrix readMatrixFromInput() {
        System.out.print("Masukkan jumlah baris: ");
        int rows = scanner.nextInt();
        System.out.print("Masukkan jumlah kolom: ");
        int cols = scanner.nextInt();

        Matrix matrix = new Matrix(rows, cols);
        System.out.println("Masukkan elemen matriks:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.setElement(i, j, scanner.nextDouble());
            }
        }
        return matrix;
    }

    // Membaca matriks dari file
    public static Matrix readMatrixFromFile(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int rowCount = 0;
            int colCount = -1; // Inisialisasi dengan -1 untuk memeriksa kolom pertama
            List<double[]> rows = new ArrayList<>();

            // Baca setiap baris dan periksa jumlah kolom
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(" ");
                if (colCount == -1) {
                    colCount = elements.length; // Inisialisasi kolom dari baris pertama
                } else if (elements.length != colCount) {
                    throw new IOException("Jumlah elemen dalam baris berbeda-beda, file matriks tidak valid.");
                }

                // Konversi elemen-elemen string menjadi double dan tambahkan ke list rows
                double[] row = new double[elements.length];
                for (int i = 0; i < elements.length; i++) {
                    row[i] = Double.parseDouble(elements[i]);
                }
                rows.add(row);
                rowCount++;
            }

            // Buat matriks dengan ukuran yang sudah ditentukan
            Matrix matrix = new Matrix(rowCount, colCount);

            // Masukkan elemen dari list rows ke dalam objek matriks
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    matrix.setElement(i, j, rows.get(i)[j]);
                }
            }

            return matrix;
        }
    }

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
            System.out.println("Input tidak valid, masukkan double.");
            scanner.next();
        }
        return scanner.nextDouble();
    }

    // Metode untuk meminta matriks dari pengguna
    public static double[][] getMatrix(int rows, int cols, String prompt) {
        System.out.println(prompt);
        double[][] matrix = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            System.out.println("BARIS KE-" + (i + 1));
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = getDouble("Elemen baris ke-[" + (i + 1) + "] kolom ke-[" + (j + 1) + "]: ");
            }
        }
        return matrix;
    }

    public static double[][] getXYdata(int n, String prompt) {
        System.out.println(prompt);
        double[][] data_array = new double[n][2];

        for (int i = 0; i < n; i++) {
            System.out.println("DATA KE-" + (i + 1));
            data_array[i][0] = getDouble("X data ke-" + (i+1) + ": ");
            data_array[i][1] = getDouble("Y data ke-" + (i+1) + ": ");
        }
        return data_array;
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