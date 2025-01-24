package com.example.todoapp;

import com.example.todoapp.model.ToDoList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TodoController {


    private List<ToDoList> listOfLists = new ArrayList<>();
    ObservableList<ToDoList> observableToDoList;

    @FXML
    private ListView<ToDoList> toDoListListView = new ListView<>();

    @FXML
    private TextField textField;

    @FXML
    protected void addNewList() {

        String listTitle = getNewListName();
        if (listTitle != null) {
            ToDoList newList = new ToDoList(listTitle);
            listOfLists.add(newList);
            System.out.println(listOfLists.size());
            updateListView();
        }

    }

    @FXML
    protected void removeList() {
        ToDoList chosenList = toDoListListView.getSelectionModel().getSelectedItem();
        if (chosenList != null) {
            listOfLists.remove(chosenList);
            System.out.println(listOfLists.size());
            updateListView();
        }
    }

    @FXML
    protected void editListTitle() {
        ToDoList chosenList = toDoListListView.getSelectionModel().getSelectedItem();
        if (chosenList != null) {
            String newTitle = editListTitle(chosenList);
            chosenList.setTitle(newTitle);
            updateListView();
        }
    }

    private String getNewListName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New List");
        dialog.setHeaderText("Add New List");
        dialog.setContentText("Please enter the title of the list you would like to create");
        Optional<String> result = dialog.showAndWait();
        textField.setText(result.orElse(""));
        String resultString = dialog.getResult();
        if (resultString != null && !result.get().isEmpty()) {
            return result.get();
        }
        return null;
    }

    private String editListTitle(ToDoList list) {
        TextInputDialog dialog = new TextInputDialog(list.getTitle());
        dialog.setTitle("Edit Title");
        dialog.setHeaderText("Edit Title");
        dialog.setContentText("Please enter the new title of the list");
        Optional<String> result = dialog.showAndWait();
        String resultString = dialog.getResult();
        if (resultString == null || result.get().isEmpty()) {
            return list.getTitle();
        }
        return result.get();

    }

    private void updateListView() {
        observableToDoList = FXCollections.observableArrayList(listOfLists);
        toDoListListView.setItems(observableToDoList);
        toDoListListView.refresh();
    }
}