package com.example.taskmaster;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    private String roomname;
    private String username;

    private String permission;

    @GetMapping("/login")
    public String login() {
        return "Login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "SignUp";
    }

    @PostMapping("/room")
    public String room(@ModelAttribute UserHandler user, Model model) throws IOException, NoSuchAlgorithmException {
        System.out.println("/Login");
        System.out.println("user = " + user + ", model = " + model);
        Path fileLocation = Path.of("rooms/" + user.getRoomname() + "/" + user.getUsername() + ".task");

        if (Files.exists(fileLocation)) {
            if (PasswordEncryptor.encrypt(user.getPassword()).equals(FileManager.getFirstRow(user)[0])) {
                if (!Files.exists(Path.of("private_tasks/" + username + ".todo"))) {
                    Files.createFile(Path.of("private_tasks/" + username + ".todo"));
                }
                FileManager.createRoom(user);

                CustomLogger.logCustomInfo(user.getUsername() + " just logged in!");

                username = user.getUsername();
                try (BufferedWriter out = Files.newBufferedWriter(Path.of("var/currentUser.txt"))) {
                    out.write(username);
                }
                roomname = user.getRoomname();
                try (BufferedReader in = Files.newBufferedReader(fileLocation)) {
                    permission = in.readLine().split(";")[1];
                }
                System.out.println("permission = " + permission);

                model.addAttribute("username", username);
                model.addAttribute("roomname", roomname);
                model.addAttribute("permission", permission);
                model.addAttribute("usernameLetter", username.charAt(0));

                if (TaskManager.getTasks(username, roomname).size() == 0)
                    model.addAttribute("info_tasks", "' ' No room-tasks yet!");
                else model.addAttribute("tasks", TaskManager.getTasks(username, roomname));
                if (TodoManager.getTodos(username, roomname).size() == 0)
                    model.addAttribute("info_todos", "' ' No private tasks yet!");
                else model.addAttribute("todos", TodoManager.getTodos(username, roomname));

                if (!permission.equals("teacher")) {
                    return "Structure_Student";
                }
                return "Structure_Teacher";
            } else {
                model.addAttribute("wronglogin", "Login data is not valid!");

                return "Login";
            }
        } else {
            model.addAttribute("wronglogin", "Login data is not valid!");

            return "Login";
        }
        /*
        System.out.println("/room");
        System.out.println("user = " + user + ", model = " + model);
        CustomLogger.logCustomInfo(user.getUsername() + " just logged in!");
        Path fileLocation = Path.of("rooms/" + user.getRoomname() + "/" + user.getUsername() + ".task");

        if (Files.exists(fileLocation)) {
            if (PasswordEncryptor.encrypt(user.getPassword()).equals(FileManager.getFirstRow(user)[0])) {
                model.addAttribute("roomName", user.getRoomname());
                model.addAttribute("username", user.getUsername());
                model.addAttribute("usernameLetter", user.getUsername().charAt(0));
                model.addAttribute("permission", user.getPermission());

                if (TaskManager.getTasks(username, roomname).size() == 0)
                    model.addAttribute("info_tasks", "       No room-tasks yet!");
                else model.addAttribute("tasks", TaskManager.getTasks(username, roomname));
                if (TodoManager.getTodos(username, roomname).size() == 0)
                    model.addAttribute("info_todos", "       No private tasks yet!");
                else model.addAttribute("todos", TodoManager.getTodos(username, roomname));

                username = user.getUsername();
                roomname = user.getRoomname();

                try (BufferedReader in = Files.newBufferedReader(fileLocation)) {
                    permission = in.readLine().split(";")[1];
                }
                System.out.println("permission = " + permission);
                if (!permission.equals("teacher")) {
                    return "Structure_Student";
                }

                return "Structure_Teacher";
            } else {
                model.addAttribute("wronglogin", "Login data is not valid!");

                return "Login";
            }
        } else {
            model.addAttribute("wronglogin", "Login data is not valid!");

            return "Login";
        }
         */
    }

    @PostMapping("/Login")
    public String backtoLogin(@ModelAttribute UserHandler user, Model model) throws IOException, NoSuchAlgorithmException {
        System.out.println("/Login");
        System.out.println("user = " + user + ", model = " + model);
        Path fileLocation = Path.of("rooms/" + user.getRoomname() + "/" + user.getUsername() + ".task");
        if (Files.exists(fileLocation)) {
            model.addAttribute("wrongsignup", "This User already exists!");

            return "SignUp";
        } else {
            if (!Files.exists(Path.of("private_tasks/" + username + ".todo")) && username != null) {
                Files.createFile(Path.of("private_tasks/" + username + ".todo"));
                System.out.println("Just created the file: " + "private_tasks/" + username + ".todo");
            }
            FileManager.createRoom(user);

            if (!FileManager.createUserData(user)) {
                System.out.println("backToLogin(): An IOException got thrown.");
                throw new IOException("This User already exists!");
            }

            CustomLogger.logCustomInfo(user.getUsername() + " just signed up!");

            username = user.getUsername();
            try (BufferedWriter out = Files.newBufferedWriter(Path.of("var/currentUser.txt"))) {
                out.write(username);
            }
            roomname = user.getRoomname();
            try (BufferedReader in = Files.newBufferedReader(fileLocation)) {
                permission = in.readLine().split(";")[1];
            }
            System.out.println("permission = " + permission);

            model.addAttribute("username", username);
            model.addAttribute("roomname", roomname);
            model.addAttribute("permission", permission);
            model.addAttribute("usernameLetter", username.charAt(0));

            if (TaskManager.getTasks(username, roomname).size() == 0)
                model.addAttribute("info_tasks", "' ' No room-tasks yet!");
            else model.addAttribute("tasks", TaskManager.getTasks(username, roomname));
            if (TodoManager.getTodos(username, roomname).size() == 0)
                model.addAttribute("info_todos", "' ' No private tasks yet!");
            else model.addAttribute("todos", TodoManager.getTodos(username, roomname));

            if (!user.getPermission().equals("teacher")) {
                return "Structure_Student";
            }
            return "Structure_Teacher";
        }
    }

    @PostMapping("/update-after-addTask")
    public String addTask(Task task, Model model) throws IOException {
        List<File> txtfiles = FileManager.findTxtFiles("rooms/" + roomname);
        for (File tmp : txtfiles) {
            FileManager.deleteEmptyLines(tmp.getPath());
            TaskManager.writeTask(task, Path.of(tmp.getPath()));
        }
        FileManager.deleteEmptyLines("rooms/" + roomname + "/general.rtf");
        TaskManager.writeTask(task, Path.of("rooms/" + roomname + "/general.rtf"));
        model.addAttribute("roomName", roomname);
        model.addAttribute("username", username);
        model.addAttribute("permission", permission);
        model.addAttribute("usernameLetter", username.charAt(0));
        if (TaskManager.getTasks(username, roomname).size() == 0)
            model.addAttribute("info_tasks", "' ' No room-tasks yet!");
        else model.addAttribute("tasks", TaskManager.getTasks(username, roomname));
        if (TodoManager.getTodos(username, roomname).size() == 0)
            model.addAttribute("info_todos", "' ' No private tasks yet!");
        else model.addAttribute("todos", TodoManager.getTodos(username, roomname));
        CustomLogger.logCustomInfo(username + " just posted a new room-task(" + task + ")!");

        if (!permission.equals("teacher")) {
            return "Structure_Student";
        }
        return "Structure_Teacher";
    }

    @PostMapping("/update-after-addTodo")
    public String addTodo(Todo todo, Model model) throws IOException {
        FileManager.deleteEmptyLines("private_tasks/" + username + ".todo");
        TodoManager.writeTodo(todo, Path.of("private_tasks/" + username + ".todo"));

        model.addAttribute("roomName", roomname);
        model.addAttribute("username", username);

        model.addAttribute("usernameLetter", username.charAt(0));
        model.addAttribute("permission", permission);
        if (TaskManager.getTasks(username, roomname).size() == 0)
            model.addAttribute("info_tasks", "' ' No room-tasks yet!");
        else model.addAttribute("tasks", TaskManager.getTasks(username, roomname));
        if (TodoManager.getTodos(username, roomname).size() == 0)
            model.addAttribute("info_todos", "' ' No private tasks yet!");
        else model.addAttribute("todos", TodoManager.getTodos(username, roomname));
        CustomLogger.logCustomInfo(username + " just posted a new private task(" + todo + ")!");

        /*
        try (BufferedReader in = Files.newBufferedReader(Path.of(String.format("rooms/%s/%s.task", roomname, username)))) {
            String line = in.readLine();

            if (line.split(";")[1].equals("teacher")) {
                return "Structure_Teacher";
            }
        }
         */

        if (!permission.equals("teacher")) {
            return "Structure_Student";
        }

        return "Structure_Teacher";
    }

    @PostMapping("get-back")
    public String getbackfromStructure() {
        return "Login";
    }

    @GetMapping("about")
    public String about() {
        System.out.println("Controller.about");
        return "About";
    }

    @GetMapping("ignore-expired-tasks")
    public String ignoreExpiredTasks(Model model) throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(Path.of("var/showAll.txt"))) {
            out.write("0");
        }

        model.addAttribute("roomName", roomname);
        model.addAttribute("username", username);
        model.addAttribute("permission", permission);
        model.addAttribute("usernameLetter", username.charAt(0));
        if (TaskManager.getTasks(username, roomname).size() == 0)
            model.addAttribute("info_tasks", "' ' No room-tasks yet!");
        else model.addAttribute("tasks", TaskManager.getTasks(username, roomname));
        if (TodoManager.getTodos(username, roomname).size() == 0)
            model.addAttribute("info_todos", "' ' No private tasks yet!");
        else model.addAttribute("todos", TodoManager.getTodos(username, roomname));

        if (!permission.equals("teacher")) {
            return "Structure_Student";
        }
        return "Structure_Teacher";
    }

    @GetMapping("show-all-tasks")
    public String showAllTasks(Model model) throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(Path.of("var/showAll.txt"))) {
            out.write("1");
        }

        model.addAttribute("roomName", roomname);
        model.addAttribute("username", username);
        model.addAttribute("permission", permission);
        model.addAttribute("usernameLetter", username.charAt(0));
        if (TaskManager.getTasks(username, roomname).size() == 0)
            model.addAttribute("info_tasks", "' ' No room-tasks yet!");
        else model.addAttribute("tasks", TaskManager.getTasks(username, roomname));
        if (TodoManager.getTodos(username, roomname).size() == 0)
            model.addAttribute("info_todos", "' ' No private tasks yet!");
        else model.addAttribute("todos", TodoManager.getTodos(username, roomname));

        if (!permission.equals("teacher")) {
            return "Structure_Student";
        }
        return "Structure_Teacher";
    }
}