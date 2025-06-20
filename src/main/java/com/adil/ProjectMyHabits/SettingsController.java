package com.adil.ProjectMyHabits;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.Arrays;

public class SettingsController {
    @FXML 
    private Button btnProfile;
    @FXML 
    private Button btnAbout;
    @FXML 
    private Button btnBack;
    @FXML 
    private AnchorPane contentAnchorPane;
    private Stage previousStage;

    @FXML
    private void initialize() {
    	setActiveButton(btnProfile);
    	loadPage("profile.fxml");
    }

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }
    
    @FXML
    private void goProfileMenu() {
    	setActiveButton(btnProfile);
    	loadPage("profile.fxml");
    }
    
    @FXML
    private void goAboutMenu() {
    	setActiveButton(btnAbout);
    	loadPage("about.fxml");
    }
    
    @FXML
    private void goBackToMenu() {
        Stage currentStage = (Stage) contentAnchorPane.getScene().getWindow();
        currentStage.close();
        if (previousStage != null) {
            previousStage.show();
        }
    }
    
    public void setActiveButton(Button activeBtn) {
    	for(Button button : Arrays.asList(btnProfile, btnAbout)) {
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
