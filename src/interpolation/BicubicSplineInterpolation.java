package interpolation;
import matrix.Matrix;
import matrix.MatrixSolver;
import java.io.IOException;

public class BicubicSplineInterpolation {

    public static double calculate(Matrix vectorY, double a, double b) {
        // membuat matrix X
        Matrix matrixX = new Matrix(16, 16);

        // membaca file matrix X
        try {
            matrixX = matrixX.readMatrixFromFile("interpolation/MatrixX.txt"); 
            // btw matrix X adalah matrix konstan
        } catch (IOException err){
            System.out.println(err);
        }

        // membuat object solver
        MatrixSolver solver = new MatrixSolver(matrixX);
        Matrix inversedMatrixX = solver.inverseAdjoin();

        // mengalikan inverse X dengan vector Y
        Matrix vectorA = inversedMatrixX.multiplyMatrix(vectorY);

        // melakukan operasi sigma dari f(a,b)
        double solution = 0;
        int rowIdx = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                solution += vectorA.data[rowIdx][0] * Math.pow(a, i) * Math.pow(b, j);
                rowIdx ++;
            }
        }
        return solution;
    }
}

