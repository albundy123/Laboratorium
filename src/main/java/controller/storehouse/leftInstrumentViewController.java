package controller.storehouse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.registerModel;
import model.storehouseModel;
import model.userModel;
import util.Converter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class leftInstrumentViewController {
    public leftInstrumentViewController() {System.out.println("Halo świry jestem kontruktorem klasy leftInstrumentViewController");
    }

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
        System.out.println("Halo świry jestem funkcją initialize klasy leftInstrumentViewController");
        leftDateDatePicker.setConverter(Converter.getConverter());
    }

    @FXML
    public void leftInstrument(){
        if(leftInstrument!=null) {
            if (leftDateDatePicker.getValue() != null) {
                if(leftInstrument.getCalibrationDate().equals("")){ // Bez wzorcowania
                    if(!leftDateDatePicker.getValue().isAfter(LocalDate.parse(leftInstrument.getAddDate()))){
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
                        Stage window = (Stage) mainVBox.getScene().getWindow();
                        window.close();
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
                        Stage window = (Stage) mainVBox.getScene().getWindow();
                        window.close();
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
        Stage window = (Stage) mainVBox.getScene().getWindow();
        window.close();
    }
}
