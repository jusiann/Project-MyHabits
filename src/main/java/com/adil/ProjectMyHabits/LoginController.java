package com.adil.ProjectMyHabits;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.*;
import java.util.List;

public class LoginController {
    @FXML 
    private TextField tfUsername;
    @FXML 
    private PasswordField pfPassword;
    @FXML
    private ImageView imgLogo;
    private RegisterController registerController;
    
    @FXML
    private void loginAction() {
        String username = tfUsername.getText();
        String password = pfPassword.getText();
        registerController = new RegisterController();
        List<UserSession> userList = registerController.readUsersFromFile();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Fields cannot be empty!");
            return;
        }


        for (UserSession user : userList) {

            if (user.getUsername().equals(username)) {
                String salt = user.getSalt();
                String hashedPassword = HashUtil.SHA_256(password + salt);

                if (user.getPassword().equals(hashedPassword)) {
                    UserSession.setSession(
                		user.getUsername(),
                		user.getName(),
                		user.getSurname(),
                		user.getPassword(),
                        user.getSalt(),
                		user.getGender(),
                		user.getAge(),
                		user.getHeight(),
                		user.getWeight(),
                		user.getAvatarPath(),
                		user.isFirstLogin());
                    showAlert(Alert.AlertType.INFORMATION, "Login successful!");
                    loadMenuStage();
                    return;
                }
            }
        }
        showAlert(Alert.AlertType.ERROR, "Incorrect username or password!");
    }

    private void loadMenuStage() {
        try {        	
        	Stage menuStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            menuStage.setTitle("My Habits");
            menuStage.initStyle(StageStyle.DECORATED);
            menuStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconMyHabits.jpg")));
            menuStage.setResizable(false);
            menuStage.setScene(scene);
            menuStage.show();
            
            Stage currentStage = (Stage) tfUsername.getScene().getWindow();
            currentStage.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadRegisterScene() {
        try {
        	Stage stage = (Stage) tfUsername.getScene().getWindow();
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
        	Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }
}
