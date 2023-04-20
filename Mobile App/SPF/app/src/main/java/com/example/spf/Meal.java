package com.example.spf;

import java.sql.Time;

public class Meal {
    int id;
    int doses;
    int hour;
    int min;

    public Meal(int id, int doses, int hour, int min) {
        this.id = id;
        this.doses = doses;
        this.hour = hour;
        this.min = min;
    }

}
