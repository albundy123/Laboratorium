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
import javafx.util.StringConverter;
import model.registerModel;
import util.Converter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class calibratedInstrumentViewController {
    public calibratedInstrumentViewController() {System.out.println("Halo świry jestem kontruktorem klasy calibratedInstrumentViewController");
    }
    @FXML
    private void initialize(){
        System.out.println("Halo świry jestem funkcją initialize klasy calibratedInstrumentViewController");
        calibrationDateDatePicker.setConverter(Converter.getConverter());

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
    private DatePicker calibrationDateDatePicker;
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
    public void calibrateInstrument(){
        if(calibratedInstrument!=null) {
            if (calibrationDateDatePicker.getValue() != null) {
                try {
                    Dao<registerModel, Integer> registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), registerModel.class);
                    registerDao.create(calibratedInstrument);
                    Dao<registerModel, Integer> registerDao2 = DaoManager.createDao(dbSqlite.getConnectionSource(), registerModel.class);
                    QueryBuilder<registerModel, Integer> registerQueryBuilder = registerDao2.queryBuilder();
                    registerQueryBuilder.where().eq("idRegister", calibratedInstrument.getIdRegister() - 1);
                    PreparedQuery<registerModel> prepare = registerQueryBuilder.prepare();
                    List<registerModel> result = registerDao2.query(prepare);
                    calibratedInstrument.setCalibrationDate(calibrationDateDatePicker.getValue().toString());
                    if (result.isEmpty()) { //znaczy się ze pierwszy wpis :)
                        calibratedInstrument.setIdRegisterByYear(1);
                        calibratedInstrument.setCardNumber("1-2018");
                        registerDao.update(calibratedInstrument);
                    } else { //nie jest to pierwszy wpis
                        calibratedInstrument.setIdRegisterByYear(result.get(0).getIdRegisterByYear() + 1);
                        calibratedInstrument.setCardNumber(result.get(0).getIdRegisterByYear() + 1 + "-2019");
                        registerDao.update(calibratedInstrument);
                    }

                    System.out.println();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                informationLabel.setText("Wybierz datę wzorcowania");
            }
        }
    }

}
