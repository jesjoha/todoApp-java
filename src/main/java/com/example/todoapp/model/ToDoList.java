package com.example.todoapp.model;

import java.util.ArrayList;
import java.util.List;

public class ToDoList {

    private List<Task> toDoList;
    private String title;
    private String description;

    public ToDoList(String title, String description) {
        this.title = title;
        this.description = description;
        toDoList = new ArrayList<>();
    }

    // Getters and setters

    public List<Task> getToDoList() {
        return toDoList;
    }

    public void setToDoList(List<Task> toDoList) {
        this.toDoList = toDoList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method for adding a task to the list.
     * @param task The task to be added.
     */
    public void addTask(Task task) {
        toDoList.add(task);
    }

    /**
     * Method for removing a task from the list.
     * @param task The task to be removed.
     */
    public void removeTask(Task task) {
        toDoList.remove(task);
    }

    /**
     * Method for setting a task as completed.
     * @param task The task to mark as completed.
     */
    public void markTaskAsCompleted(Task task) {
        task.setCompleted();
    }


}
