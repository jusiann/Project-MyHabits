package com.adil.ProjectMyHabits;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AvatarSelectionController {
    @FXML 
    private RadioButton radioBtnAvatar1;
    @FXML 
    private RadioButton radioBtnAvatar2;
    @FXML 
    private RadioButton radioBtnAvatar3;
    @FXML 
    private RadioButton radioBtnAvatar4;
    @FXML
    private RadioButton radioBtnAvatar5;
    @FXML
    private RadioButton radioBtnAvatar6;
    private ProfileController profileController;
    private final ToggleGroup avatarGroup = new ToggleGroup();
    private final Map<RadioButton, String> avatarPathMap = new HashMap<>();
    private Consumer<String> avatarCallback;

    @FXML
    public void initialize() {
        radioBtnAvatar1.setToggleGroup(avatarGroup);
        radioBtnAvatar2.setToggleGroup(avatarGroup);
        radioBtnAvatar3.setToggleGroup(avatarGroup);
        radioBtnAvatar4.setToggleGroup(avatarGroup);
        radioBtnAvatar5.setToggleGroup(avatarGroup);
        radioBtnAvatar6.setToggleGroup(avatarGroup); 
        avatarPathMap.put(radioBtnAvatar1, "/avatars/avatar1.png");
        avatarPathMap.put(radioBtnAvatar2, "/avatars/avatar2.png");
        avatarPathMap.put(radioBtnAvatar3, "/avatars/avatar3.png");
        avatarPathMap.put(radioBtnAvatar4, "/avatars/avatar4.png");
        avatarPathMap.put(radioBtnAvatar5, "/avatars/avatar5.png");
        avatarPathMap.put(radioBtnAvatar6, "/avatars/avatar6.png");
    }

    public void setCurrentAvatar(String avatarPath) {
        for (Map.Entry<RadioButton, String> entry : avatarPathMap.entrySet()) {
            if (entry.getValue().equals(avatarPath)) {
                entry.getKey().setSelected(true);
                break;
            }
        }
    }
    
    public void setAvatarCallback(Consumer<String> callback) {
        this.avatarCallback = callback;
    }

    @FXML
    private void saveAction() {
        RadioButton selectedAvatar = (RadioButton) avatarGroup.getSelectedToggle();
        if (selectedAvatar != null && avatarCallback != null) {
            String selectedAvatarPath = avatarPathMap.get(selectedAvatar);
            avatarCallback.accept(selectedAvatarPath);
        } else {
            showAlert(AlertType.WARNING, "Please select an avatar before saving.");
        }
        profileController.saveAction();
        backAction();
    }
    
    @FXML 
    private void backAction() { 
        Stage currentStage = (Stage) radioBtnAvatar1.getScene().getWindow(); 
        currentStage.close(); 
    } 
    
    public void setProfileController(ProfileController profileController) {
		this.profileController = profileController;
	}
    
    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }
}
