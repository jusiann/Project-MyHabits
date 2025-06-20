package com.adil.ProjectMyHabits;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class WaterHabitDetailController {
    @FXML 
    private Spinner<Double> spinnerWater;
    @FXML
    private Button btnApply;
    @FXML 
    private Button btnDelete;
    private HomeController homeController;
    private WaterHabit habit;
    
    public void setHabit(WaterHabit habit) {
    	this.habit = habit;
    	
    	spinnerWater.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 5.0, 0.0, 0.25));
    	
    	String habitData = habit.getExtraData();
    	if(habitData != null && habitData.contains(" ")) {
    		String[] parts = habitData.split(" ");
    		double value = Double.parseDouble(parts[0]);
    		spinnerWater.getValueFactory().setValue(value);
    	}
    }
    
    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }
    
    public void applyChanges() {
        double water = spinnerWater.getValue();
        habit.setExtraData(String.format("%.2f L", water));
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
