package com.example.todoapp;

import com.example.todoapp.model.Task;
import com.example.todoapp.model.ToDoList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TodoController {


    private List<ToDoList> listOfLists = new ArrayList<>();

    @FXML
    private ListView<ToDoList> toDoListListView = new ListView<>();

    @FXML
    private ListView<Task> taskList;

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
            updateTaskList(newList);
        }

    }

    @FXML
    protected void removeList() {
        ToDoList chosenList = toDoListListView.getSelectionModel().getSelectedItem();
        if (chosenList != null) {
            listOfLists.remove(chosenList);
            System.out.println(listOfLists.size());
            updateListView();
            taskList.getItems().clear();
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

    @FXML
    protected void createTask() {
        String taskDescription = getTaskDescription();
        ToDoList activeList = toDoListListView.getSelectionModel().getSelectedItem();
        if (activeList != null && taskDescription != null) {
            Task newTask = new Task(taskDescription);
            activeList.addTask(newTask);
            updateTaskList(activeList);
        }
    }

    @FXML
    protected void removeTask() {
        ToDoList activeList = toDoListListView.getSelectionModel().getSelectedItem();
        if (activeList != null) {
            Task selectedTask = taskList.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                activeList.removeTask(selectedTask);
                updateTaskList(activeList);
            }
        }
    }

    @FXML
    protected void editTask() {
        Task selectedTask = taskList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            String newDescription = editTaskDescription(selectedTask);
            selectedTask.setDescription(newDescription);
            updateTaskList(toDoListListView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    protected void moveDown() {
        ToDoList activeList = toDoListListView.getSelectionModel().getSelectedItem();
        Task selectedTask = taskList.getSelectionModel().getSelectedItem();
        int taskIndex = activeList.getToDoList().indexOf(selectedTask);

        if (selectedTask != null &&  taskIndex < activeList.getToDoList().size()-1) {
            Task nextTask = activeList.getToDoList().remove(taskIndex+1);
            Task placeholder = activeList.getToDoList().remove(taskIndex);
            activeList.getToDoList().add(taskIndex, nextTask);
            activeList.getToDoList().add(taskIndex+1, placeholder);
            updateTaskList(activeList);
        }
    }

    @FXML
    protected void moveUp() {
        ToDoList activeList = toDoListListView.getSelectionModel().getSelectedItem();
        Task selectedTask = taskList.getSelectionModel().getSelectedItem();
        int taskIndex = activeList.getToDoList().indexOf(selectedTask);
        if (selectedTask != null &&  taskIndex > 0) {
            Task placeholder = activeList.getToDoList().remove(taskIndex);
            Task taskAbove = activeList.getToDoList().remove(taskIndex-1);
            activeList.getToDoList().add(taskIndex-1,placeholder);
            activeList.getToDoList().add(taskIndex, taskAbove);
            updateTaskList(activeList);
        }
    }

    @FXML
    protected void saveDefault() {
        ToDoList selectedList = toDoListListView.getSelectionModel().getSelectedItem();
        if (selectedList != null) {
            updateTaskList(selectedList);
        }
        try {
            String fileName = "default.ser";
            String filePath = Paths.get(System.getProperty("user.home"), "Documents",fileName).toString();
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(listOfLists);
            out.close();
            file.close();
            System.out.println("The lists have been saved.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void initialize() {
        toDoListListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoList>() {
            @Override
            public void changed(ObservableValue<? extends ToDoList> observableValue, ToDoList previouslySelectedList, ToDoList newlySelectedList) {
                updateTaskList(newlySelectedList);
                System.out.println("Selected list: " + newlySelectedList);
            }
        });
        loadFromFile();
        updateListView();

    }

    /**
     * Method for getting the new list name from the user.
     * @return returns the new list name if entered. Else return null.
     */
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

    /**
     * Method for editing the list title with a user specified title.
     * @param list the list which title the user wants to edit.
     * @return the edited title. If left unedited the old title is returned.
     */
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

    private String getTaskDescription() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Task");
        dialog.setHeaderText("Add Task");
        dialog.setContentText("Please enter the description of the task you would like to add");
        Optional<String> result = dialog.showAndWait();
        String resultString = dialog.getResult();
        if (resultString == null || result.get().isEmpty()) {
            return null;
        }
        return result.get();
    }

    private String editTaskDescription(Task task) {
        TextInputDialog dialog = new TextInputDialog(task.getDescription());
        dialog.setTitle("Edit Task");
        dialog.setHeaderText("Edit Task");
        dialog.setContentText("Please enter the description of the task you would like to edit");
        Optional<String> result = dialog.showAndWait();
        String resultString = dialog.getResult();
        if (resultString == null || result.get().isEmpty()) {
            return null;
        }
        return result.get();
    }

    private void updateListView() {
        ObservableList<ToDoList> observableToDoList = FXCollections.observableArrayList(listOfLists);
        toDoListListView.setItems(observableToDoList);
        toDoListListView.refresh();
    }

    private void updateTaskList(ToDoList list) {
        if (list != null) {
            ObservableList<Task> observableTaskList = FXCollections.observableArrayList(list.getToDoList());
            checkBoxClick();
            taskList.setItems(observableTaskList);
//            for (Task task : list.getToDoList()) {
//                System.out.println(task.getDescription()+ " - Completed: " + task.isCompleted() + " -- done: " + task.isDone());
//            }
        }

    }

    private void checkBoxClick() {
        taskList.setCellFactory(CheckBoxListCell.forListView(new Callback<Task, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Task task) {
                BooleanProperty obs = new SimpleBooleanProperty();
                obs.addListener((observable, wasSelected, isNowSelected) ->
                        System.out.println("Checkbox for "+observable+" changed from "+wasSelected+ " to " + isNowSelected));

                task.updateDone();

                return task.completedProperty();
            }
        }));
    }

    private void loadFromFile() {
        String fileName = "default.ser";
        String filePath = Paths.get(System.getProperty("user.home"), "Documents",fileName).toString();

        try {
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);

            listOfLists = (List<ToDoList>) in.readObject();
            in.close();
            file.close();

            for (ToDoList list : listOfLists) {
                for (Task task : list.getToDoList()) {
                    task.setCompleted(task.isDone());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}