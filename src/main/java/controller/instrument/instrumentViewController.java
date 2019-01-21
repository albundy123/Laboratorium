package controller.instrument;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import controller.dialogClientViewController;
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
import model.*;
import model.fxModel.instrumentFxModel;
import model.fxModel.instrumentStorehouseFxModel;
import model.fxModel.registerFxModel;
import util.Converter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class instrumentViewController {
    public instrumentViewController() {System.out.println("Siemanko jestem konstruktorem klasy instrumentViewController.");}


    //Tabela z danymi wszystkie kolumny muszą być wstrzyknięte żeby potem można było z nich korzystać
    @FXML
    private TableView<instrumentFxModel> instrumentTableView;
    @FXML
    private TableColumn<instrumentFxModel, Integer> idInstrumentColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentNameColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentTypeColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentProducerColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentSerialNumberColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentIdentificationNumberColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentRangeColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentClientColumn;

    @FXML
    private VBox mainVBox;

    //Wyświetlanie zleceniodawcy na dolnym TabPane
    @FXML
    private Label shortNameLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label streetLabel;

    //Tu będzie jeszcze wyświetlanie wzorcowań i przygód w magazynie

    @FXML
    private TextField searchTextField;
    @FXML
    private Button editClientButton;
    @FXML
    private Button editInstrumentButton;

    //Różne listy służądo obsługi TableView itd
    private List<instrumentModel> instrumentModelList = new ArrayList<instrumentModel>(); //Lista instrumentModel z elementami z bazy danych
    private ObservableList<instrumentFxModel> instrumentFxObservableList = FXCollections.observableArrayList(); //Przerzucenie do listy obserwowalnej wykorzystywanej do wyświetlania
    private FilteredList<instrumentFxModel> filteredInstrumentFxObservableList = new FilteredList<>(instrumentFxObservableList, p -> true); //Lista filtrowana służy do szukania
    private List<registerModel> registerModelList = new ArrayList<registerModel>();
    private List<register2Model> register2ModelList = new ArrayList<register2Model>();
    private List<storehouseModel> storehouseModelList = new ArrayList<storehouseModel>();
    private ObservableList<registerFxModel> registerFxObservableList = FXCollections.observableArrayList();
    private ObservableList<instrumentStorehouseFxModel> instrumentStorehouseFxObservableList = FXCollections.observableArrayList();



    private instrumentFxModel editedInstrumentFromList;

    public void setEditedInstrumentFromList(instrumentFxModel editedInstrumentFromList) {
        this.editedInstrumentFromList = editedInstrumentFromList;
    }

    private dialogClientViewController editedClientController;  //Potrzebny, żebym mógł edytować klienta z dolnego tabPane

    private editInstrumentViewController editedInstrumentController;  //Kontroler z nowego okna, które służy do edycji danych przyrządu. Potrzebne do przesyłania danych między oknami

    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy instrumentViewController.");
        initializeTableView();
        editClientButton.disableProperty().bind(Bindings.isEmpty(instrumentTableView.getSelectionModel().getSelectedItems()));
        editInstrumentButton.disableProperty().bind(Bindings.isEmpty(instrumentTableView.getSelectionModel().getSelectedItems()));
        addFilter();
    }

     //Metoda służy do pobierania rekordów z tabeli bazy danych "INSTRUMENTS". Następnie wyniki przerzucane sądo listy elementów, które będą wyświetlane
    public void getInstruments(){
        try {
            instrumentFxObservableList.clear();
            Dao<instrumentModel, Integer> instrumentDao = DaoManager.createDao(dbSqlite.getConnectionSource(),instrumentModel.class);
            instrumentModelList = instrumentDao.queryForAll();
            Integer indeks = 0;
            for (instrumentModel instrument : instrumentModelList) {
                instrumentFxObservableList.add(new instrumentFxModel(indeks, instrument.getIdInstrument(),
                        instrument.getInstrumentName().getInstrumentName(), instrument.getInstrumentType().getInstrumentType(),
                        instrument.getInstrumentProducer().getInstrumentProducer(), instrument.getSerialNumber(),
                        instrument.getIdentificationNumber(), instrument.getInstrumentRange().getInstrumentRange(),
                        instrument.getClient().getShortName()));
                indeks++;
            }
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Metoda służy do łaczenia kolumn TableView z elementami listy. Następnie wybierana jest konkretne lista obserwowalna.
    private void initializeTableView(){
        idInstrumentColumn.setCellValueFactory(new PropertyValueFactory<>("idInstrument"));
        instrumentNameColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentName"));
        instrumentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentType"));
        instrumentProducerColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentProducer"));
        instrumentSerialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        instrumentIdentificationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        instrumentRangeColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentRange"));
        instrumentClientColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        instrumentTableView.setItems(filteredInstrumentFxObservableList); //Wiązanie z listą obserwowalną.
        instrumentTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Dodawanie listenera
            if(newValue!=null){
                setEditedInstrumentFromList(newValue); //Setowanie przyrządu, każde przyciśnięcie
                showInformationAboutClient(instrumentModelList.get(editedInstrumentFromList.getIndexOfInstrumentModelList()).getClient());
                getStorehouseList();
                getRegisterList();
            }
        });

        idRegisterByYearColumn.setCellValueFactory(new PropertyValueFactory<>("idRegisterByYear"));
        cardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
        calibrationDateRegisterColumn.setCellValueFactory(new PropertyValueFactory<>("calibrationDate"));
        userWhoCalibrateColumn.setCellValueFactory(new PropertyValueFactory<>("calibratePerson"));
        certificateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("certificateNumber"));
        documentKindColumn.setCellValueFactory(new PropertyValueFactory<>("documentKind"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        registerTableView.setItems(registerFxObservableList);

        idStorehouseColumn.setCellValueFactory(new PropertyValueFactory<>("idStorehouse"));
        addDateColumn.setCellValueFactory(new PropertyValueFactory<>("addDate"));
        addPersonColumn.setCellValueFactory(new PropertyValueFactory<>("addPerson"));
        calibrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("calibrationDate"));
        calibratePersonColumn.setCellValueFactory(new PropertyValueFactory<>("calibratePerson"));
        leftDateColumn.setCellValueFactory(new PropertyValueFactory<>("leftDate"));
        leftPersonColumn.setCellValueFactory(new PropertyValueFactory<>("leftPerson"));
        storehouseTableView.setItems(instrumentStorehouseFxObservableList);


    }
    @FXML
    private void editInstrument(){
        if(editedInstrumentFromList!=null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/instrument/editInstrumentView.fxml"));
                VBox vBox = loader.load();
                editedInstrumentController = loader.getController();
                if (editedInstrumentController != null){
                    editedInstrumentController.setEditedInstrumentMainController(this);
                    editedInstrumentController.setEditedInstrument(instrumentModelList.get(editedInstrumentFromList.getIndexOfInstrumentModelList()));
                    editedInstrumentController.setClientInstrument(instrumentModelList.get(editedInstrumentFromList.getIndexOfInstrumentModelList()).getClient());
                    setEditedInstrumentValues();
                }
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Edytuj wybrany przyrząd");
                Scene scene = new Scene(vBox);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void setEditedInstrumentValues(){
        editedInstrumentController.setInstrumentNameComboBox(editedInstrumentFromList.getInstrumentName());
        editedInstrumentController.setInstrumentTypeComboBox(editedInstrumentFromList.getInstrumentType());
        editedInstrumentController.setInstrumentProducerComboBox(editedInstrumentFromList.getInstrumentProducer());
        editedInstrumentController.setSerialNumberTextField(editedInstrumentFromList.getSerialNumber());
        editedInstrumentController.setIdentificationNumberTextField(editedInstrumentFromList.getIdentificationNumber());
        editedInstrumentController.setInstrumentRangeComboBox(editedInstrumentFromList.getInstrumentRange());
        editedInstrumentController.setInstrumentClientComboBox2(editedInstrumentFromList.getClient());
    }
    private void showInformationAboutClient(clientModel client){
        if(editedInstrumentFromList!=null){
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
    }
    @FXML
    private void editClient(){
        if(editedInstrumentFromList!=null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/dialogClientView.fxml"));
                VBox vBox = loader.load();
                editedClientController = loader.getController();
                if (editedClientController != null){
                    editedClientController.setEditedInstrumentMainController(this);
                    editedClientController.setIdEditedClient(instrumentModelList.get(editedInstrumentFromList.getIndexOfInstrumentModelList()).getClient().getIdClient());
                    editedClientController.setEditedClientShortName(instrumentModelList.get(editedInstrumentFromList.getIndexOfInstrumentModelList()).getClient().getShortName());
                    editedClientController.setEditedClientFullName(instrumentModelList.get(editedInstrumentFromList.getIndexOfInstrumentModelList()).getClient().getFullName());
                    loadEditDialogView(instrumentModelList.get(editedInstrumentFromList.getIndexOfInstrumentModelList()).getClient());
                }
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Edytuj wybranego klienta");
                Scene scene = new Scene(vBox);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void getInstrumentList(){
        getInstruments();
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
    private void addFilter(){
        searchTextField.textProperty().addListener((value,oldValue, newValue) ->{
            filteredInstrumentFxObservableList.setPredicate(item -> {
                if (item.getInstrumentName().toUpperCase().contains(newValue.toUpperCase())||item.getInstrumentType().toUpperCase().contains(newValue.toUpperCase())||
                        item.getInstrumentProducer().toUpperCase().contains(newValue.toUpperCase())||item.getSerialNumber().toUpperCase().contains(newValue.toUpperCase())||
                        item.getIdentificationNumber().toUpperCase().contains(newValue.toUpperCase())||item.getInstrumentRange().toUpperCase().contains(newValue.toUpperCase())||
                        item.getClient().toUpperCase().contains(newValue.toUpperCase())) {
                    return true;
                } else {
                    return false;
                }
            });
        } );
    }

    @FXML
    private TableView<instrumentStorehouseFxModel> storehouseTableView;
    @FXML
    private TableColumn<instrumentStorehouseFxModel, Integer> idStorehouseColumn;
    @FXML
    private TableColumn<instrumentStorehouseFxModel, String> addDateColumn;
    @FXML
    private TableColumn<instrumentStorehouseFxModel, String> calibrationDateColumn;
    @FXML
    private TableColumn<instrumentStorehouseFxModel, String> leftDateColumn;
    @FXML
    private TableColumn<instrumentStorehouseFxModel, String> addPersonColumn;
    @FXML
    private TableColumn<instrumentStorehouseFxModel, String> calibratePersonColumn;
    @FXML
    private TableColumn<instrumentStorehouseFxModel, String> leftPersonColumn;

    @FXML
    private  TableView<registerFxModel> registerTableView;
    @FXML
    private TableColumn<registerFxModel, Integer> idRegisterByYearColumn;
    @FXML
    private TableColumn<registerFxModel, String> cardNumberColumn;
    @FXML
    private TableColumn<registerFxModel, String> calibrationDateRegisterColumn;
    @FXML
    private TableColumn<registerFxModel, String> userWhoCalibrateColumn;
    @FXML
    private TableColumn<registerFxModel, String> certificateNumberColumn;
    @FXML
    private TableColumn<registerFxModel, String> documentKindColumn;
    @FXML
    private TableColumn<registerFxModel, String> stateColumn;

    public void getStorehouseList(){
        try {
            instrumentStorehouseFxObservableList.clear();
            Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(),storehouseModel.class);
            QueryBuilder<storehouseModel, Integer> storehouseQueryBuilder = storehouseDao.queryBuilder();
            storehouseQueryBuilder.where().eq("instrument_id",instrumentModelList.get(editedInstrumentFromList.getIndexOfInstrumentModelList()).getIdInstrument());
            PreparedQuery<storehouseModel> prepare = storehouseQueryBuilder.prepare();
            storehouseModelList=storehouseDao.query(prepare);
            Integer indeks = 1;//Ta konstrukcja jest potrzebna do połączenie wyników z bazydanych z tymi wyswietlanymi
            for (storehouseModel storehouseElement : storehouseModelList) {
                System.out.println(storehouseElement.toString());
                instrumentStorehouseFxModel storehouseFx= Converter.convertToInstrumentStorehouseFx(storehouseElement);
                storehouseFx.setIdStorehouse(indeks);
                instrumentStorehouseFxObservableList.add(storehouseFx);
                indeks++;
            }
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void getRegisterList(){
        try {
            registerFxObservableList.clear();
            Dao<registerModel, Integer> registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(),registerModel.class);
            QueryBuilder<registerModel, Integer> registerQueryBuilder = registerDao.queryBuilder();
            registerQueryBuilder.where().eq("instrument_id",instrumentModelList.get(editedInstrumentFromList.getIndexOfInstrumentModelList()).getIdInstrument());
            PreparedQuery<registerModel> prepare = registerQueryBuilder.prepare();
            registerModelList=registerDao.query(prepare);
            Integer indeks = 1;
            for (registerModel registerElement : registerModelList) {
                System.out.println(registerElement.getUserWhoCalibrate().getLogin());
                System.out.println(registerElement.getCalibrationDate());
                registerFxObservableList.add(new registerFxModel(indeks,indeks,registerElement.getCardNumber(),
                        registerElement.getCalibrationDate(), "","","", "",
                        registerElement.getUserWhoCalibrate().getLogin(), registerElement.getCertificateNumber(),
                        registerElement.getDocumentKind(),registerElement.getState()));
                indeks++;
            }
            Dao<register2Model, Integer> register2Dao = DaoManager.createDao(dbSqlite.getConnectionSource(),register2Model.class);
            QueryBuilder<register2Model, Integer> register2QueryBuilder = register2Dao.queryBuilder();
            register2QueryBuilder.where().eq("instrument_id",instrumentModelList.get(editedInstrumentFromList.getIndexOfInstrumentModelList()).getIdInstrument());
            PreparedQuery<register2Model> prepare2 = register2QueryBuilder.prepare();
            register2ModelList=register2Dao.query(prepare2);
            for (register2Model registerElement : register2ModelList) {
                System.out.println(registerElement.toString());
                registerFxObservableList.add(new registerFxModel(indeks,indeks,registerElement.getCardNumber(),
                        registerElement.getCalibrationDate(), "","","", "",
                        registerElement.getUserWhoCalibrate().getLogin(), registerElement.getCertificateNumber(),
                        registerElement.getDocumentKind(),registerElement.getState()));
                indeks++;
            }
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
