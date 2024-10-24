package interpolation;
import matrix.Matrix;
import matrix.MatrixSolver;

public class PolynomialInterpolation {

    public static double[] calculatePolynomialEquation(int n, double[][] tuples) {
        // Buat matriks augmented berukuran n x n + 1 (kenapa ditambah 1? untuk ruang vektor y.)
        Matrix matrixAugmented = new Matrix(n, n + 1);

        // Isi matriks dengan nilai dari titik-titik data
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrixAugmented.setElement(i, j, Math.pow(tuples[i][0], j)); // Set x^j
            }
            matrixAugmented.setElement(i, n, tuples[i][1]); // Set y value
        }

        // Pecahkan matriks dengan eliminasi gauss.
        return MatrixSolver.gaussElimination(matrixAugmented);

    }

    public static double calculateY(double[] coefficients, double x) {
        int numberOfCoefficients = coefficients.length;
        double estimatedY = coefficients[0]; // Mulai dari suku konstan

        for (int i = 1; i < numberOfCoefficients; i++) {
            estimatedY += coefficients[i] * Math.pow(x, i); // Tambahkan suku-suku setelahnya
        }

        return estimatedY;
    }

    public static String getPolynomialEquation(double[] coefficients, int numFeatures) {
        StringBuilder equation = new StringBuilder();
        equation.append("y = ").append(String.format("%.4f", coefficients[0]));

        for (int i = 1; i < numFeatures; i++) {
            if (coefficients[i] >= 0) {
                equation.append(" + ").append(String.format("%.4f", coefficients[i])).append(" * x^").append(i);
            } else {
                equation.append(" - ").append(String.format("%.4f", Math.abs(coefficients[i]))).append(" * x^").append(i);
            }
        }

        return equation.toString();
    }
}
