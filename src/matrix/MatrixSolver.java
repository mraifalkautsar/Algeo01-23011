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
        Matrix augmentedMatrix = new Matrix(n, n);
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
                inverseMatrix.data[i][j] = augmentedMatrix.data[i][j + n];
            }
        }
        return inverseMatrix;
    }
}
