package controller.register;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import dbUtil.dbSqlite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.fxModel.registerFxModel;
import model.fxModel.storehouseFxModel;
import model.registerModel;
import model.storehouseModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class registerViewController {
    public  registerViewController() {System.out.println("Siemanko jestem konstruktorem klasy  registerViewController.");}


    @FXML
    private TableColumn<registerFxModel, String> calibrationDateColumn;
    @FXML
    private TextField fullNameSearchTextField;
    @FXML
    private TableColumn<registerFxModel, String> instrumentIdentificationNumberColumn;
    @FXML
    private TableColumn<registerFxModel, String> userColumn;
    @FXML
    private TextField shortNameTextField;
    @FXML
    private VBox mainVBox;
    @FXML
    private TableColumn<registerFxModel, String> instrumentSerialNumberColumn;
    @FXML
    private TableColumn<registerFxModel, String> cardNumberColumn;
    @FXML
    private TableColumn<registerFxModel, String> instrumentClientColumn;
    @FXML
    private TextField cityTextField;
    @FXML
    private TableView<registerFxModel> registerTableView;
    @FXML
    private TableColumn<registerFxModel, Integer> idRegisterByYearColumn;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TableColumn<registerFxModel, String> instrumentNameColumn;
    @FXML
    private TableColumn<registerFxModel, String> certificateNumberColumn;
    @FXML
    private Button editInstrumentButton;
    @FXML
    private TextField streetTextField;




    private List<registerModel> registerModelList = new ArrayList<registerModel>();
    private ObservableList<registerFxModel> registerFxObservableList = FXCollections.observableArrayList();

private registerFxModel editedRegisterElementFromList;

    public void setEditedRegisterElementFromList(registerFxModel editedRegisterElementFromList) {
        this.editedRegisterElementFromList = editedRegisterElementFromList;
    }

    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy registerViewController.");
    getStorehouseList();
    initializeTableView();

    }

    public void getStorehouseList(){
        try {
            registerFxObservableList.clear();
            Dao<registerModel, Integer> registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(),registerModel.class);
            registerModelList = registerDao.queryForAll();
            Integer indeks = 0;
            for (registerModel registerElement : registerModelList) {
                registerFxObservableList.add(new registerFxModel(indeks, registerElement.getIdRegisterByYear(),registerElement.getCardNumber(),
                        registerElement.getCalibrationDate(), registerElement.getInstrument().getInstrumentName().getInstrumentName(),
                        registerElement.getInstrument().getSerialNumber(),registerElement.getInstrument().getIdentificationNumber(),
                        registerElement.getInstrument().getClient().getShortName()));
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



       registerTableView.setItems(registerFxObservableList);
        registerTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setEditedRegisterElementFromList(newValue);
            //    showInformation(newValue);
        });
    }

}
