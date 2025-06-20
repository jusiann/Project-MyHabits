package com.adil.ProjectMyHabits;

import java.time.LocalDate;
import java.util.*;

public class HabitListManager {
    private static final Map<String, HabitListManager> instances = new HashMap<>();
    private final Map<LocalDate, List<Habit>> habitMapByDate;
    private final String username;

    private HabitListManager(String username) {
        this.username = username;
        this.habitMapByDate = new HashMap<>();
    }

    public static HabitListManager getInstance(String username) {
        return instances.computeIfAbsent(username, HabitListManager::new);
    }

    public List<Habit> getHabitsForDate(LocalDate date) {
        return habitMapByDate.getOrDefault(date, new ArrayList<>());
    }

    public void setHabitsForDate(LocalDate date, List<Habit> habits) {
        habitMapByDate.put(date, habits);
    }

    public List<Habit> getTodayHabits() {
        return getHabitsForDate(LocalDate.now());
    }

    public void setTodayHabits(List<Habit> habits) {
        setHabitsForDate(LocalDate.now(), habits);
    }

    public Map<LocalDate, List<Habit>> getAllHabitData() {
        return habitMapByDate;
    }

    public void setAllHabitData(Map<LocalDate, List<Habit>> habitData) {
        habitMapByDate.clear();
        habitMapByDate.putAll(habitData);
    }

    public void deleteAllUserHabitData() {
        habitMapByDate.clear();
    }
}
