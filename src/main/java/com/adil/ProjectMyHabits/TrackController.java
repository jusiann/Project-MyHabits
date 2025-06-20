package com.adil.ProjectMyHabits;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class TrackController {
    @FXML 
    private ComboBox<String> comboBoxMonth;
    @FXML 
    private ComboBox<Integer> comboBoxYear;
    @FXML 
    private Label lblHeader;
    @FXML 
    private GridPane calendarGrid;
    private HabitListManager habitListManager;
    public static TrackController instance;
    
    @FXML
    public void initialize() {
    	instance = this;
        populateMonthComboBox();
        populateYearComboBox();
        
        LocalDate today = LocalDate.now();
        comboBoxMonth.setValue(String.format("%02d", today.getMonthValue()));
        comboBoxYear.setValue(today.getYear());

        String username = UserSession.getSession().getUsername();
        habitListManager = HabitListManager.getInstance(username);

        updateCalendar();

        comboBoxMonth.setOnAction(e -> updateCalendar());
        comboBoxYear.setOnAction(e -> updateCalendar());
    }
    
    public static TrackController getInstance() {
        return instance;
    }
    
    private void populateMonthComboBox() {
        for (int i = 1; i <= 12; i++) {
            comboBoxMonth.getItems().add(String.format("%02d", i));
        }
    }

    private void populateYearComboBox() {
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear - 5; y <= currentYear + 5; y++) {
            comboBoxYear.getItems().add(y);
        }
    }

    private double getDailyCompletionRate(LocalDate date) {
        Map<LocalDate, List<Habit>> allData = habitListManager.getAllHabitData();
        List<Habit> habitsForDate = allData.getOrDefault(date, new ArrayList<>());

        List<Habit> completedHabits = habitsForDate.stream()
            .filter(habit -> habit.isCompleted() && habit.getCompletedDates().contains(date))
            .collect(Collectors.toList());

        List<Habit> totalHabits = habitsForDate.stream()
            .filter(habit -> !date.isAfter(LocalDate.now()))
            .collect(Collectors.toList());

        double total = totalHabits.size();
        double completed = completedHabits.size();
        return total == 0 ? 0.0 : completed / total;
    }
    
    public void updateCalendar() {
        calendarGrid.getChildren().clear();

        int selectedMonth = Integer.parseInt(comboBoxMonth.getValue());
        int selectedYear = comboBoxYear.getValue();

        YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);
        lblHeader.setText(yearMonth.getMonth()
        		.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + selectedYear);

        String[] nameDays = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        for (int i = 0; i < 7; i++) {
            Label lblDay = new Label(nameDays[i]);
            lblDay.setMaxWidth(Double.MAX_VALUE);
            lblDay.setAlignment(Pos.CENTER);
            lblDay.setStyle("-fx-font-weight: bold;");
            calendarGrid.add(lblDay, i, 0);
        }

        LocalDate firstDay = yearMonth.atDay(1);
        int startColumn = firstDay.getDayOfWeek().getValue() % 7;
        int daysInMonth = yearMonth.lengthOfMonth();
        int row = 1;
        int column = startColumn;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = LocalDate.of(selectedYear, selectedMonth, day);
            double rateCompletion = getDailyCompletionRate(currentDate);
            StackPane calendarCell = createCalendarCell(day, rateCompletion);
            calendarGrid.add(calendarCell, column, row);
            column++;
            if (column > 6) {
                column = 0;
                row++;
            }
        }
    }

    private StackPane createCalendarCell(int day, double rate) {
        Canvas canvas = new Canvas(20, 50);
        drawVerticalProgressBar(canvas.getGraphicsContext2D(), rate, 20, 50);

        Label label = new Label(String.valueOf(day));
        VBox vBox = new VBox(label, canvas);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        StackPane cell = new StackPane(vBox);
        cell.setPrefSize(80, 80);
        cell.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-color: white;");
        return cell;
    }

    private void drawVerticalProgressBar(GraphicsContext gc, double rate, double width, double height) {
        int steps = 6;
        double[] thresholds = {0.0, 0.1, 0.3, 0.5, 0.7, 0.9, 1.0};
//        int steps = 5;
//        double[] thresholds = { 0.0, 0.2, 0.4, 0.6, 0.8, 1.0 };   
        int filledSteps = 0;
        for (int i = 1; i < thresholds.length; i++) {
            if (rate >= thresholds[i]) filledSteps++;
        }

        double stepHeight = height / steps;

        for (int i = 0; i < steps; i++) {
            double y = height - (i + 1) * stepHeight;
            gc.setFill(i < filledSteps ? Color.ROYALBLUE : Color.LIGHTGRAY);
            gc.fillRect(0, y, width, stepHeight - 1);
        }
    }
}
