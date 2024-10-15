package interpolation;
import matrix.Matrix;

public class PolynomialInterpolation {

    public static double[] calculatePolynomialEquation(int n, double[][] tuples, double x) {

        Matrix matrixAugmented = new Matrix(n, n+1);

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

    // n = input()
    // for (int i = 0; i < n; i++)
    //      arr[i] = input()
    //
    // for (int i = 0; i < n; i++)
    //      for (int j = 1; j < n + 1; j++)
    //          output(pow(arr[i], j))
    //      output(arr[n +1])
}
