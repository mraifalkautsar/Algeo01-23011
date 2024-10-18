package utils;

import matrix.Matrix;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputFunctions {

    // This method reads a matrix from the input terminal (TextArea)
    public static Matrix readMatrixFromText(TextArea inputTerminal) {
        String[] lines = inputTerminal.getText().split("\\n");
        int rows = lines.length;
        int cols = lines[0].split(" ").length;
        Matrix matrix = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            String[] elements = lines[i].split(" ");
            for (int j = 0; j < cols; j++) {
                matrix.setElement(i, j, Double.parseDouble(elements[j]));
            }
        }

        return matrix;
    }

    // Reads a matrix from text, supporting flexible input (for example, pasted from a file)
    public static Matrix readMatrixFromTextFlexible(TextArea inputTerminal) {
        String[] lines = inputTerminal.getText().split("\\n");
        List<double[]> rows = new ArrayList<>();
        int rowCount = 0;
        int colCount = -1; // Determine column count from first row

        for (String line : lines) {
            String[] elements = line.split("\\s+"); // Handle spaces/tabs
            if (colCount == -1) colCount = elements.length; // Set columns on first pass

            double[] row = new double[colCount];
            for (int i = 0; i < colCount; i++) {
                row[i] = Double.parseDouble(elements[i]);
            }
            rows.add(row);
            rowCount++;
        }

        // Construct the matrix from the parsed rows
        Matrix matrix = new Matrix(rowCount, colCount);
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                matrix.setElement(i, j, rows.get(i)[j]);
            }
        }
        return matrix;
    }

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

    // Refactored method to request an integer from the TextArea instead of console input
    public static int getIntFromText(TextArea inputTerminal, String prompt) {
        // Assuming prompt is displayed in a label above input terminal in the UI
        return Integer.parseInt(inputTerminal.getText().trim());
    }

    // Refactored method to request a double from the TextArea instead of console input
    public static double getDoubleFromText(TextArea inputTerminal, String prompt) {
        return Double.parseDouble(inputTerminal.getText().trim());
    }

    // Method to get a matrix of given rows and columns from TextArea
    public static double[][] getMatrixFromText(TextArea inputTerminal, int rows, int cols) {
        String[] lines = inputTerminal.getText().split("\\n");
        double[][] matrix = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            String[] elements = lines[i].split("\\s+");
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Double.parseDouble(elements[j]);
            }
        }

        return matrix;
    }

    // Get XY data points from TextArea for interpolation
    public static double[][] getXYDataFromText(TextArea inputTerminal, int n) {
        String[] lines = inputTerminal.getText().split("\\n");
        double[][] data_array = new double[n][2];

        for (int i = 0; i < n; i++) {
            String[] elements = lines[i].split("\\s+");
            data_array[i][0] = Double.parseDouble(elements[0]); // X data
            data_array[i][1] = Double.parseDouble(elements[1]); // Y data
        }

        return data_array;
    }

    // Method to request an array (for example, constants or x/y values)
    public static double[] getArrayFromText(TextArea inputTerminal, int size) {
        String[] elements = inputTerminal.getText().split("\\s+");
        double[] array = new double[size];

        for (int i = 0; i < size; i++) {
            array[i] = Double.parseDouble(elements[i]);
        }
        return array;
    }
}
