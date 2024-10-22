import interpolation.PolynomialInterpolation;
import interpolation.BicubicSplineInterpolation;
import matrix.Matrix;
import matrix.MatrixSolver;
import regression.MultipleLinearRegression;
import utils.InputUtils;
import utils.OutputUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// MAIN MENU UNTUK PROGRAM

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

    // FUNGSI-FUNGSI UNTUK TIAP PILIHAN

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

        System.out.println("Metode yang ingin digunakan?");
        System.out.println("1. Eliminasi Gauss");
        System.out.println("2. Eliminasi Gauss-Jordan");
        System.out.println("3. Invers Matriks");
        System.out.println("4. Cramer");

        double[] solution;

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 4)) {
                System.out.println("Masukan salah.");
            }
            else if (choice == 1) {
                solution = MatrixSolver.gaussElimination(matrixAugmented);
                break;
            }
            else if (choice == 2) {
                solution = MatrixSolver.gaussJordanElimination(matrixAugmented);
                break;
            }
            else if (choice == 3) {
                solution = MatrixSolver.solveUsingInverse(matrixAugmented);
                break;
            }
            else {
                solution = MatrixSolver.solveUsingCramer(matrixAugmented);
                break;
            }
        }
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

        System.out.println("Metode yang ingin digunakan?");
        System.out.println("1. Ekspansi kofaktor");
        System.out.println("2. Reduksi baris");

        double determinan;

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 2)) {
                System.out.println("Masukan salah.");
            }
            else if (choice == 1) {
                determinan = MatrixSolver.determinantByRowReduction(matrix);
                break;
            }
            else {
                determinan = MatrixSolver.determinant(matrix);
                break;
            }
        }

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

        System.out.println("Metode yang ingin digunakan?");
        System.out.println("1. Invers Gauss-Jordan");
        System.out.println("2. Invers Adjoin");

        Matrix matriksBalikan = null;

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 2)) {
                System.out.println("Masukan salah.");
            }
            else if (choice == 1) {
                matriksBalikan = MatrixSolver.inverseGaussJordan(matriksBalikan);
                break;
            }
            else {
                matriksBalikan = MatrixSolver.inverseAdjoin(matriksBalikan);
                break;
            }
        }

        OutputUtils.displayMatrix(matriksBalikan);
    }

    public static void InterpolasiPolinom() {

        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if (choice <= 0 || choice > 2) {
                System.out.println("Masukan salah.");
            } else if (choice == 1) {
                int n = InputUtils.getInt("Masukkan jumlah data: ");
                double[][] data_array = InputUtils.getXYdata(n, "Masukkan titik-titik data: ");
                double[] solution = PolynomialInterpolation.calculatePolynomialEquation(n, data_array);
                OutputUtils.printCoefficients(solution, true);

                double x = InputUtils.getDouble("Nilai x yang ingin ditaksir: ");
                double estimation = PolynomialInterpolation.calculateY(solution, x);

                System.out.print("Nilai y hasil taksiran: " + estimation);
                break;
            } else if (choice == 2) {
                // file input
                try {
                    String filename = InputUtils.getString("Masukkan nama file: ");
                    File file = new File(filename);
                    Scanner fileScanner = new Scanner(file);

                    // baca titik-titik data dari file
                    List<double[]> dataList = new ArrayList<>();
                    double xToEstimate = 0;

                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine().trim();
                        String[] values = line.split("\\s+");

                        if (values.length == 2) {
                            // melakukan parsing pada titik-titik data
                            double x = Double.parseDouble(values[0]);
                            double y = Double.parseDouble(values[1]);
                            dataList.add(new double[]{x, y});
                        } else if (values.length == 1) {
                            // melakukan parsing untuk x yang ingin diperkirakan
                            xToEstimate = Double.parseDouble(values[0]);
                        }
                    }

                    fileScanner.close();

                    // convert list ke array untuk interpolasi
                    int n = dataList.size();
                    double[][] data_array = new double[n][2];
                    for (int i = 0; i < n; i++) {
                        data_array[i] = dataList.get(i);
                    }

                    // hitung solusi
                    double[] solution = PolynomialInterpolation.calculatePolynomialEquation(n, data_array);
                    OutputUtils.printCoefficients(solution, true);

                    // melakukan estimasi
                    double estimation = PolynomialInterpolation.calculateY(solution, xToEstimate);
                    System.out.println("Nilai y hasil taksiran: " + estimation);

                } catch (FileNotFoundException e) {
                    System.out.println("File tidak ditemukan. Coba lagi.");
                } catch (Exception e) {
                    System.out.println("Terjadi kesalahan saat membaca file.");
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    public static void InterpolasiBicubicSpline() {
        
        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if (choice <= 0 || choice > 2) {
                System.out.println("Masukan salah.");

            } else if (choice == 1) {
                Matrix vectorY = InputUtils.readMatrixFromInput();
                double a = InputUtils.getDouble("Masukkan nilai a : ");
                double b = InputUtils.getDouble("Masukkan nilai b : ");

                double res = BicubicSplineInterpolation.calculate(vectorY, a, b);
                System.out.println("Hasil interpolasi : " + res);
                break;

            } else {
                Object[] file = InputUtils.readVectorAndABfromFile("../test/TC1BS.txt");
                double[] vector = (double[]) file[0];
                double a = (double) file[1];
                double b = (double) file[2];

                // convert array ke ADT matrix
                Matrix vectorY = new Matrix(16, 1);
                for (int i = 0; i < 16; i++) {
                    vectorY.setElement(i, 0, vector[i]);
                }

                double res = BicubicSplineInterpolation.calculate(vectorY, a, b);
                System.out.println("Hasil interpolasi : " + res);
                break;
            }
        }
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

