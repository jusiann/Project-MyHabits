package com.adil.ProjectMyHabits;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class FoodHabitDetailController {
	@FXML
	private TextField tfValue;
	@FXML 
    private Button btnApply;
    @FXML
    private Button btnDelete;
    private HomeController homeController;
    private FoodHabit habit;
    
    public void setHabit(FoodHabit habit) {
    	this.habit=habit;
    	String habitData = habit.getExtraData();
    	if(habitData != null) {
    		habitData.contains("kcal");
    		tfValue.setText(habitData.replace(" kcal", ""));
    	}
    }
    
    public void setHomeController(HomeController controller) {
    	this.homeController=controller;
    }
    
    public void applyChanges() {
    	String input = tfValue.getText();
    	if (input != null) {
    		habit.setExtraData(input + " kcal");
            ((ITrackable) habit).markCompletedOn(LocalDate.now());
            homeController.saveHabitsToFile();
		}
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
