package com.adil.ProjectMyHabits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.adil.ProjectMyHabits.HashUtil;

public class RegisterController {
	@FXML 
    private TextField tfUsername;
    @FXML 
    private TextField tfName;
    @FXML 
    private TextField tfSurname;
    @FXML 
    private PasswordField pfPassword;
    @FXML 
    private PasswordField pfConfirmPassword;
    private final String FILE_PATH = "data/UserList.json";
    private final Gson gson = new Gson();

    @FXML
    private void registerAction() {
        String name = tfName.getText();
        String surname = tfSurname.getText();
        String username = tfUsername.getText();
        String password = pfPassword.getText();
        String confirmPassword = pfConfirmPassword.getText();
        List<UserSession> userList = readUsersFromFile();
        
        if (username.equals("admin") && password.equals("adminroot") && confirmPassword.equals("adminroot") && surname.isEmpty() && name.isEmpty()) {
            loadAdminScene();
            return;
        }
        
        if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields must be filled!");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Passwords do not match!");
            return;
        }
        
        if (password.length() < 8) {
            showAlert(Alert.AlertType.ERROR, "Password must be 8 digits or more!");
            return;
        }
        
        for (UserSession user : userList) {
            if (user.getUsername().equals(username)) {
                showAlert(Alert.AlertType.ERROR, "Username already exists!");
                return;
            }
        }

        String salt = HashUtil.generateSalt();
        String hashedPassword = HashUtil.SHA_256(password + salt);
        userList.add(new UserSession(username, name, surname, hashedPassword, salt));
        writeUsersToFile(userList);
        showAlert(Alert.AlertType.INFORMATION, "Account created successfully!");
        loadLoginScene();
    }

    @FXML
    private void loadLoginScene() {
        try {
        	Stage stage = (Stage) tfUsername.getScene().getWindow();
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        	Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void loadAdminScene() {
        try {
        	Stage stage = (Stage) tfUsername.getScene().getWindow();
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
        	Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<UserSession> readUsersFromFile() {
    	Path dataFolder = Paths.get("data");
    	if (!Files.exists(dataFolder)) {
    	    try {
				Files.createDirectories(dataFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	Path usersDataFolder = Paths.get("data/users_data");
    	if (!Files.exists(usersDataFolder)) {
    	    try {
				Files.createDirectories(usersDataFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
        File file = new File(FILE_PATH);
        if (!file.exists()) 
        	return new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            Type userListType = new TypeToken<List<UserSession>>(){}.getType();
            return gson.fromJson(reader, userListType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void writeUsersToFile(List<UserSession> users) {
    	Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (Exception e){
        	e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }
}