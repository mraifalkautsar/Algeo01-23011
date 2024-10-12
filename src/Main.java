import matrix.MatrixSolver;
import regression.RegressionCalculator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // pilihan ditampilkan kepada pengguna
            System.out.println("Hak cipta sepenuhnya dimiliki oleh SadangSerang Co.");
            System.out.println("Selamat datang di program pemecah persamaan dan penghitung regresi!");
            System.out.println("1. Sistem Persamaan Linier"); //
            System.out.println("2. Determinan");
            System.out.println("3. Matriks Balikan");
            System.out.println("4. Interpolasi Polinom");
            System.out.println("5. Interpolasi Bicubic Spline");
            System.out.println("6. Regresi Linier dan Kuadratik Berganda");
            System.out.println("7. Interpolasi Gambar");
            System.out.println("8. Ngemaling helm-nya reza");

            // meminta pilihan dari pengguna
            System.out.print("Masukkan pilihanmu: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    SistemPersamaanLinier(scanner);
                    break;
                case 2:
//                    Determinan(scanner);
                    break;
                case 3:
//                    MatriksBalikan(scanner);
                    break;
                case 4:
//                    InterpolasiPolinom(scanner);
                    break;
                case 5:
//                    InterpolasiBicubicSpline(scanner);
                    break;
                case 6:
//                    RegresiLinierdanKuadratikBerganda(scanner);
                case 7:
//                    InterpolasiGambar(scanner);
                case 8:
                    System.out.println("Keluar dari program.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Yang bener lah.");
            }
        }
    }

    private static void SistemPersamaanLinier(Scanner scanner) {
        // Get matrix dimensions and coefficients from the user
        System.out.print("Enter the number of equations: ");
        int n = scanner.nextInt();
        double[][] coefficients = new double[n][n];
        double[] constants = new double[n];

        System.out.println("Enter the coefficients of the equations:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                coefficients[i][j] = scanner.nextDouble();
            }
        }

        System.out.println("Enter the constants:");
        for (int i = 0; i < n; i++) {
            constants[i] = scanner.nextDouble();
        }

        // Call matrix solver
        double[] solution = MatrixSolver.solve(coefficients, constants);

        // Display the solution
        if (solution != null) {
            System.out.println("Solution:");
            for (int i = 0; i < n; i++) {
                System.out.println("x" + (i + 1) + " = " + solution[i]);
            }
        } else {
            System.out.println("No solution found.");
        }
    }

    private static void performRegression(Scanner scanner) {
        // Get data points for regression
        System.out.print("Enter the number of data points: ");
        int n = scanner.nextInt();
        double[] x = new double[n];
        double[] y = new double[n];

        System.out.println("Enter the x values:");
        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextDouble();
        }

        System.out.println("Enter the y values:");
        for (int i = 0; i < n; i++) {
            y[i] = scanner.nextDouble();
        }

        // Call regression calculator
        double[] regression = RegressionCalculator.calculateLinearRegression(x, y);

        // Display the regression equation
        System.out.println("Linear Regression Equation: y = " + regression[0] + " * x + " + regression[1]);
    }
}

