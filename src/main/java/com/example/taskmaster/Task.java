package com.example.taskmaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Task {
	private String title;
	private String deadline;
	private String info;

	private String author;

	public Task(String title, String deadline, String info, String author, Boolean newTask) throws IOException {
		this.title = title;
		this.deadline = deadline;
		this.info = info;
		this.author = newTask == null ? getCurrentUser() : author;
	}

	private String getCurrentUser() throws IOException {
		try (BufferedReader in = Files.newBufferedReader(Path.of("var/currentUser.txt"))) {
			return in.readLine();
		}
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "[" + title + "][" +deadline + "][" + info + "][" + author + "]";
	}
}
