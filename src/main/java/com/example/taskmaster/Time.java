package com.example.taskmaster;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Time {

    public static String getDate() {
        return String.valueOf(java.time.LocalDate.now());
    }
}
