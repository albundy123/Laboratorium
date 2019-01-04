package controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.userModel;

import java.sql.SQLException;

public class dialogUserViewController {
    public dialogUserViewController() {System.out.println("Siemanko jestem konstruktorem klasy dialogUserViewController.");}

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
    private Button saveUserButton;
    @FXML
    private Button cancelSaveUserButton;
    @FXML
    private Label userLabel;
    @FXML
    private VBox mainVBox;


    //Potrzebny do przekazania id miedzy oknami dla nowego usera bedzie tutaj null dla edytowanego bedzie idUser z tablicy USERS
    private Integer idEditedUser;
    private userViewController mainUserController;
    public void setMainUserController(userViewController mainUserController) {
        this.mainUserController = mainUserController;
    }
    //Setter i getter
    public void setIdEditedUser(Integer idEditedUser) {
        this.idEditedUser = idEditedUser;
    }
    public void setFirstNameTextField(String firstNameTextField) {
        this.firstNameTextField.setText(firstNameTextField);
    }
    //Settery textField i comboBox
    public void setLastNameTextField(String lastNameTextField) {
        this.lastNameTextField.setText(lastNameTextField);
    }
    public void setLoginTextField(String loginTextField) {
        this.loginTextField.setText(loginTextField);
    }
    public void setPasswordTextField(String passwordTextField) {
        this.passwordTextField.setText(passwordTextField);
    }
    public void setPermissionComboBox(String permissionComboBox) {
        this.permissionComboBox.setValue(permissionComboBox);
    }
    public void setUserLabel(String userLabel) {
        this.userLabel.setText(userLabel);
    }

    //Funkcja initialize wykonuje się po konstruktorze
    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy dialogUserViewController.");
        permissionComboBox.getItems().addAll("admin","user");
    }
    @FXML
    private void saveUser(){
        if(idEditedUser !=null){
            System.out.println("Edytujemy");
            editUser();
        }else{
            System.out.println("Dodajemy");
            addNewUser();
        }
    }
    @FXML
    private void cancelSaveUser(){
        Stage window = (Stage) mainVBox.getScene().getWindow();
        window.close();
    }
    private void addNewUser(){
        if(isValidUserData()){
            try {
                Dao<userModel, Integer> userDao = DaoManager.createDao(dbSqlite.getConnectionSource(),userModel.class);

                userDao.create(getUser());
                Stage window = (Stage) mainVBox.getScene().getWindow();
                window.close();
                mainUserController.getUsers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void editUser(){
        if(isValidUserData()){
            try {
                Dao<userModel, Integer> userDao = DaoManager.createDao(dbSqlite.getConnectionSource(),userModel.class);
                userDao.update(getUser());
                Stage window = (Stage) mainVBox.getScene().getWindow();
                window.close();
                mainUserController.getUsers();
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
            errorMessage += "Nie wybrałeś poziomu dostępu ! \n";
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
        userModel user=new userModel(idEditedUser,firstNameTextField.getText(),lastNameTextField.getText(),loginTextField.getText(),passwordTextField.getText(),permissionComboBox.getValue());
        return user;
    }
}
