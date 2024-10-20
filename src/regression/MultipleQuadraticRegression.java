package regression;

import matrix.Matrix;
import matrix.MatrixSolver;

public class MultipleQuadraticRegression {
    public Matrix buildQuadraticDesignMatrix(double[][] X) {
        int n = X.length; // Jumlah observasi
        int N = X[0].length; // Jumlah variabel

        int totalColumns = 1 + N + N + (N * (N - 1)) / 2;
        Matrix Quadratic = new Matrix(n, totalColumns);

        for (int i = 0; i < n; i++) {
            int columnIndex = 0;

            // Intercept
            Quadratic.data[i][columnIndex++] = 1.0;

            // Variabel linier
            for (int j = 0; j < N; j++) {
                Quadratic.data[i][columnIndex++] = X[i][j];
            }

            // Variabel kuadrat
            for (int j = 0; j < N; j++) {
                Quadratic.data[i][columnIndex++] = X[i][j] * X[i][j];
            }

            // Variabel interaksi
            for (int j = 0; j < N; j++) {
                for (int k = j + 1; k < N; k++) {
                    Quadratic.data[i][columnIndex++] = X[i][j] * X[i][k];
                }
            }
        }

        return Quadratic;
    }

    public Matrix calculateBetaQuadraticRegression(Matrix Quadratic,double[][] Y) {
        Matrix Xt = Quadratic.transpose();
        Matrix XtX = Xt.multiplyMatrix(Quadratic);

        // Invers XtX
        Matrix XtXInverseMatrix = null;
        try {
            XtXInverseMatrix = MatrixSolver.inverseAdjoin(XtX);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Matriks XtX tidak dapat di-invers, regresi tidak dapat dilanjutkan.");
        }

        // XtY
        Matrix YMatrix = new Matrix(Y.length, 1);
        YMatrix.fillMatrix(Y);
        Matrix XtY = Xt.multiplyMatrix(YMatrix);

        // Beta
        Matrix betaMatrix = XtXInverseMatrix.multiplyMatrix(XtY);

        Matrix beta = new Matrix(betaMatrix.rowEff,1);
        for (int i = 0; i < betaMatrix.rowEff; i++) {
            beta.data[i][0] = betaMatrix.data[i][0];
        }

        return beta;
    }

    public double predictQuadraticRegression(Matrix coefficients, Matrix observation) {
        double prediction = coefficients.data[0][0]; // Intercept

        //  Istilah linear
        for (int i = 0; i < observation.rowEff; i++) {
            prediction += coefficients.data[i + 1][0] * observation.data[0][i+1];
        }

        // Istilah kuadrat
        for (int i = 0; i < observation.rowEff; i++) {
            prediction += coefficients.data[i + 1 + observation.rowEff][0] * Math.pow(observation.data[0][i+1], 2);
        }

        // Istilah interaksi
        int interactionStartIndex = 1 + 2 * observation.rowEff;
        for (int i = 0; i < observation.rowEff; i++) {
            for (int j = i + 1; j < observation.rowEff; j++) {
                prediction += coefficients.data[interactionStartIndex++][0] * observation.data[0][i+1] * observation.data[0][j+1];
            }
        }

        return prediction;
    }

    // Fungsi untuk mencetak persamaan regresi kuadratik
    public void printQuadraticEquation(Matrix coefficients, int numFeatures) {
        StringBuilder equation = new StringBuilder();
        equation.append("y = ").append(coefficients.data[0][0]);

        // Tambah istilah linear
        for (int i = 0; i < numFeatures; i++) {
            equation.append(" + ").append(coefficients.data[i + 1][0]).append(" * x").append(i + 1);
        }

        // Tambah istilah kuadrat
        for (int i = 0; i < numFeatures; i++) {
            equation.append(" + ").append(coefficients.data[i + 1 + numFeatures][0]).append(" * x").append(i + 1).append("^2");
        }

        // Tambah istilah interaksi
        int idx = 1 + 2 * numFeatures;
        for (int i = 0; i < numFeatures; i++) {
            for (int j = i + 1; j < numFeatures; j++) {
                equation.append(" + ").append(coefficients.data[idx++][0]).append(" * x").append(i + 1).append(" * x").append(j + 1);
            }
        }

        System.out.println(equation.toString());
    }
}
