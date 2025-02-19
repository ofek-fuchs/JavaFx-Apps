package com.example.reminderapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.HashMap;
/*
 * ReminderController class handles all GUI and logic of the reminder app.
 */
public class ReminderController {
    @FXML
    private ComboBox<Integer> dayBox;
    @FXML
    private ComboBox<Integer>  monthBox;
    @FXML
    private ComboBox<Integer>  yearBox;
    @FXML
    private TextArea textArea;
    private HashMap<CustomDate, String> dataMap = new HashMap<>();

    /*
     * this method loads a string from a saved date into the text area
     */
    @FXML
    void loadClicked(ActionEvent event) {

        CustomDate date = new CustomDate((Integer) yearBox.getValue(),(Integer)monthBox.getValue(),(Integer)dayBox.getValue());

        textArea.setText((String) dataMap.get(date));
    }

    /*
     * this method saves a string to a the dates map using the date as a key
     */
    @FXML
    void saveClicked(ActionEvent event) {
        CustomDate date = new CustomDate((Integer) yearBox.getValue(),(Integer)monthBox.getValue(),(Integer)dayBox.getValue());
        dataMap.put(date,textArea.getText());
        saveMapToFile();

    }
    /*
     * the initialize method acts as our constructor, creating the comboBox objects and loading the dates map
     */
    public void initialize() {
        initComboBox();
        loadMapFromFile();

    }
    /*
    this method gets a file which should contain a hash map of saved dates.
    the method loads the map and initializes this @dataMap property.
     */
    public void loadMapFromFile() {
        File file = getFile();

        if (file != null) {

            try {
                FileInputStream fileInput = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInput);
                Object object = objectInputStream.readObject();
                objectInputStream.close();
                fileInput.close();
                if(object instanceof HashMap) {
                    dataMap = (HashMap<CustomDate, String>) object;
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Save file is empty");
            }
        }
    }
    /*
    this method gets a file and saves the @dataMap property to the file
     */
    public void saveMapToFile(){
        File file = getFile();

        try{
            FileOutputStream fileOutput = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutput);
            objectOutputStream.writeObject(dataMap);
            objectOutputStream.close();
            fileOutput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    this method initializes the comboBoxes with values
     */
    private void initComboBox() {
        final int  MAX_DAYS = 31;
        final int  MAX_MONTH = 12;
        final int  MAX_YEARS = 5;
        final int  STARTING_YEAR = 2023;
        for (int i = 1; i <= MAX_DAYS; i++)
            dayBox.getItems().add(i);
        for (int i = 1; i <= MAX_MONTH; i++)
            monthBox.getItems().add(i);
        for (int i = STARTING_YEAR; i < STARTING_YEAR + MAX_YEARS; i++)
            yearBox.getItems().add(i);
        dayBox.setValue(1);
        monthBox.setValue(1);
        yearBox.setValue(2023);
    }

    /*
    this method opens a file chooser and returns the selected file
     */
    private File getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select your save file");
        fileChooser.setInitialDirectory(new File("."));
        return fileChooser.showOpenDialog(null);

    }

}