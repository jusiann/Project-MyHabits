package com.adil.ProjectMyHabits;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ProfileController {
    @FXML 
    private ImageView imgAvatar;
    @FXML 
    private Label lblUsername;
    @FXML 
    private Label lblFullName;
    @FXML 
    private TextField tfHeight; 
    @FXML 
    private TextField tfWeight;
    @FXML 
    private TextField tfAge;
    @FXML 
    private Button btnEdit;
    @FXML 
    private Button changePasswordButton;
    @FXML
    private Button btnChangeAvatar;
    @FXML 
    private RadioButton maleRadio;
    @FXML 
    private RadioButton femaleRadio;
    @FXML 
    private Button btnDeleteAllHabit;
    private final ToggleGroup genderGroup = new ToggleGroup();
    private boolean isEditable = false;
	private ProfileController profileController;
	private RegisterController registerController;

    @FXML
    public void initialize() {
        UserSession session = UserSession.getSession();
        if (session != null) {
            lblUsername.setText("@" + session.getUsername());
            lblFullName.setText(session.getName() + " " + session.getSurname());
            tfHeight.setText(session.getHeight());
            tfWeight.setText(session.getWeight());
            tfAge.setText(session.getAge());
            loadAvatar(session.getAvatarPath());
            if (session.getGender() == 'M') 
            	maleRadio.setSelected(true);
            else
            	femaleRadio.setSelected(true);
        }
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);

        Circle clip = new Circle(50, 50, 50);
        clip.centerXProperty().bind(imgAvatar.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(imgAvatar.fitHeightProperty().divide(2));
        imgAvatar.setClip(clip);

        restrictToNumbers(tfHeight);
        restrictToNumbers(tfWeight);
        restrictToNumbers(tfAge);
        setEditableFields(false);
    }
    
    private void setEditableFields(boolean editable) {
        tfHeight.setEditable(editable);
        tfWeight.setEditable(editable);
        tfAge.setEditable(editable);

        tfHeight.setDisable(!editable);
        tfWeight.setDisable(!editable);
        tfAge.setDisable(!editable);
        maleRadio.setDisable(!editable);
        femaleRadio.setDisable(!editable);
        btnDeleteAllHabit.setDisable(!editable);
        changePasswordButton.setDisable(!editable);
        btnChangeAvatar.setDisable(!editable);
        
        btnEdit.setText(editable ? "Lock" : "Edit");
        isEditable = editable;
    }

    @FXML
    private void setEditToggle() {
        setEditableFields(!isEditable);
    }

    private void restrictToNumbers(TextField textFieldArea) {
    	textFieldArea.textProperty().addListener((observable, oldText, newText) -> {
            if (!newText.matches("\\d*")) {
            	textFieldArea.setText(newText.replaceAll("[^\\d]", ""));
                showAlert(Alert.AlertType.WARNING, "Only numbers are allowed!");
            }
        });
    }

    private void loadAvatar(String storedPath) {
    	if (storedPath != null && !storedPath.isEmpty()) {
        	URL avatarURL = getClass().getResource(storedPath);
           	if (avatarURL != null) {
           		imgAvatar.setImage(new Image(avatarURL.toExternalForm()));
          		return;
            }
    	}
    	URL defaultURL = getClass().getResource("/avatars/default.jpg");
    	imgAvatar.setImage(new Image(defaultURL.toExternalForm()));
    }
    
    @FXML
    private void changeAvatarAction() {
        try {
        	Stage changeAvatarStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("avatarSelection.fxml"));
            Parent root = loader.load();
            AvatarSelectionController controller = loader.getController();
            controller.setProfileController(this);
            controller.setCurrentAvatar(UserSession.getSession().getAvatarPath());
            controller.setAvatarCallback(selectedAvatar -> {
                UserSession.getSession().setAvatarPath(selectedAvatar);
                loadAvatar(selectedAvatar);
                registerController = new RegisterController();
                List<UserSession> users = registerController.readUsersFromFile();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUsername().equals(UserSession.getSession().getUsername())) {
                        users.set(i, UserSession.getSession());
                        break;
                    }
                }
                registerController.writeUsersToFile(users);
            });

            Scene scene = new Scene(root);
            changeAvatarStage.setTitle("Select Avatar");
            changeAvatarStage.initModality(Modality.APPLICATION_MODAL);
            changeAvatarStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconMyHabits.jpg")));
            changeAvatarStage.setResizable(false);
            changeAvatarStage.setScene(scene);
            changeAvatarStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void changePasswordAction() {
        try {
        	Stage changePasswordStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("changePassword.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            ChangePasswordController controller = loader.getController();
            controller.setUser(UserSession.getSession());
            controller.setProfileController(this);
            changePasswordStage.setTitle("Change Password");
            changePasswordStage.initModality(Modality.APPLICATION_MODAL);
            changePasswordStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconMyHabits.jpg")));
            changePasswordStage.setResizable(false);
            changePasswordStage.setScene(scene);
            changePasswordStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void saveAction() {
        UserSession session = UserSession.getSession();
        session.setHeight(tfHeight.getText());
        session.setWeight(tfWeight.getText());
        session.setAge(tfAge.getText());
        session.setGender(maleRadio.isSelected() ? 'M' : 'F');

        RegisterController registerController = new RegisterController();
        List<UserSession> userList = registerController.readUsersFromFile();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(session.getUsername())) {
                userList.set(i, session);
                break;
            }
        }
        registerController.writeUsersToFile(userList);
        showAlert(Alert.AlertType.INFORMATION, "Saved successfully!");
        setEditableFields(false);
    }

    @FXML
    private void logoutAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Press OK to confirm.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                loadLoginStage();
            }
        });
    }

    @FXML
    private void deleteAllHabitsAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete All Habits");
        alert.setHeaderText("Are you sure you want to delete ALL YOUR HABIT DATA?");
        alert.setContentText("This action cannot be undone.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String username = UserSession.getSession().getUsername();
                    HabitListManager.getInstance(username).deleteAllUserHabitData();
                    File userFolder = new File("data/users_data", username);
                    deleteDirectory(userFolder);
                    showAlert(Alert.AlertType.INFORMATION, "ALL HABITS DATA HAS BEEN DELETED.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    private void loadLoginStage() {
        try {
        	Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            loginStage.setTitle("Welcome to MyHabits");
            loginStage.initStyle(StageStyle.DECORATED);
            loginStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconMyHabits.jpg")));
            loginStage.setResizable(false);
            loginStage.setScene(new Scene(root));
            loginStage.show();
            
            Stage currentStage = (Stage) imgAvatar.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }
    
    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }
}
