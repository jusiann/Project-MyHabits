package com.adil.ProjectMyHabits;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import java.time.LocalDate;

public class WalkingHabitDetailController {
    @FXML 
    public ComboBox<String> comboBoxChoice;
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
    @FXML 
    private VBox boxCalorie;
    @FXML 
    private VBox boxKilometer;
    @FXML
    private TextField tfCalorie;
    @FXML 
    private TextField tfKilometer;
    private HomeController homeController;
    private WalkingHabit habit;

    public void setHabit(WalkingHabit habit) {
        this.habit = habit;

        comboBoxChoice.getItems().addAll("Time", "Calorie", "Kilometer");
        comboBoxChoice.setValue("Time");

        spinnerHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        spinnerMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        String habitData = habit.getExtraData();
        if (habitData != null) {
            if (habitData.contains("kcal")) {
            	comboBoxChoice.setValue("Calorie");
            	tfCalorie.setText(habitData.replace(" kcal", ""));
            } else if (habitData.contains("km")) {
            	comboBoxChoice.setValue("Kilometer");
            	tfKilometer.setText(habitData.replace(" km", ""));
            } else if (habitData.contains("hour")) {
            	comboBoxChoice.setValue("Time");
                String[] parts = habitData.split(" ")[0].split(":");
                spinnerHour.getValueFactory().setValue(Integer.parseInt(parts[0]));
                spinnerMinute.getValueFactory().setValue(Integer.parseInt(parts[1]));
            }
        }
        updateFieldsVisibility();
        comboBoxChoice.valueProperty().addListener((observable, oldVal, newVal) -> updateFieldsVisibility());
    }

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }

    private void updateFieldsVisibility() {
        boolean isTime = comboBoxChoice.getValue().equals("Time");
        boolean isCalorie = comboBoxChoice.getValue().equals("Calorie");
        boolean isKilometer = comboBoxChoice.getValue().equals("Kilometer");
        boxTime.setVisible(isTime);
        boxTime.setManaged(isTime);
        boxCalorie.setVisible(isCalorie);
        boxCalorie.setManaged(isCalorie);
        boxKilometer.setVisible(isKilometer);
        boxKilometer.setManaged(isKilometer);
    }

    @FXML
    public void applyChanges() {
        String selected = comboBoxChoice.getValue();
        switch (selected) {
            case "Calorie":
                String kcal = tfCalorie.getText();
                habit.setExtraData(kcal + " kcal");
                break;
            case "Kilometer":
                String mil = tfKilometer.getText();
                habit.setExtraData(mil + " km");
                break;
            case "Time":
                int hour = spinnerHour.getValue();
                int minute = spinnerMinute.getValue();
                habit.setExtraData(String.format("%02d:%02d hour", hour, minute));
            	break;
        }
        ((ITrackable) habit).markCompletedOn(LocalDate.now());
        homeController.saveHabitsToFile();
        closeWindow();
    }

    @FXML
    public void deleteHabit() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete");
    	alert.setHeaderText("Are you sure you want to delete this habit?");
    	alert.setContentText("Press OK to confirm.");
    	alert.showAndWait().ifPresent(response -> {
    		if(response == ButtonType.OK) {
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
