package regression;

import matrix.Matrix;
import matrix.MatrixSolver;
import java.util.Scanner;

public class MultipleQuadraticRegression {
    public static Scanner scanner = new Scanner(System.in);

    public static Matrix buildQuadraticDesignMatrix(double[][] X) {
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

    public static Matrix calculateBetaQuadraticRegression(Matrix Quadratic,double[][] Y) {
        Matrix Xt = Quadratic.transpose();
        Matrix XtX = Xt.multiplyMatrix(Quadratic);

        // Invers XtX
        Matrix XtXInverseMatrix = null;
        try {
            XtXInverseMatrix = MatrixSolver.inverseGaussJordan(XtX);
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

    // Fungsi untuk mencetak persamaan regresi kuadratik
    public static void printQuadraticEquation(Matrix coefficients, int numFeatures) {
        StringBuilder equation = new StringBuilder();
        equation.append("y = ").append(String.format("%.4f", coefficients.data[0][0]));

        // Tambah istilah linear
        for (int i = 0; i < numFeatures; i++) {
            equation.append(" + ").append(String.format("%.4f",coefficients.data[i + 1][0])).append(" * x").append(i + 1);
        }

        // Tambah istilah kuadrat
        for (int i = 0; i < numFeatures; i++) {
            equation.append(" + ").append(String.format("%.4f", coefficients.data[i + 1 + numFeatures][0])).append(" * x").append(i + 1).append("^2");
        }

        // Tambah istilah interaksi
        int idx = 1 + 2 * numFeatures;
        for (int i = 0; i < numFeatures; i++) {
            for (int j = i + 1; j < numFeatures; j++) {
                equation.append(" + ").append(String.format("%.4f", coefficients.data[idx++][0])).append(" * x").append(i + 1).append(" * x").append(j + 1);
            }
        }

        System.out.println(equation.toString());
    }
    
    public static double predictQuadraticRegression(Matrix coefficients, Matrix observation) {
        // Intercept
        double prediction = coefficients.data[0][0];
        for (int i = 1; i < coefficients.rowEff; i++) {
             prediction += coefficients.data[i][0] * observation.data[0][i];
        }
        return Math.round(prediction * 10000.0) / 10000.0;
    }

    // Melatih model regresi kuadratik
    public static Matrix trainQuadraticModel(double[][] data_array, int n) {
        // Membentuk matriks X dan Y
        double[][] X = new double[data_array.length][n];
        double[][] Y = new double[data_array.length][1];
        
        Matrix.splitAugmentedMatrix(data_array, X, Y);

        // Membangun matriks desain kuadratik
        Matrix designMatrix = MultipleQuadraticRegression.buildQuadraticDesignMatrix(X);
    
        // Menghitung koefisien regresi kuadratik
        Matrix coefficients = MultipleQuadraticRegression.calculateBetaQuadraticRegression(designMatrix, Y);
        
        // Mencetak fungsi regresi
        MultipleQuadraticRegression.printQuadraticEquation(coefficients, X[0].length);
    
        return coefficients;
    }

    public static double[][] inputObservation(int n) {
        double[][] observation = new double[1][n];
        System.out.println("Masukkan nilai untuk observasi baru:");
    
        for (int i = 0; i < n; i++) {
            System.out.print("Nilai untuk variabel x" + (i + 1) + ": ");
            observation[0][i] = scanner.nextDouble();
        }
        
        return observation;
    }    
        
    public static double predictQuadratic(Matrix coefficients, double[][] observation) {
        // Membangun matriks desain kuadratik dari observasi baru
        Matrix obsMatrix = MultipleQuadraticRegression.buildQuadraticDesignMatrix(observation);
    
        // Melakukan prediksi
        double predictedValue = MultipleQuadraticRegression.predictQuadraticRegression(coefficients, obsMatrix);
        
        // Menampilkan observasi dan hasil prediksi
        StringBuilder observationStr = new StringBuilder();
        for (int i = 0; i < observation[0].length; i++) {
            observationStr.append(observation[0][i]);
            if (i < observation[0].length - 1) {
                observationStr.append(", ");
            }
        }
        System.out.println("Prediksi nilai untuk observasi [" + observationStr.toString() + "] adalah: " + String.format("%.4f", predictedValue));
    
        return predictedValue;
    }    
}
