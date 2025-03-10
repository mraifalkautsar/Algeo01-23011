import interpolation.PolynomialInterpolation;
import interpolation.BicubicSplineInterpolation;
import matrix.Matrix;
import matrix.MatrixSolver;
import regression.MultipleLinearRegression;
import regression.MultipleQuadraticRegression;
import utils.InputUtils;
import utils.OutputUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static interpolation.BicubicSplineInterpolation.*;

// MAIN MENU UNTUK PROGRAM

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.print("███████  █████  ██████   █████  ███    ██  ██████  ███████ ███████ ██████   █████  ███    ██  ██████       ██████  ██████     \n" +
                         "██      ██   ██ ██   ██ ██   ██ ████   ██ ██       ██      ██      ██   ██ ██   ██ ████   ██ ██           ██      ██    ██    \n" +
                         "███████ ███████ ██   ██ ███████ ██ ██  ██ ██   ███ ███████ █████   ██████  ███████ ██ ██  ██ ██   ███     ██      ██    ██    \n" +
                         "     ██ ██   ██ ██   ██ ██   ██ ██  ██ ██ ██    ██      ██ ██      ██   ██ ██   ██ ██  ██ ██ ██    ██     ██      ██    ██    \n" +
                         "███████ ██   ██ ██████  ██   ██ ██   ████  ██████  ███████ ███████ ██   ██ ██   ██ ██   ████  ██████       ██████  ██████  ██ \n" +
                         "                               mempersembahkan IRC (Interpolator and Regression Calculator) v1.0" +
                         "\n");

        while (true) {
            // pilihan ditampilkan kepada pengguna
            System.out.println("\n== MAIN MENU ==");
            System.out.println("1. Sistem Persamaan Linier");
            System.out.println("2. Determinan");
            System.out.println("3. Matriks Balikan");
            System.out.println("4. Interpolasi Polinom");
            System.out.println("5. Interpolasi Bicubic Spline");
            System.out.println("6. Regresi Linier Berganda");
            System.out.println("7. Regresi Kuadratik Berganda");
            System.out.println("8. Interpolasi Gambar");
            System.out.println("9. Keluar");
            System.out.println("NOTE: Apabila program meminta masukan nama file, tidak perlu meng-include extension dari file.");

            // meminta pilihan dari pengguna
            System.out.print("Masukkan pilihan Anda: ");
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
                    RegresiKuadratikBerganda();
                    break;
                case 8:
                    ImageResizing();
                case 9:
                    System.out.println("Keluar dari program.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Yang bener aja lah.");
            }
        }
    }

    // FUNGSI-FUNGSI UNTUK TIAP PILIHAN

    public static void SistemPersamaanLinier() {
        System.out.println("\n== SISTEM PERSAMAAN LINIER ==");
        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        Matrix matrixAugmented;

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 2)) {
                System.out.println("Masukan salah.");
            } else if (choice == 1) {
                matrixAugmented = InputUtils.readMatrixFromInput();
                break;
            } else {
                try {
                    String fileName = InputUtils.getString("Masukkan nama file: ");
                    matrixAugmented = InputUtils.readMatrixFromFile("spl", fileName);
                } catch (IOException e) {
                    System.out.println("Error membaca matriks dari file: " + e.getMessage());
                    return;
                }
                break;
            }
        }

        System.out.println("\nMetode yang ingin digunakan?");
        System.out.println("1. Eliminasi Gauss");
        System.out.println("2. Eliminasi Gauss-Jordan");
        System.out.println("3. Matriks Balikan");
        System.out.println("4. Cramer");

        double[] solution_double = null;
        boolean gauss = false;
        String solution_string = null;

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");
            
            if ((choice <= 0) || (choice > 4)) {
                System.out.println("Masukan salah.");
            } else {
                try {
                    
                    if (choice == 1) {
                        solution_string = MatrixSolver.gaussEliminationForMain(matrixAugmented);
                        System.out.println(solution_string);
                        gauss = true;
                    } else if (choice == 2) {
                        solution_string = MatrixSolver.gaussJordanEliminationForMain(matrixAugmented);
                        System.out.println(solution_string);
                        gauss = true;
                    } else if (choice == 3) {
                        solution_double = MatrixSolver.solveUsingInverse(matrixAugmented);
                    } else {
                        solution_double = MatrixSolver.solveUsingCramer(matrixAugmented);
                    }
                    break; // Keluar dari loop jika tidak ada exception
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    // Bisa tambahkan logging untuk info lebih detail jika diperlukan
                }
            }
        }

        if (!gauss) {
            OutputUtils.printCoefficients(solution_double);
        }

        // Opsi untuk menyimpan solusi ke file
        System.out.println("\nApakah Anda ingin menyimpan hasil ke dalam file?");
        System.out.println("1. Ya");
        System.out.println("2. Tidak");

        int saveChoice = InputUtils.getInt("Masukkan pilihanmu: ");

        if (saveChoice == 1) {
            String outputFileName = InputUtils.getString("Masukkan nama file output: ");
            try {
                if (gauss) {
                    OutputUtils.saveSistemPersamaanLinierGauss(solution_string, "../test/spl/output/" + outputFileName + ".txt");
                } else {
                    OutputUtils.saveSistemPersamaanLinier(solution_double, "../test/spl/output/" + outputFileName + ".txt");
                }
                System.out.println("Hasil berhasil disimpan ke dalam file: ../test/spl/output/" + outputFileName + ".txt");
            } catch (IOException e) {
                System.out.println("Terjadi kesalahan saat menyimpan file: " + e.getMessage());
            }
        } else {
            System.out.println("Hasil tidak disimpan ke dalam file.");
        }

    }



    public static void Determinan() {
        System.out.println("\n== DETERMINAN ==");
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
                matrix = InputUtils.readMatrixFromKeyboard();
                break;
            } else {
                String fileName = InputUtils.getString("Masukkan nama file: ");
                try {
                    matrix = InputUtils.readMatrixFromFile("determinan", fileName);
                } catch (IOException e) {
                    System.out.println("Error membaca matriks dari file: " + e.getMessage());
                    return;
                }
                break;
            }
        }

        System.out.println("\nMetode yang ingin digunakan?");
        System.out.println("1. Ekspansi kofaktor");
        System.out.println("2. Reduksi baris");

        double determinan;

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 2)) {
                System.out.println("Masukan salah.");
            }
            else if (choice == 2) {
                try {
                    determinan = MatrixSolver.determinantByRowReduction(matrix);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            }
            else {
                try {
                    determinan = MatrixSolver.determinant(matrix);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return;
                }
                break;
            }
        }

        System.out.println("Determinan: " + determinan);

        // Opsi untuk menyimpan solusi ke file
        System.out.println("\nApakah Anda ingin menyimpan hasil ke dalam file?");
        System.out.println("1. Ya");
        System.out.println("2. Tidak");

        int saveChoice = InputUtils.getInt("Masukkan pilihanmu: ");

        if (saveChoice == 1) {
            String outputFileName = InputUtils.getString("Masukkan nama file output: ");
            try {
                OutputUtils.saveDeterminant(determinan, "../test/determinan/output/" + outputFileName + ".txt");
                System.out.println("Hasil berhasil disimpan ke dalam file: ../test/determinan/output/" + outputFileName + ".txt");
            } catch (IOException e) {
                System.out.println("Terjadi kesalahan saat menyimpan file: " + e.getMessage());
            }
        } else {
            System.out.println("Hasil tidak disimpan ke dalam file.");
        }
    }

    public static void MatriksBalikan() {
        System.out.println("\n== INVERS ATAU MATRIKS BALIKAN ==");
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
                matrix = InputUtils.readMatrixFromKeyboard();
                break;
            } else {
                String fileName = InputUtils.getString("Masukkan nama file: ");
                try {
                    matrix = InputUtils.readMatrixFromFile("inverse", fileName);
                } catch (IOException e) {
                    System.out.println("Error membaca matriks dari file: " + e.getMessage());
                    return;
                }
                break;
            }
        }

        System.out.println("\nMetode yang ingin digunakan?");
        System.out.println("1. Invers Gauss-Jordan");
        System.out.println("2. Invers Adjoin");

        Matrix matriksBalikan = null;
        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 2)) {
            System.out.println("Masukan salah.");
            } else {
            try {
                if (choice == 1) {
                matriksBalikan = MatrixSolver.inverseGaussJordan(matrix);
                } else {
                matriksBalikan = MatrixSolver.inverseAdjoin(matrix);
                }
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
            }
        }

        OutputUtils.displayMatrix(matriksBalikan);

        // Opsi untuk menyimpan solusi ke file
        System.out.println("\nApakah Anda ingin menyimpan hasil ke dalam file?");
        System.out.println("1. Ya");
        System.out.println("2. Tidak");

        int saveChoice = InputUtils.getInt("Masukkan pilihanmu: ");

        if (saveChoice == 1) {
            String outputFileName = InputUtils.getString("Masukkan nama file output: ");
            try {
                OutputUtils.saveInverseMatrix(matriksBalikan, "../test/inverse/output/" + outputFileName + ".txt");
                System.out.println("Hasil matriks balikan berhasil disimpan ke dalam file: ../test/inverse/output/" + outputFileName + ".txt");
            } catch (IOException e) {
                System.out.println("Terjadi kesalahan saat menyimpan file: " + e.getMessage());
            }
        } else {
            System.out.println("Hasil tidak disimpan ke dalam file.");
        }

    }

    public static void InterpolasiPolinom() {
        System.out.println("\n== INTERPOLASI POLINOM ==");
        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if (choice <= 0 || choice > 2) {
                System.out.println("Masukan salah.");
            } else if (choice == 1) {
                // Input lewat keyboard
                int n = InputUtils.getInt("Masukkan jumlah data: ");
                double[][] data_array = InputUtils.getXYdata(n, "Masukkan titik-titik data: ");
                double[] solution = PolynomialInterpolation.calculatePolynomialEquation(n, data_array);
                String equation = PolynomialInterpolation.getPolynomialEquation(solution, n);
                System.out.println(equation);

                double x = InputUtils.getDouble("Nilai x yang ingin ditaksir: ");
                double estimation = PolynomialInterpolation.calculateY(solution, x);
                String estimation_output = ("Nilai y hasil taksiran: " + estimation);
                System.out.println(estimation_output);

                // Opsi untuk menyimpan solusi ke file
                OutputUtils.saveInterpolasiPolinom(equation, estimation_output);
                break;

            } else if (choice == 2) {
                // Input lewat file
                try {
                    // Minta nama file dari user
                    String filename = InputUtils.getString("Masukkan nama file: ");

                    // Buat path lengkapnya
                    String filePath = "../test/interpolasi_polinom/input/" + filename + ".txt";

                    // Baca file-nya
                    File file = new File(filePath);
                    Scanner fileScanner = new Scanner(file);

                    // Baca titik-titik data dari file
                    List<double[]> dataList = new ArrayList<>();
                    double xToEstimate = 0;

                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine().trim();
                        String[] values = line.split("\\s+");

                        if (values.length == 2) {
                            // Parsing titik-titik data
                            double x = Double.parseDouble(values[0]);
                            double y = Double.parseDouble(values[1]);
                            dataList.add(new double[]{x, y});
                        } else if (values.length == 1) {
                            // Parsing x yang mau diestimasi
                            xToEstimate = Double.parseDouble(values[0]);
                        }
                    }

                    fileScanner.close();

                    // Convert list jadi array buat interpolasi
                    int n = dataList.size();
                    double[][] data_array = new double[n][2];
                    for (int i = 0; i < n; i++) {
                        data_array[i] = dataList.get(i);
                    }

                    // Hitung solusi interpolasi polinomial
                    double[] solution = PolynomialInterpolation.calculatePolynomialEquation(n, data_array);
                    String equation = PolynomialInterpolation.getPolynomialEquation(solution, n);

                    System.out.println(equation);
                    // Estimasi nilai y untuk x yang diberikan
                    double estimation = PolynomialInterpolation.calculateY(solution, xToEstimate);
                    String estimation_output = ("f(" + xToEstimate + ") =" + estimation);
                    System.out.println(estimation_output);

                    // Opsi untuk menyimpan solusi ke file
                    OutputUtils.saveInterpolasiPolinom(equation, estimation_output);

                } catch (FileNotFoundException e) {
                    System.out.println("File tidak ditemukan di path: ../test/interpolasi_polinom/input/" + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Ada kesalahan saat pembacaan file.");
                    e.printStackTrace();
                }

                break;
            }
        }
    }

    public static void InterpolasiBicubicSpline() {
        System.out.println("\n== INTERPOLASI BICUBIC SPLINE ==");
        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if (choice <= 0 || choice > 2) {
                System.out.println("Masukan salah.");

            } else if (choice == 1) {
                // Input melalui keyboard
                Matrix vectorY = InputUtils.readMatrixFromInput();
                double a = InputUtils.getDouble("Masukkan nilai a : ");
                double b = InputUtils.getDouble("Masukkan nilai b : ");

                double res = BicubicSplineInterpolation.calculate(vectorY, a, b, false);
                System.out.println("Hasil interpolasi : " + res);

                // Option to save the result to a file
                OutputUtils.saveBicubicSpline(res);
                break;

            } else {
                // Input melalui file
                String filename = InputUtils.getString("Masukkan nama file: ");
                Object[] file = InputUtils.readVectorAndABfromFile("../test/interpolasi_bicubic/input/" + filename + ".txt");
                double[] vector = (double[]) file[0];
                double a = (double) file[1];
                double b = (double) file[2];

                // convert array ke ADT matrix
                Matrix vectorY = new Matrix(16, 1);
                for (int i = 0; i < 16; i++) {
                    vectorY.setElement(i, 0, vector[i]);
                }

                double res = BicubicSplineInterpolation.calculate(vectorY, a, b, false);
                System.out.println("Hasil interpolasi : " + res);

                // Opsi untuk menyimpan solusi ke file
                OutputUtils.saveBicubicSpline(res);
                break;
            }
        }
    }

    public static void RegresiLinier() {
        System.out.println("\n== REGRESI LINIER BERGANDA ==");
        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            if ((choice <= 0) || (choice > 2)) {
                System.out.println("Masukan salah.");
            } else if (choice == 1) {
                // Input dari keyboard
                int m = InputUtils.getInt("Masukkan jumlah data: ");
                int n = InputUtils.getInt("Masukkan banyak peubah: ");
                double[][] data_array = InputUtils.readAugmentedMatrixFromKeyboard(m, n);

                double[] solution = MultipleLinearRegression.calculateRegressionEquation(data_array, m, n);
                String equation = MultipleLinearRegression.getLinearRegressionEquation(solution, n);

                double[] x_array = InputUtils.getArray(n, "Masukkan data yang ingin ditaksir:");
                double estimation = MultipleLinearRegression.calculateY(solution, x_array);

                // Format the output as f(x1, x2, ..., xn) = estimated y
                StringBuilder estimationOutput = new StringBuilder("f(");
                for (int i = 0; i < x_array.length; i++) {
                    estimationOutput.append(x_array[i]);
                    if (i < x_array.length - 1) {
                        estimationOutput.append(", ");
                    }
                }
                estimationOutput.append(") = ").append(estimation);

                System.out.println(estimationOutput);

                // Option to save solution and estimation to file
                OutputUtils.saveRegresiLinier(equation, estimationOutput.toString());
                break;

            } else if (choice == 2) {
                // Input from file using Scanner
                String filename = InputUtils.getString("Masukkan nama file: ");

                try {
                    String filePath = "../test/regresi_linier/input/" + filename + ".txt";
                    File file = new File(filePath);
                    Scanner scanner = new Scanner(file);

                    List<double[]> dataList = new ArrayList<>();
                    double[] xToEstimate = null;
                    int n = -1;

                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        String[] values = line.split("\\s+");

                        if (n == -1) {
                            // Initialize number of variables from the first data line
                            n = values.length - 1;
                        }

                        double[] row = new double[values.length];
                        for (int i = 0; i < values.length; i++) {
                            row[i] = Double.parseDouble(values[i]);
                        }

                        if (values.length == n) {
                            // Values of x to estimate
                            xToEstimate = row;
                        } else {
                            // Add data points x1, x2, ..., xn, y
                            dataList.add(row);
                        }
                    }

                    scanner.close();

                    // Process data into 2D array for regression
                    int m = dataList.size();
                    n = dataList.get(0).length - 1; // Number of variables = number of columns - 1 (y column)
                    double[][] data_array = new double[m][n + 1];
                    for (int i = 0; i < m; i++) {
                        data_array[i] = dataList.get(i);
                    }

                    // Calculate regression solution
                    double[] solution = MultipleLinearRegression.calculateRegressionEquation(data_array, m, n);
                    String equation = MultipleLinearRegression.getLinearRegressionEquation(solution, n);
                    System.out.println(equation);

                    if (xToEstimate != null) {
                        double estimation = MultipleLinearRegression.calculateY(solution, xToEstimate);

                        // Format the output as f(x1, x2, ..., xn) = estimated y
                        StringBuilder estimationOutput = new StringBuilder("f(");
                        for (int i = 0; i < xToEstimate.length; i++) {
                            estimationOutput.append(xToEstimate[i]);
                            if (i < xToEstimate.length - 1) {
                                estimationOutput.append(", ");
                            }
                        }
                        estimationOutput.append(") = ").append(estimation);

                        System.out.println(estimationOutput);

                        // Option to save solution and estimation to file
                        OutputUtils.saveRegresiLinier(equation, estimationOutput.toString());
                    } else {
                        System.out.println("Tidak ada nilai x untuk ditaksir di file.");
                    }

                } catch (FileNotFoundException e) {
                    System.out.println("File tidak ditemukan: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Terjadi kesalahan saat membaca file.");
                    e.printStackTrace();
                }

                break;
            }
        }
    }


    public static void RegresiKuadratikBerganda() {
        System.out.println("\n== REGRESI KUADRATIK BERGANDA ==");
        System.out.println("Input dari keyboard atau file?");
        System.out.println("1. Keyboard");
        System.out.println("2. File");

        while (true) {
            int choice = InputUtils.getInt("Masukkan pilihanmu: ");

            int m = 0, n = 0;
            double[][] data_array = null;
            double[][] observationfromfile = null;
            boolean isObservation = false;
            
            if ((choice <= 0) || (choice > 2)) {
                System.out.println("Masukan salah.");
            } else if (choice == 1) {
                // Input dari keyboard
                m = InputUtils.getInt("Masukkan jumlah data: ");
                n = InputUtils.getInt("Masukkan banyak peubah: ");
                data_array = InputUtils.readAugmentedMatrixFromKeyboard(m, n);
            } else if (choice == 2) {
                // Input dari file menggunakan Scanner
                String filename = InputUtils.getString("Masukkan nama file : ");

                try {
                    String filePath = "../test/regresi_kuadratik/input/" + filename + ".txt";
                    File file = new File(filePath);
                    Scanner scanner = new Scanner(file);

                    List<double[]> dataList = new ArrayList<>();

                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        String[] values = line.split("\\s+");

                        double[] row = new double[values.length];
                        for (int i = 0; i < values.length; i++) {
                            row[i] = Double.parseDouble(values[i]);
                        }

                        dataList.add(row);
                    }

                    scanner.close();

                    // Proses data menjadi array 2D untuk regresi
                    m = dataList.size();
                    n = dataList.get(0).length - 1; // Jumlah peubah (n) = jumlah kolom - 1 (kolom y)

                    double[] lastRow = dataList.remove(m - 1); // Menghapus dan mengambil baris terakhir
                    m--;

                    observationfromfile = new double[1][n]; // Simpan dalam variabel luar
                    System.arraycopy(lastRow, 0, observationfromfile[0], 0, n);
                    isObservation = true;

                    data_array = new double[m][n + 1];
                    for (int i = 0; i < m; i++) {
                        data_array[i] = dataList.get(i);
                    }

                } catch (FileNotFoundException e) {
                    System.out.println("File tidak ditemukan: " + e.getMessage());
                    return;
                } catch (Exception e) {
                    System.out.println("Terjadi kesalahan saat membaca file.");
                    e.printStackTrace();
                    return;
                }
            }

            try {
                // Jika data sudah siap, jalankan regresi
                System.out.println("\nPersamaan regresi kuadratik: ");
                Matrix coefficients = MultipleQuadraticRegression.trainQuadraticModel(data_array, n);
                String equation = MultipleQuadraticRegression.getQuadraticEquation(coefficients, n);

                double prediction = 0;
                double[][] observation = new double[1][n];

                if (isObservation) {
                    prediction = MultipleQuadraticRegression.predictQuadratic(coefficients, observationfromfile);

                    // Opsi untuk menyimpan hasil ke file
                    OutputUtils.saveRegresiKuadratikBerganda(equation, observationfromfile, prediction);
                } else {
                    // Prediksi dengan observasi baru
                    observation = MultipleQuadraticRegression.inputObservation(n);
    
                    // Lakukan prediksi
                    prediction = MultipleQuadraticRegression.predictQuadratic(coefficients, observation);
                    
                    // Opsi untuk menyimpan hasil ke file
                    OutputUtils.saveRegresiKuadratikBerganda(equation, observation, prediction);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            break;
        }
    }


    public static void ImageResizing() {
        System.out.println("\n== IMAGE RESIZING ==");
        try {
            // Minta input nama file tanpa path atau .jpg
            String filename = InputUtils.getString("Masukkan nama file: ");
            int[][][] image = loadImage("../test/image_resizing/input/" + filename + ".jpg");

            // Meminta faktor skala panjang dan tinggi
            double lengthScale = InputUtils.getDouble("Masukkan faktor skala panjang (contoh: 1.5 untuk 150%): ");
            double heightScale = InputUtils.getDouble("Masukkan faktor skala tinggi (contoh: 1.5 untuk 150%): ");

            int newLength = (int) (image.length * lengthScale);
            int newHeight = (int) (image[0].length * heightScale);
            int[][][] scaledImage = scaleImage(image, newLength, newHeight);

            // Menyimpan hasil gambar setelah proses skala
            String outputFileName = "../test/image_resizing/output/" + filename + "_resized.jpg";
            saveImage(scaledImage, outputFileName);

            System.out.println("Gambar berhasil diproses dan disimpan sebagai: " + outputFileName);
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat memproses gambar: " + e.getMessage());
        }
    }

}

