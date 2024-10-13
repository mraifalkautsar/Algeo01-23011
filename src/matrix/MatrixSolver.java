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
}
