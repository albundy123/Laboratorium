package controller.admin;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import dbUtil.dbSqlite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import model.fxModel.commonFxModel;

import java.sql.SQLException;
import java.util.List;

public class commonInstrumentViewController {
    public commonInstrumentViewController() {System.out.println("Siemanko jestem konstruktorem klasy commonInstrumentViewController.");}

    @FXML
    private Button editButton;

    @FXML
    private Button addNewButton;
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
}
