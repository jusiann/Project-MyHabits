package com.adil.ProjectMyHabits;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class HomeController {
    @FXML 
    private Button btnAddHabit;  
    @FXML 
    private TableView<Habit> tableViewHabit;
    @FXML 
    private TableColumn<Habit, Boolean> columnIsCompleted;
    @FXML 
    private TableColumn<Habit, String> columnName;
    @FXML 
    private TableColumn<Habit, String> columnExtraData;
    @FXML 
    private TableColumn<Habit, Void> columnDetails;
    @FXML
    private Label lblGreetingMessage;
    @FXML
    private Label lblGreetingName;
    private final ObservableList<Habit> listHabit = FXCollections.observableArrayList();
    private final String FOLDER_PATH = "data/users_data";
    private final String user = UserSession.getSession().getUsername();
    public static HomeController instance;

    @FXML
    public void initialize() {
    	instance = this;
        greeting(lblGreetingMessage);
        lblGreetingName.setText(UserSession.getSession().getName());
        tableViewHabit.setEditable(true);
        
        columnIsCompleted.setCellValueFactory(cellData -> {
            BooleanProperty prop = cellData.getValue().completedProperty();
            prop.addListener((observable, oldValue, newValue) -> {
                Habit habit = cellData.getValue(); 
                habit.setCompleted(newValue);
                if (habit instanceof ITrackable) {
                    ITrackable trackableHabit = (ITrackable) habit;
                    LocalDate today = LocalDate.now();
                    if (newValue) {
                        trackableHabit.markCompletedOn(today);
                    } else {
                        trackableHabit.unmarkCompletedOn(today);
                    }
                }
                if (TrackController.getInstance() != null) {
                    TrackController.getInstance().updateCalendar();
                }
                saveHabitsToFile();
                
            });
            return prop;
        });
        columnIsCompleted.setCellFactory(CheckBoxTableCell.forTableColumn(columnIsCompleted));
        
        columnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        
        columnExtraData.setCellValueFactory(new PropertyValueFactory<>("ExtraData"));
        
        columnDetails.setCellFactory(column -> new TableCell<>() {
            private final Button btnDetail = new Button("Details"); {
                btnDetail.setOnAction(event -> {
                    Habit habit = getTableView().getItems().get(getIndex());
                    loadDetailStage(habit);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnDetail);
            }
        });
        
        tableViewHabit.setItems(listHabit);
        btnAddHabit.setOnAction(event -> loadAddHabitStage());
        loadHabitsFromFile();
        loadAllHabitDataForUser();
    }

    public void greeting(Label label) {
        int hour = LocalTime.now().getHour();
        String greeting;

        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good Evening";
        } else {
            greeting = "Good Night";
        }
        label.setText(greeting+", ");
    }
    
    public ObservableList<Habit> getHabitList() {
        return listHabit;
    }

    public void addHabit(Habit habit) {
    	HabitListManager.getInstance(user).setTodayHabits(new ArrayList<>(listHabit));
        listHabit.add(habit);
        saveHabitsToFile();
    }

    private void loadAddHabitStage() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addHabit.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Add Habit");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconMyHabits.jpg")));
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            
            AddHabitController controller = loader.getController();
            controller.setHomeController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDetailStage(Habit habit) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root;
            
            if (habit instanceof ReadingHabit) {
                loader.setLocation(getClass().getResource("readingHabitDetail.fxml"));
                root = loader.load();
                ReadingHabitDetailController controller = loader.getController();
                controller.setHabit((ReadingHabit) habit);
                controller.setHomeController(this);
            } else if (habit instanceof SleepingHabit) {
                loader.setLocation(getClass().getResource("sleepingHabitDetail.fxml"));
                root = loader.load();
                SleepingHabitDetailController controller = loader.getController();
                controller.setHabit((SleepingHabit) habit);
                controller.setHomeController(this);
            } else if (habit instanceof ExerciseHabit) {
                loader.setLocation(getClass().getResource("exerciseHabitDetail.fxml"));
                root = loader.load();
                ExerciseHabitDetailController controller = loader.getController();
                controller.setHabit((ExerciseHabit) habit);
                controller.setHomeController(this);
            } else if (habit instanceof WalkingHabit) {
                loader.setLocation(getClass().getResource("walkingHabitDetail.fxml"));
                root = loader.load();
                WalkingHabitDetailController controller = loader.getController();
                controller.setHabit((WalkingHabit) habit);
                controller.setHomeController(this);
            } else if (habit instanceof FoodHabit) {
                loader.setLocation(getClass().getResource("foodHabitDetail.fxml"));
                root = loader.load();
                FoodHabitDetailController controller = loader.getController();
                controller.setHabit((FoodHabit) habit);
                controller.setHomeController(this);
            } else if (habit instanceof WaterHabit) {
                loader.setLocation(getClass().getResource("waterHabitDetail.fxml"));
                root = loader.load();
                WaterHabitDetailController controller = loader.getController();
                controller.setHabit((WaterHabit) habit);
                controller.setHomeController(this);
            } else 
            	return;
            
            Scene scene = new Scene(root);
            stage.setTitle(habit.getName() + " Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconMyHabits.jpg")));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            loadHabitsFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHabitsToFile() {
        try {
            LocalDate today = LocalDate.now();
            File userFolder  = new File(FOLDER_PATH, user);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String fileName = today.format(dateFormatter) + ".json";
            File file = new File(userFolder, fileName);

            Gson gson = new GsonBuilder()
            		.setPrettyPrinting()
            		.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            		.create();
            
            if (!userFolder.exists()) 
            	userFolder.mkdirs();
            	
            List<HabitJsonModel> habitJsonList = new ArrayList<>();
            for (Habit habit : listHabit) {
                HabitJsonModel model = new HabitJsonModel();
                model.className = habit.getClass().getSimpleName();
                model.name = habit.getName();
                model.detail = habit.getExtraData();
                model.isCompleted = habit.isCompleted();
                model.completedDates = ((ITrackable) habit).getCompletedDates();
                habitJsonList.add(model);
            }
            
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(habitJsonList, writer);
            }
            
            List<Habit> habitListMap = (List<Habit>) listHabit;
            HabitListManager.getInstance(user).setTodayHabits(habitListMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadHabitsFromFile() {
        try {
        	listHabit.clear();
            LocalDate today = LocalDate.now();
            File userFolder = new File(FOLDER_PATH, user);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String fileName = today.format(dateFormatter) + ".json";
            File file = new File(userFolder, fileName);
            
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

            if (!file.exists()) {
            	LocalDate yesterday = today.minusDays(1);
                String yesterdayFileName = yesterday.format(dateFormatter) + ".json";
                File yesterdayFile = new File(userFolder, yesterdayFileName);
                
                if (yesterdayFile.exists()) {
                    try (Reader reader = new FileReader(yesterdayFile)) {
                        Type listType = new TypeToken<List<HabitJsonModel>>(){}.getType();
                        List<HabitJsonModel> oldHabitList = gson.fromJson(reader, listType);
                        List<HabitJsonModel> newHabitList = new ArrayList<>();
                        
                        for (HabitJsonModel oldModel : oldHabitList) {
                            HabitJsonModel newModel = new HabitJsonModel();
                            newModel.className = oldModel.className;
                            newModel.name = oldModel.name;
                            newModel.detail = oldModel.detail;
                            newModel.isCompleted = false; 
                            newModel.completedDates = new HashSet<>(); 
                            newHabitList.add(newModel);
                        }
                        
                        try (Writer writer = new FileWriter(file)) {
                            gson.toJson(newHabitList, writer);
                        }
                    }
                } else
                	return;
            }

            try (Reader reader = new FileReader(file)) {
                Type listType = new TypeToken<List<HabitJsonModel>>(){}.getType();
                List<HabitJsonModel> habitJsonList = gson.fromJson(reader, listType);

                for (HabitJsonModel model : habitJsonList) {
                    Habit habit = null;
                    switch (model.className) {
                        case "ReadingHabit":
                            habit = new ReadingHabit(model.name);
                            break;
                        case "SleepingHabit":
                            habit = new SleepingHabit(model.name);
                            break;
                        case "ExerciseHabit":
                            habit = new ExerciseHabit(model.name);
                            break;
                        case "WalkingHabit":
                            habit = new WalkingHabit(model.name);
                            break;
                        case "FoodHabit":
                            habit = new FoodHabit(model.name);
                            break;
                        case "WaterHabit":
                            habit = new WaterHabit(model.name);
                            break;
                        default:
                            break;
                    }
                    
                    if (habit != null) {
                        habit.setCompleted(model.isCompleted);
                        habit.setExtraData(model.detail);
                        for (LocalDate date : model.completedDates) {
                            ((ITrackable) habit).markCompletedOn(date);
                        }
                        listHabit.add(habit);
                    }
                }
            }
            List<Habit> habitListMap = (List<Habit>) listHabit;
            HabitListManager.getInstance(user).setTodayHabits(habitListMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadAllHabitDataForUser() {
        Map<LocalDate, List<Habit>> habitDataMap = new HashMap<>();
        File userFolder = new File(FOLDER_PATH, user);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        File[] files = userFolder.listFiles((dir, name) -> name.endsWith(".json"));
        
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        
        if (!userFolder.exists()) 
        	return;

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName().replace(".json", "");
                
                try {
                    LocalDate date = LocalDate.parse(fileName, dateFormatter);
                    
                    try (Reader reader = new FileReader(file)) {
                        Type listType = new TypeToken<List<HabitJsonModel>>(){}.getType();
                        List<HabitJsonModel> habitJsonList = gson.fromJson(reader, listType);
                        
                        if (habitJsonList != null) {
                            List<Habit> habitsList = new ArrayList<>();
                            
                            for (HabitJsonModel model : habitJsonList) {
                            	Habit habit = null;
                            	switch (model.className) {
                            	    case "ReadingHabit":
                            	        habit = new ReadingHabit(model.name);
                            	        break;
                            	    case "SleepingHabit":
                            	        habit = new SleepingHabit(model.name);
                            	        break;
                            	    case "ExerciseHabit":
                            	        habit = new ExerciseHabit(model.name);
                            	        break;
                            	    case "WalkingHabit":
                            	        habit = new WalkingHabit(model.name);
                            	        break;
                            	    case "FoodHabit":
                            	        habit = new FoodHabit(model.name);
                            	        break;
                            	    case "WaterHabit":
                            	        habit = new WaterHabit(model.name);
                            	        break;
                            	    default:
                            	        break;
                            	}
                                
                                if (habit != null) {
                                    habit.setCompleted(model.isCompleted);
                                    habit.setExtraData(model.detail);
                                    for (LocalDate d : model.completedDates) {
                                        ((ITrackable) habit).markCompletedOn(d);
                                    }
                                    habitsList.add(habit);
                                }
                            }
                            habitDataMap.put(date, habitsList);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        HabitListManager.getInstance(user).setAllHabitData(habitDataMap);
    }
}
