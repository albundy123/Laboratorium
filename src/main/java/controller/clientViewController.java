package controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import controller.instrument.editInstrumentViewController;
import controller.storehouse.newInstrumentViewController;
import dbUtil.dbSqlite;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.clientModel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.showAlert;


import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Klasa kontrolera odpowiedzialnego za obsługę okna Zleceniodawcy
 */
public class clientViewController {
    public clientViewController() {}

    @FXML
    private VBox mainVBox;
    @FXML
    private TableView<clientModel> clientTableView;
    @FXML
    private TableColumn<clientModel, String> idClientColumn;
    @FXML
    private TableColumn<clientModel, String> shortNameColumn;
    @FXML
    private TableColumn<clientModel, String> fullNameColumn;
    @FXML
    private TableColumn<clientModel, String> postCodeColumn;
    @FXML
    private TableColumn<clientModel, String> cityColumn;
    @FXML
    private TableColumn<clientModel, String> streetColumn;
    @FXML
    private TableColumn<clientModel, String> houseNumberColumn;
    @FXML
    private TableColumn<clientModel, String> flatNumberColumn;
    @FXML
    private TableColumn<clientModel, String> statusColumn;
    @FXML
    private Label shortNameLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private TextField fullNameSearchTextField;
    @FXML
    private Button editClientButton;
    @FXML
    private Button choseClientButton;
    public void setChoseButtonDisable(){
        choseClientButton.setDisable(false);
    }

    private ObservableList<clientModel> clientObservableList = FXCollections.observableArrayList();
    FilteredList<clientModel> filteredClientObservableList = new FilteredList<>(clientObservableList, p -> true);

    private newInstrumentViewController newInstrumentMainController;
    public void setNewInstrumentMainController(newInstrumentViewController newInstrumentMainController) {
        this.newInstrumentMainController = newInstrumentMainController;
    }

    private editInstrumentViewController editInstrumentMainController;
    public void setEditInstrumentMainController(editInstrumentViewController editInstrumentMainController) {
        this.editInstrumentMainController = editInstrumentMainController;
    }

    private clientModel editedClientFromList;
    public void setEditedClientFromList(clientModel editedClientFromList) {
        this.editedClientFromList = editedClientFromList;
    }

    private dialogClientViewController editedClientController;

    @FXML
    private void initialize(){
        addFilter();
        initializeTableView();
        editClientButton.disableProperty().bind(Bindings.isEmpty(clientTableView.getSelectionModel().getSelectedItems()));
        choseClientButton.setDisable(true);
    }
    @FXML
    private void addClient(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/dialogClientView.fxml"));
            VBox vBox = loader.load();
            editedClientController = loader.getController();
            if (editedClientController != null){
                editedClientController.setMainClientController(this);
            }
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Dodaj nowego klient");
            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            showAlert.display(e.getMessage());
        }
    }
    @FXML
    private void editClient(){
        if(editedClientFromList!=null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/dialogClientView.fxml"));
                VBox vBox = loader.load();
                editedClientController = loader.getController();
                if (editedClientController != null){
                    editedClientController.setMainClientController(this);
                    editedClientController.setEditedClientShortName(editedClientFromList.getShortName());
                    editedClientController.setEditedClientFullName(editedClientFromList.getFullName());
                    loadEditDialogView(editedClientFromList);
                }
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Edytuj wybranego klienta");
                Scene scene = new Scene(vBox);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                showAlert.display(e.getMessage());
            }
        }
    }
    @FXML
    private void choseClient(){
        if(editedClientFromList!=null) {
            if(editInstrumentMainController!=null){
                editInstrumentMainController.setInstrumentClientComboBox(editedClientFromList.getShortName());
                editInstrumentMainController.setClientInstrument(editedClientFromList);}
            if(newInstrumentMainController!=null){
            newInstrumentMainController.setInstrumentClientComboBox(editedClientFromList.getShortName());
            newInstrumentMainController.setClientInstrument(editedClientFromList);}

            Stage window = (Stage) mainVBox.getScene().getWindow();
            window.close();
        }
    }
    @FXML
    public void getClientList(){
        getClients();
    }

    public void getClients(){
        try {
            clientObservableList.clear();
            Dao<clientModel, Integer> clientDao = DaoManager.createDao(dbSqlite.getConnectionSource(),clientModel.class);
            List<clientModel> clientList = clientDao.queryForAll();
            clientList.forEach(client ->{
                clientObservableList.add(client);
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }
    public void getActiveClients(){
        try {
            clientObservableList.clear();
            Dao<clientModel, Integer> clientDao = DaoManager.createDao(dbSqlite.getConnectionSource(),clientModel.class);
            List<clientModel> clientList = clientDao.queryForAll();
            clientList.forEach(client ->{
                if(client.getStatus().equals("aktywny")) {
                    clientObservableList.add(client);
                }
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }
    /** Metoda do konfiguracji TableView */
    private void initializeTableView(){
        idClientColumn.setCellValueFactory(new PropertyValueFactory<>("idClient"));
        shortNameColumn.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        postCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postCode"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        houseNumberColumn.setCellValueFactory(new PropertyValueFactory<>("houseNumber"));
        flatNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flatNumber"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        clientTableView.setItems(filteredClientObservableList);
        clientTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setEditedClientFromList(newValue);
            showInformation(newValue);
        });
        clientTableView.prefHeightProperty().bind(mainVBox.heightProperty().multiply(0.7));
    }
    private void addFilter(){
        fullNameSearchTextField.textProperty().addListener((value,oldValue, newValue) ->{
            filteredClientObservableList.setPredicate(item -> {
                if (item.getShortName().toUpperCase().contains(newValue.toUpperCase())||item.getFullName().toUpperCase().contains(newValue.toUpperCase())||item.getCity().toUpperCase().contains(newValue.toUpperCase())) {
                    return true;
                } else {
                    return false;
                }
            });
        } );
    }
    private void loadEditDialogView(clientModel client){
        editedClientController.setShortNameTextField(client.getShortName());
        editedClientController.setFullNameTextField(client.getFullName());
        editedClientController.setPostCodeTextField(client.getPostCode());
        editedClientController.setCityTextField(client.getCity());
        editedClientController.setStreetTextField(client.getStreet());
        editedClientController.setHouseNumberTextField(client.getHouseNumber());
        editedClientController.setFlatNumberTextField(client.getFlatNumber());
        editedClientController.setStatusComboBox(client.getStatus());
        editedClientController.setIdEditedClient(client.getIdClient());
        editedClientController.setClientLabel("Klient");
    }
    /** Metoda wyświetla informacje o kliencie */
    private void showInformation(clientModel client){
        if(client != null){
            shortNameLabel.setText(client.getShortName());
            fullNameLabel.setText(client.getFullName());
            cityLabel.setText(client.getPostCode()+ " "+ client.getCity());
            if(client.getFlatNumber().isEmpty()) {
                streetLabel.setText(client.getStreet() + " " + client.getHouseNumber());
            }else{
                streetLabel.setText(client.getStreet() + " " + client.getHouseNumber()+"/"+client.getFlatNumber());
            }
        }

    }
    @FXML
    private void exportToExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet("Arkusz1");
        Row row = spreadsheet.createRow(0);
        //Nazwy kolumn
        row.createCell(0).setCellValue("Lp. ");
        row.createCell(1).setCellValue("Skrót");
        row.createCell(2).setCellValue("Pełna nazwa");
        row.createCell(3).setCellValue("Kod pocztowy");
        row.createCell(4).setCellValue("Miejscowość");
        row.createCell(5).setCellValue("Ulica");
        row.createCell(6).setCellValue("Nr domu");
        row.createCell(7).setCellValue("Nr mieszkania");

        int i = 0;
        for (clientModel clientElement : filteredClientObservableList) {
            row = spreadsheet.createRow(i + 1);
            row.createCell(0).setCellValue(i+1);
            row.createCell(1).setCellValue(clientElement.getShortName());
            row.createCell(2).setCellValue(clientElement.getFullName());
            row.createCell(3).setCellValue(clientElement.getPostCode());
            row.createCell(4).setCellValue(clientElement.getCity());
            row.createCell(5).setCellValue(clientElement.getStreet());
            row.createCell(6).setCellValue(clientElement.getHouseNumber());
            row.createCell(7).setCellValue(clientElement.getFlatNumber());
            i++;
        }
        for (int j = 0; j < 8; j++) {
            spreadsheet.autoSizeColumn(j);
        }
        FileOutputStream fileOut = new FileOutputStream("Zleceniodawcy.xlsx");
        workbook.write(fileOut);
        fileOut.close();
    }
}
