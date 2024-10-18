package Algeo01;

import Algeo01.interpolation.PolynomialInterpolation;
import Algeo01.matrix.Matrix;
import Algeo01.matrix.MatrixSolver;
import Algeo01.regression.MultipleLinearRegression;
import Algeo01.utils.InputFunctions;
import Algeo01.utils.OutputFunctions;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // pilihan ditampilkan kepada pengguna
            System.out.println("Hak cipta sepenuhnya dimiliki oleh SadangSerang Co.");
            System.out.println("Selamat datang di program pemecah persamaan dan penghitung regresi!");
            System.out.println("1. Sistem Persamaan Linier");
            System.out.println("2. Determinan");
            System.out.println("3. Matriks Balikan");
            System.out.println("4. Interpolasi Polinom");
            System.out.println("5. Interpolasi Bicubic Spline");
            System.out.println("6. Regresi Linier");
            System.out.println("7. Regresi Kuadratik Berganda");
            System.out.println("8. Interpolasi Gambar");
            System.out.println("9. Ngemaling helm-nya reza");

            // meminta pilihan dari pengguna
            System.out.print("Masukkan pilihanmu: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    SistemPersamaanLinier();
                    break;
                case 2:
                    Determinan();
                    break;
                case 3:
                    MatriksBalikan();
                    break;
                case 4:
                    InterpolasiPolinom();
                    break;
                case 5:
                    InterpolasiBicubicSpline();
                    break;
                case 6:
                    RegresiLinier();
                case 7:

                case 8:
//                    InterpolasiGambar(scanner);
                case 9:
                    System.out.println("Keluar dari program.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Yang bener lah.");
            }
        }
    }

    public static void SistemPersamaanLinier() {
        Matrix matrixAugmented = InputFunctions.readMatrixFromInput();

        double[] solution = MatrixSolver.gaussElimination(matrixAugmented);
        OutputFunctions.printCoefficients(solution, true);
    }


    public static void Determinan() {
        Matrix matrix = InputFunctions.readMatrixFromInput();
        double determinan = MatrixSolver.determinant(matrix);
        System.out.println("Determinan: " + determinan);
    }

    public static void MatriksBalikan() {
        Matrix matrix = InputFunctions.readMatrixFromInput();
        Matrix matrixBalikan = MatrixSolver.inverseGaussJordan(matrix);
        OutputFunctions.displayMatrix(matrixBalikan);
    }

    public static void InterpolasiPolinom() {

        int n = InputFunctions.getInt("Masukkan jumlah data: ");
        double[][] data_array = InputFunctions.getXYdata(n, "Masukkan titik-titik data: ");
        double[] solution = PolynomialInterpolation.calculatePolynomialEquation(n, data_array);
        OutputFunctions.printCoefficients(solution, true);

        double x = InputFunctions.getDouble("Nilai x yang ingin ditaksir: ");
        double estimation = PolynomialInterpolation.calculateY(solution, x);

        System.out.print("Nilai y hasil taksiran: " + estimation);
    }

    public static void InterpolasiBicubicSpline() {

    }

    public static void RegresiLinier() {

        int m = InputFunctions.getInt("Masukkan jumlah data: ");
        int n = InputFunctions.getInt("Masukkan banyak peubah: ");
        double[][] data_array = InputFunctions.getMatrix(m, n+1, "Masukkan titik-titik data.");

        double[] solution = MultipleLinearRegression.calculateRegressionEquation(data_array, m, n);
        OutputFunctions.printCoefficients(solution, true);

        double[] x_array = InputFunctions.getArray(m, "Masukkan data yang ingin ditaksir: ");
        double estimation = MultipleLinearRegression.calculateY(solution, x_array);

        System.out.print("Nilai y hasil taksiran: " + estimation);
    }
}

