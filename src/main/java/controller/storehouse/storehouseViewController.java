package controller.storehouse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import controller.instrument.editInstrumentViewController;
import dbUtil.dbSqlite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.fxModel.instrumentFxModel;
import model.fxModel.storehouseFxModel;
import model.instrumentModel;
import model.registerModel;
import model.storehouseModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class storehouseViewController {
    public  storehouseViewController() {System.out.println("Siemanko jestem konstruktorem klasy  storehouseViewController.");}

    @FXML
    private Button addNewInstrumentButton;
    @FXML
    private TextField fullNameSearchTextField;
    @FXML
    private TableView<storehouseFxModel> storehouseTableView;
    @FXML
    private TableColumn<storehouseFxModel, String> idInstrumentColumn;
    @FXML
    private TableColumn<storehouseFxModel, String> instrumentNameColumn;
    @FXML
    private TableColumn<storehouseFxModel, String> instrumentTypeColumn;
    @FXML
    private TableColumn<storehouseFxModel, String> instrumentProducerColumn;
    @FXML
    private TableColumn<storehouseFxModel, String> instrumentSerialNumberColumn;
    @FXML
    private TableColumn<storehouseFxModel, String> instrumentIdentificationNumberColumn;
    @FXML
    private TableColumn<storehouseFxModel, String> instrumentRangeColumn;
    @FXML
    private TableColumn<storehouseFxModel, String> instrumentClientColumn;
    @FXML
    private TableColumn<storehouseFxModel, Date> addDateColumn;
    @FXML
    private TableColumn<storehouseFxModel, Date> calibrationDateColumn;
    @FXML
    private TableColumn<storehouseFxModel, Date> leftDateColumn;
    @FXML
    private TextField shortNameTextField;
    @FXML
    private VBox mainVBox;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private Button leftInstrumentButton;
    @FXML
    private Button calibrateInstrumentButton;
    @FXML
    private Button editInstrumentButton;
    @FXML
    private TextField streetTextField;
    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy storehouseViewController.");
        getStorehouseList();
       initializeTableView();
    }

    private storehouseFxModel editedStorehouseElementFromList;

    public void setEditedStorehouseElementFromList(storehouseFxModel editedStorehouseElementFromList) {
        this.editedStorehouseElementFromList = editedStorehouseElementFromList;
    }

    private newInstrumentViewController newInstrumentController;
    private calibratedInstrumentViewController calibratedInstrumentController;
    private leftInstrumentViewController leftInstrumentController;

    private List<storehouseModel> storehouseModelList = new ArrayList<storehouseModel>();
    private ObservableList<storehouseFxModel> storehouseFxObservableList = FXCollections.observableArrayList();
    private editInstrumentViewController editedInstrumentController;


    public void getStorehouseList(){
        try {
            storehouseFxObservableList.clear();
            Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(),storehouseModel.class);
            storehouseModelList = storehouseDao.queryForAll();
            Integer indeks = 0;
            for (storehouseModel storehouseElement : storehouseModelList) {
                storehouseFxObservableList.add(new storehouseFxModel(indeks, storehouseElement.getIdStorehouse(),
                        storehouseElement.getInstrument().getInstrumentName().getInstrumentName(), storehouseElement.getInstrument().getInstrumentType().getInstrumentType(),
                        storehouseElement.getInstrument().getInstrumentProducer().getInstrumentProducer(), storehouseElement.getInstrument().getSerialNumber(),
                        storehouseElement.getInstrument().getIdentificationNumber(), storehouseElement.getInstrument().getInstrumentRange().getInstrumentRange(),
                        storehouseElement.getInstrument().getClient().getShortName(), storehouseElement.getAddDate(), storehouseElement.getCalibrationDate(),storehouseElement.getLeftDate()));
                indeks++;
            }
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void initializeTableView(){
        idInstrumentColumn.setCellValueFactory(new PropertyValueFactory<>("idInstrument"));
        instrumentNameColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentName"));
        instrumentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentType"));
        instrumentProducerColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentProducer"));
        instrumentSerialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        instrumentIdentificationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        instrumentRangeColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentRange"));
        instrumentClientColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        addDateColumn.setCellValueFactory(new PropertyValueFactory<>("addDate"));
        calibrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("calibrationDate"));
        leftDateColumn.setCellValueFactory(new PropertyValueFactory<>("leftDate"));
        storehouseTableView.setItems(storehouseFxObservableList);
        storehouseTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setEditedStorehouseElementFromList(newValue);
            //    showInformation(newValue);
        });
    }
    @FXML
    private void editInstrument(){
        if(editedStorehouseElementFromList!=null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/instrument/editInstrumentView.fxml"));
                VBox vBox = loader.load();
                editedInstrumentController = loader.getController();
                if (editedInstrumentController != null){
                    editedInstrumentController.setStorehouseMainController(this);
                    editedInstrumentController.setEditedInstrument(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getInstrument());
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
        editedInstrumentController.setInstrumentNameComboBox(editedStorehouseElementFromList.getInstrumentName());
        editedInstrumentController.setInstrumentTypeComboBox(editedStorehouseElementFromList.getInstrumentType());
        editedInstrumentController.setInstrumentProducerComboBox(editedStorehouseElementFromList.getInstrumentProducer());
        editedInstrumentController.setSerialNumberTextField(editedStorehouseElementFromList.getSerialNumber());
        editedInstrumentController.setIdentificationNumberTextField(editedStorehouseElementFromList.getIdentificationNumber());
        editedInstrumentController.setInstrumentRangeComboBox(editedStorehouseElementFromList.getInstrumentRange());
        editedInstrumentController.setInstrumentClientComboBox2(editedStorehouseElementFromList.getClient());
    }
    private void setEditedInstrumentLabels(){
        calibratedInstrumentController.setInstrumentNameLabel(editedStorehouseElementFromList.getInstrumentName());
        calibratedInstrumentController.setInstrumentTypeLabel(editedStorehouseElementFromList.getInstrumentType());
        calibratedInstrumentController.setInstrumentProducerLabel(editedStorehouseElementFromList.getInstrumentProducer());
        calibratedInstrumentController.setSerialNumberLabel(editedStorehouseElementFromList.getSerialNumber());
        calibratedInstrumentController.setIdentificationNumberLabel(editedStorehouseElementFromList.getIdentificationNumber());
        calibratedInstrumentController.setInstrumentRangeLabel(editedStorehouseElementFromList.getInstrumentRange());
        calibratedInstrumentController.setClientLabel(editedStorehouseElementFromList.getClient());
    }
    private void setLeftInstrumentLabels(){
        leftInstrumentController.setInstrumentNameLabel(editedStorehouseElementFromList.getInstrumentName());
        leftInstrumentController.setInstrumentTypeLabel(editedStorehouseElementFromList.getInstrumentType());
        leftInstrumentController.setInstrumentProducerLabel(editedStorehouseElementFromList.getInstrumentProducer());
        leftInstrumentController.setSerialNumberLabel(editedStorehouseElementFromList.getSerialNumber());
        leftInstrumentController.setIdentificationNumberLabel(editedStorehouseElementFromList.getIdentificationNumber());
        leftInstrumentController.setInstrumentRangeLabel(editedStorehouseElementFromList.getInstrumentRange());
        leftInstrumentController.setClientLabel(editedStorehouseElementFromList.getClient());
    }
    @FXML
    private void addNewInstrument(){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/newInstrumentView.fxml"));
                VBox vBox = loader.load();
                newInstrumentController = loader.getController();
                if (newInstrumentController != null){
                    newInstrumentController.setStorehouseMainController(this);
                 //   newInstrumentController.setNewInstrumentModel(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getInstrument());
                }
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Dodaj przyrząd do magazynu");
                Scene scene = new Scene(vBox);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    @FXML
    public void calibrateInstrument(){
        if(editedStorehouseElementFromList!=null) {
            try {
                registerModel calibrateInstrument = new registerModel(0,0,storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getIdStorehouse(),
                        "",storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getCalibrationDate(),
                        storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getInstrument(),"");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/calibratedInstrumentView.fxml"));
                VBox vBox = loader.load();
                calibratedInstrumentController = loader.getController();
                if (calibratedInstrumentController != null){
                    calibratedInstrumentController.setStorehouseMainController(this);
                    calibratedInstrumentController.setCalibratedInstrument(calibrateInstrument);
                    setEditedInstrumentLabels();
                    //   newInstrumentController.setNewInstrumentModel(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getInstrument());
                }
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Dodaj przyrząd do wzorcowania");
                Scene scene = new Scene(vBox);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void leftInstrument(){
        if(editedStorehouseElementFromList!=null) {
            try {
                storehouseModel leftInstrument = storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/leftInstrumentView.fxml"));
                VBox vBox = loader.load();
                leftInstrumentController = loader.getController();
                if (leftInstrumentController != null){
                    leftInstrumentController.setStorehouseMainController(this);
                    leftInstrumentController.setLeftInstrument(leftInstrument);
                    setLeftInstrumentLabels();
                    //   newInstrumentController.setNewInstrumentModel(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getInstrument());
                }
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Dodaj przyrząd do wzorcowania");
                Scene scene = new Scene(vBox);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
