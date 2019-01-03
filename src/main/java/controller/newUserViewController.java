package controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.userModel;

import java.sql.SQLException;

public class newUserViewController {
    public newUserViewController() {System.out.println("Siemanko jestem konstruktorem klasy newUserViewController.");}

    //Wstrzyknięcie pól z widoku, żeby można było na nich operować
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private ComboBox<String> permissionComboBox;
    @FXML
    private Button addNewUserButton;
    @FXML
    private Button cancelAddNewUserButton;

    private String funkcjaOkna;
    //Funkcja initialize wykonuje się po konstruktorze
    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy newUserViewController.");
        permissionComboBox.getItems().addAll("admin","user");
    }

    @FXML
    private void addNewUser(){
        if(isValidUserData()){
            try {
                Dao<userModel, Integer> userDao = DaoManager.createDao(dbSqlite.getConnectionSource(),userModel.class);
                userDao.create(getUser());
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isValidUserData() {
        String errorMessage = "";
        if (firstNameTextField.getText() == null || firstNameTextField.getText().length() == 0) {
            errorMessage += "Nie wprowadziłeś prawidłowo imienia ! \n";
        }
        if (lastNameTextField.getText() == null || lastNameTextField.getText().length() == 0) {
            errorMessage += "Nie wprowadziłeś prawidłowo nazwiska ! \n";
        }
        if (loginTextField.getText() == null || loginTextField.getText().length() == 0) {
            errorMessage += "Nie wprowadziłeś prawidłowo loginu ! \n";
        }
        if (passwordTextField.getText() == null || passwordTextField.getText().length() == 0) {
            errorMessage += "Nie wprowadziłeś prawidłowo hasła ! \n";
        }
        if (permissionComboBox.getValue() == null ) {
            errorMessage += "Nie poziomu dostępu ! \n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            //alert.initOwner(dialogStage);
            alert.setTitle("Nieprawidłowe dane");
            alert.setHeaderText("Proszę wprowadzić prawidłowe dane dla podanych niżej pól");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
    private userModel getUser(){
        userModel user=new userModel(0,firstNameTextField.getText(),lastNameTextField.getText(),loginTextField.getText(),passwordTextField.getText(),permissionComboBox.getValue());
        return user;
    }
}
