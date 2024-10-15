package regression;

import matrix.Matrix;

// pengisian baris paling atas
// matrixAugmented[0][0] = n
// for (int j = 0; j < m; j++)
//      element_sum = 0;
//      for (int k = 0; k < n; k++)
//          element_sum += tuples[k][j]
//      matrixAugmented[0][j+1] = element_sum
// matrixAugmented[0][m+1] = n * tuples[0][m]

// pengisian baris-baris seterusnya
// for (int i = 1; i < m; i++)
//      for (int j = 0; j < n; j++)
//          matrixAugmented[i][0] += tuples[j][i-1]
//      for (int

// x1 x2 x3 x4 y
// 6  7  8  9  5
// 4  2  3  9  3

public class MultipleLinearRegression {
    public static double[] calculateEstimationEquation(double[][] tuples, int n, int m, double x) {

        Matrix matrixAugmented = new Matrix(n, n+1);

        matrixAugmented.setElement(0,0,n);
        for (int j = 0; j < m; j++) {
            int element_sum = 0;
            for (int k=0; k < n; k++) {
                element_sum += tuples[j-1][k];
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrixAugmented.setElement(i, j, tuples[i][0]); // dipangkatkan
            }
            matrixAugmented.setElement(i, n, tuples[i][1]);
        }

        // Solve dengan eliminasi Gauss-Jordan atau Kaidah Cramer.

        return new double[n];
    }

    public static double calculateY(double[] coefficients) {

        int numberOfCoefficients = coefficients.length;
        double estimatedY = coefficients[0];

        for (int i = 1; i < numberOfCoefficients; i++) {
            estimatedY = coefficients[i]; // dipangkatkan
        }

        return estimatedY;
    }

}
