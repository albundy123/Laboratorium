package controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.clientModel;
import model.userModel;

import java.sql.SQLException;


public class dialogClientViewController {
    public dialogClientViewController() {System.out.println("Siemanko jestem konstruktorem klasy dialogClientViewController.");}



    @FXML
    private VBox mainVBox;
    @FXML
    private TextField shortNameTextField;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField postCodeTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField houseNumberTextField;
    @FXML
    private TextField flatNumberTextField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Label clientLabel;
    @FXML
    private Button saveClientButton;
    @FXML
    private Button cancelSaveClientButton;


    private Integer idEditedClient;
    private clientViewController mainClientController;

    public void setIdEditedClient(Integer idEditedClient) {
        this.idEditedClient = idEditedClient;
    }
    public void setMainClientController(clientViewController mainClientController) {
        this.mainClientController = mainClientController;
    }

    public void setShortNameTextField(String shortNameTextField) {
        this.shortNameTextField.setText(shortNameTextField);
    }
    public void setFullNameTextField(String fullNameTextField) {
        this.fullNameTextField.setText(fullNameTextField);
    }
    public void setPostCodeTextField(String postCodeTextField) {
        this.postCodeTextField.setText(postCodeTextField);
    }
    public void setCityTextField(String cityTextField) {
        this.cityTextField.setText(cityTextField);
    }
    public void setStreetTextField(String streetTextField) {
        this.streetTextField.setText(streetTextField);
    }
    public void setHouseNumberTextField(String houseNumberTextField) {
        this.houseNumberTextField.setText(houseNumberTextField);
    }
    public void setFlatNumberTextField(String flatNumberTextField) {
        this.flatNumberTextField.setText(flatNumberTextField);
    }
    public void setStatusComboBox(String statusComboBox) {
        this.statusComboBox.setValue(statusComboBox);
    }
    public void setClientLabel(String clientLabel) {
        this.clientLabel.setText(clientLabel);
    }
    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy dialogClientViewController.");
        statusComboBox.getItems().addAll("aktywny","nieaktywny");

    }
    @FXML
    private void saveClient(){
        if(idEditedClient !=null){
            System.out.println("Edytujemy");
            editClient();
        }else{
            System.out.println("Dodajemy");
            addNewClient();
        }
    }
    private void addNewClient(){
        if(isValidClientData()){
            try {
                Dao<clientModel, Integer> clientDao = DaoManager.createDao(dbSqlite.getConnectionSource(),clientModel.class);
                clientDao.create(getClient());
                Stage window = (Stage) mainVBox.getScene().getWindow();
                window.close();
                mainClientController.getClients();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void editClient(){
        if(isValidClientData()){
            try {
                Dao<clientModel, Integer> clientDao = DaoManager.createDao(dbSqlite.getConnectionSource(),clientModel.class);
                clientDao.update(getClient());
                Stage window = (Stage) mainVBox.getScene().getWindow();
                window.close();
                mainClientController.getClients();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isValidClientData() {
        String errorMessage = "";
        if (shortNameTextField.getText() == null || shortNameTextField.getText().length() == 0) {
            errorMessage += "Nie wprowadziłeś prawidłowo skróconej nazwy ! \n";
        }
        if (fullNameTextField.getText() == null || fullNameTextField.getText().length() == 0) {
            errorMessage += "Nie wprowadziłeś prawidłowo pełnej nazwy ! \n";
        }
        if (postCodeTextField.getText() == null || postCodeTextField.getText().length() == 0) {
            errorMessage += "Nie wprowadziłeś prawidłowo kodu pocztowego ! \n";
        }
        if (cityTextField.getText() == null || cityTextField.getText().length() == 0) {
            errorMessage += "Nie wprowadziłeś prawidłowo nazwy miejscowości ! \n";
        }
        if (streetTextField.getText() == null || streetTextField.getText().length() == 0) {
            errorMessage += "Nie wprowadziłeś prawidłowo ulicy ! \n";
        }
        if (houseNumberTextField.getText() == null || houseNumberTextField.getText().length() == 0) {
            errorMessage += "Nie wprowadziłeś prawidłowo numeru domu ! \n";
        }
        if (statusComboBox.getValue() == null ) {
            errorMessage += "Nie wybrałeś czy klient jes aktywny ! \n";
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
    private clientModel getClient(){
        clientModel client;
        if (flatNumberTextField.getText().equals("")) {
            client = new clientModel(idEditedClient, shortNameTextField.getText(), fullNameTextField.getText(), postCodeTextField.getText(),cityTextField.getText(), streetTextField.getText(),houseNumberTextField.getText(),"", statusComboBox.getValue(),null);

        }else{
            client = new clientModel(idEditedClient, shortNameTextField.getText(), fullNameTextField.getText(), postCodeTextField.getText(), cityTextField.getText(),streetTextField.getText(),houseNumberTextField.getText(),flatNumberTextField.getText(), statusComboBox.getValue(),null);
        }
        return client;
    }
    @FXML
    private void cancelSaveClient(){
        Stage window = (Stage) mainVBox.getScene().getWindow();
        window.close();
    }
}
