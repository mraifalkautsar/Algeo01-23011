package utils;

import matrix.Matrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputUtils {

    // BASIC UTILITIES

    // Menampilkan matriks ke layar dengan pembulatan empat angka belakang koma
    public static void displayMatrix(Matrix matrix) {
        for (int i = 0; i < matrix.rowEff; i++) {
            for (int j = 0; j < matrix.colEff; j++) {
                System.out.printf("%.4f", matrix.data[i][j]);
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
    public static void printCoefficients(double[] solution) {
        if (solution == null) {
            System.out.println("Solusi tidak ditemukan.");
        } else {
            System.out.println("Solusi:");
            for (int i = 0; i < solution.length; i++) {
                System.out.printf("x%d = %.4f%n", (i + 1), solution[i]);
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

    public static void saveSistemPersamaanLinier(double[] solution, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Hasil Penyelesaian SPL:\n");
            for (int i = 0; i < solution.length; i++) {
                writer.write("x" + (i + 1) + " = " + solution[i] + "\n");
            }
        }
    }

    public static void saveSistemPersamaanLinierGauss(String solution, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Hasil Penyelesaian SPL:\n");
            writer.write(solution);
        }
    }

    public static void saveDeterminant(double determinant, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Hasil Determinan Matriks:\n");
            writer.write("Determinan = " + determinant + "\n");
        }
    }

    public static void saveInverseMatrix(Matrix matrix, String filePath) throws IOException {
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

    public static void saveInterpolasiPolinom(double[] solution, double estimation) {
        int saveChoice = InputUtils.getInt("Apakah Anda ingin menyimpan hasil ke file? (1: Ya, 2: Tidak)");

        if (saveChoice == 1) {
            String outputFileName = InputUtils.getString("Masukkan nama file output: ");
            String filePath = "test/interpolasi_polinom/output/" + outputFileName + ".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("Koefisien polinomial:\n");
                for (int i = 0; i < solution.length; i++) {
                    writer.write("a" + (i + 1) + " = " + solution[i] + "\n");
                }
                writer.write("\nNilai y hasil taksiran: " + estimation);
                System.out.println("Hasil berhasil disimpan di " + filePath);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan hasil ke file: " + e.getMessage());
            }
        } else {
            System.out.println("Hasil tidak disimpan.");
        }
    }

    public static void saveBicubicSpline(double res) {
        int saveChoice = InputUtils.getInt("Apakah Anda ingin menyimpan hasil ke file? (1: Ya, 2: Tidak)");

        if (saveChoice == 1) {
            String outputFileName = InputUtils.getString("Masukkan nama file output (dengan ekstensi .txt): ");
            String filePath = "test/interpolasi_bicubic/output/" + outputFileName;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("Hasil Interpolasi Bicubic Spline:\n");
                writer.write("Hasil interpolasi: " + res + "\n");
                System.out.println("Hasil berhasil disimpan di " + filePath);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan hasil ke file: " + e.getMessage());
            }
        } else {
            System.out.println("Hasil tidak disimpan.");
        }
    }

    public static void saveRegresiLinier(String equation, String estimationOutput) {
        int saveChoice = InputUtils.getInt("Apakah Anda ingin menyimpan hasil ke file? (1: Ya, 2: Tidak)");

        if (saveChoice == 1) {
            String outputFileName = InputUtils.getString("Masukkan nama file output: ");
            String filePath = "test/regresi_linier/output/" + outputFileName + ".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(equation);
                writer.write(estimationOutput);
                System.out.println("Hasil berhasil disimpan di " + filePath);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan hasil ke file: " + e.getMessage());
            }
        } else {
            System.out.println("Hasil tidak disimpan.");
        }
    }


    public static void saveRegresiKuadratikBerganda(String equation, double[][] observation, double prediction) {
        int saveChoice = InputUtils.getInt("\nApakah Anda ingin menyimpan hasil ke file? (1: Ya, 2: Tidak)");
    
        if (saveChoice == 1) {
            String outputFileName = InputUtils.getString("Masukkan nama file output: ");
            String filePath = "test/regresi_kuadratik/output/" + outputFileName + ".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("Hasil Regresi Kuadratik Berganda:\n");
                writer.write("Persamaan:\n");
                writer.write(equation + "\n");  // Menulis koefisien yang berbentuk String
    
                writer.write("\nObservasi baru:\n");
                for (int i = 0; i < observation.length; i++) {
                    for (int j = 0; j < observation[i].length; j++) {
                        writer.write(observation[i][j] + " ");
                    }
                    writer.write("\n");
                }
    
                writer.write("\nHasil prediksi: " + prediction + "\n");
                System.out.println("Hasil berhasil disimpan di " + filePath);
            } catch (IOException e) {
                System.out.println("Gagal menyimpan hasil ke file: " + e.getMessage());
            }
        } else {
            System.out.println("Hasil tidak disimpan.");
        }
    }
    
}