package com.example.taskmaster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class TodoManager {

    public static List<Todo> todos = new ArrayList<>();

    public static List<Todo> getTodos(UserHandler user) throws IOException {
        todos.clear();

        Path fileLocation = Path.of("private_tasks/" + user.getUsername() + ".todo");

        try (BufferedReader in = Files.newBufferedReader(fileLocation, StandardCharsets.UTF_8)) {
            String line;

            while ((line = in.readLine()) != null) {
                String title = line;
                String date = in.readLine();
                String info = in.readLine();
                try (BufferedReader in2 = Files.newBufferedReader(Path.of("var/showAll.txt"))) {
                    if (in2.readLine().equals("0")) {
                        if (isValidDate(date)) {
                            todos.add(new Todo(title, date, info));
                        }
                    } else {
                        todos.add(new Todo(title, date, info));
                    }
                }
            }
        }

        sortTodos();

        return todos;
    }

    public static List<Todo> getTodos(String username, String roomname) throws IOException {
        todos.clear();

        Path fileLocation = Path.of("private_tasks/" + username + ".todo");
        if (!Files.exists(fileLocation)) {
            Files.createFile(fileLocation);
        }

        try (BufferedReader in = Files.newBufferedReader(fileLocation)) {
            String line;

            while ((line = in.readLine()) != null) {
                String title = line;
                String date = in.readLine();
                String info = in.readLine();
                try (BufferedReader in2 = Files.newBufferedReader(Path.of("var/showAll.txt"))) {
                    if (in2.readLine().equals("0")) {
                        if (isValidDate(date)) {
                            todos.add(new Todo(title, date, info));
                        }
                    } else {
                        todos.add(new Todo(title, date, info));
                    }
                }
            }
        }

        sortTodos();

        return todos;
    }

    private static boolean isValidDate(String date) {
        // YEAR-MONTH-DAY
        String[] currentDate = Time.getDate().split("-");
        String[] taskDate = date.split("-");

        if (Integer.parseInt(taskDate[0]) >= Integer.parseInt(currentDate[0])) {
            if (Integer.parseInt(taskDate[1]) >= Integer.parseInt(currentDate[1])) {
                if (Integer.parseInt(taskDate[2]) >= Integer.parseInt(currentDate[2])) {
                    return true;
                }
            }
        }

        return false;
    }
    private static void sortTodos() {
        if (todos.size() > 1) {
            todos.sort((o1, o2) -> {
                String[] date1 = o1.getDeadline().split("-");
                String[] date2 = o2.getDeadline().split("-");

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

    public static void addTodo(Todo todo) {
        todos.add(todo);
    }

    public static void writeTodo(Todo todo, Path fileLocation) {
        try (BufferedWriter out = Files.newBufferedWriter(fileLocation, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            out.write(todo.getTitle() + System.lineSeparator());
            out.write(todo.getDeadline() + System.lineSeparator());
            out.write(todo.getInfo() + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
