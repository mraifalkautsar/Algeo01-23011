package interpolation;

import matrix.Matrix;
import matrix.MatrixSolver;
import utils.InputUtils;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BicubicSplineInterpolation {

    // METODE BICUBIC SPLINE INTERPOLATION DASAR
    public static double calculate(Matrix vectorY, double a, double b, boolean imageResizing) {
        Matrix matrixX = new Matrix(16, 16);
        Matrix vectorA = null;

        // membaca matriks dari file
        try {
            if (imageResizing) {
                Matrix inversedMatrixX = InputUtils.readMatrixFromFileForBicubic("../src/interpolation/ImageResizingMatrix.txt");
                vectorA = inversedMatrixX.multiplyMatrix(vectorY);
            }
            else {
                matrixX = InputUtils.readMatrixFromFileForBicubic("../src/interpolation/MatrixX.txt");
                // inversi matriks dan pemecahan
                Matrix inversedMatrixX = MatrixSolver.inverseAdjoin(matrixX);
                vectorA = inversedMatrixX.multiplyMatrix(vectorY);
            }
        } catch (IOException err) {
            System.out.println(err);
        }

        double solution = 0;
        int rowIdx = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                solution += vectorA.data[rowIdx][0] * Math.pow(a, i) * Math.pow(b, j);
                rowIdx++;
            }
        }
        return solution;
    }


    // METODE-METODE UNTUK IMAGE RESIZING
    // membuka gambar RGB ke sebuah array 3D
    public static int[][][] loadImage(String filePath) throws IOException {
        File imageFile = new File(filePath);
        BufferedImage image = ImageIO.read(imageFile);

        int width = image.getWidth();
        int height = image.getHeight();
        int[][][] rgbArray = new int[width][height][3]; // untuk menyetor nilai RGB.

        // mengekstrak nilai RGB dari gambar
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;

                rgbArray[x][y][0] = r;
                rgbArray[x][y][1] = g;
                rgbArray[x][y][2] = b;
            }
        }
        return rgbArray;
    }

    // menyimpan gambar RGB
    public static void saveImage(int[][][] rgbArray, String outputPath) throws IOException {
        int width = rgbArray.length;
        int height = rgbArray[0].length;
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int r = rgbArray[x][y][0];
                int g = rgbArray[x][y][1];
                int b = rgbArray[x][y][2];
                int rgb = (r << 16) | (g << 8) | b;
                outputImage.setRGB(x, y, rgb);
            }
        }
        File outputFile = new File(outputPath);
        ImageIO.write(outputImage, "jpg", outputFile);
    }

    // fungsi untuk mempersiapkan dan melakukan kalkulasi bicubic
    private static double calculateBicubic(double[][] block, double x, double y) {

        Matrix vectorY = new Matrix(16, 1);
        int idx = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                vectorY.data[idx][0] = block[i][j];
                idx++;
            }
        }

        return calculate(vectorY, x, y, true);
    }


    // melakukan image resizing
    public static int[][][] scaleImage(int[][][] image, int newWidth, int newHeight) {
        int oldWidth = image.length;
        int oldHeight = image[0].length;
        double scaleX = (double) oldWidth / newWidth;
        double scaleY = (double) oldHeight / newHeight;

        int[][][] newImage = new int[newWidth][newHeight][3];

        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                double origX = i * scaleX;
                double origY = j * scaleY;

                // melakukan interpolasi untuk tiap-tiap channel (R, G, B)
                for (int channel = 0; channel < 3; channel++) {
                    int x0 = (int) Math.floor(origX) - 1;
                    int y0 = (int) Math.floor(origY) - 1;
                    double[][] block = new double[4][4];

                    // mengekstrak grid berbentuk 4x4.
                    for (int bx = 0; bx < 4; bx++) {
                        for (int by = 0; by < 4; by++) {
                            int imgX = Math.min(Math.max(x0 + bx, 0), oldWidth - 1);
                            int imgY = Math.min(Math.max(y0 + by, 0), oldHeight - 1);
                            block[bx][by] = image[imgX][imgY][channel];
                        }
                    }

                    // melakukan interpolasi bicubic
                    newImage[i][j][channel] = (int) Math.max(0, Math.min(255, calculateBicubic(block, origX - x0 - 1, origY - y0 - 1)));
                }
            }
        }
        return newImage;
    }
}
