package com.example.taskmaster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class TaskManager {

    private static List<Task> tasks = new LinkedList<>();

    public static List<Task> getTasks(UserHandler user) throws IOException {
        tasks.clear();

        Path fileLocation = Path.of("rooms/" + user.getRoomname() + "/" + user.getUsername() + ".task");

        try (BufferedReader in = Files.newBufferedReader(fileLocation, StandardCharsets.UTF_8)) {
            String line;
            in.readLine();

            while ((line = in.readLine()) != null) {
                tasks.add(new Task(line, in.readLine(), in.readLine()));
            }
        }

        sortTasks();

        return tasks;
    }

    public static List<Task> getTasks(String username, String roomname) throws IOException {
        tasks.clear();

        Path fileLocation = Path.of("rooms/" + roomname + "/" + username + ".task");

        try (BufferedReader in = Files.newBufferedReader(fileLocation, StandardCharsets.UTF_8)) {
            String line;
            in.readLine();

            while ((line = in.readLine()) != null) {
                tasks.add(new Task(line, in.readLine(), in.readLine()));
            }
        }

        sortTasks();

        return tasks;
    }

    private static void sortTasks() {
        tasks.sort((o1, o2) -> {
            String[] date1 = o1.getDeadline().split("-");
            String[] date2 = o2.getDeadline().split("-");

            if (Integer.parseInt(date1[2]) < Integer.parseInt(date2[2])) {
                return -1;
            } else if (Integer.parseInt(date1[1]) < Integer.parseInt(date2[1])) {
                return -1;
            } else if (Integer.parseInt(date1[0]) < Integer.parseInt(date2[0])) {
                return -1;
            } else {
                return 1;
            }
        });
    }

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static void writeTask(Task task, Path fileLocation) {
        try (BufferedWriter out = Files.newBufferedWriter(fileLocation, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            out.write(task.getTitle() + System.lineSeparator());
            out.write(task.getDeadline() + System.lineSeparator());
            out.write(task.getInfo() + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
