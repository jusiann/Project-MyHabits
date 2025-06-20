package com.adil.ProjectMyHabits;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class HabitJsonModel {
	public String className;
	public String name;
	public String detail;
	public boolean isCompleted;
	public Set<LocalDate> completedDates = new HashSet<>();
}
