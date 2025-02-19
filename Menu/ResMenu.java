package com.example.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
/*
this class is responsible for the menu's GUI and logic
 */
public class ResMenu {
    @FXML
    private CheckBox[] checkBoxes;
    @FXML
    private ComboBox<String>[] comboBoxes;
    @FXML
    private HBox[] hBoxes;
    @FXML
    private VBox dessertBox;
    @FXML
    private VBox drinkBox;
    @FXML
    private VBox maincourseBox;
    @FXML
    private VBox startBox;
    @FXML
    private TextField[] itemLabels;
    @FXML
    private TextField[] priceLabels;
    @FXML
    private TextField totalCost;
    Order order = new Order();
    private ArrayList<String> menuItems = new ArrayList<String>();
    private final int ITEM_PROPERTIES_AMOUNT = 3;


    /*
    The constructor class creates the static menu elements and calls initMenuItems which will dynamically add
    items to the menu based on the "menu.txt" file
     */
    public ResMenu(VBox dessertBox, VBox drinkBox, VBox maincourseBox, VBox startBox, TextField totalCost) {
        this.totalCost = totalCost;
        this.dessertBox = dessertBox;
        this.drinkBox = drinkBox;
        this.maincourseBox = maincourseBox;
        this.startBox = startBox;
        initMenuItems();
    }

    /*
    the @processOrder method creates a new alert and asks the user for his id for confirmation.
    the method supports the user accepting, updating, or canceling his order via the alert
     */
    public void processOrder() {
        TextArea newText = new TextArea(order.getOrder(totalCost.getText()));
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Order");
        alert.getDialogPane().setContent(newText);
        ButtonType acceptButton = new ButtonType("Accept");
        ButtonType updateButton = new ButtonType("Update");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(acceptButton, updateButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == acceptButton) {
                acceptOrder();
            } else if (result.get() == cancelButton) {
                newOrder();
            }
        }
    }
    // private methods

    /*
    this method creates a formatted string from the menu files using @loadMenu, and adds them to the GUI.
     */
    private void initMenuItems() {

        String menu = loadMenu();

        String wordBuilder = "";

        for (int i = 1; i < menu.length(); i++) {
            char currentChar = menu.charAt(i);
            if (currentChar != '\n') {
                wordBuilder += currentChar;
            } else {

                menuItems.add(wordBuilder);
                wordBuilder = "";
            }
            if (i == (menu.length() - 1))
                menuItems.add(wordBuilder);
        }

        addMenuItems();

    }

    /*
    this method is called from @initMenuItems to create the GUI of the menu items
    this method creates elements and places them in an hbox, which is then placed in the appropriate
     container (dessert,drink, etc..)
     */
    private void addMenuItems() {
        int amount = (int) menuItems.size() / ITEM_PROPERTIES_AMOUNT;
        boolean loadedMenu = false;
        hBoxes = new HBox[amount];
        comboBoxes = (ComboBox<String>[]) new ComboBox[amount];
        checkBoxes = new CheckBox[amount];
        itemLabels = new TextField[amount];
        priceLabels = new TextField[amount];
        totalCost.setText("0.00");
        totalCost.setPadding(new Insets(0, 0, 0, 5));
        for (int i = 0; i < menuItems.size(); i += 3) {
            int j = i / ITEM_PROPERTIES_AMOUNT;

            itemLabels[j] = new TextField(menuItems.get(i));
            itemLabels[j].setEditable(false);
            itemLabels[j].setAlignment(Pos.CENTER_RIGHT);
            itemLabels[j].setPrefWidth(260);
            String dishType = menuItems.get(i + 1);
            try {
                Double.parseDouble(menuItems.get(i + 2));
                priceLabels[j] = new TextField(menuItems.get(i + 2));
            } catch (NumberFormatException e) {
                priceLabels[j] = new TextField("error");
                System.out.println("ERROR: " + menuItems.get(i + 2) + " is NOT a valid price! please edit menu file");
            }

            priceLabels[j].setEditable(false);
            priceLabels[j].setPrefWidth(70);
            checkBoxes[j] = new CheckBox();
            checkBoxes[j].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    checkBoxClick(actionEvent, j);
                }
            });
            comboBoxes[j] = new ComboBox();
            hBoxes[j] = new HBox(comboBoxes[j], checkBoxes[j], priceLabels[j], itemLabels[j]);
            hBoxes[j].setSpacing(10);
            switch (dishType) {
                case "dessert":
                    dessertBox.getChildren().add(hBoxes[j]);
                    loadedMenu = true;
                    break;
                case "first":
                    startBox.getChildren().add(hBoxes[j]);
                    loadedMenu = true;
                    break;
                case "main":
                    maincourseBox.getChildren().add(hBoxes[j]);
                    loadedMenu = true;
                    break;
                case "drink":
                    drinkBox.getChildren().add(hBoxes[j]);
                    loadedMenu = true;
                    break;
            }
            if (loadedMenu == false) {
                System.out.println("Failed to load item to menu \n" +
                        dishType + " is not a valid dish Type");
            }
        }
        initComboBox();
    }

    /*
    this helper method initializes @this comboBoxes property and adds selectable values up to MAX_QUANTITY
     */
    private void initComboBox() {
        final int MAX_QUANTITY = 100;
        for (ComboBox<String> combobox : comboBoxes) {
            for (int i = 0; i < MAX_QUANTITY; i++) {
                combobox.getItems().add(String.valueOf(i));
                combobox.setValue("1");
            }
        }
    }

    /*
    this method is called when a checkBox is clicked, adds or remove the item from the order according to the checkBox
    state. when selected comboBox is disabled so item quantity can't be changed.
     */
    private void checkBoxClick(ActionEvent actionEvent, int i) {
        CheckBox temp = (CheckBox) actionEvent.getSource();
        DecimalFormat df = new DecimalFormat("#.##");
        String currentPrice = totalCost.getText();
        String toBeAdded = priceLabels[i].getText();
        Double newPrice;
        Item newItem = new Item(itemLabels[i].getText(), priceLabels[i].getText(), comboBoxes[i].getValue());
        if (temp.isSelected()) {
            order.add(newItem);
            newPrice = Double.parseDouble(currentPrice) + (Double.parseDouble(toBeAdded) * Integer.parseInt(comboBoxes[i].getValue()));
            totalCost.setText("" + newPrice);
            comboBoxes[i].setDisable(true);
        } else if (!temp.isSelected()) {
            newPrice = Double.parseDouble(currentPrice) - (Double.parseDouble(toBeAdded) * Integer.parseInt(comboBoxes[i].getValue()));
            order.remove(newItem);
            totalCost.setText("" + newPrice);
            comboBoxes[i].setDisable(false);
        }
        totalCost.setText(df.format(Double.parseDouble(totalCost.getText())));
        if (Double.parseDouble(totalCost.getText()) <= 0) {
            totalCost.setText("0");
        }

    }

    /*
    this helper method is called when the user accepts his order.
    it creates a file with the user id, shows the user confirmation, and initiates a new order as the previous
    one was completed.
    if user id is null an order will not be created and the process will be stopped
     */
    private void acceptOrder() {
        String name = getNameFromUserAlert(false);

        if (name == null)
            return;
        createOrderFile(name);
        orderOkAlert();
        newOrder();
    }

    /*
    this helper method renders an order confirmed Alert to the screen
     */
    private void orderOkAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Order Confirmed");
        alert.setTitle("Order Complete");
        alert.setContentText("Order successful! \nThank you for Shopping JavaFX!");
        alert.showAndWait();

    }

    /*
    this method creates an alert through which the user provides his id.
    the return value is the user id or null if the user didn't input any.
     */
    private String getNameFromUserAlert(boolean retry) {
        TextField textField = new TextField();
        textField.setPromptText("Enter name and i.d here");
        TextArea retryText = new TextArea("Invalid format. please try again");
        retryText.setStyle("-fx-text-fill: red;");
        TextArea textArea = new TextArea("Please enter your name along with your i.d for confirmation \n" +
                "For example : User12345");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirm Order");
    if(retry == true){
        alert.getDialogPane().setContent(new VBox(textArea, textField,retryText));
    }else {
        alert.getDialogPane().setContent(new VBox(textArea, textField));
    }
        ButtonType acceptButtonType = new ButtonType("Accept");
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(acceptButtonType, cancelButtonType);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == acceptButtonType) {
            String input = textField.getText();


            // Validate the input using regular expressions
            if (input.matches("^[a-zA-Z]+[0-9]+$")) { // Only allow letters followed by digits
                return input;
            } else {
                // Show an error message in the same alert
               return getNameFromUserAlert(true);
            }

        } else {
            return null;
        }
    }


    // this method reads the menu.txt file and returns the data as a string
    private String loadMenu() {
        String string = null;
        try {
            FileReader fr = new FileReader("menu.txt");
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append("\n");
                sb.append(line);
            }
            string = sb.toString();

            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Error, please create a menu.txt file");
            System.out.println();
        }
        return string;
    }

    /*
    this method uses @FileWriter and @BufferedWriter to write a user's id into a file.
     */
    private void createOrderFile(String userName) {
        try {
            FileWriter fw = new FileWriter(userName + ".txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(order.getOrder(totalCost.getText()));
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Error creating user File! No Username found!");
        }
    }

    /*
    this method restores the menu to its original state by undoing all checkBox and comboBox changes
     */
    private void newOrder() {
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {
                checkBoxes[i].fire();
            }
            if (Integer.parseInt(comboBoxes[i].getValue()) != 1) {
                comboBoxes[i].setValue("1");
            }
        }
    }

}
