package controller.storehouse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import controller.instrument.editInstrumentViewController;
import controller.mainViewController;
import dbUtil.dbSqlite;
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
import javafx.util.StringConverter;
import model.*;
import model.fxModel.instrumentFxModel;
import model.fxModel.storehouseFxModel;
import util.ConfirmBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class storehouseViewController {
    public  storehouseViewController() {System.out.println("Siemanko jestem konstruktorem klasy  storehouseViewController.");}

    private userModel user;
    public void setUser(userModel user) {
        this.user = user;
    }

    public userModel getUser() {
        return user;
    }

    //Wstrzyknięcie TableView i poszczególnych kolumn, musi być ze względu na wyświetlanie
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
    private TextArea remarksTextArea;
    //Pole tekstowe do filtrowania listy
    @FXML
    private TextField searchTextField;
    //Elementy do ładowania danych z tabeli STOREHOUSE
    @FXML
    private ComboBox<String> yearComboBox;
    @FXML
    private ComboBox<String> isInStorehouseComboBox; //Czy wszystkie czy tylko te co mamy na stanie
    @FXML
    private Button loadStorehouseDataButton;          //Uruchamia ładowanie
//Póki co nie wiem czy jest potrzebne chyba nie :D
    @FXML
    private VBox mainVBox;
//Przyciski do edycji
    @FXML
    private Button addNewInstrumentButton;
    @FXML
    private Button leftInstrumentButton;
    @FXML
    private Button calibrateInstrumentButton;
    @FXML
    private Button editInstrumentButton;
//Deklaracja różnych zmiennych potrzebnych mniej lub bardziej

    //Element observable list. Zaznaczenie na TableView powoduje ustawienie zmiennej
    private storehouseFxModel editedStorehouseElementFromList;
    public void setEditedStorehouseElementFromList(storehouseFxModel editedStorehouseElementFromList) {
        this.editedStorehouseElementFromList = editedStorehouseElementFromList;
    }
    //Obiekty klas kontrolerów tych okien które coś robią
    private newInstrumentViewController newInstrumentController;                    //Kontroler widoku okna dodawania nowego przyrządu
    private calibratedInstrumentViewController calibratedInstrumentController;      //Kontroler widoku okna dodawania przyrządu do wzorcowania
    private leftInstrumentViewController leftInstrumentController;                  //Kontroler widoku okna wydawania przyrządu z magazynu
    private editInstrumentViewController editedInstrumentController;                //Kontroler widoku okna edycji przyrządu
    private editAddDateViewController editedAddDateController;
    private editLeftDateViewController editedLeftDateController;

    //Lista obiektów klasy storehouseModel, pobierane wprost z bazy danych
    private List<storehouseModel> storehouseModelList = new ArrayList<storehouseModel>();
    //Lista obiektów klasy storehouseFxModel zawiera przetworzoną listę storehouseModelList, zbindowana do storehouseTableView służy do wyświetlania
    private ObservableList<storehouseFxModel> storehouseFxObservableList = FXCollections.observableArrayList();
    private FilteredList<storehouseFxModel> filteredStorehouseFxObservableList = new FilteredList<>(storehouseFxObservableList, p -> true); //Lista filtrowana służy do szukania

    private mainViewController mainWindowController;

    public void setMainWindowController(mainViewController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    //Funkcja initialize wykonuje się konstruktorze, róże rzeczy tu można robić zwłaszcza że mamy już załadowane kontroli opisane wyżej
    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy storehouseViewController.");
        isInStorehouseComboBox.getItems().addAll("Wszystkie","W magazynie");
        isInStorehouseComboBox.setValue("Wszystkie");
        yearComboBox.setItems(getYearsList());
        yearComboBox.setValue("2019"); //Domyślnie będzie rok bieżący :)
        initializeTableView();
        addFilter();
    }
    //Pobiera listę obiektów storehouseModel z tabeli "STOREHOUSE" wypełnia jednocześnie listę obiektów storehouseFxObservableList
    //Oczywiscie możemy wybrać co chcemy pobrać z super wielkiej tabeli tak przyszłościowo :)
    public void getStorehouseList(){

        try {
            storehouseFxObservableList.clear();
            Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(),storehouseModel.class);
            QueryBuilder<storehouseModel, Integer> storehouseQueryBuilder = storehouseDao.queryBuilder();
            if(isInStorehouseComboBox.getValue().equals(yearComboBox.getValue())) {     //Tylko kiedy obydwa mają tę samą wartość całkiem przypadkowo :)
                storehouseModelList = storehouseDao.queryForAll();
                System.out.println("Wszystkie");
            }else{
                if(!isInStorehouseComboBox.getValue().equals("Wszystkie")&& yearComboBox.getValue().equals("Wszystkie")){
                    storehouseQueryBuilder.where().eq("leftDate","");
                    System.out.println("W magazynie");
                }else if(isInStorehouseComboBox.getValue().equals("Wszystkie")&& !yearComboBox.getValue().equals("Wszystkie")){
                    storehouseQueryBuilder.where().like("addDate","%"+yearComboBox.getValue()+"%").or().like("leftDate","%"+yearComboBox.getValue()+"%");
                    System.out.println("Wszystkie z danego roku");
                }else{
                    storehouseQueryBuilder.where().eq("leftDate","").and().like("addDate","%"+yearComboBox.getValue()+"%");
                    System.out.println("W magazynie z danego roku");
                }
                PreparedQuery<storehouseModel> prepare = storehouseQueryBuilder.prepare();
                storehouseModelList=storehouseDao.query(prepare);
            }
            Integer indeks = 0;//Ta konstrukcja jest potrzebna do połączenie wyników z bazydanych z tymi wyswietlanymi
            for (storehouseModel storehouseElement : storehouseModelList) {
                System.out.println(storehouseElement.toString());
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
    //Przypisywanie pól storehouseFxObservableList do storehouseTableView
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
        storehouseTableView.setItems(filteredStorehouseFxObservableList);
        storehouseTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null) {
                setEditedStorehouseElementFromList(newValue);
                showInformationAboutClient(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getInstrument().getClient());
                showInformationAboutHistory(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()));
            }

        });
    }
    @FXML   //Uruchamia okno edycji przyrządu
    private void editInstrument(){
        if(editedStorehouseElementFromList!=null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/instrument/editInstrumentView.fxml"));
                VBox vBox = loader.load();
                editedInstrumentController = loader.getController();
                if (editedInstrumentController != null){
                    editedInstrumentController.setStorehouseMainController(this);
                    editedInstrumentController.setEditedInstrument(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getInstrument());
                    editedInstrumentController.setClientInstrument(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getInstrument().getClient());
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
    private void setEditedInstrumentValues(){       //Wypełnia pola w oknie do edycji przyrządu
        editedInstrumentController.setInstrumentNameComboBox(editedStorehouseElementFromList.getInstrumentName());
        editedInstrumentController.setInstrumentTypeComboBox(editedStorehouseElementFromList.getInstrumentType());
        editedInstrumentController.setInstrumentProducerComboBox(editedStorehouseElementFromList.getInstrumentProducer());
        editedInstrumentController.setSerialNumberTextField(editedStorehouseElementFromList.getSerialNumber());
        editedInstrumentController.setIdentificationNumberTextField(editedStorehouseElementFromList.getIdentificationNumber());
        editedInstrumentController.setInstrumentRangeComboBox(editedStorehouseElementFromList.getInstrumentRange());
        editedInstrumentController.setInstrumentClientComboBox2(editedStorehouseElementFromList.getClient());
    }
    private void setCalibratedInstrumentLabels(){   //Wypełnia labele w oknie przenoszenia przyrządu do wzorcowania
        calibratedInstrumentController.setInstrumentNameLabel(editedStorehouseElementFromList.getInstrumentName());
        calibratedInstrumentController.setInstrumentTypeLabel(editedStorehouseElementFromList.getInstrumentType());
        calibratedInstrumentController.setInstrumentProducerLabel(editedStorehouseElementFromList.getInstrumentProducer());
        calibratedInstrumentController.setSerialNumberLabel(editedStorehouseElementFromList.getSerialNumber());
        calibratedInstrumentController.setIdentificationNumberLabel(editedStorehouseElementFromList.getIdentificationNumber());
        calibratedInstrumentController.setInstrumentRangeLabel(editedStorehouseElementFromList.getInstrumentRange());
        calibratedInstrumentController.setClientLabel(editedStorehouseElementFromList.getClient());
    }
    private void setLeftInstrumentLabels(){         //Wypełnia labele w oknie wydawania przyrządu
        leftInstrumentController.setInstrumentNameLabel(editedStorehouseElementFromList.getInstrumentName());
        leftInstrumentController.setInstrumentTypeLabel(editedStorehouseElementFromList.getInstrumentType());
        leftInstrumentController.setInstrumentProducerLabel(editedStorehouseElementFromList.getInstrumentProducer());
        leftInstrumentController.setSerialNumberLabel(editedStorehouseElementFromList.getSerialNumber());
        leftInstrumentController.setIdentificationNumberLabel(editedStorehouseElementFromList.getIdentificationNumber());
        leftInstrumentController.setInstrumentRangeLabel(editedStorehouseElementFromList.getInstrumentRange());
        leftInstrumentController.setClientLabel(editedStorehouseElementFromList.getClient());
    }
    @FXML
    private void addNewInstrument(){    //Uruchamia okno dodawania nowego przyrządu do magazynu
            try {
                setUser(mainWindowController.getUser());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/newInstrumentView.fxml"));
                VBox vBox = loader.load();
                newInstrumentController = loader.getController();
                if (newInstrumentController != null){
                    newInstrumentController.setStorehouseMainController(this);
                    newInstrumentController.setUser(user);
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
    public void calibrateInstrument(){  //Uruchamia okno przenoszenia przyrządu do wzorcowania
        if(editedStorehouseElementFromList!=null && editedStorehouseElementFromList.getLeftDate().equals("")) {
            if(!editedStorehouseElementFromList.getCalibrationDate().equals("")){
                if(ConfirmBox.display("Ponowne wzorcowanie!", "Czy chcesz wzorcować ten przyrząd ponownie ?")){
                    calibrateInstrumentAfterCheckConditions();
                }
            }else{
                calibrateInstrumentAfterCheckConditions();
            }
        }
    }
    public void calibrateInstrumentAfterCheckConditions(){
        try {
            setUser(mainWindowController.getUser());
            registerModel calibrateInstrument = new registerModel(0,0,storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getIdStorehouse(),
                    "",storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getCalibrationDate(),
                    storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()).getInstrument(),user,"","","ON");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/calibratedInstrumentView.fxml"));
            VBox vBox = loader.load();
            calibratedInstrumentController = loader.getController();
            if (calibratedInstrumentController != null){
                calibratedInstrumentController.setStorehouseMainController(this);
                calibratedInstrumentController.setCalibratedInstrument(calibrateInstrument);
                calibratedInstrumentController.setCalibratedInstrumentStorehouse(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()));
                calibratedInstrumentController.setUser(user);
                setCalibratedInstrumentLabels();
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
    @FXML
    public void leftInstrument(){   //Uruchamia okno wydawania przyrządu
        if(editedStorehouseElementFromList!=null && editedStorehouseElementFromList.getLeftDate().equals("")) {
            if(editedStorehouseElementFromList.getCalibrationDate().equals("")){
                if(ConfirmBox.display("Przyrząd  nie był wzorcowany !", "Czy na pewno chcesz go wydać bez wzorcowania ?")){
                    leftInstrumentAfterCheckConditions();
                }
            }else{
                leftInstrumentAfterCheckConditions();
            }
        }
    }
    public void leftInstrumentAfterCheckConditions(){
        try {
            setUser(mainWindowController.getUser());
            storehouseModel leftInstrument = storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/leftInstrumentView.fxml"));
            VBox vBox = loader.load();
            leftInstrumentController = loader.getController();
            if (leftInstrumentController != null){
                leftInstrumentController.setStorehouseMainController(this);
                leftInstrumentController.setLeftInstrument(leftInstrument);
                leftInstrumentController.setLeftInstrument(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()));
                leftInstrumentController.setUser(user);
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
    private void addFilter(){
        searchTextField.textProperty().addListener((value,oldValue, newValue) ->{
            filteredStorehouseFxObservableList.setPredicate(item -> {
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
    private void editAddDate(){
        if(editedStorehouseElementFromList!=null){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/editAddDateView.fxml"));
                VBox vBox = loader.load();
                editedAddDateController=loader.getController();
                if(editedAddDateController!=null){
                    editedAddDateController.setStorehouseMainController(this);
                    editedAddDateController.setEditedStorehouseElement(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()));
                    editedAddDateController.setAddDateDatePicker(LocalDate.parse(editedStorehouseElementFromList.getAddDate()));
                }
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Edytuj datę przyjęcia");
                Scene scene = new Scene(vBox);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void editLeftDate(){
        if(editedStorehouseElementFromList!=null && !editedStorehouseElementFromList.getLeftDate().equals("")){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/editLeftDateView.fxml"));
                VBox vBox = loader.load();
                editedLeftDateController=loader.getController();
                if(editedLeftDateController!=null){
                    editedLeftDateController.setStorehouseMainController(this);
                    editedLeftDateController.setEditedStorehouseElement(storehouseModelList.get(editedStorehouseElementFromList.getIndexOfStorehouseModelList()));
                    editedLeftDateController.setLeftDateDatePicker(LocalDate.parse(editedStorehouseElementFromList.getLeftDate()));
                }
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Edytuj datę przyjęcia");
                Scene scene = new Scene(vBox);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void loadStorehouseData(){
        getStorehouseList();
    }
    private void showInformationAboutClient(clientModel client){
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
    private void showInformationAboutHistory(storehouseModel storehouse){
        if(storehouse != null){
            addDateLabel.setText(storehouse.getAddDate());
            calibrationDateLabel.setText(storehouse.getCalibrationDate());
            leftDateLabel.setText(storehouse.getLeftDate());
            if(storehouse.getUserWhoAdd()!=null){
                addPersonLabel.setText(storehouse.getUserWhoAdd().getLogin());
            }else{
                addPersonLabel.setText("");
            }
            if(storehouse.getUserWhoCalibrate()!=null) {
                calibrationPersonLabel.setText(storehouse.getUserWhoCalibrate().getLogin());
            }else{
                calibrationPersonLabel.setText("");
            }
            if(storehouse.getUserWhoLeft()!=null) {
                leftPersonLabel.setText(storehouse.getUserWhoLeft().getLogin());
            }else{
                leftPersonLabel.setText("");
            }
            if(storehouse.getRemarks()!=null){
                remarksTextArea.setText(storehouse.getRemarks());
            }else{
                remarksTextArea.setText("");
            }
        }
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
