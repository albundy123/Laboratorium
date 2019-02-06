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
import model.*;
import model.fxModel.storehouseFxModel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.ConfirmBox;
import util.Converter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class storehouseViewController {
    public  storehouseViewController() {}

    private userModel user;
    public void setUser(userModel user) {
        this.user = user;
    }
    public userModel getUser() {
        return user;
    }
    private yearModel year;
    private void setYear(yearModel year) {
        this.year = year;
    }

    //Wstrzyknięcie TableView i poszczególnych kolumn, musi być ze względu na wyświetlanie
    @FXML
    private VBox mainVBox;
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
    private TableColumn<storehouseFxModel, String> addDateColumn;
    @FXML
    private TableColumn<storehouseFxModel, String> calibrationDateColumn;
    @FXML
    private TableColumn<storehouseFxModel, String> leftDateColumn;
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
    private Label remarksLabel;
    //Pole tekstowe do filtrowania listy
    @FXML
    private TextField searchTextField;
    //Elementy do ładowania danych z tabeli STOREHOUSE
    @FXML
    private ComboBox<String> yearComboBox;
    @FXML
    private ComboBox<String> isInStorehouseComboBox; //Czy wszystkie czy tylko te co mamy na stanie
//Deklaracja różnych zmiennych potrzebnych mniej lub bardziej

    //Element observable list. Zaznaczenie na TableView powoduje ustawienie zmiennej
    private storehouseFxModel storehouseFxElementFromList;
    public void setStorehouseFxElementFromList(storehouseFxModel storehouseFxElementFromList) {
        this.storehouseFxElementFromList = storehouseFxElementFromList;
    }
    //I jednocześnie ustawienie tej zmiennej
    private storehouseModel storehouseElement;
    public void setStorehouseElement(storehouseModel storehouseElement) {
        this.storehouseElement = storehouseElement;
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
        isInStorehouseComboBox.getItems().addAll("Wszystkie","W magazynie");
        isInStorehouseComboBox.setValue("Wszystkie");
        yearComboBox.setItems(getYearsList());
        yearComboBox.setValue(year.getYear()); //Domyślnie będzie rok bieżący :)
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
            }else{
                if(!isInStorehouseComboBox.getValue().equals("Wszystkie")&& yearComboBox.getValue().equals("Wszystkie")){
                    storehouseQueryBuilder.where().eq("leftDate","");
                }else if(isInStorehouseComboBox.getValue().equals("Wszystkie")&& !yearComboBox.getValue().equals("Wszystkie")){
                    storehouseQueryBuilder.where().like("addDate","%"+yearComboBox.getValue()+"%").or().like("leftDate","%"+yearComboBox.getValue()+"%");
                }else{
                    storehouseQueryBuilder.where().eq("leftDate","").and().like("addDate","%"+yearComboBox.getValue()+"%");
                }
                PreparedQuery<storehouseModel> prepare = storehouseQueryBuilder.prepare();
                storehouseModelList=storehouseDao.query(prepare);
            }
            Integer indeks = 1;//Ta konstrukcja jest potrzebna do połączenie wyników z bazydanych z tymi wyswietlanymi
            for (storehouseModel storehouseElement : storehouseModelList) {
                System.out.println(storehouseElement.toString());
                storehouseFxModel storehouseFx= Converter.convertToStorehouseFx(storehouseElement);
                storehouseFx.setIndexOfStorehouseModelList(indeks);
                storehouseFxObservableList.add(storehouseFx);
                indeks++;
            }
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Przypisywanie pól storehouseFxObservableList do storehouseTableView
    private void initializeTableView(){
        idInstrumentColumn.setCellValueFactory(new PropertyValueFactory<>("indexOfStorehouseModelList"));
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
                setStorehouseFxElementFromList(newValue);
                setStorehouseElement(storehouseModelList.get(storehouseFxElementFromList.getIndexOfStorehouseModelList()-1));
                showInformationAboutClient(storehouseElement.getInstrument().getClient());
                showInformationAboutHistory(storehouseElement);
            }
        });
        storehouseTableView.prefHeightProperty().bind(mainVBox.heightProperty().multiply(0.68));
    }
    @FXML   //Uruchamia okno edycji przyrządu
    private void editInstrument(){
        if(storehouseFxElementFromList!=null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/instrument/editInstrumentView.fxml"));
                VBox vBox = loader.load();
                editedInstrumentController = loader.getController();
                if (editedInstrumentController != null){
                    editedInstrumentController.setStorehouseMainController(this);
                    editedInstrumentController.setEditedInstrument(storehouseElement.getInstrument());
                    editedInstrumentController.setClientInstrument(storehouseElement.getInstrument().getClient());
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
        editedInstrumentController.setInstrumentNameComboBox(storehouseFxElementFromList.getInstrumentName());
        editedInstrumentController.setInstrumentTypeComboBox(storehouseFxElementFromList.getInstrumentType());
        editedInstrumentController.setInstrumentProducerComboBox(storehouseFxElementFromList.getInstrumentProducer());
        editedInstrumentController.setSerialNumberTextField(storehouseFxElementFromList.getSerialNumber());
        editedInstrumentController.setIdentificationNumberTextField(storehouseFxElementFromList.getIdentificationNumber());
        editedInstrumentController.setInstrumentRangeComboBox(storehouseFxElementFromList.getInstrumentRange());
        editedInstrumentController.setInstrumentClientComboBox2(storehouseFxElementFromList.getClient());
    }
    private void setCalibratedInstrumentLabels(){   //Wypełnia labele w oknie przenoszenia przyrządu do wzorcowania
        calibratedInstrumentController.setInstrumentNameLabel(storehouseFxElementFromList.getInstrumentName());
        calibratedInstrumentController.setInstrumentTypeLabel(storehouseFxElementFromList.getInstrumentType());
        calibratedInstrumentController.setInstrumentProducerLabel(storehouseFxElementFromList.getInstrumentProducer());
        calibratedInstrumentController.setSerialNumberLabel(storehouseFxElementFromList.getSerialNumber());
        calibratedInstrumentController.setIdentificationNumberLabel(storehouseFxElementFromList.getIdentificationNumber());
        calibratedInstrumentController.setInstrumentRangeLabel(storehouseFxElementFromList.getInstrumentRange());
        calibratedInstrumentController.setClientLabel(storehouseFxElementFromList.getClient());
    }
    private void setLeftInstrumentLabels(){         //Wypełnia labele w oknie wydawania przyrządu
        leftInstrumentController.setInstrumentNameLabel(storehouseFxElementFromList.getInstrumentName());
        leftInstrumentController.setInstrumentTypeLabel(storehouseFxElementFromList.getInstrumentType());
        leftInstrumentController.setInstrumentProducerLabel(storehouseFxElementFromList.getInstrumentProducer());
        leftInstrumentController.setSerialNumberLabel(storehouseFxElementFromList.getSerialNumber());
        leftInstrumentController.setIdentificationNumberLabel(storehouseFxElementFromList.getIdentificationNumber());
        leftInstrumentController.setInstrumentRangeLabel(storehouseFxElementFromList.getInstrumentRange());
        leftInstrumentController.setClientLabel(storehouseFxElementFromList.getClient());
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
    public void calibrateInstrument1(){  //Uruchamia okno przenoszenia przyrządu do wzorcowania
        if(storehouseFxElementFromList!=null && storehouseFxElementFromList.getLeftDate().equals("")) {
            if(!storehouseFxElementFromList.getCalibrationDate().equals("")){
                if(ConfirmBox.display("Ponowne wzorcowanie!", "Czy chcesz wzorcować ten przyrząd ponownie ?")){
                    calibrateInstrumentAfterCheckConditions(1,"W zakresie akredytacji AP 131");
                }
            }else{
                calibrateInstrumentAfterCheckConditions(1,"W zakresie akredytacji AP 131");
            }
        }
    }
    @FXML
    public void calibrateInstrument2(){  //Uruchamia okno przenoszenia przyrządu do wzorcowania
        if(storehouseFxElementFromList!=null && storehouseFxElementFromList.getLeftDate().equals("")) {
            if(!storehouseFxElementFromList.getCalibrationDate().equals("")){
                if(ConfirmBox.display("Ponowne wzorcowanie!", "Czy chcesz wzorcować ten przyrząd ponownie ?")){
                    calibrateInstrumentAfterCheckConditions(2,"Poza zakresem akredytacji");
                }
            }else{
                calibrateInstrumentAfterCheckConditions(2,"Poza zakresem akredytacji");
            }
        }
    }
    public void calibrateInstrumentAfterCheckConditions(int whichRegister,String label){
        try {
            setUser(mainWindowController.getUser());
            registerModel calibrateInstrument = new registerModel(0,0,storehouseElement.getIdStorehouse(),
                    "",storehouseElement.getCalibrationDate(), storehouseElement.getInstrument(),user,"","","ON");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/calibratedInstrumentView.fxml"));
            VBox vBox = loader.load();
            calibratedInstrumentController = loader.getController();
            if (calibratedInstrumentController != null){
                calibratedInstrumentController.setStorehouseMainController(this);
                calibratedInstrumentController.setCalibratedInstrument(calibrateInstrument);
                calibratedInstrumentController.setCalibratedInstrumentStorehouse(storehouseElement);
                calibratedInstrumentController.setUser(user);
                calibratedInstrumentController.setYear(year);
                calibratedInstrumentController.setWhichRegister(whichRegister);
                calibratedInstrumentController.setRegisterLabel(label);
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
        if(storehouseFxElementFromList!=null && storehouseFxElementFromList.getLeftDate().equals("")) {
            if(storehouseFxElementFromList.getCalibrationDate().equals("")){
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
            storehouseModel leftInstrument = storehouseModelList.get(storehouseFxElementFromList.getIndexOfStorehouseModelList()-1);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/leftInstrumentView.fxml"));
            VBox vBox = loader.load();
            leftInstrumentController = loader.getController();
            if (leftInstrumentController != null){
                leftInstrumentController.setStorehouseMainController(this);
                leftInstrumentController.setLeftInstrument(leftInstrument);
                leftInstrumentController.setLeftInstrument(storehouseElement);
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
        if(storehouseFxElementFromList!=null){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/editAddDateView.fxml"));
                VBox vBox = loader.load();
                editedAddDateController=loader.getController();
                if(editedAddDateController!=null){
                    editedAddDateController.setStorehouseMainController(this);
                    editedAddDateController.setEditedStorehouseElement(storehouseElement);
                    editedAddDateController.setAddDateDatePicker(LocalDate.parse(storehouseFxElementFromList.getAddDate()));
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
        if(storehouseFxElementFromList!=null && !storehouseFxElementFromList.getLeftDate().equals("")){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/editLeftDateView.fxml"));
                VBox vBox = loader.load();
                editedLeftDateController=loader.getController();
                if(editedLeftDateController!=null){
                    editedLeftDateController.setStorehouseMainController(this);
                    editedLeftDateController.setEditedStorehouseElement(storehouseElement);
                    editedLeftDateController.setLeftDateDatePicker(LocalDate.parse(storehouseFxElementFromList.getLeftDate()));
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
                remarksLabel.setText(storehouse.getRemarks());
            }else{
                remarksLabel.setText("");
            }
        }
    }
    public ObservableList<String> getYearsList() {
        ObservableList<String> yearObservableList = FXCollections.observableArrayList();
        try {
            Dao<yearModel, Integer> yearDao = DaoManager.createDao(dbSqlite.getConnectionSource(), yearModel.class);
            List<yearModel> yearList = yearDao.queryForAll();
            setYear(yearList.get(yearList.size()-1));
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
    @FXML
    private void exportToExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet("Arkusz1");
        Row row = spreadsheet.createRow(0);
        //Nazwy kolumn
        row.createCell(0).setCellValue("Lp.");
        row.createCell(1).setCellValue("Nazwa");
        row.createCell(2).setCellValue("Typ");
        row.createCell(3).setCellValue("Producent");
        row.createCell(4).setCellValue("Nr fabryczny");
        row.createCell(5).setCellValue("Nr identyfikacyjny");
        row.createCell(6).setCellValue("Zakres pomiarowy");
        row.createCell(7).setCellValue("Zleceniodawca");
        row.createCell(8).setCellValue("Data przyjęcia");
        row.createCell(9).setCellValue("Data wzorcowania");
        row.createCell(10).setCellValue("Data wydania");

        int i=0;
        for (storehouseFxModel storehouseElement : filteredStorehouseFxObservableList) {
            row = spreadsheet.createRow(i + 1);
            row.createCell(0).setCellValue(i+1);
            row.createCell(1).setCellValue(storehouseElement.getInstrumentName());
            row.createCell(2).setCellValue(storehouseElement.getInstrumentType());
            row.createCell(3).setCellValue(storehouseElement.getInstrumentProducer());
            row.createCell(4).setCellValue(storehouseElement.getSerialNumber());
            row.createCell(5).setCellValue(storehouseElement.getIdentificationNumber());
            row.createCell(6).setCellValue(storehouseElement.getInstrumentRange());
            row.createCell(7).setCellValue(storehouseElement.getClient());
            row.createCell(8).setCellValue(storehouseElement.getAddDate());
            row.createCell(9).setCellValue(storehouseElement.getCalibrationDate());
            row.createCell(10).setCellValue(storehouseElement.getLeftDate());
            i++;
        }
        for(int j=0;j<11;j++){
            spreadsheet.autoSizeColumn(j);
        }
        FileOutputStream fileOut = new FileOutputStream("Magazyn.xlsx");
        workbook.write(fileOut);
        fileOut.close();
    }
}
