package maman13.sudoku;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;

import java.util.Optional;
/*
The controller class for the sudoku.
creates board and handles logic
 */
public class SudokuController {

    @FXML
    private GridPane grid;
    @FXML
    private Button setButton;
    private final int SIZE = 9;
    private final int BLOCK_SIZE = 3;
    private Button[] btns;

    public void initialize() {

        grid.setPrefSize(500, 500);
        handleButtons();

    }
    /*
    this method creates the button grid along with their on action events
     */
    private void handleButtons() {
        btns = new Button[SIZE * SIZE];

        for (int i = 0; i < SIZE * SIZE; i++) {
            btns[i] = new Button();
            btns[i].setPrefSize(grid.getPrefWidth() / SIZE, grid.getPrefHeight() / SIZE);
            btns[i].setStyle("-fx-font-size: 21px;");
            grid.add(btns[i], i % SIZE, i / SIZE);
            int finalI = i;
            btns[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    btnAction(actionEvent, finalI);
                }
            });
        }
    }

    /*
    this method is called when the user clicks clear.
    clear event will return the board to its original state
     */
    @FXML
    void clearClick(ActionEvent event) {

        for (Button button : btns) {
            button.setText("");
            button.setDisable(false);
            setButton.setDisable(false);
            button.setStyle("-fx-text-fill: black;"+"-fx-font-size: 21px;");
        }
    }

    /*
    this method called when the user clicks the "set" button
    it the button has a number it will be disabled, locking the number in place.
     */
    @FXML
    void setClick(ActionEvent event) {

        for (Button button : btns) {
            if (!button.getText().equals("")) {
                button.setDisable(true);
                button.setStyle("-fx-text-fill: red;"+"-fx-font-size: 21px;");


            }
        }
        setButton.setDisable(true);
    }

    /*
    helper method, creates a not number alert
     */
    private void notNumAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Illegal input Exception");
        alert.setHeaderText(null);
        alert.setContentText("Please enter a number between 1 and 9");
        alert.showAndWait();
    }

    /*
    helper method, creates an illegal move alert
     */
    private void notLegalAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid number");
        alert.setHeaderText(null);
        alert.setContentText("This number violates the rules of the game, try again!");
        alert.showAndWait();

    }


    /*
     * this helper method checks if a number is legal in the game by checking its row, column and block
     */
    private boolean isLegal(String string, int index) {

        int num;
        if (string.equals("")) {
            return true;
        } else {
            num = Integer.parseInt(string);

            return checkCol(num, index % SIZE) && checkRow(num, index / SIZE) && checkBlock(num, index);
        }
    }


    /*
     * helper method called by the @isLegal method to check if a number exists in a block
     */
    private boolean checkBlock(int num, int index) {
        int i = index;
        i -= i % BLOCK_SIZE;
        i -= ((i / SIZE) % BLOCK_SIZE) * SIZE;

        int origI = i;
        for (int k = 0; k < 3; k++) {
            i = origI + k * SIZE;
            for (int j = 0; j < 3; j++) {
                if (!btns[i].getText().equals(""))
                    if (num == Integer.parseInt(btns[i].getText())) {
                        return false;
                    }
                i++;
            }
        }


        return true;


    }
    /*
     * helper method called by the @isLegal method to check if a number exists in a column
     */
    private boolean checkCol(int num, int col) {

        for (int i = col; i < SIZE * SIZE; i += SIZE) {
            if (!btns[i].getText().equals(""))
                if (num == Integer.parseInt(btns[i].getText())) {
                    return false;
                }
        }
        return true;
    }
    /*
     * helper method called by the @isLegal method to check if a number exists in a row
     */

    private boolean checkRow(int num, int rowNum) {

        int rowStartIndex = rowNum * SIZE;
        for (int i = rowStartIndex; i < rowStartIndex + SIZE; i++) {
            if (!btns[i].getText().equals(""))
                if (num == Integer.parseInt(btns[i].getText())) {
                    return false;
                }
        }
        return true;
    }

    /*
     * private helper method to check if a String is a number between 1-9
     * this method treats an empty string as a number
     */
    private boolean isNum(String text) {


        if (text.equals("")) {
            return true;
        }
        try {
            Integer.parseInt(text);
        } catch (Exception e) {
            return false;
        }


        if (Integer.valueOf(text) > 10 || Integer.valueOf(text) < 1) {
            return false;
        }

        return true;
    }

    // the onClick method for the number buttons
    private void btnAction(ActionEvent actionEvent, int index) {


        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter a number");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter a number:");

        String userInput = dialog.showAndWait()
                .map(String::trim) // remove leading/trailing whitespace
                .orElse("");

        Button temp = (Button) actionEvent.getSource();

        // is a number between 1-9 or empty space
        System.out.println(userInput);
        if (!isNum(userInput)) {
            notNumAlert();
            btnAction(actionEvent, index);
            return;

        }
        // is a legal number

        if (!isLegal(userInput, index)) {
            temp.setText("");
            notLegalAlert();
            return;
        }
        temp.setText(userInput);
    }


}
