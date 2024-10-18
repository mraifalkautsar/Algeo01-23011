package Algeo01.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Matrix {
    public double[][] data;
    public int rowEff; // jumlah baris efektif
    public int colEff; // jumlah kolom efektif

    // Konstruktor matriks
    public Matrix(int nRows, int nCols) {
        this.rowEff = nRows;
        this.colEff = nCols;
        data = new double[nRows][nCols]; // Inisialisasi matriks
    }

    // PRIMITIF MATRIKS
    // Mengatur nilai elemen matriks
    public void setElement(int row, int col, double value) {
        data[row][col] = value;
    }

    // Mengecek apakah indeks matriks valid ( asumsi 100 sebagai batas maksimal)
    public static boolean isMatrixIdxValid(int i, int j) {
        return (i >= 0 && i < 100) && (j >= 0 && j < 100);
    }

    // Mendapatkan indeks terakhir dari baris efektif
    public int getLastIdxRow() {
        return rowEff - 1;
    }

    // Mendapatkan indeks terakhir dari kolom efektif
    public int getLastIdxCol() {
        return colEff - 1;
    }

    // Mengecek apakah indeks efektif (i, j) valid
    public boolean isIdxEff(int i, int j) {
        return (i >= 0 && i < rowEff) && (j >= 0 && j < colEff);
    }

    // Mengambil elemen diagonal dari matriks pada baris i (khusus matriks persegi)
    public double getElmtDiagonal(int i) {
        return data[i][i];
    }

    // Menyalin suatu matriks ke matriks lain
    public void copyMatrix(Matrix mIn) {
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < colEff; j++) {
                this.data[i][j] = mIn.data[i][j];
            }
        }
    }

    // Membaca matriks dari input keyboard
    public static Matrix readMatrixFromInput(Scanner sc) {
        System.out.print("Masukkan jumlah baris: ");
        int rows = sc.nextInt();
        System.out.print("Masukkan jumlah kolom: ");
        int cols = sc.nextInt();

        Matrix matrix = new Matrix(rows, cols);
        System.out.println("Masukkan elemen matriks:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.setElement(i, j, sc.nextDouble());
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

    // Menampilkan matriks ke layar
    public void displayMatrix() {
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < colEff; j++) {
                System.out.print(data[i][j]);
                if (j < colEff - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    // Penjumlahan dua matriks
    public Matrix addMatrix(Matrix m2) {
        if (this.rowEff != m2.rowEff || this.colEff != m2.colEff) {
            throw new IllegalArgumentException("Matriks harus memiliki ukuran yang sama untuk ditambahkan.");
        }
        Matrix result = new Matrix(rowEff, colEff);
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < colEff; j++) {
                result.data[i][j] = this.data[i][j] + m2.data[i][j];
            }
        }
        return result;
    }

    // Pengurangan dua matriks 
    public Matrix subtractMatrix(Matrix m2) {
        if (this.rowEff != m2.rowEff || this.colEff != m2.colEff) {
            throw new IllegalArgumentException("Matriks harus memiliki ukuran yang sama untuk dikurangkan.");
        }
        Matrix result = new Matrix(rowEff, colEff);
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < colEff; j++) {
                result.data[i][j] = this.data[i][j] - m2.data[i][j];
            }
        }
        return result;
    }

    // Perkalian dua matriks
    public Matrix multiplyMatrix(Matrix m2) {
        if (this.colEff != m2.rowEff) {
            throw new IllegalArgumentException("Jumlah kolom matriks pertama harus sama dengan jumlah baris matriks kedua.");
        }
        Matrix result = new Matrix(rowEff, m2.colEff);
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < m2.colEff; j++) {
                result.data[i][j] = 0;
                for (int k = 0; k < colEff; k++) {
                    result.data[i][j] += this.data[i][k] * m2.data[k][j];
                }
            }
        }
        return result;
    }

    // Perkalian matriks dengan skalar
    public Matrix multiplyByConst(double x) {
        Matrix result = new Matrix(rowEff, colEff);
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < colEff; j++) {
                result.data[i][j] = this.data[i][j] * x;
            }
        }
        return result;
    }

    // Mengecek apakah dua matriks sama
    public boolean isMatrixEqual(Matrix m2) {
        if (this.rowEff != m2.rowEff || this.colEff != m2.colEff) {
            return false;
        }
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < colEff; j++) {
                if (this.data[i][j] != m2.data[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Mengecek apakah matriks persegi
    public boolean isSquare() {
        return rowEff == colEff;
    }

    // Mengecek apakah matriks simetris
    public boolean isSymmetric() {
        if (!isSquare()) {
            return false;
        }
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < colEff; j++) {
                if (data[i][j] != data[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Mengecek apakah matriks identitas
    public boolean isIdentity() {
        if (!isSquare()) {
            return false;
        }
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < colEff; j++) {
                if (i == j && data[i][j] != 1.0) {
                    return false;
                }
                if (i != j && data[i][j] != 0.0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Mengecek apakah matriks satuan (hanya 1s dan 0s pada elemen diagonal)
    public boolean isSparse() {
        int nonZeroCount = 0;
        int threshold = (int) (0.05 * rowEff * colEff); // 5% threshold
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < colEff; j++) {
                if (data[i][j] != 0) {
                    nonZeroCount++;
                    if (nonZeroCount > threshold) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Mentranspose suatu matriks 
    public Matrix transpose() {
        Matrix result = new Matrix(colEff, rowEff);
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < colEff; j++) {
                result.data[j][i] = data[i][j];
            }
        }
        return result;
    }  

    // Mengisi suatu matriks
    public void fillMatrix(double[][] values) {
        for (int i = 0; i < rowEff; i++) {
            for (int j = 0; j < colEff; j++) {
                data[i][j] = values[i][j];
            }
        }
    }

    // OBE (Operasi Baris Elementer) pada matriks
    // OBE: Penukaran dua baris
    public void swapRows(int row1, int row2) {
        if (isIdxEff(row1, 0) && isIdxEff(row2, 0)) {
            for (int i = 0; i < colEff; i++) {
                double temp = data[row1][i];
                data[row1][i] = data[row2][i];
                data[row2][i] = temp;
            }
        } else {
            System.out.println("Indeks baris tidak valid.");
        }
    }

    // OBE: Mengalikan satu baris dengan konstanta
    public void multiplyRowByConstant(int row, double constant) {
        if (isIdxEff(row, 0)) {
            for (int i = 0; i < colEff; i++) {
                data[row][i] *= constant;
            }
        } else {
            System.out.println("Indeks baris tidak valid.");
        }
    }

    // OBE: Menambahkan kelipatan dari satu baris ke baris lain
    public void addMultipleOfRow(int targetRow, int sourceRow, double multiple) {
        if (isIdxEff(targetRow, 0) && isIdxEff(sourceRow, 0)) {
            for (int i = 0; i < colEff; i++) {
                data[targetRow][i] += multiple * data[sourceRow][i];
            }
        } else {
            System.out.println("Indeks baris tidak valid.");
        }
    }
}
