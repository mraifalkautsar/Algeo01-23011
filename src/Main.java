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
//                    SistemPersamaanLinier(scanner);
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
}

