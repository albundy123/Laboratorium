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
    public userViewController() {}

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

    private ObservableList<userModel> userObservableList = FXCollections.observableArrayList();

    private userModel editedUserFromList;
    private dialogUserViewController editedUserController;

    public userModel getEditedUserFromList() {
        return editedUserFromList;
    }

    public void setEditedUserFromList(userModel editedUserFromList) {
        this.editedUserFromList = editedUserFromList;
    }

    @FXML
    private void initialize(){
        initializeTableView();
        getUsers();
    }
    @FXML
    private void addUser(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/dialogUserView.fxml"));
            VBox vBox = loader.load();
            editedUserController = loader.getController();
            if (editedUserController != null){
                editedUserController.setMainUserController(this);
            }
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Dodaj nowego użytkownika");
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/dialogUserView.fxml"));
                VBox vBox = loader.load();
                editedUserController = loader.getController();
                if (editedUserController != null){
                    editedUserController.setMainUserController(this);
                    loadEditDialogView(editedUserFromList);
                }
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Edytuj wybranego użytkownika");
                Scene scene = new Scene(vBox);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void getUsers(){
        try {
            userObservableList.clear();
            Dao<userModel, Integer> userDao = DaoManager.createDao(dbSqlite.getConnectionSource(),userModel.class);
            List<userModel> userList = userDao.queryForAll();
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
    private void loadEditDialogView(userModel user){
        editedUserController.setFirstNameTextField(user.getFirstName());
        editedUserController.setLastNameTextField(user.getLastName());
        editedUserController.setLoginTextField(user.getLogin());
        editedUserController.setPasswordTextField(user.getPassword());
        editedUserController.setPermissionComboBox(user.getPersmissionLevel());
        editedUserController.setIdEditedUser(user.getIdUser());
        editedUserController.setUserLabel("UŻYTKOWNIK");
    }
}
