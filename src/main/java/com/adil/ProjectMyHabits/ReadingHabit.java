package com.adil.ProjectMyHabits;

public class ReadingHabit extends Habit {
    public ReadingHabit(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "Reading";
    }
}
