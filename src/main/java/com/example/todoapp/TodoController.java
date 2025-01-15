package com.example.todoapp;

import com.example.todoapp.model.ToDoList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class TodoController {
    @FXML
    private ListView<ToDoList> toDoListListView;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}