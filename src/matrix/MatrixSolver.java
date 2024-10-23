package matrix;

public class MatrixSolver {
    private static final double epsilon = 1e-9; // Toleransi untuk angka yang sangat kecil

    // Determinan matriks dengan metode eksansi kofaktor
    public static double determinant(Matrix matrix) {
        if (matrix == null || matrix.data == null || matrix.data.length == 0) {
            throw new IllegalArgumentException("Matriks tidak boleh kosong");
        }
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException("Matriks harus berbentuk persegi");
        }
        double det = determinantByCofactorExpansion(matrix.data);
        
        // Mengatur toleransi numerik untuk mendeteksi nilai mendekati nol
        if (Math.abs(det) < epsilon) {
            return 0;
        }
        return det;
    }
    
    private static double determinantByCofactorExpansion(double[][] matrix) {
        int n = matrix.length;
        if (n == 1) return matrix[0][0];
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
    
        double det = 0;
        for (int i = 0; i < n; i++) {
            if (matrix[0][i] == 0) continue;
            det += Math.pow(-1, i) * matrix[0][i] * determinantByCofactorExpansion(minor(matrix, 0, i));
        }
        return det;
    }

    private static double[][] minor(double[][] matrix, int excludingRow, int excludingCol) {
        int size = matrix.length;
        double[][] result = new double[size - 1][size - 1];
        for (int i = 0, r = 0; i < size; i++) {
            if (i == excludingRow) continue;
            for (int j = 0, c = 0; j < size; j++) {
                if (j == excludingCol) continue;
                result[r][c] = matrix[i][j];
                c++;
            }
            r++;
        }
        return result;
    }

    // Determinan matriks dengan metode reduksi baris
    public static double determinantByRowReduction(Matrix matrix) {
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException("Matriks harus berbentuk persegi");
        }
        if (matrix == null || matrix.data == null || matrix.data.length == 0) {
            throw new IllegalArgumentException("Matriks tidak boleh kosong");
        }
        
        Matrix matrixCopy = new Matrix(matrix.rowEff, matrix.colEff);
        matrixCopy.copyMatrix(matrix); 

        int n = matrixCopy.rowEff; 
        double determinant = 1;
    
        for (int i = 0; i < n; i++) {
            // Mencari baris dengan pivot terbesar
            int pivotRow = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(matrixCopy.data[j][i]) > Math.abs(matrixCopy.data[pivotRow][i])) {
                    pivotRow = j; 
                }
            }
    
            // Jika elemen pivot adalah nol, determinan adalah nol
            if (matrixCopy.data[pivotRow][i] == 0) {
                return 0; 
            }
    
            // Menukar baris pivot dengan baris saat ini jika perlu
            if (pivotRow != i) {
                matrixCopy.swapRows(i, pivotRow); 
                determinant *= -1;
            }
    
            // Mengalikan determinan dengan elemen pivot
            determinant *= matrixCopy.data[i][i];
    
            // Menormalkan baris pivot dan mengeliminasi elemen di bawahnya
            for (int j = i + 1; j < n; j++) {
                double factor = -matrixCopy.data[j][i] / matrixCopy.data[i][i];
                matrixCopy.addMultipleOfRow(j, i, factor); 
            }
        }

        if (Math.abs(determinant) < epsilon) {
            return 0;
        }
    
        return Math.round(determinant * 10000.0) / 10000.0;
    }

    // Fungsi untuk menghitung adjoin matriks
    public static Matrix adjoin(Matrix matrix) {
        int n = matrix.rowEff;
        Matrix adjugateMatrix = new Matrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double[][] minor = minor(matrix.data, i, j);
                double det = determinantByCofactorExpansion(minor);
                adjugateMatrix.data[j][i] = Math.pow(-1, i + j) * det;
            }
        }

        return adjugateMatrix;
    }
    
    // Fungsi untuk menghitung balikan matriks
    public static Matrix inverseAdjoin(Matrix matrix) {
        double det = determinantByCofactorExpansion(matrix.data);
        if (det == 0) throw new IllegalArgumentException("Matrix tidak dapat dibalik, determinan = 0.");
        Matrix adjoinMatrix = adjoin(matrix);
        int n = matrix.data.length;
        Matrix inverseMatrix = new Matrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatrix.data[i][j] = adjoinMatrix.data[i][j] / det;
            }
        }
        return inverseMatrix;
    }

    // Fungsi untuk menghitung invers matriks menggunakan metode Gauss-Jordan
    public static Matrix inverseGaussJordan(Matrix matrix) {
        int n = matrix.rowEff;  

        // cek apakah matriks memiliki determinan
        double determinant = determinant(matrix);  // Tambahkan metode ini untuk cek determinan
        if (determinant == 0) {
            throw new ArithmeticException("Matrix tidak memiliki invers (singular).");
        }

        // Mmenambah matriks identitas di sebelah kanan
        Matrix augmentedMatrix = new Matrix(n, 2*n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmentedMatrix.data[i][j] = matrix.data[i][j];
            }
            for (int j = n; j < 2 * n; j++) {
                augmentedMatrix.data[i][j] = (i == j - n) ? 1.0 : 0.0 ;
            }
        }

        // Melakukan eliminasi Gauss-Jordan
        for (int i = 0; i < n; i++) {
            if (augmentedMatrix.data[i][i] == 0) {
                boolean swapped = false;
                for (int k = i + 1; k < n; k++) {
                    if (augmentedMatrix.data[k][i] != 0) {
                        augmentedMatrix.swapRows(i, k);
                        swapped = true;
                        break;
                    }
                }
                if (!swapped) {
                    throw new ArithmeticException("Matrix tidak memiliki invers.");
                }
            }

            // Normalisasi baris pivot (buat elemen diagonal menjadi 1)
            double pivot = augmentedMatrix.data[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmentedMatrix.data[i][j] /= pivot;
            }

            // Eliminasi elemen di atas dan di bawah pivot
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmentedMatrix.data[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmentedMatrix.data[k][j] -= factor * augmentedMatrix.data[i][j];
                        if (Math.abs(augmentedMatrix.data[k][j]) < epsilon) {
                            augmentedMatrix.data[k][j] = 0;
                        }
                    }
                }
            }
        }

        // Ekstrak matriks invers dari matriks augmented
        Matrix inverseMatrix = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatrix.data[i][j] = augmentedMatrix.data[i][j + n];
            }
        }
        return inverseMatrix;
    }

    // Fungsi untuk menyelesaikan SPL dengan matriks balikan menggunakan matriks augmented
    public static double[] solveUsingInverse(Matrix matrix) {
        int rows = matrix.rowEff;
        int cols = matrix.colEff;

        // Pastikan matriks augmented memiliki kolom lebih dari 1
        if (cols < 2) {
            throw new IllegalArgumentException("Matriks augmented tidak valid. Harus memiliki lebih dari satu kolom.");
        }

        // Pisahkan matriks augmented menjadi A dan B
        Matrix A = new Matrix(rows, cols - 1);
        double[] B = new double[rows];

        for (int i = 0; i < rows; i++) {
            B[i] = matrix.data[i][cols - 1]; // Kolom terakhir adalah B
            for (int j = 0; j < cols - 1; j++) {
                A.data[i][j] = matrix.data[i][j]; // Kolom-kolom sebelumnya adalah A
            }
        }

        // Pastikan A adalah matriks persegi
        if (!A.isSquare()) {
            throw new IllegalArgumentException("Matriks A harus berbentuk persegi.");
        }

        // Menghitung invers matriks A
        Matrix inverseMatrix = MatrixSolver.inverseAdjoin(A);

        // Mengalikan matriks balikan dengan vektor B untuk mendapatkan solusi
        double[] solution = multiplyMatrixWithVector(inverseMatrix, B);

        return solution;
    }


    // Fungsi untuk mengisi matriks
    public static void fillMatrix(Matrix matrix, Matrix values) {
        // Mengisi matriks dengan nilai dari array 2D
        for (int i = 0; i < matrix.rowEff; i++) {
            for (int j = 0; j < matrix.colEff; j++) {
                matrix.data[i][j] = values.data[i][j];
            }
        }
    }

    // Fungsi untuk mengalikan matriks (Matrix) dengan vektor (double[])
    public static double[] multiplyMatrixWithVector(Matrix matrix, double[] vector) {
        int n = matrix.data.length;
        double[] result = new double[n];

        for (int i = 0; i < n; i++) {
            result[i] = 0;
            for (int j = 0; j < vector.length; j++) {
                result[i] += Math.round(matrix.data[i][j] * vector[j] * 10000.0) / 10000.0;
            }
        }
        return result;
    }

    // Fungsi untuk menyelesaikan SPL dengan kaidah Cremer menggunakan matriks augmented (khusus untuk SPL dengan n peubah dan n persamaan)
    public static double[] solveUsingCramer(Matrix matrix) {
        int rows = matrix.rowEff;
        int cols = matrix.colEff;

        // Pisahkan matriks augmented menjadi A dan B
        Matrix A = new Matrix(rows, cols - 1);
        double[] B = new double[rows];

        for (int i = 0; i < rows; i++) {
            B[i] = matrix.data[i][cols - 1]; 
            for (int j = 0; j < cols - 1; j++) {
                A.data[i][j] = matrix.data[i][j];
            }
        }

        // Menghitung determinan A
        double detA = MatrixSolver.determinantByRowReduction(A);

        if (detA == 0) {
            throw new IllegalArgumentException("SPL tidak memiliki solusi unik karena determinan matriks A = 0.");
        }

        double[] solutions = new double[rows];

        // Menghitung solusi menggunakan kaidah Cramer
        for (int i = 0; i < rows; i++) {
            Matrix Ai = substituteColumn(A, B, i);
            double detAi = MatrixSolver.determinantByRowReduction(Ai);
            solutions[i] = Math.round((detAi / detA) * 10000.0) / 10000.0;

            // Ubah -0 menjadi 0
            if (Math.abs(solutions[i]) < 1e-10) {
                solutions[i] = 0; 
            }
        }

        return solutions;
    }

    // Fungsi untuk mengganti kolom pada matriks A dengan vektor B
    private static Matrix substituteColumn(Matrix A, double[] B, int columnIndex) {
        int n = A.data.length;
        Matrix modifiedMatrix = new Matrix(n, A.data[0].length);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < A.data[i].length; j++) {
                if (j == columnIndex) {
                    modifiedMatrix.data[i][j] = B[i];
                } else {
                    modifiedMatrix.data[i][j] = A.data[i][j];
                }
            }
        }

        return modifiedMatrix;
    }

    // Fungsi untuk memotong elemen terakhir baris
    private static double[] truncateLastCol(double[] row) {
        double[] truncatedRow = new double[row.length - 1];
        int i;
        for (i = 0; i < truncatedRow.length; i++) {
            truncatedRow[i] = row[i];
        }
        return truncatedRow;
    }

    // Fungsi untuk mengecek apakah elemen baris adalah nol semua
    private static boolean isRowZero(double[] row) {
        boolean res = true;
        int i;
        for (i = 0; i < row.length; i++) {
            res = res && (row[i] == 0);
        }
        return res;
    }

    // Fungsi Eliminasi gauss
    public static double[] gaussElimination(Matrix matrix) {
        int n = matrix.rowEff;
        int m = matrix.colEff;

        int i, j, k;
        for (i = 0; i < n; i++) {
            // mencari pivot terbesar di kolom i
            int maxRow = i;
            for (k = i + 1; k < n; k++) {
                if (Math.abs(matrix.data[k][i]) > Math.abs(matrix.data[maxRow][i])) {
                    maxRow = k;
                }
            }

            // menukar baris maksimum dengan baris saat ini
            double[] temp = matrix.data[i];
            matrix.data[i] = matrix.data[maxRow];
            matrix.data[maxRow] = temp;

            // membuat menjadi nol semua elemen dibawah pivot
            for (k = i + 1; k < n; k++) {
                if (matrix.data[i][i] == 0) continue; // skip if pivot is zero
                double factor = matrix.data[k][i] / matrix.data[i][i];
                for (j = i; j <= n; j++) {
                    matrix.data[k][j] -= factor * matrix.data[i][j];
                }
            }
        }

        // cek inkonsistensi
        boolean consistent = true;
        for (i = 0; i < n; i++) {
            double[] truncatedRow = truncateLastCol(matrix.data[i]);
            if (isRowZero(truncatedRow) && matrix.data[i][m - 1] != 0) {
                consistent = false;
                System.out.println("No Solution");
                return null; // mengembalikan null jika tidak ada solusi
            }
        }

        // penyulihan balik
        double[] solution = new double[n];
        boolean[] isFreeVariable = new boolean[n];  // melacak variabel bebas

        for (i = n - 1; i >= 0; i--) {
            if (matrix.data[i][i] == 0) {
                solution[i] = Double.NaN; // menandai sebagai variabel bebas.
                isFreeVariable[i] = true;
                continue;
            }

            solution[i] = matrix.data[i][n] / matrix.data[i][i];
            for (k = i - 1; k >= 0; k--) {
                matrix.data[k][n] -= matrix.data[k][i] * solution[i];
            }
        }

        // Menangani variabel bebas
        for (i = 0; i < n; i++) {
            if (isFreeVariable[i]) {
                System.out.println("Variabel x" + i + " adalah variabel bebas atau solusi parametrik .");
            }
        }

        return solution; // mengembalikan solusi
    }


    // Fungsi Eliminasi gauss jordan
    public static double[] gaussJordanElimination(Matrix matrix) {
        int row = matrix.data.length;
        int col = matrix.data[0].length;
        int i, j, k, colEff;
        double koefisien, divider;

        // array buat nyimpen solusi
        double[] solution = new double[col - 1]; // kolom terakhir adalah konstanta
        boolean[] foundSol = new boolean[col - 1]; // ngecek variabel yang udah ketemu solusinya

        // inisialisasi array solusi dengan nan buat variabel bebas
        for (i = 0; i < col - 1; i++) {
            solution[i] = Double.NaN; // anggap awalnya semua variabel bebas
        }

        // eliminasi maju buat dapetin bentuk rref
        for (k = 0; k < row; k++) { // k adalah baris pivot
            if (!isRowZero(matrix.data[k])) {
                // cari kolom pivot
                colEff = k;
                divider = matrix.data[k][k];
                for (j = 0; j < col; j++) {
                    if (matrix.data[k][j] != 0) {
                        divider = matrix.data[k][j];
                        colEff = j;
                        break;
                    }
                }

                // bagi baris pivot sama elemen terdepannya biar jadi 1
                for (i = 0; i < col; i++) {
                    matrix.data[k][i] /= divider;
                }

                // bikin semua elemen di kolom pivot jadi 0 kecuali yang di pivot
                for (i = 0; i < row; i++) {
                    if (i == k) continue; // lewatin baris pivot
                    koefisien = matrix.data[i][colEff];
                    for (j = 0; j < col; j++) {
                        matrix.data[i][j] -= koefisien * matrix.data[k][j];
                    }
                }
            }
        }

        // cek apakah ada yang inkonsisten
        boolean consistent = true;
        for (i = 0; i < row; i++) {
            double[] truncatedRow = truncateLastCol(matrix.data[i]);
            if (isRowZero(truncatedRow) && matrix.data[i][col - 1] != 0) {
                consistent = false;
                System.out.println("nggak ada solusi (sistem inkonsisten).");
                return null; // balik null kalau sistem inkonsisten
            }
        }

        // kalau konsisten, cari solusinya (unik atau parametrik)
        if (consistent) {
            boolean found;
            for (i = 0; i < row; i++) {
                found = false;
                for (j = 0; j < col - 1; j++) {
                    if (matrix.data[i][j] == 1 && !found) { // ketemu leading 1 (solusi unik)
                        solution[j] = matrix.data[i][col - 1];
                        foundSol[j] = true;
                        found = true;
                    } else if (found && matrix.data[i][j] != 0) { // solusi parametrik
                        // tandai variabel sebagai variabel bebas
                        solution[j] = Double.NaN; // variabel bebas (bisa ganti placeholder)
                    }
                }
            }

            // tandai variabel bebas (kalau ada)
            for (i = 0; i < foundSol.length; i++) {
                if (!foundSol[i]) {
                    solution[i] = Double.NaN; // variabel bebas (solusi parametrik)
                }
            }
        }

        return solution; // balikin array solusi
    }

// Fungsi Eliminasi gauss jordan
    public static void gaussJordanEliminationForMain(Matrix matrix) { 
        int row = matrix.rowEff;
        int col = matrix.colEff;
        int i, j, k, colEff;
        double koefisien, divider;
        for (k = 0; k < row; k++) { // k adalah basis row 
            if (!isRowZero(matrix.data[k])) {
                colEff = k;
                divider = matrix.data[k][k];
                for (j = 0; j < col; j++) { // mencari divider
                    if (matrix.data[k][j] != 0) {
                        divider = matrix.data[k][j];
                        colEff = j;
                        break;
                    }
                }
                for (i = 0; i < col; i++) {
                    matrix.data[k][i] /= divider ; // generate angka 1 
                }
                for (i = 0; i < row; i++) { // generate angka 0
                    if (i == k) {continue;}
                    koefisien = matrix.data[i][colEff];
                    for (j = 0; j < col; j++) {
                        matrix.data[i][j] -= koefisien*matrix.data[k][j];
                    }
                }
            }
        }
        // cek inkonsistensi
        boolean konsisten = true;
        for (i = 0; i < row; i++) {
            double[] truncatedRow = truncateLastCol(matrix.data[i]);
            if (isRowZero(truncatedRow) && matrix.data[i][col - 1] != 0) {
                konsisten = false;
                System.out.println("Tidak ada Solusi");
            }
        }
        // hitung SPL
        boolean[] foundedSol = new boolean[col - 1];  // array yang menyimpan index solusi2 yg sudah ketemu
        if (konsisten) {
            boolean found = false;
            for (i = 0; i < row; i++) {
                for (j = 0; j < col -1; j++) {
                    if (matrix.data[i][j] == 1  && !found) {
                        System.out.print("X" + (i+1) + " = " + matrix.data[i][col - 1]);
                        foundedSol[j] = true;
                        found = true;
                    } else if (found && matrix.data[i][j] != 0) {
                        if (matrix.data[i][j] < 0) {
                            System.out.print(" + ");
                        } else {System.out.print(" - ");}
                        if (Math.abs(matrix.data[i][j]) == 1) {  // handling output solusi parameter jika koefisiennya 1
                            System.out.print("X"+(j+1));
                        } else {System.out.print(Math.abs(matrix.data[i][j])+"X"+(j+1));}
                    }
                }
                found = false;
                System.out.println();
            }
            for (i = 0; i < foundedSol.length; i++) {
                if (!foundedSol[i]) {
                    System.out.println("X"+(i+1)+" = free");
                }
            }
        }
    }

}

