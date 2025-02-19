package maman13.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SudokuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SudokuApplication.class.getResource("sudoku-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 530);
        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);


    }



    public static void main(String[] args) {
        launch();
    }
}