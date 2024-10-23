package utils;

import matrix.Matrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputUtils {

    // BASIC UTILITIES

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

    // OUTPUT KE FILE UNTUK TIAP FUNGSI

    public static void saveMatrixToFile(Matrix matrix, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Matriks:\n");
            for (int i = 0; i < matrix.rowEff; i++) {
                for (int j = 0; j < matrix.colEff; j++) {
                    writer.write(String.valueOf(matrix.data[i][j])); // Convert double to String
                    if (j < matrix.colEff - 1) {
                        writer.write(" "); // Add space between elements
                    }
                }
                writer.write("\n");  // New line after each row
            }
        }
    }


    public static void SaveSistemPersamaanLinier(double[] solution, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Hasil Penyelesaian SPL:\n");
            for (int i = 0; i < solution.length; i++) {
                writer.write("x" + (i + 1) + " = " + solution[i] + "\n");
            }
        }
    }

    public static void SaveDeterminant(double determinant, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Hasil Determinan Matriks:\n");
            writer.write("Determinan = " + determinant + "\n");
        }
    }

    public static void SaveInverseMatrix    (Matrix matrix, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Hasil Matriks Balikan:\n");
            for (int i = 0; i < matrix.rowEff; i++) {
                for (int j = 0; j < matrix.colEff; j++) {
                    writer.write(String.valueOf(matrix.data[i][j])); // Convert double to String
                    if (j < matrix.colEff - 1) {
                        writer.write(" "); // Add space between elements
                    }
                }
                writer.write("\n"); // Move to the next line after each row
            }
        }
    }

    public static void SaveInterpolasiPolinom (double[] solution, double estimation) {
        int saveChoice = InputUtils.getInt("Apakah Anda ingin menyimpan hasil ke file? (1: Ya, 2: Tidak)");

        if (saveChoice == 1) {
            String outputFileName = InputUtils.getString("Masukkan nama file output (dengan ekstensi .txt): ");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                writer.write("Koefisien polinomial:\n");
                for (int i = 0; i < solution.length; i++) {
                    writer.write("a" + (i + 1) + " = " + solution[i] + "\n");
                }
                writer.write("\nNilai y hasil taksiran: " + estimation);
                System.out.println("Hasil berhasil disimpan di " + outputFileName);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan hasil ke file: " + e.getMessage());
            }
        } else {
            System.out.println("Hasil tidak disimpan.");
        }
    }

    public static void SaveBicubicSpline(double res) {
        int saveChoice = InputUtils.getInt("Apakah Anda ingin menyimpan hasil ke file? (1: Ya, 2: Tidak)");

        if (saveChoice == 1) {
            String outputFileName = InputUtils.getString("Masukkan nama file output (dengan ekstensi .txt): ");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                writer.write("Hasil Interpolasi Bicubic Spline:\n");
                writer.write("Hasil interpolasi: " + res + "\n");
                System.out.println("Hasil berhasil disimpan di " + outputFileName);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan hasil ke file: " + e.getMessage());
            }
        } else {
            System.out.println("Hasil tidak disimpan.");
        }
    }

    public static void SaveRegresiLinier(double[] solution, double estimation) {
        int saveChoice = InputUtils.getInt("Apakah Anda ingin menyimpan hasil ke file? (1: Ya, 2: Tidak)");

        if (saveChoice == 1) {
            String outputFileName = InputUtils.getString("Masukkan nama file output (dengan ekstensi .txt): ");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                writer.write("Hasil Regresi Linier:\n");
                for (int i = 0; i < solution.length; i++) {
                    writer.write("x" + (i + 1) + " = " + solution[i] + "\n");
                }
                writer.write("Nilai y hasil taksiran: " + estimation + "\n");
                System.out.println("Hasil berhasil disimpan di " + outputFileName);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan hasil ke file: " + e.getMessage());
            }
        } else {
            System.out.println("Hasil tidak disimpan.");
        }
    }

    public static void SaveRegresiKuadratikBerganda(Matrix coefficients, double[][] observation, double prediction) {
        int saveChoice = InputUtils.getInt("Apakah Anda ingin menyimpan hasil ke file? (1: Ya, 2: Tidak)");

        if (saveChoice == 1) {
            String outputFileName = InputUtils.getString("Masukkan nama file output (dengan ekstensi .txt): ");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                writer.write("Hasil Regresi Kuadratik Berganda:\n");
                writer.write("Koefisien Model:\n");
                OutputUtils.saveMatrixToFile(coefficients, outputFileName);

                writer.write("\nObservasi baru:\n");
                for (int i = 0; i < observation.length; i++) {
                    for (int j = 0; j < observation[i].length; j++) {
                        writer.write(observation[i][j] + " ");
                    }
                    writer.write("\n");
                }

                writer.write("\nHasil prediksi: " + prediction + "\n");
                System.out.println("Hasil berhasil disimpan di " + outputFileName);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan hasil ke file: " + e.getMessage());
            }
        } else {
            System.out.println("Hasil tidak disimpan.");
        }
    }
}