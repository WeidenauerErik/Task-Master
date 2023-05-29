package com.example.taskmaster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

public class TaskManager {

    private static final List<Task> tasks = new LinkedList<>();

    public static List<Task> getTasks(UserHandler user) throws IOException {
        tasks.clear();

        Path fileLocation = Path.of("rooms/" + user.getRoomname() + "/" + user.getUsername() + ".task");

        try (BufferedReader in = Files.newBufferedReader(fileLocation, StandardCharsets.UTF_8)) {
            String line;
            in.readLine();

            while ((line = in.readLine()) != null) {
                String title = line;
                String date = in.readLine();
                String info = in.readLine();
                String author = in.readLine();
                try (BufferedReader in2 = Files.newBufferedReader(Path.of("var/showAll.txt"))) {
                    if (in2.readLine().equals("0")) {
                        if (isValidDate(date)) {
                            tasks.add(new Task(title, date, info, author, false));
                        }
                    } else {
                        tasks.add(new Task(title, date, info, author, false));
                    }
                }
            }
        }

        sortTasks();

        return tasks;
    }

    public static List<Task> getTasks(String username, String roomname) throws IOException {
        System.out.println("username = " + username + ", roomname = " + roomname);
        tasks.clear();

        Path fileLocation = Path.of("rooms/" + roomname + "/" + username + ".task");

        try (BufferedReader in = Files.newBufferedReader(fileLocation, StandardCharsets.UTF_8)) {
            String line;
            in.readLine();

            while ((line = in.readLine()) != null) {
                String title = line;
                String date = in.readLine();
                String info = in.readLine();
                String author = in.readLine();
                try (BufferedReader in2 = Files.newBufferedReader(Path.of("var/showAll.txt"))) {
                    if (in2.readLine().equals("0")) {
                        if (isValidDate(date)) {
                            tasks.add(new Task(title, date, info, author, false));
                        }
                    } else {
                        tasks.add(new Task(title, date, info, author, false));
                    }
                }
            }
        }

        sortTasks();

        return tasks;
    }

    private static boolean isValidDate(String date) {
        // YEAR-MONTH-DAY
        String[] currentDate = Time.getDate().split("-");
        String[] taskDate = date.split("-");

        if (Integer.parseInt(taskDate[0]) >= Integer.parseInt(currentDate[0])) {
            if (Integer.parseInt(taskDate[1]) >= Integer.parseInt(currentDate[1])) {
                return Integer.parseInt(taskDate[2]) >= Integer.parseInt(currentDate[2]);
            }
        }

        return false;
    }

    private static void sortTasks() {
        if (tasks.size() > 1) {
            tasks.sort((o1, o2) -> {
                String[] date1, date2;
                try {
                    date1 = o1.getDeadline().split("-");
                } catch (NullPointerException e) {
                    return 1;
                }
                try {
                    date2 = o2.getDeadline().split("-");
                } catch (NullPointerException e) {
                    return -1;
                }

                if (date1.length < 2) {
                    return 1;
                } else if (date2.length < 2) {
                    return -1;
                }

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
    }

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static void writeTask(Task task, Path fileLocation) {
        try (BufferedWriter out = Files.newBufferedWriter(fileLocation, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            out.write(task.getTitle() + System.lineSeparator());
            out.write(task.getDeadline() + System.lineSeparator());
            out.write(task.getInfo() + System.lineSeparator());
            out.write(task.getAuthor() + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentUser() throws IOException {
        try (BufferedReader in = Files.newBufferedReader(Path.of("var/currentUser.txt"))) {
            return in.readLine();
        }
    }

}
