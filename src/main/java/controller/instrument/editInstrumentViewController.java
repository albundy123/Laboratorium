package controller.instrument;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import controller.clientViewController;
import controller.storehouse.storehouseViewController;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class editInstrumentViewController {
    public editInstrumentViewController() {}

    private static final String INSTRUMENT_NAME_VIEW = "/instrument/newInstrumentNameView.fxml";
    private static final String INSTRUMENT_TYPE_VIEW = "/instrument/newInstrumentTypeView.fxml";
    private static final String INSTRUMENT_PRODUCER_VIEW = "/instrument/newInstrumentProducerView.fxml";
    private static final String INSTRUMENT_RANGE_VIEW = "/instrument/newInstrumentRangeView.fxml";
    private static final String INSTRUMENT_CLIENT_VIEW="/client/clientView.fxml";

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

    private instrumentViewController editedInstrumentMainController;
    public void setEditedInstrumentMainController(instrumentViewController editedInstrumentMainController) {this.editedInstrumentMainController = editedInstrumentMainController; }
    private storehouseViewController storehouseMainController;
    public void setStorehouseMainController(storehouseViewController storehouseMainController) {this.storehouseMainController = storehouseMainController;}

    private instrumentModel editedInstrument;
    public void setEditedInstrument(instrumentModel editedInstrument) {
        this.editedInstrument = editedInstrument;
    }

    //Obiekty które stworzą nowy przyrząd
    private clientModel clientInstrument;
    public void setClientInstrument(clientModel clientInstrument) {
        this.clientInstrument = clientInstrument;
    }



    //Wstrzyknięcia elementów z FXMLA
    @FXML
    private VBox mainVBox;
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
    private Label informationLabel;
//Settery do elementów z formularzy
    public void setInstrumentNameComboBox(String instrumentNameComboBox) {
        this.instrumentNameComboBox.setValue(instrumentNameComboBox);
    }
    public void setInstrumentTypeComboBox(String instrumentTypeComboBox) {
        this.instrumentTypeComboBox.setValue(instrumentTypeComboBox);
    }
    public void setInstrumentProducerComboBox(String instrumentProducerComboBox) {
        this.instrumentProducerComboBox.setValue(instrumentProducerComboBox);
    }
    public void setInstrumentRangeComboBox(String instrumentRangeComboBox) {
        this.instrumentRangeComboBox.setValue(instrumentRangeComboBox);
    }
    public void setInstrumentClientComboBox2(String instrumentClientComboBox) {
        this.instrumentClientComboBox.setValue(instrumentClientComboBox);
    }
    public void setSerialNumberTextField(String serialNumberTextField) {
        this.serialNumberTextField.setText(serialNumberTextField);
    }
    public void setIdentificationNumberTextField(String identificationNumberTextField) {
        this.identificationNumberTextField.setText(identificationNumberTextField);
    }
    public void setInstrumentClientComboBox(String instrumentClientComboBox) {
        this.instrumentClientComboBox.setValue(instrumentClientComboBox);
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
    }
    @FXML
    private void addNewInstrumentName(){
        newInstrumentName=loadInstrumentData(newInstrumentName,INSTRUMENT_NAME_VIEW,"Nazwa");
        newInstrumentName.setEditInstrumentMainController(this);
    }
    @FXML
    private void addNewInstrumentType(){
        newInstrumentType=loadInstrumentData(newInstrumentType,INSTRUMENT_TYPE_VIEW,"Typ");
        newInstrumentType.setEditInstrumentMainController(this);
    }
    @FXML
    private void addNewInstrumentProducer(){
        newInstrumentProducer=loadInstrumentData(newInstrumentProducer,INSTRUMENT_PRODUCER_VIEW,"Producent");
        newInstrumentProducer.setEditInstrumentMainController(this);
    }
    @FXML
    private void addNewInstrumentRange(){
        newInstrumentRange=loadInstrumentData(newInstrumentRange,INSTRUMENT_RANGE_VIEW,"Zakres");
        newInstrumentRange.setEditInstrumentMainController(this);
    }
    @FXML
    private void addClientInstrument(){
        clientMainController=loadInstrumentData(clientMainController,INSTRUMENT_CLIENT_VIEW,"Zleceniodawcy");
        clientMainController.setEditInstrumentMainController(this);
        clientMainController.setChoseButtonDisable();
        clientMainController.getActiveClients();
    }
    private <T> T loadInstrumentData(T instrumentData,String resource, String title){//Generyczna metoda do ładowania różnych widoków
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
    public instrumentNameModel getName(String instrumentName){
        for (instrumentNameModel name : instrumentNameList) {
            if (name.getInstrumentName().equals(instrumentName)) {
                return name;
            }
        }
        return null;
    }
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
    @FXML
    private void editInstrument(){
        if(isValidInstrumentData()){

            saveEditInstrument();
        }
    }
    private void saveEditInstrument(){
        try {
            Dao<instrumentModel, Integer> instrumentDao= DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentModel.class);
            QueryBuilder<instrumentModel, Integer> instrumentQueryBuilder = instrumentDao.queryBuilder();
            if(!serialNumberTextField.getText().isEmpty() && !identificationNumberTextField.getText().isEmpty()) {
                instrumentQueryBuilder.where().eq("serialNumber", serialNumberTextField.getText()).or().eq("identificationNumber", identificationNumberTextField.getText());
            }else if(!serialNumberTextField.getText().isEmpty() && identificationNumberTextField.getText().isEmpty()){
                instrumentQueryBuilder.where().eq("serialNumber", serialNumberTextField.getText());
            }else if(serialNumberTextField.getText().isEmpty() && !identificationNumberTextField.getText().isEmpty()){
                instrumentQueryBuilder.where().eq("identificationNumber", identificationNumberTextField.getText());
            }
            instrumentModel instrument = new instrumentModel(editedInstrument.getIdInstrument(), getName(instrumentNameComboBox.getValue()), getType(instrumentTypeComboBox.getValue()), getProducer(instrumentProducerComboBox.getValue()), (serialNumberTextField.getText().trim()).replaceAll("\\s+",""), (identificationNumberTextField.getText().trim()).replaceAll("\\s+",""), getRange(instrumentRangeComboBox.getValue()), clientInstrument);
            PreparedQuery<instrumentModel> prepare = instrumentQueryBuilder.prepare();
            List<instrumentModel> result = instrumentDao.query(prepare);
            if(result.size()>1 ) {
                    informationLabel.setText("Nie możesz dodać drugiego takiego samego przyrządu !");
            } else if(result.size()==1 && result.get(0).getIdInstrument().equals(editedInstrument.getIdInstrument())) {
                instrumentDao.update(instrument);
                Close.closeVBoxWindow(mainVBox);
            }
            else if(result.size()==0){//Edycja przyrzadu nie spowoduje zdublowania innego przyrzadu
                instrumentDao.update(instrument);
                Close.closeVBoxWindow(mainVBox);
            }
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
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
    @FXML
    private void cancelSaveInstrument(){
        Close.closeVBoxWindow(mainVBox);
    }
    private boolean isValidInstrumentData() {
        String errorMessage = "";
        if (getName(instrumentNameComboBox.getValue()) == null) {
            errorMessage += "Nie wybrałeś nazwy urządzenia ! \n";
        }
        if (getType(instrumentTypeComboBox.getValue()) == null) {
            errorMessage += "Nie wybrałeś typu urządzenia ! \n";
        }
        if (getProducer(instrumentProducerComboBox.getValue()) == null) {
            errorMessage += "Nie wybrałeś producenta urządzenia ! \n";
        }
        if ((serialNumberTextField.getText().isEmpty())&&(identificationNumberTextField.getText().isEmpty())) {
            errorMessage += "Przyrząd musi posiadać numer fabryczny lub numer identyfikacyjny ! \n";
        }
        if (getRange(instrumentRangeComboBox.getValue()) == null) {
            errorMessage += "Nie wybrałeś zakresu urządzenia ! \n";
        }
        if (clientInstrument == null ) {
            errorMessage += "Nie wybrałeś zleceniodawcy ! \n";
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
}
