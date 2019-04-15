package controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import controller.instrument.instrumentViewController;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.clientModel;
import util.Close;
import util.showAlert;

import java.sql.SQLException;
import java.util.List;

/**
 * Klasa kontrolera odpowiedzialnego za obsługę okna służącego od edycji Zleceniodawcy
 */
public class dialogClientViewController {
    public dialogClientViewController() {}


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

    private Integer idEditedClient;
    private clientViewController mainClientController;
    private instrumentViewController editedInstrumentMainController; //Potrzebne od odświeżenia okna :)

    public void setEditedInstrumentMainController(instrumentViewController editedInstrumentMainController) {
        this.editedInstrumentMainController = editedInstrumentMainController;
    }

    private String editedClientShortName;
    private String editedClientFullName;

    public void setEditedClientShortName(String editedClientShortName) {
        this.editedClientShortName = editedClientShortName;
    }

    public void setEditedClientFullName(String editedClientFullName) {
        this.editedClientFullName = editedClientFullName;
    }

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
        statusComboBox.getItems().addAll("aktywny","nieaktywny");
    }
    @FXML
    private void saveClient(){
        if(idEditedClient !=null){
            editClient();
        }else{
            addNewClient();
        }
    }
    private void addNewClient(){
        if(isValidClientData()&& isValidClientDataInDB()==0){
            try {
                Dao<clientModel, Integer> clientDao = DaoManager.createDao(dbSqlite.getConnectionSource(),clientModel.class);
                clientDao.create(getClient());
                Close.closeVBoxWindow(mainVBox);
                mainClientController.getClients();
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                showAlert.display(e.getMessage());
            }
        }else{
            showAlert();
        }
    }
    private void editClient() {
        if (isValidClientData() && (isValidClientDataInDB() == idEditedClient || isValidClientDataInDB()==0 || statusComboBox.getValue().equals("nieaktywny"))) {
            try {
                Dao<clientModel, Integer> clientDao = DaoManager.createDao(dbSqlite.getConnectionSource(), clientModel.class);
                clientDao.update(getClient());
                Close.closeVBoxWindow(mainVBox);
                if (mainClientController != null) {
                    mainClientController.getClients();
                }
                if (editedInstrumentMainController != null) {
                    editedInstrumentMainController.getInstruments();
                }
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                showAlert.display(e.getMessage());
            }
        }else{
            showAlert();
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
            alert.setTitle("Nieprawidłowe dane");
            alert.setHeaderText("Proszę wprowadzić prawidłowe dane dla podanych niżej pól");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
    private int isValidClientDataInDB(){
        try {
            Dao<clientModel, Integer> clientDao= DaoManager.createDao(dbSqlite.getConnectionSource(), clientModel.class);
            QueryBuilder<clientModel, Integer> userQueryBuilder = clientDao.queryBuilder();
            Where<clientModel, Integer> where = userQueryBuilder.where();
            where.and(where.or(where.eq("shortName",shortNameTextField.getText()),where.eq("fullName",fullNameTextField.getText())),where.eq("status","aktywny"));
            PreparedQuery<clientModel> prepare = userQueryBuilder.prepare();
            List<clientModel> result = clientDao.query(prepare);
            if(result.isEmpty()) {
                dbSqlite.closeConnection();
                return 0;
            }else{
                dbSqlite.closeConnection();
                return result.get(0).getIdClient();
            }
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
            return 0;
        }
    }
    private void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Nieprawidłowe dane");
        alert.setHeaderText("W bazie danych istnieje już klient o podanej nazwie");
        alert.showAndWait();
    }
    private clientModel getClient(){
        clientModel client;
        if (flatNumberTextField.getText().equals("")) {
            client = new clientModel(idEditedClient, shortNameTextField.getText(), fullNameTextField.getText(), postCodeTextField.getText(),cityTextField.getText(), streetTextField.getText(),houseNumberTextField.getText(),"", statusComboBox.getValue());
        }else{
            client = new clientModel(idEditedClient, shortNameTextField.getText(), fullNameTextField.getText(), postCodeTextField.getText(), cityTextField.getText(),streetTextField.getText(),houseNumberTextField.getText(),flatNumberTextField.getText(), statusComboBox.getValue());
        }
        return client;
    }
    @FXML
    private void cancelSaveClient(){
        Close.closeVBoxWindow(mainVBox);
    }
}
