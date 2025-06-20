package com.adil.ProjectMyHabits;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class ChangePasswordController {
    @FXML 
    private PasswordField pfCurrentPassword;
    @FXML 
    private PasswordField pfNewPassword;
    @FXML 
    private PasswordField pfConfirmNewPassword;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnSave;
    private ProfileController profileController;
    private UserSession user; 

    public void setUser(UserSession user) {
    	this.user = user; 
    } 
    
    @FXML
    private void saveAction() { 
        String currentPassword = pfCurrentPassword.getText(); 
        String newPassword = pfNewPassword.getText(); 
        String confirmPassword = pfConfirmNewPassword.getText();

        String salt = user.getSalt();
        String hashedPassword = HashUtil.SHA_256(currentPassword + salt);

        if (hashedPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "All fields must be filled!");
            return;
        }

//        System.out.println("Current Password: " + currentPassword);
//        System.out.println("Password: " + hashedPassword);
//        System.out.println("User Password: " + user.getPassword());
//        System.out.println("Salt : " + user.getSalt());

        if (!hashedPassword.equals(user.getPassword())) {
            showAlert(Alert.AlertType.ERROR, "Current password is incorrect!");
            return;
        }

        if (newPassword.equals(hashedPassword)) {
            showAlert(Alert.AlertType.WARNING, "New password cannot be the same as the current password!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "New passwords do not match!");
            return;
        }
        
        if(newPassword.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "New Password must be 8 digits or more!");
            return;
        }

        user.setPassword(HashUtil.SHA_256(newPassword + user.getSalt()));
        profileController.saveAction();
        backAction();
    }
    
    @FXML 
    private void backAction() { 
        Stage currentStage = (Stage) pfCurrentPassword.getScene().getWindow(); 
        currentStage.close(); 
    } 

	public void setProfileController(ProfileController profileController) {
		this.profileController = profileController;
	}
	
    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }
}
