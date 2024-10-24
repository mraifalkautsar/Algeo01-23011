package regression;

import matrix.Matrix;
import matrix.MatrixSolver;

public class MultipleLinearRegression {
    public static double[] calculateRegressionEquation(double[][] data_array, int m, int n) {
        // MEMPERSIAPKAN MATRIKS TERAUGMENTASI
        Matrix matrixAugmented = new Matrix(n + 1, n + 2);

        // PENGISIAN MATRIKS TERAUGMENTASI
        // Baris pertama
        matrixAugmented.setElement(0, 0, m);  // Sum of the number of data points

        for (int j = 0; j < n; j++) {
            double sumXj = 0;
            for (int i = 0; i < m; i++) {
                sumXj += data_array[i][j];
            }
            matrixAugmented.setElement(0, j + 1, sumXj);
        }

        double sumY = 0;
        for (int i = 0; i < m; i++) {
            sumY += data_array[i][n];  //
        }
        matrixAugmented.setElement(0, n + 1, sumY);  // Set sum of y values

        // Baris seterusnya
        for (int j = 0; j < n; j++) {
            // Isi kolom pertama dengan sumXj
            double sumXj = 0;
            for (int i = 0; i < m; i++) {
                sumXj += data_array[i][j];
            }
            matrixAugmented.setElement(j + 1, 0, sumXj);

            // Mengisi baris seterusnya dengan jumlah dari suku-suku Xi * Xj
            for (int k = 0; k < n; k++) {
                double sumXiXj = 0;
                for (int i = 0; i < m; i++) {
                    sumXiXj += data_array[i][j] * data_array[i][k];
                }
                matrixAugmented.setElement(j + 1, k + 1, sumXiXj);
            }

            // mengisi kolom terakhir pada baris dengan jumlah dari Xi * y
            double sumXiY = 0;
            for (int i = 0; i < m; i++) {
                sumXiY += data_array[i][j] * data_array[i][n];
            }
            matrixAugmented.setElement(j + 1, n + 1, sumXiY);
        }

        // Memecahkan matriks augmented menggunakan metode Gauss-Jordan.
        return MatrixSolver.gaussElimination(matrixAugmented);
    }

    public static double calculateY(double[] coefficients, double[] x_array) {
        // Memulai dengan intercept
        double estimatedY = coefficients[0];

        // Add the rest of the terms: coefficients[1] * x1 + coefficients[2] * x2 + ...
        for (int i = 1; i < coefficients.length; i++) {
            estimatedY += coefficients[i] * x_array[i - 1];  // x_array is of size n, coefficients is n+1
        }

        return estimatedY;
    }

    public static String getLinearRegressionEquation(double[] coefficients, int numFeatures) {
        StringBuilder equation = new StringBuilder();
        equation.append("y = ").append(String.format("%.4f", coefficients[0]));

        for (int i = 0; i < numFeatures; i++) {
            equation.append(" + ").append(String.format("%.4f", coefficients[i + 1])).append(" * x").append(i + 1);
        }
        return equation.toString();
    }
}

