package com.adil.ProjectMyHabits;

import java.time.LocalDate;
import java.util.Set;

public interface ITrackable {
    void markCompletedOn(LocalDate date);
    void unmarkCompletedOn(LocalDate date);
    boolean isCompletedOn(LocalDate date);
    double getCompletionRate(LocalDate date);
    Set<LocalDate> getCompletedDates();
}
