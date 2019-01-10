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
import model.registerModel;
import model.storehouseModel;

import java.sql.SQLException;
import java.util.List;

public class leftInstrumentViewController {
    public leftInstrumentViewController() {System.out.println("Halo świry jestem kontruktorem klasy leftInstrumentViewController");
    }


    @FXML
    private void initialize(){
        System.out.println("Halo świry jestem funkcją initialize klasy leftInstrumentViewController");

    }
    private storehouseModel leftInstrument;

    public void setLeftInstrument(storehouseModel leftInstrument) {
        this.leftInstrument = leftInstrument;
    }

    private storehouseViewController storehouseMainController;

    public void setStorehouseMainController(storehouseViewController storehouseMainController) {
        this.storehouseMainController = storehouseMainController;
    }

    private registerModel calibratedInstrument;

    public void setCalibratedInstrument(registerModel calibratedInstrument) {
        this.calibratedInstrument = calibratedInstrument;
    }

    @FXML
    private Button cancelAddNewInstrumentButton;
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
    private Button addNewInstrumentButton;

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
                try {
                    Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(), storehouseModel.class);
                    leftInstrument.setLeftDate(leftDateDatePicker.getValue().toString());
                    storehouseDao.update(leftInstrument);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                informationLabel.setText("Wybierz datę wydania");
            }
        }
    }

}
