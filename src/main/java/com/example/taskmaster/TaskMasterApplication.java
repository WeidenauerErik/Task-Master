package com.example.taskmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class TaskMasterApplication {
    public static void main(String[] args) throws IOException {
        // wenn man die Terminal Ausgabe in einer Datei haben will:
        try (BufferedWriter out = Files.newBufferedWriter(Path.of("var/showAll.txt"))) {
            out.write("1");
        }

        TerminalReader.TerminalToFile("Terminal/out.txt");
        SpringApplication app = new SpringApplication(TaskMasterApplication.class);
        app.run(args);
    }
}
