package com.adil.ProjectMyHabits;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.property.*;

public abstract class Habit implements ITrackable {
    private StringProperty name;
    private BooleanProperty completed;
    private StringProperty extraData;
    private Set<LocalDate> completedDates;

    public Habit(String name) {
        this.name = new SimpleStringProperty(name);
        this.completed = new SimpleBooleanProperty(false);
        this.extraData = new SimpleStringProperty("");
        this.completedDates = new HashSet<>();
    }
    
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public boolean isCompleted() {
        return completed.get();
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    public String getExtraData() {
        return extraData.get();
    }

    public void setExtraData(String extraData) {
        this.extraData.set(extraData);
    }

    public StringProperty extraDataProperty() {
        return extraData;
    }

    public abstract String getType();
    
    @Override
    public void markCompletedOn(LocalDate date) {
    	this.completedDates.add(date);
    }
    
    @Override
    public void unmarkCompletedOn(LocalDate date) {
        this.completedDates.remove(date);
    }

    @Override
    public boolean isCompletedOn(LocalDate date) {
        return completedDates.contains(date);
    }

    @Override
    public double getCompletionRate(LocalDate date) {
        return isCompletedOn(date) ? 1.0 : 0.0;
    }
    
    @Override
    public Set<LocalDate> getCompletedDates() {
        return new HashSet<>(completedDates);
    }
}
