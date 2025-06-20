package com.adil.ProjectMyHabits;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.time.LocalDate;

public class ReadingHabitDetailController {
    @FXML
    private ComboBox<String> comboBoxChoice;
    @FXML
    private TextField  tfValue;
    @FXML 
    private Spinner<Integer> spinnerHour;
    @FXML 
    private Spinner<Integer> spinnerMinute;
    @FXML 
    private HBox boxTime;
    @FXML 
    private Button btnApply;
    @FXML
    private Button btnDelete;
    private HomeController homeController;
    private ReadingHabit habit;

    public void setHabit(ReadingHabit habit) {
        this.habit = habit;

        comboBoxChoice.getItems().addAll("Time", "Page");
        comboBoxChoice.setValue("Time");

        spinnerHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        spinnerMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        String habitData = habit.getExtraData();
        if (habitData != null && !habitData.isEmpty()) {
            if (habitData.contains("pages")) {
            	comboBoxChoice.setValue("Page");
            	tfValue.setText(habitData.replace(" pages", ""));
            } else if (habitData.contains("hour")) {
            	comboBoxChoice.setValue("Time");
                String[] parts = habitData.split(" ")[0].split(":");
                spinnerHour.getValueFactory().setValue(Integer.parseInt(parts[0]));
                spinnerMinute.getValueFactory().setValue(Integer.parseInt(parts[1]));
            }
        }
        updateFieldVisibility();
        comboBoxChoice.valueProperty().addListener((observable, oldVal, newVal) -> updateFieldVisibility());
        
    }

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }

    private void updateFieldVisibility() {
        boolean isTime = comboBoxChoice.getValue().equals("Time");
        boxTime.setVisible(isTime);
        boxTime.setManaged(isTime);
        tfValue.setVisible(!isTime);
        tfValue.setManaged(!isTime);
    }

    public void applyChanges() {

        if (comboBoxChoice.getValue().equals("Page")) {
            String pages =  tfValue.getText();
            habit.setExtraData(pages + " pages");
        } else {
            int hour = spinnerHour.getValue();
            int minute = spinnerMinute.getValue();
            habit.setExtraData(String.format("%02d:%02d hour", hour, minute));
        }
        ((ITrackable) habit).markCompletedOn(LocalDate.now());
        homeController.saveHabitsToFile();
        closeWindow();
    }

    @FXML
    public void deleteHabit() {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Are you sure you want to delete this habit?");
        alert.setContentText("Press OK to confirm.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
            	homeController.getHabitList().remove(habit);
                homeController.saveHabitsToFile();
                closeWindow();
            }
        });
    }

    private void closeWindow() {
    	Stage currentStage = (Stage) btnApply.getScene().getWindow();
    	currentStage.close();
    } 
}
