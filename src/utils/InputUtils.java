package utils;

import matrix.Matrix;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputUtils {
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

    public static Object[] readVectorAndABfromFile(String filename) {
        double[] vector = new double[16];
        double a = 0, b = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // membaca array
            int idx = 0;
            for (int i = 0; i < 4; i++) {
                String[] rowValues = br.readLine().split(" ");
                for (int j = 0; j < 4; j++) {
                    vector[idx++] = Double.parseDouble(rowValues[j]);
                }
            }

            // membaca baris kelima
            String[] lastLine = br.readLine().split(" ");
            a = Double.parseDouble(lastLine[0]);
            b = Double.parseDouble(lastLine[1]);
        
        } catch (IOException e) {
            System.out.println(" Error reading file : " + e.getMessage());
        }

        // return value
        return new Object[]{vector, a, b};
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

    public static double[][] readAugmentedMatrixFromKeyboard(int m, int n) {
            double[][] augmentedMatrix = new double[m][n + 1]; // n peubah + 1 untuk yi

            // Membangun pesan untuk menunjukkan peubah x1, x2, ..., xn
            StringBuilder variables = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                variables.append("x").append(i);
                if (i < n) {
                    variables.append(", "); // Menambahkan koma jika bukan peubah terakhir
                }
            }

            System.out.println("Masukkan nilai-nilai " + variables + " dan yi (satu baris untuk setiap sampel):");
            for (int i = 0; i < m; i++) {
                for (int j = 0; j <= n; j++) {
                    augmentedMatrix[i][j] = scanner.nextDouble();
                }
            }

            return augmentedMatrix; // Kembalikan matriks augmented yang dibaca
    }

    public static double[][] readAugmentedMatrixFromFile(String filePath, int n) {
        double[][] augmentedMatrix = null;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int m = 0;
    
            // Hitung jumlah baris untuk menentukan ukuran matriks
            while ((line = br.readLine()) != null) {
                m++;
            }
    
            augmentedMatrix = new double[m][n + 1]; // n peubah + 1 untuk yi
            
            // Reset BufferedReader untuk membaca ulang file
            br.close(); // Menutup BufferedReader setelah menghitung baris
    
            // Membaca file lagi untuk mendapatkan data
            try (BufferedReader br2 = new BufferedReader(new FileReader(filePath))) {
                int rowIndex = 0;
    
                while ((line = br2.readLine()) != null) {
                    String[] values = line.trim().split(" "); // Pisahkan berdasarkan spasi
                    for (int j = 0; j <= n; j++) {
                        augmentedMatrix[rowIndex][j] = Double.parseDouble(values[j]);
                    }
                    rowIndex++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return augmentedMatrix; // Kembalikan matriks augmented yang dibaca
    }
    
    public static double[][] readMatrixFromKeyboard() {
            System.out.print("Masukkan ukuran matriks (n x n): ");
            int n = scanner.nextInt();
            double[][] matrix = new double[n][n];
   
            System.out.println("Masukkan elemen matriks (pisahkan dengan spasi): ");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = scanner.nextDouble();
                }
            }
            
            return matrix;
    }

    public static double[][] readMatrixFromFile2(String filePath)  {
        double[][] matrix = null;
    
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int n = 0;
    
            // Hitung jumlah baris untuk menentukan ukuran matriks
            while ((line = br.readLine()) != null) {
                n++;
            }
    
            matrix = new double[n][n]; // Matriks n x n
    
            // Reset BufferedReader untuk membaca ulang file
            br.close();
            
            try (BufferedReader br2 = new BufferedReader(new FileReader(filePath))) {
                int rowIndex = 0;
    
                while ((line = br2.readLine()) != null) {
                    String[] values = line.trim().split(" "); // Pisahkan berdasarkan spasi
                    for (int j = 0; j < n; j++) {
                        matrix[rowIndex][j] = Double.parseDouble(values[j]);
                    }
                    rowIndex++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return matrix; // Kembalikan matriks yang dibaca
    }    

    public static double[][] readAugmentedMatrix(double[][] augmentedMatrix) {
        int n = augmentedMatrix[0].length - 1; // Jumlah peubah
        int m = augmentedMatrix.length; // Jumlah observasi

        // Periksa apakah jumlah kolom sesuai dengan struktur yang diharapkan
        if (n < 1) {
            throw new IllegalArgumentException("Matriks tidak sesuai dengan struktur yang diharapkan.");
        }

        double[][] designMatrix = new double[m][1 + n * 2 + n * (n - 1)];

        for (int i = 0; i < m; i++) {
            int columnIndex = 0;

            // Kolom 1: Konstanta
            designMatrix[i][columnIndex++] = 1.0;

            // Kolom 2 - N+1: Variabel linier (u1, u2, ..., un)
            for (int j = 0; j < n; j++) {
                designMatrix[i][columnIndex++] = augmentedMatrix[i][j];
            }

            // Kolom N+2 - 2N+1: Variabel kuadrat (u1^2, u2^2, ..., un^2)
            for (int j = 0; j < n; j++) {
                designMatrix[i][columnIndex++] = augmentedMatrix[i][j] * augmentedMatrix[i][j];
            }

            // Kolom berikutnya: Variabel interaksi (u1*u2, u1*u3, ..., un*un)
            for (int j = 0; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    designMatrix[i][columnIndex++] = augmentedMatrix[i][j] * augmentedMatrix[i][k];
                }
            }
        }

        return designMatrix; // Kembalikan matriks desain yang telah dibangun
    }

    public static String getString(String s) {
        return s; // ini method siapa jir belum ada isinya
    }
}