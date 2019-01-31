package controller.storehouse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.storehouseModel;
import model.userModel;
import util.Close;
import util.Converter;
import java.sql.SQLException;
import java.time.LocalDate;


public class leftInstrumentViewController {
    public leftInstrumentViewController() {}

    @FXML
    VBox mainVBox;

    @FXML
    private Label instrumentNameLabel;
    @FXML
    private Label instrumentProducerLabel;
    @FXML
    private DatePicker leftDateDatePicker;
    @FXML
    private Label instrumentRangeLabel;
    @FXML
    private Label identificationNumberLabel;
    @FXML
    private Label serialNumberLabel;
    @FXML
    private Label clientLabel;
    @FXML
    private Label instrumentTypeLabel;
    @FXML
    private Label informationLabel;
    //Potrzebne do ustawienia danych w nowo otwartym oknie
    public void setInstrumentNameLabel(String instrumentName) {
        this.instrumentNameLabel.setText(instrumentName);
    }
    public void setInstrumentProducerLabel(String instrumentProducer) {
        this.instrumentProducerLabel.setText(instrumentProducer);
    }
    public void setInstrumentRangeLabel(String instrumentRange) {
        this.instrumentRangeLabel.setText(instrumentRange);
    }
    public void setIdentificationNumberLabel(String identificationNumber) {
        this.identificationNumberLabel.setText(identificationNumber);
    }
    public void setSerialNumberLabel(String serialNumber) {
        this.serialNumberLabel.setText(serialNumber);
    }

    public void setClientLabel(String client) {
        this.clientLabel.setText(client);
    }

    public void setInstrumentTypeLabel(String instrumentType) {
        this.instrumentTypeLabel.setText(instrumentType);
    }

    private storehouseModel leftInstrument;
    public void setLeftInstrument(storehouseModel leftInstrument) {
        this.leftInstrument = leftInstrument;
    }

    private storehouseViewController storehouseMainController;
    public void setStorehouseMainController(storehouseViewController storehouseMainController) {
        this.storehouseMainController = storehouseMainController;
    }

    private userModel user;
    public void setUser(userModel user) {
        this.user = user;
    }

    @FXML
    private void initialize(){
        leftDateDatePicker.setConverter(Converter.getConverter());
    }

    @FXML
    public void leftInstrument(){
        if(leftInstrument!=null) {
            if (leftDateDatePicker.getValue() != null) {
                if(leftInstrument.getCalibrationDate().equals("")){ // Bez wzorcowania
                    if(leftDateDatePicker.getValue().isBefore(LocalDate.parse(leftInstrument.getAddDate()))){
                        informationLabel.setText("Data wydanie jest wcześniejsza niż przyjęcia !");
                    }else{
                        try {
                            Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(), storehouseModel.class);
                            leftInstrument.setLeftDate(leftDateDatePicker.getValue().toString());
                            leftInstrument.setUserWhoLeft(user);
                            storehouseDao.update(leftInstrument);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Close.closeVBoxWindow(mainVBox);
                        storehouseMainController.getStorehouseList();
                    }
                }else{
                    if(leftDateDatePicker.getValue().isBefore(LocalDate.parse(leftInstrument.getCalibrationDate()))){
                        informationLabel.setText("Data wydanie jest wcześniejsza niż wzorcowania !");
                    }else{
                        try {
                            Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(), storehouseModel.class);
                            leftInstrument.setLeftDate(leftDateDatePicker.getValue().toString());
                            leftInstrument.setUserWhoLeft(user);
                            storehouseDao.update(leftInstrument);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Close.closeVBoxWindow(mainVBox);
                        storehouseMainController.getStorehouseList();
                    }
                }
            }
            else{
                informationLabel.setText("Wybierz datę wydania");
            }
        }
    }
    @FXML
    private void cancelLeftInstrument(){
        Close.closeVBoxWindow(mainVBox);
    }
}
