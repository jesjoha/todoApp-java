package com.example.todoapp.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {

    private StringProperty description;
    private BooleanProperty completed;

    public Task(String description) {
        descriptionProperty().set(description);
        completedProperty().set(false);
    }



    // getters and setters


    public String getDescription() {
        return descriptionProperty().get();
    }

    public void setDescription(String description) {
        descriptionProperty().set(description);
    }

    public StringProperty descriptionProperty() {
        if (description == null) description = new SimpleStringProperty(this, "description");
        return description;
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

    @Override
    public String toString() {
        return descriptionProperty().get();
    }
}
