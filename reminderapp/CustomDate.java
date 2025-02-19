package com.example.reminderapp;

import java.io.Serializable;
import java.util.Objects;

/*
CustomDate class implements Serializable and enables saving custom dates to a file
this class overrides the @equals and @hashcode methods.
 */
public class CustomDate implements Serializable {
    private int year;
    private int month;
    private int day;
    public CustomDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof CustomDate other
                && this.year == other.year
                && this.month == other.month
                && this.day == other.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.year, this.month, this.day);
    }

}
