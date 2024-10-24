package regression;

import matrix.Matrix;
import matrix.MatrixSolver;

// BRAINSTORM ALGORITMA
// pengisian baris paling atas
// inisialisasi matriks ukuran m+1 x m+2

// matrixAugmented[0][0] = n
// for (int k_kanan = 0; k_kanan < m+1; k++)
//      element_sum = 0;
//      for (int i = 0; i < n; i++)
//          element_sum += tuples[i][k_kanan]
//      matrixAugmented[0][k_kanan+1] = element_sum

// pengisian baris-baris seterusnya
// for (int k_bawah = 0; k_bawah < m; k_bawah++)
//      for (int i = 0; i < n; i++)
//          matrixAugmented[k_bawah+1][0] += tuples[i][k_bawah]
//      for (int k_kanan = 0; k_kanan < m + 1; k_kanan++)
//          for (int i = 0; i < n; i++)
//              matrixAugmented[k_bawah+1][k_kanan+1] += tuples[i][k_bawah] * tuples[i][k_kanan]

// solve using gauss elimination
// return array

// IMPLEMENTASI
public class MultipleLinearRegression {
    public static double[] calculateRegressionEquation(double[][] data_array, int m, int n) {
        // Create an augmented matrix (n+1)x(n+2) for the normal equation
        Matrix matrixAugmented = new Matrix(n + 1, n + 2);

        // Fill the augmented matrix
        // The first row (sum of y values and sums of x columns)
        matrixAugmented.setElement(0, 0, m);  // Sum of the number of data points

        for (int j = 0; j < n; j++) {
            double sumXj = 0;
            for (int i = 0; i < m; i++) {
                sumXj += data_array[i][j];
            }
            matrixAugmented.setElement(0, j + 1, sumXj);  // Set the sum of xj for the first row
        }

        double sumY = 0;
        for (int i = 0; i < m; i++) {
            sumY += data_array[i][n];  // Last column in data_array is y
        }
        matrixAugmented.setElement(0, n + 1, sumY);  // Set sum of y values

        // Fill the rest of the rows
        for (int j = 0; j < n; j++) {
            // Fill the first column in the current row with sumXj (again)
            double sumXj = 0;
            for (int i = 0; i < m; i++) {
                sumXj += data_array[i][j];
            }
            matrixAugmented.setElement(j + 1, 0, sumXj);

            // Fill the rest of the row with sums of Xi * Xj terms
            for (int k = 0; k < n; k++) {
                double sumXiXj = 0;
                for (int i = 0; i < m; i++) {
                    sumXiXj += data_array[i][j] * data_array[i][k];
                }
                matrixAugmented.setElement(j + 1, k + 1, sumXiXj);
            }

            // Fill the last column in the current row with sum(Xi * y)
            double sumXiY = 0;
            for (int i = 0; i < m; i++) {
                sumXiY += data_array[i][j] * data_array[i][n];
            }
            matrixAugmented.setElement(j + 1, n + 1, sumXiY);
        }

        // Solve the augmented matrix using Gaussian elimination or similar method
        return MatrixSolver.gaussElimination(matrixAugmented);
    }

    public static double calculateY(double[] coefficients, double[] x_array) {
        // Start with the intercept (coefficients[0])
        double estimatedY = coefficients[0];

        // Add the rest of the terms: coefficients[1] * x1 + coefficients[2] * x2 + ...
        for (int i = 1; i < coefficients.length; i++) {
            estimatedY += coefficients[i] * x_array[i - 1];  // x_array is of size n, coefficients is n+1
        }

        return estimatedY;
    }
}

