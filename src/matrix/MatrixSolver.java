package matrix;

import matrix.Matrix.Fraction;


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
    public Fraction[][] adjoin() {
        int n = matrix.rowEff;
        Fraction[][] adjugateMatrix = new Fraction[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double[][] minor = minor(matrix.data, i, j);
                double det = determinantByCofactorExpansion(minor);
                adjugateMatrix[j][i] = matrix.new Fraction((int) Math.pow(-1, i + j) * (int) det, 1);
            }
        }

        return adjugateMatrix;
    }
    
    // Fungsi untuk menghitung balikan matriks
    public Fraction[][] inverseAdjoin() {
        double det = determinantByCofactorExpansion(matrix.data);
        if (det == 0) throw new IllegalArgumentException("Matrix tidak dapat dibalik, determinan = 0.");
        Fraction[][] adjoinMatrix = adjoin();
        Fraction determinantFraction = matrix.new Fraction((int) det, 1); 
        int n = matrix.data.length;
        Fraction[][] inverseMatrix = new Fraction[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatrix[i][j] = adjoinMatrix[i][j].divide(determinantFraction);
            }
        }
        return inverseMatrix;
    }

    // Fungsi untuk menghitung invers matriks menggunakan metode Gauss-Jordan
    public Fraction[][] inverseGaussJordan() {
        int n = matrix.rowEff;

        // Mmenambah matriks identitas di sebelah kanan
        Fraction[][] augmentedMatrix = new Fraction[n][2 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmentedMatrix[i][j] = matrix.new Fraction((int) matrix.data[i][j], 1); // Salin matriks asli
            }
            for (int j = n; j < 2 * n; j++) {
                augmentedMatrix[i][j] = (i == j - n) ? matrix.new Fraction(1, 1) : matrix.new Fraction(0, 1); // Matriks identitas
            }
        }

        // Melakukan eliminasi Gauss-Jordan
        for (int i = 0; i < n; i++) {
            if (augmentedMatrix[i][i].toDouble() == 0) {
                boolean swapped = false;
                for (int k = i + 1; k < n; k++) {
                    if (augmentedMatrix[k][i].toDouble() != 0) {
                        swapRows(augmentedMatrix, i, k);
                        swapped = true;
                        break;
                    }
                }
                if (!swapped) {
                    throw new ArithmeticException("Matrix tidak memiliki invers.");
                }
            }

            // Normalisasi baris pivot (buat elemen diagonal menjadi 1)
            Fraction pivot = augmentedMatrix[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmentedMatrix[i][j] = augmentedMatrix[i][j].divide(pivot);
            }

            // Eliminasi elemen di atas dan di bawah pivot
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    Fraction factor = augmentedMatrix[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmentedMatrix[k][j] = augmentedMatrix[k][j].subtract(factor.multiply(augmentedMatrix[i][j]));
                    }
                }
            }
        }

        // Ekstrak matriks invers dari matriks augmented
        Fraction[][] inverseMatrix = new Fraction[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatrix[i][j] = augmentedMatrix[i][j + n];
            }
        }
        return inverseMatrix;
    }

    // Fungsi untuk menukar dua baris dalam matriks bentuk Fraction
    private void swapRows(Fraction[][] matrix, int row1, int row2) {
        for (int j = 0; j < matrix[row1].length; j++) {
            Fraction temp = matrix[row1][j];
            matrix[row1][j] = matrix[row2][j];
            matrix[row2][j] = temp;
        }
    }
}
