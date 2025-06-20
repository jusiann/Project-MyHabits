package com.adil.ProjectMyHabits;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.*;
import java.util.*;

public class AdminController {
    @FXML
    private ListView<String> userListView;
    private List<UserSession> userList = new ArrayList<>();
    private RegisterController registerController;
    @FXML
    public void initialize() {
    	registerController = new RegisterController();
        userList = registerController.readUsersFromFile();
        for (UserSession user : userList) {
            userListView.getItems().add(user.getUsername());
        }
    }

    @FXML
    private void deleteSelectedUser() {
        String selectedUser = userListView.getSelectionModel().getSelectedItem();
        if(selectedUser != null) {
        	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete " + selectedUser.toUpperCase());
            alert.setHeaderText("Are you sure you want to DELETE " + selectedUser.toUpperCase() + "'s account?");
            alert.setContentText("This action cannot be undone.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                	userList.removeIf(user -> user.getUsername().equals(selectedUser));
                    registerController.writeUsersToFile(userList);
                    userListView.getItems().remove(selectedUser);
                    HabitListManager.getInstance(selectedUser).deleteAllUserHabitData();
                    File userFolder = new File("data/users_data", selectedUser);
                    deleteDirectory(userFolder);
                    showAlert(Alert.AlertType.INFORMATION, selectedUser.toUpperCase() + " HAS BEEN DELETED.");
                }
            });
        } else {
        	showAlert(AlertType.ERROR, "Please select a user!");
        }
    }

    private void deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) deleteDirectory(file);
            }
            dir.delete();
        }
    }
    
    @FXML
    private void backToLogin() {
        try {
        	Stage loginStage = new Stage();
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        	Parent root = loader.load();
        	Scene scene = new Scene(root);
        	loginStage.setTitle("My Habits");
            loginStage.initStyle(StageStyle.DECORATED);
            loginStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconMyHabits.jpg")));
            loginStage.setResizable(false);
            loginStage.setScene(scene);
            loginStage.show();
            
            Stage currentStage = (Stage) userListView.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }
}
