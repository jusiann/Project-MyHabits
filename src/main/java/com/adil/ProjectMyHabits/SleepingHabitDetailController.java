package com.adil.ProjectMyHabits;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.time.LocalDate;

public class SleepingHabitDetailController {
    @FXML 
    private Spinner<Integer> spinnerHour;
    @FXML 
    private Spinner<Integer> spinnerMinute;
    @FXML 
    private Button btnApply;
    @FXML 
    private Button btnDelete;
    private HomeController homeController;
    private SleepingHabit habit;

    public void setHabit(SleepingHabit habit) {
        this.habit = habit;

        spinnerHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        spinnerMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        String habitData = habit.getExtraData();
        if (habitData != null && habitData.contains(":")) {
            String[] parts = habitData.split(" ")[0].split(":");
            spinnerHour.getValueFactory().setValue(Integer.parseInt(parts[0]));
            spinnerMinute.getValueFactory().setValue(Integer.parseInt(parts[1]));
        }
    }

    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }

    public void applyChanges() {
        int hour = spinnerHour.getValue();
        int minute = spinnerMinute.getValue();
        habit.setExtraData(String.format("%02d:%02d hour", hour, minute));
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
