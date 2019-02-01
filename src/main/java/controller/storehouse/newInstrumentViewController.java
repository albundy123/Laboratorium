package controller.storehouse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import controller.clientViewController;
import controller.instrument.*;
import dbUtil.dbSqlite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import util.Close;
import util.Converter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class newInstrumentViewController {
    public newInstrumentViewController() {}
    //Deklaracja stałych ze ścieżkami do widoków fxml
    private static final String INSTRUMENT_NAME_VIEW = "/instrument/newInstrumentNameView.fxml";
    private static final String INSTRUMENT_TYPE_VIEW = "/instrument/newInstrumentTypeView.fxml";
    private static final String INSTRUMENT_PRODUCER_VIEW = "/instrument/newInstrumentProducerView.fxml";
    private static final String INSTRUMENT_RANGE_VIEW = "/instrument/newInstrumentRangeView.fxml";
    private static final String INSTRUMENT_CLIENT_VIEW="/client/clientView.fxml";
    //Obiekty klas kontrolerów okien, z którymi wymagana jest komunikacja
    private newInstrumentNameViewController newInstrumentName;
    private newInstrumentTypeViewController newInstrumentType;
    private newInstrumentProducerViewController newInstrumentProducer;
    private newInstrumentRangeViewController newInstrumentRange;
    private clientViewController clientMainController;

    //Listy do ComboBoxów trochę przerost formy nad tręścią ale w sumie pożyteczna sprawa
    private ObservableList<String> instrumentNameObservableList = FXCollections.observableArrayList();
    private FilteredList<String> filteredNames = new FilteredList<String>(instrumentNameObservableList, p -> true);
    private ObservableList<String> instrumentTypeObservableList = FXCollections.observableArrayList();
    private FilteredList<String> filteredTypes = new FilteredList<String>(instrumentTypeObservableList, p -> true);
    private ObservableList<String> instrumentProducerObservableList = FXCollections.observableArrayList();
    private FilteredList<String> filteredProducers = new FilteredList<String>(instrumentProducerObservableList, p -> true);
    private ObservableList<String> instrumentRangeObservableList = FXCollections.observableArrayList();
    private FilteredList<String> filteredRange = new FilteredList<String>(instrumentRangeObservableList, p -> true);
    //Listy obiektów z tabel
    private List<instrumentNameModel> instrumentNameList;
    private List<instrumentTypeModel> instrumentTypeList;
    private List<instrumentProducerModel> instrumentProducerList;
    private List<instrumentRangeModel> instrumentRangeList;

    //Obiekt potrzebny, żeby można było się odwoływać do metod obiektu nadrzędnego dla tego kontrolera czyli do storehouseViewController np. odświeżenie listy w magazynie
    private storehouseViewController storehouseMainController;
    public void setStorehouseMainController(storehouseViewController storehouseMainController) {
        this.storehouseMainController = storehouseMainController;
    }

    private instrumentModel newInstrumentModel;
    public void setNewInstrumentModel(instrumentModel newInstrumentModel) {
        this.newInstrumentModel = newInstrumentModel;
    }

    //Obiekty które stworzą nowy przyrząd
    private clientModel clientInstrument;
    public void setClientInstrument(clientModel clientInstrument) {
        this.clientInstrument = clientInstrument;
    }

    private userModel user;
    public void setUser(userModel user) {
        this.user = user;
    }

    //Wstrzyknięcia elementów z FXMLA
    @FXML
    VBox mainVBox;
    @FXML
    private ComboBox<String> instrumentNameComboBox;
    @FXML
    private ComboBox<String> instrumentTypeComboBox;
    @FXML
    private ComboBox<String> instrumentProducerComboBox;
    @FXML
    private ComboBox<String> instrumentRangeComboBox;
    @FXML
    private ComboBox<String> instrumentClientComboBox;
    @FXML
    private TextField serialNumberTextField;
    @FXML
    private TextField identificationNumberTextField;
    @FXML
    private DatePicker addDateDatePicker;
    @FXML
    private TextArea newInstrumentTextArea;
    @FXML
    private Label serialNumberCheckResultLabel;
    @FXML
    private Label identificationNumberCheckResultLabel;

    //Setery potrzebn, żeby ustawić wstępne wartości z obiektu klasy storehouseViewController jak wybieramy edit
    public void setInstrumentNameComboBox(String instrumentName) {
        this.instrumentNameComboBox.setValue(instrumentName);
    }
    public void setInstrumentTypeComboBox(String instrumentType) {
        this.instrumentTypeComboBox.setValue(instrumentType);
    }
    public void setInstrumentProducerComboBox(String instrumentProducer) {
        this.instrumentProducerComboBox.setValue(instrumentProducer);
    }
    public void setInstrumentRangeComboBox(String instrumentRange) {
        this.instrumentRangeComboBox.setValue(instrumentRange);
    }
    public void setInstrumentClientComboBox2(String instrumentClient) {
        this.instrumentClientComboBox.setValue(instrumentClient);
    }
    public void setSerialNumberTextField(String serialNumber) {
        this.serialNumberTextField.setText(serialNumber);
    }
    public void setIdentificationNumberTextField(String identificationNumber) {
        this.identificationNumberTextField.setText(identificationNumber);
    }
    public void setInstrumentClientComboBox(String instrumentClient) {
        this.instrumentClientComboBox.setValue(instrumentClient);
    }
    @FXML
    private void initialize(){
        getInstrumentNameList();
        initComboBox(instrumentNameComboBox,filteredNames);
        getInstrumentTypeList();
        initComboBox(instrumentTypeComboBox,filteredTypes);
        getInstrumentProducerList();
        initComboBox(instrumentProducerComboBox,filteredProducers);
        getInstrumentRangeList();
        initComboBox(instrumentRangeComboBox,filteredRange);
        addDateDatePicker.setConverter(Converter.getConverter());
    }
    @FXML
    private void addNewInstrumentName(){
        newInstrumentName=loadInstrumentData(newInstrumentName,INSTRUMENT_NAME_VIEW,"Nazwa");
        newInstrumentName.setNewInstrumentMainController(this);
    }
    @FXML
    private void addNewInstrumentType(){
        newInstrumentType=loadInstrumentData(newInstrumentType,INSTRUMENT_TYPE_VIEW,"Typ");
        newInstrumentType.setNewInstrumentMainController(this);
    }
    @FXML
    private void addNewInstrumentProducer(){
        newInstrumentProducer=loadInstrumentData(newInstrumentProducer,INSTRUMENT_PRODUCER_VIEW,"Producent");
        newInstrumentProducer.setNewInstrumentMainController(this);
    }
    @FXML
    private void addNewInstrumentRange(){
        newInstrumentRange=loadInstrumentData(newInstrumentRange,INSTRUMENT_RANGE_VIEW,"Zakres");
        newInstrumentRange.setNewInstrumentMainController(this);
    }
    @FXML
    private void addClientInstrument(){
        clientMainController=loadInstrumentData(clientMainController,INSTRUMENT_CLIENT_VIEW,"Zleceniodawcy");
        clientMainController.setNewInstrumentMainController(this);
        clientMainController.setChoseButtonDisable();
        clientMainController.getActiveClients();
    }
    private <T> T loadInstrumentData(T instrumentData,String resource, String title){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            VBox vBox = loader.load();
            instrumentData=loader.getController();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(title);
            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instrumentData;
    }
    //Metody, które pobierają listy z konkretnych tabel bazy danych, które przechowują dane do ComboBoxów
    public void getInstrumentNameList() {
        try {
            instrumentNameObservableList.clear();
            Dao<instrumentNameModel, Integer> instrumentNameDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentNameModel.class);
            instrumentNameList = instrumentNameDao.queryForAll();
            instrumentNameList.forEach(instrumentName -> {
                instrumentNameObservableList.add(instrumentName.getInstrumentName());
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getInstrumentTypeList(){
        try {
            instrumentTypeObservableList.clear();
            Dao<instrumentTypeModel, Integer> instrumentTypeDao = DaoManager.createDao(dbSqlite.getConnectionSource(),instrumentTypeModel.class);
            instrumentTypeList = instrumentTypeDao.queryForAll();
            instrumentTypeList.forEach(instrumentType ->{
                instrumentTypeObservableList.add(instrumentType.getInstrumentType());
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getInstrumentProducerList(){
        try {
            instrumentProducerObservableList.clear();
            Dao<instrumentProducerModel, Integer> instrumentProducerDao = DaoManager.createDao(dbSqlite.getConnectionSource(),instrumentProducerModel.class);
            instrumentProducerList = instrumentProducerDao.queryForAll();
            instrumentProducerList.forEach(instrumentProducer ->{
                instrumentProducerObservableList.add(instrumentProducer.getInstrumentProducer());
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getInstrumentRangeList() {
        try {
            instrumentRangeObservableList.clear();
            Dao<instrumentRangeModel, Integer> instrumentRangeDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentRangeModel.class);
            instrumentRangeList = instrumentRangeDao.queryForAll();
            instrumentRangeList.forEach(instrumentRange -> {
                instrumentRangeObservableList.add(instrumentRange.getInstrumentRange());
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public instrumentNameModel getName(String instrumentName){
        for (instrumentNameModel name : instrumentNameList) {
            if (name.getInstrumentName().equals(instrumentName)) {
                return name;
            }
        }
        return null;
    }
    //Zwracają cały obiekt znając tylko wartość pola String
    public instrumentTypeModel getType(String instrumentType){
        for (instrumentTypeModel type : instrumentTypeList) {
            if (type.getInstrumentType().equals(instrumentType)) {
                return type;
            }
        }
        return null;
    }
    public instrumentProducerModel getProducer(String instrumentProducer){
        for (instrumentProducerModel producer : instrumentProducerList) {
            if (producer.getInstrumentProducer().equals(instrumentProducer)) {
                return producer;
            }
        }
        return null;
    }
    public instrumentRangeModel getRange(String instrumentRange){
        for (instrumentRangeModel range : instrumentRangeList) {
            if (range.getInstrumentRange().equals(instrumentRange)) {
                return range;
            }
        }
        return null;
    }
    //Dodawania nowego przyrządu
    public void addNewInstrument(){
        if(isValidInstrumentData()){
            addNewInstrumentAfterCheckData();
        }
    }
    private void addNewInstrumentAfterCheckData(){
        try {
            setUser(storehouseMainController.getUser());
            Dao<instrumentModel, Integer> instrumentDao= DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentModel.class);
            QueryBuilder<instrumentModel, Integer> instrumentQueryBuilder = instrumentDao.queryBuilder();
            if(!serialNumberTextField.getText().isEmpty() && !identificationNumberTextField.getText().isEmpty()) {
                instrumentQueryBuilder.where().eq("serialNumber", serialNumberTextField.getText()).or().eq("identificationNumber", identificationNumberTextField.getText());
            }else if(!serialNumberTextField.getText().isEmpty() && identificationNumberTextField.getText().isEmpty()){
                instrumentQueryBuilder.where().eq("serialNumber", serialNumberTextField.getText());
            }else if(serialNumberTextField.getText().isEmpty() && !identificationNumberTextField.getText().isEmpty()){
                instrumentQueryBuilder.where().eq("identificationNumber", identificationNumberTextField.getText());
            }
            instrumentModel instrument = new instrumentModel(0, getName(instrumentNameComboBox.getValue()), getType(instrumentTypeComboBox.getValue()), getProducer(instrumentProducerComboBox.getValue()), (serialNumberTextField.getText().trim()).replaceAll("\\s+",""), (identificationNumberTextField.getText().trim()).replaceAll("\\s+",""), getRange(instrumentRangeComboBox.getValue()), clientInstrument);
            PreparedQuery<instrumentModel> prepare = instrumentQueryBuilder.prepare();
            List<instrumentModel> result = instrumentDao.query(prepare);
            storehouseModel storehouse = new storehouseModel(0,instrument,addDateDatePicker.getValue().toString(),user,"",null,"",null,newInstrumentTextArea.getText());
            Dao<storehouseModel, Integer> storehouseDao= DaoManager.createDao(dbSqlite.getConnectionSource(), storehouseModel.class);
            if(result.isEmpty()) {
                instrumentDao.create(instrument);
                storehouseDao.create(new storehouseModel(0,instrument,addDateDatePicker.getValue().toString(),user,"",null,"",null,newInstrumentTextArea.getText()));
            }else{
                instrument=result.get(0);
                QueryBuilder<storehouseModel, Integer> storehouseQueryBuilder = storehouseDao.queryBuilder();
                storehouseQueryBuilder.where().eq("instrument_id", instrument.getIdInstrument()).and().eq("leftDate","");
                PreparedQuery<storehouseModel> storehousePrepare = storehouseQueryBuilder.prepare();
                List<storehouseModel> storehouseResult=storehouseDao.query(storehousePrepare);
                if(!storehouseResult.isEmpty()){ //Blokuje możliwość dodania do magazynu przyrządu który nie został wydany, tzn. ciagle jest na stanie
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Nieprawidłowe dane");
                    alert.setHeaderText("Proszę zweryfikować dane lub uzupełnić wpis dotyczący przyrządu");
                    alert.setContentText("Przyrząd jest już na stanie w magazynie!!!");
                    alert.showAndWait();
                }else {
                    storehouseDao.create(new storehouseModel(0, instrument, addDateDatePicker.getValue().toString(), user, "", null, "", null, newInstrumentTextArea.getText()));
                }
            }
            Close.closeVBoxWindow(mainVBox);
            storehouseMainController.getStorehouseList();
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Metoda służy do konfiguracji ComboBoxów, żeby można było filtrować w nich wartości
    private void initComboBox(ComboBox<String> comboBox, FilteredList<String> filteredList){
        comboBox.setEditable(true);
        comboBox.getEditor().textProperty().addListener((v, oldValue, newValue) -> {
            final TextField editor = comboBox.getEditor();
            final String selected = comboBox.getSelectionModel().getSelectedItem();
            if (selected == null || !selected.equals(editor.getText())) {
                filteredList.setPredicate(item -> {
                    if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }
        });
        comboBox.setItems(filteredList);
    }
    //Prosta metoda do sprawdzania poprawności danych
    private boolean isValidInstrumentData() {
        String errorMessage = "";
        if (instrumentNameComboBox.getValue() == null) {
            errorMessage += "Nie wybrałeś nazwy urządzenia ! \n";
        }
        if (instrumentTypeComboBox.getValue() == null) {
            errorMessage += "Nie wybrałeś typu urządzenia ! \n";
        }
        if (instrumentProducerComboBox.getValue() == null) {
            errorMessage += "Nie wybrałeś producenta urządzenia ! \n";
        }
        if ((serialNumberTextField.getText().isEmpty())&&(identificationNumberTextField.getText().isEmpty())) {
            errorMessage += "Przyrząd musi posiadać numer fabryczny lub numer identyfikacyjny ! \n";
        }
        if (instrumentRangeComboBox.getValue() == null) {
            errorMessage += "Nie wybrałeś zakresu urządzenia ! \n";
        }
        if (instrumentClientComboBox.getValue() == null ) {
            errorMessage += "Nie wybrałeś zleceniodawcy ! \n";
        }
        if (addDateDatePicker.getValue() == null ) {
            errorMessage += "Nie wybrałeś prawidłowo daty dodania ! \n";
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
    @FXML
    private void checkBySerialNumber(){ //metody do przyspieszania wprowadzania nowych przyrządów na stan magazynu
        if(!serialNumberTextField.getText().isEmpty()){
            try {
                Dao<instrumentModel, Integer>instrumentDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentModel.class);
                QueryBuilder<instrumentModel, Integer> instrumentQueryBuilder = instrumentDao.queryBuilder();
                instrumentQueryBuilder.where().eq("serialNumber", serialNumberTextField.getText());
                PreparedQuery<instrumentModel> prepare = instrumentQueryBuilder.prepare();
                List<instrumentModel> result = instrumentDao.query(prepare);
                if(result.isEmpty()) {
                    serialNumberCheckResultLabel.setText("Nie znaleziono");
                    instrumentNameComboBox.setValue(null);
                    instrumentTypeComboBox.setValue(null);
                    instrumentProducerComboBox.setValue(null);
                    identificationNumberTextField.setText("");
                    instrumentRangeComboBox.setValue(null);
                    instrumentClientComboBox.setValue(null);
                    setClientInstrument(null);
                }
                else{
                    instrumentNameComboBox.setValue(result.get(0).getInstrumentName().getInstrumentName());
                    instrumentTypeComboBox.setValue(result.get(0).getInstrumentType().getInstrumentType());
                    instrumentProducerComboBox.setValue(result.get(0).getInstrumentProducer().getInstrumentProducer());
                    identificationNumberTextField.setText(result.get(0).getIdentificationNumber());
                    instrumentRangeComboBox.setValue(result.get(0).getInstrumentRange().getInstrumentRange());
                    instrumentClientComboBox.setValue(result.get(0).getClient().getShortName());
                    setClientInstrument(result.get(0).getClient());
                }
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void checkByIdentificationNumber(){
        if(!identificationNumberTextField.getText().isEmpty()){
            try {
                Dao<instrumentModel, Integer>instrumentDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentModel.class);
                QueryBuilder<instrumentModel, Integer> instrumentQueryBuilder = instrumentDao.queryBuilder();
                instrumentQueryBuilder.where().eq("identificationNumber", identificationNumberTextField.getText());
                PreparedQuery<instrumentModel> prepare = instrumentQueryBuilder.prepare();
                List<instrumentModel> result = instrumentDao.query(prepare);
                if(result.isEmpty()) {
                    identificationNumberCheckResultLabel.setText("Nie znaleziono");
                    instrumentNameComboBox.setValue(null);
                    instrumentTypeComboBox.setValue(null);
                    instrumentProducerComboBox.setValue(null);
                    serialNumberTextField.setText("");
                    instrumentRangeComboBox.setValue(null);
                    instrumentClientComboBox.setValue(null);
                    setClientInstrument(null);
                }
                else{
                    instrumentNameComboBox.setValue(result.get(0).getInstrumentName().getInstrumentName());
                    instrumentTypeComboBox.setValue(result.get(0).getInstrumentType().getInstrumentType());
                    instrumentProducerComboBox.setValue(result.get(0).getInstrumentProducer().getInstrumentProducer());
                    serialNumberTextField.setText(result.get(0).getSerialNumber());
                    instrumentRangeComboBox.setValue(result.get(0).getInstrumentRange().getInstrumentRange());
                    instrumentClientComboBox.setValue(result.get(0).getClient().getShortName());
                    setClientInstrument(result.get(0).getClient());
                }
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void cancelAddNewInstrument(){
        Close.closeVBoxWindow(mainVBox);
    }
}
