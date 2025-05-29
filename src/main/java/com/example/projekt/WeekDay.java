package com.example.projekt;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum WeekDay {
    MONDAY(1, "Monday"),
    TUESDAY(2, "Tuesday"),
    WEDNESDAY(3, "Wednesday"),
    THURSDAY(4, "Thursday"),
    FRIDAY(5, "Friday"),
    SATURDAY(6, "Saturday"),
    SUNDAY(7, "Sunday");

    private final int number;
    private final String displayName;

    WeekDay(int number, String displayName) {
        this.number = number;
        this.displayName = displayName;
    }

    public static WeekDay fromNumber(int number) {
        return Arrays.stream(WeekDay.values()).filter(e -> e.number == number).findFirst().orElse(null);
    }
}
