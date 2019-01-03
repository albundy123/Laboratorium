package controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import dbUtil.dbSqlite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.userModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class userViewController {
    public userViewController() {System.out.println("Siemanko jestem konstruktorem klasy userViewController.");}


    @FXML
    private TableView<userModel> userTableView;
    @FXML
    private TableColumn<userModel, Integer> idUserColumn;
    @FXML
    private TableColumn<userModel, String> firstNameColumn;
    @FXML
    private TableColumn<userModel, String> lastNameColumn;
    @FXML
    private TableColumn<userModel, String> loginColumn;
    @FXML
    private TableColumn<userModel, String> passwordColumn;
    @FXML
    private TableColumn<userModel, String> permissionLevelColumn;
    @FXML
    private Button addUserButton;
    @FXML
    private Button editUserButton;

    private List<userModel> userList;
    private ObservableList<userModel> userObservableList = FXCollections.observableArrayList();
    private userModel editedUserFromList;
    public void setEditedUserFromList(userModel editedUserFromList) {
        this.editedUserFromList = editedUserFromList;
    }
    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy userViewController.");
        initializeTableView();
        getUsers();
    }

    @FXML
    private void addUser(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/users/newUserView.fxml"));
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Dodaj nowego użytkownika");
            VBox vBox = loader.load();
            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void editUser(){
        if(editedUserFromList!=null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/users/editUserView.fxml"));
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Edytuj wybranego użytkownika");
                VBox vBox = loader.load();
                editUserViewController editedUserController = loader.getController();
                editedUserController.setEditedUser(editedUserFromList);
                System.out.println("User "+ editedUserFromList.getFirstName());
                System.out.println("User "+  editedUserController.getEditedUser().getFirstName());
                Scene scene = new Scene(vBox);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void getUsers(){
        try {
            userObservableList.clear();
            Dao<userModel, Integer> przyrzadDao = DaoManager.createDao(dbSqlite.getConnectionSource(),userModel.class);
            List<userModel> userList = przyrzadDao.queryForAll();
            userList.forEach(user ->{
                userObservableList.add(user);
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void initializeTableView(){
        idUserColumn.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        permissionLevelColumn.setCellValueFactory(new PropertyValueFactory<>("persmissionLevel"));
        userTableView.setItems(userObservableList);
        userTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setEditedUserFromList(newValue));
    }
}
