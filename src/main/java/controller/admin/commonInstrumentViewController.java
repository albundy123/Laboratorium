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
import javafx.stage.Stage;
import model.*;
import model.fxModel.commonFxModel;
import util.showAlert;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Klasa kontrolera odpowiada ze obsługę widoku CommonInstrumentView, w którym można dodawać, edytować lub usuwać wartości z tabel takich jak nazwy przyrządu itd.
 */
public class commonInstrumentViewController {
    public commonInstrumentViewController() {}
    //Deklaracje stałych do poszczególnych widoków
    private static final String NAME_VIEW = "/instrument/newInstrumentNameView.fxml";
    private static final String TYPE_VIEW = "/instrument/newInstrumentTypeView.fxml";
    private static final String PRODUCER_VIEW = "/instrument/newInstrumentProducerView.fxml";
    private static final String RANGE_VIEW = "/instrument/newInstrumentRangeView.fxml";
    private static final String UNIT_VIEW = "/instrument/newInstrumentUnitView.fxml";
    private static final String YEAR_VIEW = "/admin/yearView.fxml";
    //Wstrzyknięcie elementów z FXMLa
    @FXML
    private TableView<commonFxModel> commonTableView;
    @FXML
    private TableColumn<commonFxModel, Integer> idColumn;
    @FXML
    private TableColumn<commonFxModel, String> valueColumn;

    public void setValueColumn(String text) {
        this.valueColumn.setText(text);
    }
    //Listy potrzebne do przechowywania danych pobranych z bazy
    private List<instrumentNameModel> nameList;
    private List<instrumentTypeModel> typeList;
    private List<instrumentProducerModel> producerList;
    private List<instrumentRangeModel> rangeList;
    private List<unitModel> unitList;
    private List<yearModel> yearList;
    //Lista do wyświetlania elementów w TableView
    private ObservableList<commonFxModel> commonObservableList = FXCollections.observableArrayList();

    private commonFxModel editedElementFromList;
    public void setEditedElementFromList(commonFxModel editedElementFromList) {
        this.editedElementFromList = editedElementFromList;
    }
    //Zmienna przechowuje informacje o tym czego dotyczy dana operacja
    private Integer parameter;
    public void setParameter(Integer parameter) {
        this.parameter = parameter;
    }
    //Obiekty poszczególnych kontrolerów, potrzebne w opcji edycji
    private newInstrumentNameViewController newInstrumentNameController;
    private newInstrumentTypeViewController newInstrumentTypeController;
    private newInstrumentProducerViewController newInstrumentProducerController;
    private newInstrumentRangeViewController newInstrumentRangeController;
    private newInstrumentUnitViewController newInstrumentUnitController;
    private yearViewController yearController;
    @FXML
    private void initialize(){
        initializeTable();
    }

    public void initializeTable(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idCommon"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("commonString"));
        commonTableView.setItems(commonObservableList);
        commonTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setEditedElementFromList(newValue));
    }
    /**Pobieranie nazw przyrządów z bazy danych*/
    public void getNames(){
        commonObservableList.clear();
        try {
            Dao<instrumentNameModel, Integer> instrumentNameDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentNameModel.class);
            nameList=instrumentNameDao.queryForAll();
            nameList.forEach(name ->{
                commonObservableList.add(new commonFxModel(name.getIdInstrumentName(),name.getInstrumentName()));
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }
    /**Pobieranie typów przyrządów z bazy danych*/
    public void getTypes(){
        commonObservableList.clear();
        try {
            Dao<instrumentTypeModel, Integer> instrumentTypeDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentTypeModel.class);
            typeList=instrumentTypeDao.queryForAll();
            typeList.forEach(type ->{
                commonObservableList.add(new commonFxModel(type.getIdInstrumentType(),type.getInstrumentType()));
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }
    /**Pobieranie producentów przyrządów z bazy danych*/
    public void getProducers(){
        commonObservableList.clear();
        try {
            Dao<instrumentProducerModel, Integer> instrumentProducerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentProducerModel.class);
            producerList=instrumentProducerDao.queryForAll();
            producerList.forEach(producer ->{
                commonObservableList.add(new commonFxModel(producer.getIdInstrumentProducer(),producer.getInstrumentProducer()));
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }
    /**Pobieranie zakresów pomiarowych z bazy danych*/
    public void getRanges(){
        commonObservableList.clear();
        try {
            Dao<instrumentRangeModel, Integer> instrumentRangeDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentRangeModel.class);
            rangeList=instrumentRangeDao.queryForAll();
            rangeList.forEach(range ->{
                commonObservableList.add(new commonFxModel(range.getIdInstrumentRange(),range.getInstrumentRange()));
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }
    /**Pobieranie jednostek bazy danych*/
    public void getUnits(){
        commonObservableList.clear();
        try {
            Dao<unitModel, Integer> unitDao = DaoManager.createDao(dbSqlite.getConnectionSource(), unitModel.class);
            unitList=unitDao.queryForAll();
            unitList.forEach(unit ->{
                commonObservableList.add(new commonFxModel(unit.getIdUnit(),unit.getUnitName()));
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }
    /**Pobieranie lat bazy danych*/
    public void getYears(){
        commonObservableList.clear();
        try {
            Dao<yearModel, Integer> yearDao = DaoManager.createDao(dbSqlite.getConnectionSource(), yearModel.class);
            yearList=yearDao.queryForAll();
            yearList.forEach(year ->{
                commonObservableList.add(new commonFxModel(year.getIdYear(),year.getYear()));
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }
    //@FXML oznacza że metody są podpięte do przycisków
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
                    showAlert.display(e.getMessage());
                }
            } else {
                deleteParameter();
            }
        }
    }

    /**
     * Metoda służy do dodawania nowych wartości do tabel z nazwami przyrządów, typami przyrządów itd
     * W zależności od tego co chcemy dodać ładuje się inny widok i inna jest obsługa przycisków
     */
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
        }else if(parameter==6){
            yearController=loadWindow(yearController, YEAR_VIEW,"Lata");
            yearController.setEditedYear(null);
            yearController.setCommonInstrumentController(this);
        }
    }
    /**
     * Metoda służy do edycji parametrów takich jak nazwa przyrządu, typ przyrządu itd.
     * W zależności od tego co chcemy wyedytować ładuje się inny widok i inna jest obłsuga przycisków
     */
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
            } else if (parameter == 6) {
                yearController = loadWindow(yearController, YEAR_VIEW, "Lata");
                yearController.setYearTextField(editedElementFromList.getCommonString());
                yearController.setEditedYear(new yearModel(editedElementFromList.getIdCommon(), editedElementFromList.getCommonString()));
                yearController.setCommonInstrumentController(this);
            }
        }
    }
    /**
     * Metoda służy do usuwania wybranego elementu z bazy danych, może to być nazwa przyrządu, typ przyrządu itd
     */
    private void deleteParameter(){
        try {
            if(parameter==6){
                Dao<yearModel, Integer> yearDao = DaoManager.createDao(dbSqlite.getConnectionSource(),yearModel.class);
                yearDao.delete(new yearModel(editedElementFromList.getIdCommon(),editedElementFromList.getCommonString()));
                getYears();
            }else{
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
                else {//Jeżeli dany elemnet był już wykorzystany, czyli jest wpisany w inną tabelę to nie można go usunąć nie dotyczy roku
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Nie można usunąć tego pola");
                    alert.showAndWait();
                }
            }
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }
    /**
     * Metoda służy do ładowania różnych okien. Okna zbudowana są analogicznie.
     * @param controller
     * @param resource
     * @param title
     * @param <T>
     * @return
     */
    private <T> T loadWindow(T controller,String resource, String title){//Generyczna metoda do otwierania widoków
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
            showAlert.display(e.getMessage());
        }
        return controller;
    }

    /**
     * Metoda rozbija zakres przyrządu na poszczególne pola, z których się składa
     * @param range
     * @param controller
     */
    private void decodeRange(String range, newInstrumentRangeViewController controller){//Umożliwia edycję zakresu pomiarowego, który ejst specjalnie zbudowanym stringiem
        String begin=range.substring(1,range.indexOf('d')-1);
        String end=range.substring(range.indexOf('o')+1,range.indexOf(')'));
        String unit=range.substring(range.indexOf(')')+2,range.length());
        controller.setNewInstrumentRangeTextField1(begin);
        controller.setNewInstrumentRangeTextField2(end);
        controller.setNewInstrumentRangeUnitComboBox(unit);
    }

    /**
     * Metoda zmienia nagłówek pierwszej kolumny w wyświetlanym kontrolerze TableView w zależności od tego, które okno otwarliśmy
     * @return
     */
    private String getColumnName(){
        if (parameter == 1) {
            return "instrumentName_id";
        } else if (parameter == 2) {
            return "instrumentType_id";
        } else if (parameter == 3) {
            return "instrumentProducer_id";
        } else if (parameter == 4) {
            return "instrumentRange_id";
        }else if (parameter == 6) {
            return "year";
        }
        return null;
    }
}
