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
        // Buat matriks augmented berukuran n+1 x n+2
        Matrix matrixAugmented = new Matrix(n+1,  n+2);

        // Pengisian baris pertama
        matrixAugmented.setElement(0, 0, m);
        for (int k_kanan = 0; k_kanan < n+1; k_kanan++) {
            double element_sum = 0;
            for (int i = 0; i < m; i++) {
                element_sum += data_array[i][k_kanan];
            }
            matrixAugmented.setElement(0, k_kanan+1, element_sum); // Set y value
        }

        // Pengisian baris-baris seterusnya
        for (int k_bawah = 0; k_bawah < n; k_bawah++) {
            double element_sum = 0;
            for (int i = 0; i < m; i++) {
                element_sum += data_array[i][k_bawah];
            }
            matrixAugmented.setElement(k_bawah + 1, 0, element_sum);
            for (int k_kanan = 0; k_kanan < n+1; k_kanan++) {
                element_sum = 0;
                for (int i = 0; i < m; i++) {
                    element_sum += data_array[i][k_bawah] * data_array[i][k_kanan];
                }
                matrixAugmented.setElement(k_bawah+1,k_kanan+1, element_sum);
            }
        }

        return MatrixSolver.gaussElimination(matrixAugmented);
    }

    public static double calculateY(double[] coefficients, double[] x_array) {

        double estimatedY;

        estimatedY = coefficients[0];
        for (int i = 1; i < coefficients.length; i++) {
            estimatedY = coefficients[i] * x_array[i-1];
        }

        return estimatedY;
    }

}
