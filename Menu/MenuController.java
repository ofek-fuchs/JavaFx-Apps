package com.example.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MenuController {
    @FXML
    private VBox dessertBox = new VBox();
    @FXML
    private VBox drinkBox = new VBox();
    @FXML
    private VBox maincourseBox = new VBox();
    @FXML
    private VBox startBox = new VBox();
    @FXML
    private TextField totalCost = new TextField();
    ResMenu resMenu;

    public void initialize(){
        resMenu = new ResMenu(dessertBox,drinkBox,maincourseBox,startBox,totalCost);
    }
    @FXML
    void orderPressed(ActionEvent event) {
        resMenu.processOrder();
    }


}