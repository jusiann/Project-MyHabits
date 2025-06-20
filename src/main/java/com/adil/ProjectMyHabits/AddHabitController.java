package com.adil.ProjectMyHabits;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.time.LocalDate;

public class AddHabitController {
    @FXML
    private ListView<String> habitOptionsListView;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnBack;
    private HomeController homeController;

    @FXML
    public void initialize() {
        habitOptionsListView.getItems().addAll("Read", "Sleep", "Exercise", "Walk", "Food", "Water");
        
        btnBack.setOnAction(event -> {
        	Stage currentStage = (Stage) habitOptionsListView.getScene().getWindow();
        	currentStage.close();
        });
        
        btnAdd.setOnAction(event -> {
            String selectedHabit = habitOptionsListView.getSelectionModel().getSelectedItem();

            if (selectedHabit != null && homeController != null) {
                Habit habit = null;
                switch (selectedHabit) {
                    case "Read":
                    	Habit readingHabit = new ReadingHabit("Read");
                        ((ITrackable) readingHabit).markCompletedOn(LocalDate.now());
                        showAlert(Alert.AlertType.INFORMATION, "Habit Added!");
                        homeController.addHabit(readingHabit);
                        break;
                    case "Sleep":
                    	Habit sleepingHabit = new SleepingHabit("Sleep");
                        ((ITrackable) sleepingHabit).markCompletedOn(LocalDate.now());
                        showAlert(Alert.AlertType.INFORMATION, "Habit Added!");
                        homeController.addHabit(sleepingHabit);
                        break;
                    case "Exercise":
                        Habit exerciseHabit = new ExerciseHabit("Exercise");
                        ((ITrackable) exerciseHabit).markCompletedOn(LocalDate.now());
                        showAlert(Alert.AlertType.INFORMATION, "Habit Added!");
                        homeController.addHabit(exerciseHabit);
                        break;    
                    case "Walk":
                    	Habit walkingHabit = new WalkingHabit("Walk");
                    	((ITrackable) walkingHabit).markCompletedOn(LocalDate.now());
                    	showAlert(Alert.AlertType.INFORMATION, "Habit Added!");
                        homeController.addHabit(walkingHabit);
                        break;
                    case "Food":
                    	Habit foodHabit = new FoodHabit("Food");
                    	((ITrackable) foodHabit).markCompletedOn(LocalDate.now());
                    	showAlert(Alert.AlertType.INFORMATION, "Habit Added!");
                        homeController.addHabit(foodHabit);
                        break;
                    case "Water":
                    	Habit waterHabit = new WaterHabit("Water");
                    	((ITrackable) waterHabit).markCompletedOn(LocalDate.now());
                    	showAlert(Alert.AlertType.INFORMATION, "Habit Added!");
                    	homeController.addHabit(waterHabit);
                    	break;
                    default:
                        break;
                }
                Stage currentStage = ((Stage) btnAdd.getScene().getWindow());
                currentStage.close();
            }
        });
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }
    
    private void showAlert(Alert.AlertType type, String message) {
    	new Alert(type, message).showAndWait();
    }
}