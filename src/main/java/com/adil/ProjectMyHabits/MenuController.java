package com.adil.ProjectMyHabits;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.Arrays;
import java.util.List;

public class MenuController {
    @FXML
    private Button btnHome;
    @FXML
    private Button btnTrack;  
    @FXML
    private Button btnSettings;    
    @FXML
    private Label lblUser;   
    @FXML
    private AnchorPane contentAnchorPane;  
    private String user = UserSession.getSession().getName();
    private RegisterController registerController;

    @FXML
    public void initialize() {
    	lblUser.setText(user);
    	if(UserSession.getSession().isFirstLogin() == true) {
            UserSession.getSession().setFirstLogin(false);
            registerController = new RegisterController();
            List<UserSession> userList = registerController.readUsersFromFile();
            for (UserSession user : userList) {
                if (user.getUsername().equals(UserSession.getSession().getUsername())) {
                    user.setFirstLogin(false);
                    break;
                }
            }
            registerController.writeUsersToFile(userList);
    	} else {
    		setActiveButton(btnHome); 
            loadPage("home.fxml");
    	}
    }
    
    @FXML
    private void goHomeMenu() {
    	setActiveButton(btnHome);
        loadPage("home.fxml");
    }

    @FXML
    private void goTrackMenu() {
    	setActiveButton(btnTrack);
        loadPage("track.fxml");
    }
    
    @FXML
    private void goSettingsMenu() {
    	try {
    		Stage settingStage = new Stage(); 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            settingStage.setTitle("My Habits");
            settingStage.initStyle(StageStyle.DECORATED);
            settingStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconMyHabits.jpg")));
            settingStage.setResizable(false);
            settingStage.setScene(scene);
            settingStage.show();
            
            Stage currentStage = (Stage) contentAnchorPane.getScene().getWindow();    
            SettingsController controller = loader.getController();
            controller.setPreviousStage(currentStage);
            currentStage.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setActiveButton(Button activeBtn) {
        for (Button button : Arrays.asList(btnHome, btnTrack)) {
        	button.getStyleClass().remove("active");
        }
        activeBtn.getStyleClass().add("active");
    }
    
    private void loadPage(String page) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
            AnchorPane pane = loader.load();
            contentAnchorPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
