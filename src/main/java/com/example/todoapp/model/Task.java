package com.example.todoapp.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serial;
import java.io.Serializable;

public class Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String description;
    private transient BooleanProperty completed;

    private boolean done;


    public Task(String description) {
        this.description = description;
        completedProperty().set(false);
        done = false;
    }



    // getters and setters


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isCompleted() {
        return completedProperty().get();
    }

    public void setCompleted(boolean completed) {
        completedProperty().set(completed);
    }

    public BooleanProperty completedProperty() {
        if (completed == null) completed = new SimpleBooleanProperty(this, "completed");
        return completed;
    }

    public void updateDone() {
        this.done = completed.get();
    }
    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return description;
    }
}
