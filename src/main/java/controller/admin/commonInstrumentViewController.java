package controller.admin;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import controller.instrument.*;
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
import model.*;
import model.fxModel.commonFxModel;
import util.Close;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class commonInstrumentViewController {
    public commonInstrumentViewController() {System.out.println("Siemanko jestem konstruktorem klasy commonInstrumentViewController.");}

    private static final String NAME_VIEW = "/instrument/newInstrumentNameView.fxml";
    private static final String TYPE_VIEW = "/instrument/newInstrumentTypeView.fxml";
    private static final String PRODUCER_VIEW = "/instrument/newInstrumentProducerView.fxml";
    private static final String RANGE_VIEW = "/instrument/newInstrumentRangeView.fxml";
    private static final String UNIT_VIEW = "/instrument/newInstrumentUnitView.fxml";

    @FXML
    private TableView<commonFxModel> commonTableView;
    @FXML
    private TableColumn<commonFxModel, Integer> idColumn;
    @FXML
    private TableColumn<commonFxModel, String> valueColumn;

    public void setValueColumn(String text) {
        this.valueColumn.setText(text);
    }

    private List<instrumentNameModel> nameList;
    private List<instrumentTypeModel> typeList;
    private List<instrumentProducerModel> producerList;
    private List<instrumentRangeModel> rangeList;
    private List<unitModel> unitList;
    private ObservableList<commonFxModel> commonObservableList = FXCollections.observableArrayList();
    private commonFxModel editedElementFromList;
    public void setEditedElementFromList(commonFxModel editedElementFromList) {
        this.editedElementFromList = editedElementFromList;
    }
    private Integer parameter;

    public void setParameter(Integer parameter) {
        this.parameter = parameter;
    }

    private newInstrumentNameViewController newInstrumentNameController;
    private newInstrumentTypeViewController newInstrumentTypeController;
    private newInstrumentProducerViewController newInstrumentProducerController;
    private newInstrumentRangeViewController newInstrumentRangeController;
    private newInstrumentUnitViewController newInstrumentUnitController;


    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy commonInstrumentViewController.");
        initializeTable();
    }

    public void initializeTable(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idCommon"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("commonString"));
        commonTableView.setItems(commonObservableList);
        commonTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setEditedElementFromList(newValue));
    }


    public void getNames(){
        commonObservableList.clear();
        try {
            Dao<instrumentNameModel, Integer> instrumentNameDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentNameModel.class);
            nameList=instrumentNameDao.queryForAll();
            nameList.forEach(name ->{
                commonObservableList.add(new commonFxModel(name.getIdInstrumentName(),name.getInstrumentName()));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getTypes(){
        commonObservableList.clear();
        try {
            Dao<instrumentTypeModel, Integer> instrumentTypeDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentTypeModel.class);
            typeList=instrumentTypeDao.queryForAll();
            typeList.forEach(type ->{
                commonObservableList.add(new commonFxModel(type.getIdInstrumentType(),type.getInstrumentType()));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getProducers(){
        commonObservableList.clear();
        try {
            Dao<instrumentProducerModel, Integer> instrumentProducerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentProducerModel.class);
            producerList=instrumentProducerDao.queryForAll();
            producerList.forEach(producer ->{
                commonObservableList.add(new commonFxModel(producer.getIdInstrumentProducer(),producer.getInstrumentProducer()));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getRanges(){
        commonObservableList.clear();
        try {
            Dao<instrumentRangeModel, Integer> instrumentRangeDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentRangeModel.class);
            rangeList=instrumentRangeDao.queryForAll();
            rangeList.forEach(range ->{
                commonObservableList.add(new commonFxModel(range.getIdInstrumentRange(),range.getInstrumentRange()));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getUnits(){
        commonObservableList.clear();
        try {
            Dao<unitModel, Integer> unitDao = DaoManager.createDao(dbSqlite.getConnectionSource(), unitModel.class);
            unitList=unitDao.queryForAll();
            unitList.forEach(unit ->{
                commonObservableList.add(new commonFxModel(unit.getIdUnit(),unit.getUnitName()));
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void addNew(){
        addNewParameter();
    }
    @FXML
    private void edit(){
        editParameter();
    }
    @FXML
    private void delete(){
        if(editedElementFromList!=null) {
            if (parameter == 5) {
                try {
                    Dao<unitModel, Integer> instrumentUnitDao = DaoManager.createDao(dbSqlite.getConnectionSource(), unitModel.class);
                    instrumentUnitDao.delete(new unitModel(editedElementFromList.getIdCommon(),editedElementFromList.getCommonString()));
                    getUnits();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                deleteParameter();
            }
        }
    }
    private void addNewParameter(){
        if(parameter==1){
            newInstrumentNameController=loadWindow(newInstrumentNameController, NAME_VIEW,"Nazwy przyrządów");
            newInstrumentNameController.setEditedInstrumentName(null);
            newInstrumentNameController.setCommonInstrumentController(this);
        }else if(parameter==2){
            newInstrumentTypeController=loadWindow(newInstrumentTypeController, TYPE_VIEW,"Typy przyrządów");
            newInstrumentTypeController.setEditedInstrumentType(null);
            newInstrumentTypeController.setCommonInstrumentController(this);
        }else if(parameter==3){
            newInstrumentProducerController=loadWindow(newInstrumentProducerController, PRODUCER_VIEW,"Producenci przyrządów");
            newInstrumentProducerController.setEditedInstrumentProducer(null);
            newInstrumentProducerController.setCommonInstrumentController(this);
        }else if(parameter==4){
            newInstrumentRangeController=loadWindow(newInstrumentRangeController, RANGE_VIEW,"Zakresy przyrządów");
            newInstrumentRangeController.setEditedInstrumentRange(null);
            newInstrumentRangeController.setCommonInstrumentController(this);
        }else if(parameter==5){
            newInstrumentUnitController=loadWindow(newInstrumentUnitController, UNIT_VIEW,"Jednostki pomiarowe");
            newInstrumentUnitController.setEditedInstrumentUnit(null);
            newInstrumentUnitController.setCommonInstrumentController(this);
        }
    }
    private void editParameter(){
        if(editedElementFromList!=null) {
            if (parameter == 1) {
                newInstrumentNameController = loadWindow(newInstrumentNameController, NAME_VIEW, "Nazwy przyrządów");
                newInstrumentNameController.setNewInstrumentNameTextField(editedElementFromList.getCommonString());
                newInstrumentNameController.setEditedInstrumentName(new instrumentNameModel(editedElementFromList.getIdCommon(), editedElementFromList.getCommonString()));
                newInstrumentNameController.setCommonInstrumentController(this);
            } else if (parameter == 2) {
                newInstrumentTypeController = loadWindow(newInstrumentTypeController, TYPE_VIEW, "Typy przyrządów");
                newInstrumentTypeController.setNewInstrumentTypeTextField(editedElementFromList.getCommonString());
                newInstrumentTypeController.setEditedInstrumentType(new instrumentTypeModel(editedElementFromList.getIdCommon(), editedElementFromList.getCommonString()));
                newInstrumentTypeController.setCommonInstrumentController(this);
            } else if (parameter == 3) {
                newInstrumentProducerController = loadWindow(newInstrumentProducerController, PRODUCER_VIEW, "Producenci przyrządów");
                newInstrumentProducerController.setNewInstrumentProducerTextField(editedElementFromList.getCommonString());
                newInstrumentProducerController.setEditedInstrumentProducer(new instrumentProducerModel(editedElementFromList.getIdCommon(), editedElementFromList.getCommonString()));
                newInstrumentProducerController.setCommonInstrumentController(this);
            } else if (parameter == 4) {
                newInstrumentRangeController = loadWindow(newInstrumentRangeController, RANGE_VIEW, "Zakresy przyrządów");
                decodeRange(editedElementFromList.getCommonString(), newInstrumentRangeController);
                newInstrumentRangeController.setEditedInstrumentRange(new instrumentRangeModel(editedElementFromList.getIdCommon(), editedElementFromList.getCommonString()));
                newInstrumentRangeController.setCommonInstrumentController(this);
            } else if (parameter == 5) {
                newInstrumentUnitController = loadWindow(newInstrumentUnitController, UNIT_VIEW, "Jednostki pomiarowe");
                newInstrumentUnitController.setNewInstrumentUnitTextField(editedElementFromList.getCommonString());
                newInstrumentUnitController.setEditedInstrumentUnit(new unitModel(editedElementFromList.getIdCommon(), editedElementFromList.getCommonString()));
                newInstrumentUnitController.setCommonInstrumentController(this);
            }
        }
    }
    private void deleteParameter(){
        try {
            Dao<instrumentModel, Integer> instrumentDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentModel.class);
            QueryBuilder<instrumentModel, Integer> instrumentQueryBuilder = instrumentDao.queryBuilder();
            instrumentQueryBuilder.where().eq(getColumnName(), editedElementFromList.getIdCommon());
            PreparedQuery<instrumentModel> prepare = instrumentQueryBuilder.prepare();
            List<instrumentModel> result = instrumentDao.query(prepare);
            if (result.isEmpty()) {
                if (parameter == 1){
                    Dao<instrumentNameModel, Integer> instrumentNameDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentNameModel.class);
                    instrumentNameDao.delete(new instrumentNameModel(editedElementFromList.getIdCommon(),editedElementFromList.getCommonString()));
                    getNames();
                } else if (parameter == 2) {
                    Dao<instrumentTypeModel, Integer> instrumentTypeDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentTypeModel.class);
                    instrumentTypeDao.delete(new instrumentTypeModel(editedElementFromList.getIdCommon(),editedElementFromList.getCommonString()));
                    getTypes();
                } else if (parameter == 3) {
                    Dao<instrumentProducerModel, Integer> instrumentProducerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentProducerModel.class);
                    instrumentProducerDao.delete(new instrumentProducerModel(editedElementFromList.getIdCommon(),editedElementFromList.getCommonString()));
                    getProducers();
                } else if (parameter == 4) {
                    Dao<instrumentRangeModel, Integer> instrumentRangeDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentRangeModel.class);
                    instrumentRangeDao.delete(new instrumentRangeModel(editedElementFromList.getIdCommon(),editedElementFromList.getCommonString()));
                    getRanges();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Nie można usunąć tego pola");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private <T> T loadWindow(T controller,String resource, String title){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        try {
            VBox vBox = loader.load();
            controller=loader.getController();
            Stage window = new Stage();
            window.setTitle(title);
            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return controller;
    }
    private void decodeRange(String range, newInstrumentRangeViewController controller){
        String begin=range.substring(1,range.indexOf('d')-1);
        String end=range.substring(range.indexOf('o')+1,range.indexOf(')'));
        String unit=range.substring(range.indexOf(')')+2,range.length());
        controller.setNewInstrumentRangeTextField1(begin);
        controller.setNewInstrumentRangeTextField2(end);
        controller.setNewInstrumentRangeUnitComboBox(unit);
    }
    private String getColumnName(){
        if (parameter == 1) {
            return "instrumentName_id";
        } else if (parameter == 2) {
            return "instrumentType_id";
        } else if (parameter == 3) {
            return "instrumentProducer_id";
        } else if (parameter == 4) {
            return "instrumentRange_id";
        }
        return null;
    }
}