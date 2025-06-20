package com.adil.ProjectMyHabits;

public class FoodHabit extends Habit{
	public FoodHabit(String name) {
		super(name);
	}
	
	@Override
	public String getType() {
		return "Food";
	}
}
