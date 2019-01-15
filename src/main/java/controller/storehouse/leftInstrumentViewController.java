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
    private void initialize(){
        System.out.println("Halo świry jestem funkcją initialize klasy leftInstrumentViewController");
        leftDateDatePicker.setConverter(Converter.getConverter());

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
    private Button leftInstrumentButton;
    @FXML
    private Button cancelLeftInstrumentButton;

    @FXML
    private Label informationLabel;

    public void setInstrumentNameLabel(String instrumentNameLabel) {
        this.instrumentNameLabel.setText(instrumentNameLabel);
    }

    public void setInstrumentProducerLabel(String instrumentProducerLabel) {
        this.instrumentProducerLabel.setText(instrumentProducerLabel);
    }

    public void setInstrumentRangeLabel(String instrumentRangeLabel) {
        this.instrumentRangeLabel.setText(instrumentRangeLabel);
    }

    public void setIdentificationNumberLabel(String identificationNumberLabel) {
        this.identificationNumberLabel.setText(identificationNumberLabel);
    }

    public void setSerialNumberLabel(String serialNumberLabel) {
        this.serialNumberLabel.setText(serialNumberLabel);
    }

    public void setClientLabel(String clientLabel) {
        this.clientLabel.setText(clientLabel);
    }

    public void setInstrumentTypeLabel(String instrumentTypeLabel) {
        this.instrumentTypeLabel.setText(instrumentTypeLabel);
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
