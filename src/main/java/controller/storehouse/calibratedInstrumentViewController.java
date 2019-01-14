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
import javafx.util.StringConverter;
import model.fxModel.storehouseFxModel;
import model.registerModel;
import model.storehouseModel;
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
    //Połączenie do głównego kontrolera
    private storehouseViewController storehouseMainController;
    public void setStorehouseMainController(storehouseViewController storehouseMainController) {
        this.storehouseMainController = storehouseMainController;
    }
    private storehouseModel calibratedInstrumentStorehouse;
    public void setCalibratedInstrumentStorehouse(storehouseModel calibratedInstrumentStorehouse) {
        this.calibratedInstrumentStorehouse = calibratedInstrumentStorehouse;
    }




    //Obiekt, który będzie wzorcowany
    private registerModel calibratedInstrument;
    public void setCalibratedInstrument(registerModel calibratedInstrument) {
        this.calibratedInstrument = calibratedInstrument;
    }
    @FXML
    VBox mainVBox;

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
    private Button addCalibrateInstrumentButton;
    @FXML
    private Button cancelAddCalibrateInstrumentButton;

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
                if (!calibrationDateDatePicker.getValue().isBefore(LocalDate.parse(calibratedInstrumentStorehouse.getAddDate()))){
                    Dao<registerModel, Integer> registerDao=null;
                    QueryBuilder<registerModel, Integer> registerQueryBuilder=null;
                    PreparedQuery<registerModel> prepare;
                    List<registerModel> result=null;
                    try {
                        registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), registerModel.class);
                        registerQueryBuilder = registerDao.queryBuilder();
                        registerQueryBuilder.where().gt("calibrationDate", calibrationDateDatePicker.getValue());
                        prepare = registerQueryBuilder.prepare();
                        result = registerDao.query(prepare);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if(result.isEmpty()){
                        try {
                           // registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), registerModel.class);
                            registerDao.create(calibratedInstrument);
                            registerQueryBuilder.where().eq("idRegister", calibratedInstrument.getIdRegister() - 1);
                            prepare = registerQueryBuilder.prepare();
                            result = registerDao.query(prepare);
                            calibratedInstrument.setCalibrationDate(calibrationDateDatePicker.getValue().toString());
                            if (result.isEmpty()) { //znaczy się ze pierwszy wpis :)
                                calibratedInstrument.setIdRegisterByYear(1);
                                calibratedInstrument.setCardNumber("1-2019");
                                registerDao.update(calibratedInstrument);
                            } else { //nie jest to pierwszy wpis
                                calibratedInstrument.setIdRegisterByYear(result.get(0).getIdRegisterByYear() + 1);
                                calibratedInstrument.setCardNumber(result.get(0).getIdRegisterByYear() + 1 + "-2019");
                                registerDao.update(calibratedInstrument);
                            }
                            Dao<storehouseModel,Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(),storehouseModel.class);
                            calibratedInstrumentStorehouse.setCalibrationDate(calibrationDateDatePicker.getValue().toString());
                            storehouseDao.update(calibratedInstrumentStorehouse);
                            storehouseMainController.getStorehouseList();
                            Stage window = (Stage) mainVBox.getScene().getWindow();
                            window.close();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else{
                        informationLabel.setText("Data wzorcowania jest wcześniejsza niż ostatnia w rejestrze!");
                    }
                }else{
                    informationLabel.setText("Data wzorcowania jest wcześniejsza niż data przyjęcia !");
                }
            }
            else{
                informationLabel.setText("Wybierz datę wzorcowania !");
            }
        }
    }
    @FXML
    private void cancelAddCalibrateInstrument(){
        Stage window = (Stage) mainVBox.getScene().getWindow();
        window.close();
    }
}
