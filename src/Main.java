import interpolation.PolynomialInterpolation;
import matrix.Matrix;
import matrix.MatrixSolver;
import regression.MultipleLinearRegression;
import utils.InputUtils;
import utils.OutputUtils;

import java.io.IOException;
import java.util.Scanner;

// File main menerapkan prinsip "clean main, clean life"

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
                    break;
                case 7:
//                    RegresiKuadratikBerganda()
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
        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        Matrix matrixAugmented;

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 2)) {
                System.out.println("Masukan salah.");
            }
            else if (choice == 1) {
                matrixAugmented = InputUtils.readMatrixFromInput();
                break;
            }
            else {
                try {
                    matrixAugmented = InputUtils.readMatrixFromFile("interpolation/MatrixX.txt");
                } catch (IOException e) {
                    System.out.println("Error membaca matriks dari file: " + e.getMessage());
                    return;
                    }
                break;
            }
        }

        double[] solution = MatrixSolver.gaussElimination(matrixAugmented);
        OutputUtils.printCoefficients(solution, true);
    }


    public static void Determinan() {
        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        Matrix matrix;

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 2)) {
                System.out.println("Masukan salah.");
            }
            else if (choice == 1) {
                matrix = InputUtils.readMatrixFromInput();
                break;
            }
            else {
                try {
                    matrix = InputUtils.readMatrixFromFile("interpolation/MatrixX.txt");
                } catch (IOException e) {
                    System.out.println("Error membaca matriks dari file: " + e.getMessage());
                    return;
                }
                break;
            }
        }

        double determinan = MatrixSolver.determinant(matrix);
        System.out.println("Determinan: " + determinan);
    }

    public static void MatriksBalikan() {
        Matrix matrix;
        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 2)) {
                System.out.println("Masukan salah.");
            }
            else if (choice == 1) {
                matrix = InputUtils.readMatrixFromInput();
                break;
            }
            else {
                try {
                    matrix = InputUtils.readMatrixFromFile("interpolation/MatrixX.txt");
                } catch (IOException e) {
                    System.out.println("Error membaca matriks dari file: " + e.getMessage());
                    return;
                }
                break;
            }
        }

        Matrix matrixBalikan = MatrixSolver.inverseGaussJordan(matrix);
        OutputUtils.displayMatrix(matrixBalikan);
    }

    public static void InterpolasiPolinom() {

        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 2)) {
                System.out.println("Masukan salah.");
            }
            else if (choice == 1) {
                int n = InputUtils.getInt("Masukkan jumlah data: ");
                double[][] data_array = InputUtils.getXYdata(n, "Masukkan titik-titik data: ");
                double[] solution = PolynomialInterpolation.calculatePolynomialEquation(n, data_array);
                OutputUtils.printCoefficients(solution, true);

                double x = InputUtils.getDouble("Nilai x yang ingin ditaksir: ");
                double estimation = PolynomialInterpolation.calculateY(solution, x);

                System.out.print("Nilai y hasil taksiran: " + estimation);
                break;
            }
            else {

            }
        }

    }

    public static void InterpolasiBicubicSpline() {

    }

    public static void RegresiLinier() {

        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        Matrix matrixAugmented;

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 2)) {
                System.out.println("Masukan salah.");
            }
            else if (choice == 1) {
                int m = InputUtils.getInt("Masukkan jumlah data: ");
                int n = InputUtils.getInt("Masukkan banyak peubah: ");
                double[][] data_array = InputUtils.getMatrix(m, n+1, "Masukkan titik-titik data.");

                double[] solution = MultipleLinearRegression.calculateRegressionEquation(data_array, m, n);
                OutputUtils.printCoefficients(solution, true);

                double[] x_array = InputUtils.getArray(m, "Masukkan data yang ingin ditaksir: ");
                double estimation = MultipleLinearRegression.calculateY(solution, x_array);

                System.out.print("Nilai y hasil taksiran: " + estimation);
            }
            else {
                try {
                    matrixAugmented = InputUtils.readMatrixFromFile("interpolation/MatrixX.txt");
                } catch (IOException e) {
                    System.out.println("Error reading matrix from file: " + e.getMessage());
                    return;
                }
                break;
            }
        }
    }
}

