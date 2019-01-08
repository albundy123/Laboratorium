package controller.instrument;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import dbUtil.dbSqlite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.clientModel;
import model.fxModel.instrumentFxModel;
import model.instrumentModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class instrumentViewController {
    public instrumentViewController() {System.out.println("Siemanko jestem konstruktorem klasy instrumentViewController.");}
    private newInstrumentViewController newInstrumentMainController;

    public void setNewInstrumentMainController(newInstrumentViewController newInstrumentMainController) {
        this.newInstrumentMainController = newInstrumentMainController;
    }

    //Tabela z danymi
    @FXML
    private TableView<instrumentFxModel> instrumentTableView;
    @FXML
    private TableColumn<instrumentFxModel, Integer> idInstrumentColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentNameColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentTypeColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentProducerColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentSerialNumberColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentIdentificationNumberColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentRangeColumn;
    @FXML
    private TableColumn<instrumentFxModel, String> instrumentClientColumn;


    @FXML
    private VBox mainVBox;

    //Wyświetlanie zleceniodawcy
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField shortNameTextField;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField streetTextField;



    @FXML
    private TextField fullNameSearchTextField;
    @FXML
    private Button editClientButton;
    @FXML


    private List<instrumentModel> instrumentModelList = new ArrayList<instrumentModel>();
    private ObservableList<instrumentFxModel> instrumentFxObservableList = FXCollections.observableArrayList();
    FilteredList<instrumentFxModel> filteredInstrumentFxObservableList = new FilteredList<>(instrumentFxObservableList, p -> true);

    private instrumentFxModel editedInstrumentFromList;

    public void setEditedInstrumentFromList(instrumentFxModel editedInstrumentFromList) {
        this.editedInstrumentFromList = editedInstrumentFromList;
    }

    private newInstrumentViewController editedInstrumentController;

    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy instrumentViewController.");
        getInstruments();
        initializeTableView();

    }

    public void editClient(){

    }


    public void getInstruments(){
        try {
            instrumentFxObservableList.clear();
            Dao<instrumentModel, Integer> instrumentDao = DaoManager.createDao(dbSqlite.getConnectionSource(),instrumentModel.class);
            instrumentModelList = instrumentDao.queryForAll();
            Integer index =0;
            instrumentModelList.forEach(instrument ->{
                instrumentFxObservableList.add(new instrumentFxModel(index, instrument.getIdInstrument(),
                        instrument.getInstrumentName().getInstrumentName(), instrument.getInstrumentType().getInstrumentType(),
                        instrument.getInstrumentProducer().getInstrumentProducer(), instrument.getSerialNumber(),
                        instrument.getIdentificationNumber(),instrument.getInstrumentRange().getInstrumentRange(),
                        instrument.getClient().getShortName()));
            });
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
        instrumentTableView.setItems(instrumentFxObservableList);
        instrumentTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setEditedInstrumentFromList(newValue);
        //    showInformation(newValue);
        });
    }

    @FXML
    private void editInstrument(){
        if(editedInstrumentFromList!=null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/instrument/newInstrumentView.fxml"));
                VBox vBox = loader.load();
                editedInstrumentController = loader.getController();
                if (editedInstrumentController != null){
                    editedInstrumentController.setEditedInstrumentMainController(this);
                    editedInstrumentController.setEditedInstrument(instrumentModelList.get(editedInstrumentFromList.getIndexOfInstrumentModelList()));
                    setEditedInstrumentValues();

                }
                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Edytuj wybranego klienta");
                Scene scene = new Scene(vBox);
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void setEditedInstrumentValues(){
        editedInstrumentController.setInstrumentNameComboBox(editedInstrumentFromList.getInstrumentName());
        editedInstrumentController.setInstrumentTypeComboBox(editedInstrumentFromList.getInstrumentType());
        editedInstrumentController.setInstrumentProducerComboBox(editedInstrumentFromList.getInstrumentProducer());
        editedInstrumentController.setSerialNumberTextField(editedInstrumentFromList.getSerialNumber());
        editedInstrumentController.setIdentificationNumberTextField(editedInstrumentFromList.getIdentificationNumber());
        editedInstrumentController.setInstrumentRangeComboBox(editedInstrumentFromList.getInstrumentRange());
        editedInstrumentController.setInstrumentClientComboBox2(editedInstrumentFromList.getClient());
    }
    private void showInformation(clientModel client){
        if(client != null){
            shortNameTextField.setText(client.getShortName());
            fullNameTextField.setText(client.getFullName());
            cityTextField.setText(client.getPostCode()+ " "+ client.getCity());
            if(client.getFlatNumber().isEmpty()) {
                streetTextField.setText(client.getStreet() + " " + client.getHouseNumber());
            }else{
                streetTextField.setText(client.getStreet() + " " + client.getHouseNumber()+"/"+client.getFlatNumber());
            }
        }

    }
}
