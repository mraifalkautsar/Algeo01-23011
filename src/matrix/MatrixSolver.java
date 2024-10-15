package matrix;

public class MatrixSolver {
    private Matrix matrix; 

    // Konstruktor objek MatrixSolver
    public MatrixSolver(Matrix matrix) {
        this.matrix = matrix;
    }

    // Determinan matriks dengan metode eksansi kofaktor
    public double determinant() {
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException("Matriks harus berbentuk persegi");
        }
        return determinantByCofactorExpansion(matrix.data);
    }
    
    private double determinantByCofactorExpansion(double[][] matrix) {
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

    private double[][] minor(double[][] matrix, int excludingRow, int excludingCol) {
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
    public double determinantByRowReduction() {
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException("Matriks harus berbentuk persegi");
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
    
        return determinant;
    }

    // Fungsi untuk menghitung adjoin matriks
    public Matrix adjoin() {
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
    public Matrix inverseAdjoin() {
        double det = determinantByCofactorExpansion(matrix.data);
        if (det == 0) throw new IllegalArgumentException("Matrix tidak dapat dibalik, determinan = 0.");
        Matrix adjoinMatrix = adjoin();
        int n = matrix.data.length;
        Matrix inverseMatrix = new Matrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatrix.data[i][j] = Math.round(adjoinMatrix.data[i][j] / det * 10000.0) / 10000.0;
            }
        }
        return inverseMatrix;
    }

    // Fungsi untuk menghitung invers matriks menggunakan metode Gauss-Jordan
    public Matrix inverseGaussJordan() {
        int n = matrix.rowEff;

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
                    }
                }
            }
        }

        // Ekstrak matriks invers dari matriks augmented
        Matrix inverseMatrix = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatrix.data[i][j] = Math.round(augmentedMatrix.data[i][j + n] * 10000.0) / 10000.0;
            }
        }
        return inverseMatrix;
    }

    // Fungsi untuk menyelesaikan SPL dengan matriks balikan menggunakan matriks augmented
    public double[] solveUsingInverse() {
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
        MatrixSolver solverA = new MatrixSolver(A);
        Matrix inverseMatrix = solverA.inverseAdjoin();

        // Mengalikan matriks balikan dengan vektor B untuk mendapatkan solusi
        double[] solution = multiplyMatrixWithVector(inverseMatrix, B);

        return solution;
    }


    // Fungsi untuk mengisi matriks
    public void fillMatrix(Matrix values) {
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
    public double[] solveUsingCramer() {
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
        MatrixSolver solverA = new MatrixSolver(A);
        double detA = solverA.determinantByRowReduction();

        if (detA == 0) {
            throw new IllegalArgumentException("SPL tidak memiliki solusi unik karena determinan matriks A = 0.");
        }

        double[] solutions = new double[rows];

        // Menghitung solusi menggunakan kaidah Cramer
        for (int i = 0; i < rows; i++) {
            Matrix Ai = substituteColumn(A, B, i);
            MatrixSolver solverAi = new MatrixSolver(Ai);
            double detAi = solverAi.determinantByRowReduction();
            solutions[i] = Math.round((detAi / detA) * 10000.0) / 10000.0;

            // Ubah -0 menjadi 0
            if (Math.abs(solutions[i]) < 1e-10) {
                solutions[i] = 0; 
            }
        }

        return solutions;
    }

    // Fungsi untuk mengganti kolom pada matriks A dengan vektor B
    private Matrix substituteColumn(Matrix A, double[] B, int columnIndex) {
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
}
