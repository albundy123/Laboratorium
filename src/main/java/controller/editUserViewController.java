package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.userModel;

public class editUserViewController {
    public editUserViewController() {System.out.println("Siemanko jestem konstruktorem klasy editUserViewController.");}

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
    private Button saveEditUserButton;
    @FXML
    private Button cancelEditNewUserButton;
    //Potrzebny do przekazania danych miedzy oknami
    private userModel editedUser;
    public userModel getEditedUser() {
        return editedUser;
    }
    public void setEditedUser(userModel editedUser) {
        this.editedUser = editedUser;
    }


    //Funkcja initialize wykonuje się po konstruktorze
    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy editUserViewController.");
        permissionComboBox.getItems().addAll("admin","user");
        //initializeEditedUser();
        //System.out.println("User "+ editedUser.getFirstName());
    }
    private userModel getUser(){
        userModel user=new userModel(editedUser.getIdUser(),firstNameTextField.getText(),lastNameTextField.getText(),loginTextField.getText(),passwordTextField.getText(),permissionComboBox.getValue());
        return user;
    }
    private void initializeEditedUser(){
        firstNameTextField.setText(editedUser.getFirstName());
        lastNameTextField.setText(editedUser.getLastName());
        loginTextField.setText(editedUser.getLogin());
        passwordTextField.setText(editedUser.getPassword());
      //  permissionComboBox.setValue(editedUser.getPersmissionLevel());
    }
}
