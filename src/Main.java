import interpolation.PolynomialInterpolation;
import matrix.Matrix;
import matrix.MatrixSolver;
import regression.MultipleLinearRegression;
import utils.InputFunctions;
import utils.OutputFunctions;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // membuat sebuah input terminal di sisi kiri layar
        Label inputLabel = new Label("Input");
        TextArea inputTerminal = new TextArea();
        inputTerminal.setPromptText("Masukkan input di sini!");

        // membuat sebuah vbox untuk label dan juga text area
        VBox inputSection = new VBox();
        inputSection.getChildren().addAll(inputLabel, inputTerminal);
        VBox.setVgrow(inputTerminal, Priority.ALWAYS); // Ensure TextArea takes all available space

        // membuat sebuah output terminal di sisi kanan
        Label outputLabel = new Label("Output");
        TextArea outputTerminal = new TextArea();
        outputTerminal.setPromptText("Output akan ditampilkan di sini");
        outputTerminal.setEditable(false); // supaya read-only

        // membuat sebuah vbox untuk bagian output
        VBox outputSection = new VBox();
        outputSection.getChildren().addAll(outputLabel, outputTerminal);
        VBox.setVgrow(outputTerminal, Priority.ALWAYS); // memastikan text area berada di prioritas tertinggi

        // Add the input and output sections to a SplitPane
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(inputSection, outputSection);
        splitPane.setDividerPositions(0.5); // Set the divider in the middle

        // Create the scene and display the stage
        Scene scene = new Scene(splitPane, 600, 400);
        primaryStage.setTitle("Sistem Analisis Persamaan");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add a listener for Enter key to process input
        inputTerminal.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    String userInput = inputTerminal.getText();
                    processUserInput(userInput, outputTerminal);
                    inputTerminal.clear(); // Clear input terminal after processing
                    break;
                default:
                    break;
            }
        });
    }

    // Process user input and handle different menu options
    private void processUserInput(String input, TextArea outputTerminal) {
        String[] lines = input.split("\\n");
        int choice;
        try {
            choice = Integer.parseInt(lines[0].trim());
        } catch (NumberFormatException e) {
            outputTerminal.appendText("Invalid input. Please enter a number for choice.\n");
            return;
        }

        switch (choice) {
            case 1:
                SistemPersamaanLinier(outputTerminal);
                break;
            case 2:
                Determinan(outputTerminal);
                break;
            case 4:
                InterpolasiPolinom(outputTerminal);
                break;
            case 6:
                RegresiLinier(outputTerminal);
                break;
            default:
                outputTerminal.appendText("Invalid choice. Please choose a valid option.\n");
        }
    }

    // Method for solving a system of linear equations
    private void SistemPersamaanLinier(TextArea outputTerminal) {
        Matrix matrixAugmented = InputFunctions.readMatrixFromText(outputTerminal);  // Changed to read from TextArea

        double[] solution = MatrixSolver.gaussElimination(matrixAugmented);
        OutputFunctions.printCoefficients(solution, true, outputTerminal); // Output to TextArea
    }

    // Method for calculating the determinant of a matrix
    private void Determinan(TextArea outputTerminal) {
        Matrix matrix = InputFunctions.readMatrixFromText(outputTerminal);  // Read matrix from TextArea
        double determinant = MatrixSolver.determinant(matrix);
        outputTerminal.appendText("Determinan: " + determinant + "\n");
    }

    // Method for polynomial interpolation
    private void InterpolasiPolinom(TextArea outputTerminal) {
        int n = InputFunctions.getIntFromText(outputTerminal, "Masukkan jumlah data: ");
        double[][] data_array = InputFunctions.getXYDataFromText(outputTerminal, n);
        double[] solution = PolynomialInterpolation.calculatePolynomialEquation(n, data_array);
        OutputFunctions.printCoefficients(solution, true, outputTerminal);  // Output to TextArea

        double x = InputFunctions.getDoubleFromText(outputTerminal, "Nilai x yang ingin ditaksir: ");
        double estimation = PolynomialInterpolation.calculateY(solution, x);
        outputTerminal.appendText("Nilai y hasil taksiran: " + estimation + "\n");
    }

    // Method for multiple linear regression
    private void RegresiLinier(TextArea outputTerminal) {
        int m = InputFunctions.getIntFromText(outputTerminal,"Masukkan jumlah data: ");
        int n = InputFunctions.getIntFromText(outputTerminal, "Masukkan banyak peubah: ");
        double[][] data_array = InputFunctions.getMatrixFromText(outputTerminal, m, n + 1);

        double[] solution = MultipleLinearRegression.calculateRegressionEquation(data_array, m, n);
        OutputFunctions.printCoefficients(solution, true, outputTerminal);  // Output to TextArea

        double[] x_array = InputFunctions.getArrayFromText(outputTerminal, m);
        double estimation = MultipleLinearRegression.calculateY(solution, x_array);
        outputTerminal.appendText("Nilai y hasil taksiran: " + estimation + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
