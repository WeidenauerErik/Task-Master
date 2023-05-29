package com.example.taskmaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Todo {
    private String title;
    private String deadline;
    private String info;

    public Todo(String title, String deadline, String info) throws IOException {
        this.title = title;
        this.deadline = deadline;
        this.info = info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "[" + title + "][" +deadline + "][" + info + "]";
    }
}
