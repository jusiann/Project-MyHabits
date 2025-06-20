package com.adil.ProjectMyHabits;

public class WalkingHabit extends Habit {
    public WalkingHabit(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "Walking";
    }
}
