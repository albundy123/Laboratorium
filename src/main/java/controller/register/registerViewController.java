package controller.register;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import dbUtil.dbSqlite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.fxModel.registerFxModel;
import model.fxModel.storehouseFxModel;
import model.registerModel;
import model.storehouseModel;
import model.yearModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class registerViewController {
    public  registerViewController() {System.out.println("Siemanko jestem konstruktorem klasy  registerViewController.");}

//Najpierw tabela z kolumnami do wyswietlania
    @FXML
    private TableView<registerFxModel> registerTableView;
    @FXML
    private TableColumn<registerFxModel, Integer> idRegisterByYearColumn;
    @FXML
    private TableColumn<registerFxModel, String> cardNumberColumn;
    @FXML
    private TableColumn<registerFxModel, String> calibrationDateColumn;
    @FXML
    private TableColumn<registerFxModel, String> instrumentNameColumn;
    @FXML
    private TableColumn<registerFxModel, String> instrumentSerialNumberColumn;
    @FXML
    private TableColumn<registerFxModel, String> instrumentIdentificationNumberColumn;
    @FXML
    private TableColumn<registerFxModel, String> instrumentClientColumn;
    @FXML
    private TableColumn<registerFxModel, String> calibratePersonColumn;
    @FXML
    private TableColumn<registerFxModel,String> certificateNumberColumn;
    @FXML
    private TableColumn<registerFxModel,String> stateColumn;
    //Labele do wyświetlania szczegułów może dam to do innego fxmla jak mi się będzie chciało kiedyś
    @FXML
    private Label shortNameLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label addDateLabel;
    @FXML
    private Label addPersonLabel;
    @FXML
    private Label calibrationDateLabel;
    @FXML
    private Label calibrationPersonLabel;
    @FXML
    private Label leftDateLabel;
    @FXML
    private Label leftPersonLabel;
    @FXML
    private TextField searchTextField;
    //Elementy do ładowania danych z tabeli REGISTER
    @FXML
    private ComboBox<String> yearComboBox;
    @FXML
    private ComboBox<String> isStateOkComboBox; //Czy wszystkie czy tylko te co mamy na stanie
    @FXML
    private Button loadRegisterDataButton;          //Uruchamia ładowanie




    private List<registerModel> registerModelList = new ArrayList<registerModel>();
    private ObservableList<registerFxModel> registerFxObservableList = FXCollections.observableArrayList();
    FilteredList<registerFxModel> filteredRegisterFxObservableList = new FilteredList<>(registerFxObservableList, p -> true); //Lista filtrowana służy do szukania

private registerFxModel editedRegisterElementFromList;

    public void setEditedRegisterElementFromList(registerFxModel editedRegisterElementFromList) {
        this.editedRegisterElementFromList = editedRegisterElementFromList;
    }

    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy registerViewController.");

        isStateOkComboBox.getItems().addAll("Wszystkie","ON","OFF");
        isStateOkComboBox.setValue("Wszystkie");
        yearComboBox.setItems(getYearsList());
        yearComboBox.setValue("2019"); //Domyślnie będzie rok bieżący :)
        getRegisterList();
        initializeTableView();
        addFilter();

    }

    public void getRegisterList(){
        try {
            registerFxObservableList.clear();
            Dao<registerModel, Integer> registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(),registerModel.class);
            QueryBuilder<registerModel, Integer> registerQueryBuilder = registerDao.queryBuilder();
            if(isStateOkComboBox.getValue().equals(yearComboBox.getValue())) {     //Tylko kiedy obydwa mają tę samą wartość całkiem przypadkowo :)
                registerModelList = registerDao.queryForAll();
                System.out.println("Wszystkie");
            }else{
                if(!isStateOkComboBox.getValue().equals("Wszystkie")&& yearComboBox.getValue().equals("Wszystkie")){
                    registerQueryBuilder.where().eq("state",isStateOkComboBox.getValue());
                    System.out.println("W magazynie");
                }else if(isStateOkComboBox.getValue().equals("Wszystkie")&& !yearComboBox.getValue().equals("Wszystkie")){
                    registerQueryBuilder.where().like("calibrationDate","%"+yearComboBox.getValue()+"%");
                    System.out.println("Wszystkie z danego roku");
                }else{
                    registerQueryBuilder.where().eq("state",isStateOkComboBox.getValue()).and().like("calibrationDate","%"+yearComboBox.getValue()+"%");
                    System.out.println("Wybrane z danego roku");
                }
                PreparedQuery<registerModel> prepare = registerQueryBuilder.prepare();
                registerModelList=registerDao.query(prepare);
            }
            registerModelList = registerDao.queryForAll();
            Integer indeks = 0;
            for (registerModel registerElement : registerModelList) {
                System.out.println(registerElement.toString());
                registerFxObservableList.add(new registerFxModel(indeks,registerElement.getIdRegisterByYear(),registerElement.getCardNumber(),
                        registerElement.getCalibrationDate(), registerElement.getInstrument().getInstrumentName().getInstrumentName(),
                        registerElement.getInstrument().getSerialNumber(),registerElement.getInstrument().getIdentificationNumber(),
                        registerElement.getInstrument().getClient().getShortName(),registerElement.getCalibratePerson(),
                        registerElement.getCertificateNumber(),registerElement.getState()));
                indeks++;
            }
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void initializeTableView(){
        idRegisterByYearColumn.setCellValueFactory(new PropertyValueFactory<>("idRegisterByYear"));
        cardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
        calibrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("calibrationDate"));
        instrumentNameColumn.setCellValueFactory(new PropertyValueFactory<>("instrumentName"));
        instrumentSerialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        instrumentIdentificationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("identificationNumber"));
        instrumentClientColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        registerTableView.setItems(filteredRegisterFxObservableList);
        registerTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setEditedRegisterElementFromList(newValue);
            //    showInformation(newValue);
        });
    }
    private void addFilter(){
        searchTextField.textProperty().addListener((value,oldValue, newValue) ->{
            filteredRegisterFxObservableList.setPredicate(item -> {
                if (item.getInstrumentName().toUpperCase().contains(newValue.toUpperCase())|| item.getSerialNumber().toUpperCase().contains(newValue.toUpperCase())||
                        item.getIdentificationNumber().toUpperCase().contains(newValue.toUpperCase())||item.getClient().toUpperCase().contains(newValue.toUpperCase())) {
                    return true;
                } else {
                    return false;
                }
            });
        } );
    }

    public ObservableList<String> getYearsList() {
        ObservableList<String> yearObservableList = FXCollections.observableArrayList();
        try {
            Dao<yearModel, Integer> yearDao = DaoManager.createDao(dbSqlite.getConnectionSource(), yearModel.class);
            List<yearModel> yearList = yearDao.queryForAll();
            yearObservableList.add("Wszystkie");
            yearList.forEach(year -> {
                yearObservableList.add(year.getYear());
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return yearObservableList;
    }
}
