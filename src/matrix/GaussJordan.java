package matrix;

public class GaussJordan {

    public static boolean isRowZero(double[] row) {  
        boolean res = true;
        int i;
        for (i = 0; i < row.length; i++) {
            res = res && (row[i] == 0);
        }
        return res;
    }

    public static double[] truncateLastRow(double[] row) {
        double[] truncatedRow = new double[row.length - 1];
        int i;
        for (i = 0; i < truncatedRow.length; i++) {
            truncatedRow[i] = row[i];
        }
        return truncatedRow;
    }

    public static void gaussJordan(double[][] matrix) { 

        int row = matrix.length;
        int col = matrix[0].length;
        int i, j, k, colEff;
        double koefisien, divider;

        for (k = 0; k < row; k++) { // k adalah basis row 
            if (!isRowZero(matrix[k])) {

                colEff = k;
                divider = matrix[k][k];
                for (j = 0; j < col; j++) { // mencari divider
                    if (matrix[k][j] != 0) {
                        divider = matrix[k][j];
                        colEff = j;
                        break;
                    }
                }
                for (i = 0; i < col; i++) {
                    matrix[k][i] /= divider ; // generate angka 1 
                }

                for (i = 0; i < row; i++) { // generate angka 0
                    if (i == k) {continue;}
                    koefisien = matrix[i][colEff];
                    for (j = 0; j < col; j++) {
                        matrix[i][j] -= koefisien*matrix[k][j];
                    }
                }
            }
        }

        // cek inkonsistensi
        boolean konsisten = true;
        double[] truncatedRow;
        for (i = 0; i < row; i++) {
            truncatedRow= truncateLastRow(matrix[i]);
            if (isRowZero(truncatedRow) && matrix[i][col - 1] != 0) {
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
                    if (matrix[i][j] == 1  && !found) {
                        System.out.print("X" + (i+1) + " = " + matrix[i][col - 1]);
                        foundedSol[j] = true;
                        found = true;
                    } else if (found && matrix[i][j] != 0) {
                        if (matrix[i][j] < 0) {
                            System.out.print(" + ");
                        } else {System.out.print(" - ");}
                        System.out.print(Math.abs(matrix[i][j])+"X"+(j+1));
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
