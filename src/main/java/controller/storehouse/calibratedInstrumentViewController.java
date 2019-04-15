package controller.storehouse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.*;
import util.Close;
import util.Converter;
import util.showAlert;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Klasa kontrolera odpowiedzialnego za obsługę okna, które służy do dodawania przyrządu do wzorcowania
 */
public class calibratedInstrumentViewController {
    public calibratedInstrumentViewController() {}

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
    private Label informationLabel;
    @FXML
    private Label registerLabel;

    public void setRegisterLabel(String register) {
        this.registerLabel.setText(register);
    }

    //Do ładowania danych o przyrzadzie do wzorcowania
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

    //Połączenie do głównego kontrolera
    private storehouseViewController storehouseMainController;
    public void setStorehouseMainController(storehouseViewController storehouseMainController) {
        this.storehouseMainController = storehouseMainController;
    }

    private storehouseModel calibratedInstrumentStorehouse;
    public void setCalibratedInstrumentStorehouse(storehouseModel calibratedInstrumentStorehouse) {
        this.calibratedInstrumentStorehouse = calibratedInstrumentStorehouse;
    }

    private userModel user;
    public void setUser(userModel user) {
        this.user = user;
    }
    private yearModel year;
    public void setYear(yearModel year) {
        this.year = year;
    }

    //Obiekt, który będzie wzorcowany
    private int whichRegister=0;
    public void setWhichRegister(int whichRegister){
        this.whichRegister=whichRegister;
    }
    private registerModel calibratedInstrument;
    public void setCalibratedInstrument(registerModel calibratedInstrument) {
        this.calibratedInstrument = calibratedInstrument;
    }
    private register2Model calibratedInstrument2;
    public void setCalibratedInstrument2(register2Model calibratedInstrument2) {
        this.calibratedInstrument2 = calibratedInstrument2;
    }
    @FXML
    private void initialize(){
        calibrationDateDatePicker.setConverter(Converter.getConverter());
    }
    @FXML
    public void calibrateInstrument(){
        if(calibratedInstrument!=null) {
            if (calibrationDateDatePicker.getValue() != null) {
                if (!calibrationDateDatePicker.getValue().isBefore(LocalDate.parse(calibratedInstrumentStorehouse.getAddDate()))) {

                    if (whichRegister == 1) {
                        calibrateInstrumentInAP();
                    } else if (whichRegister == 2) {
                        calibrateInstrumentOutAP();
                    }
                } else {
                    informationLabel.setText("Data wzorcowania jest wcześniejsza niż data przyjęcia !");
                }
            } else {
                informationLabel.setText("Wybierz datę wzorcowania !");
            }
        }
    }

    /**
     * Metoda służy do dodawania przyrządu do rejestru wzorcowania w zakresie AP.
     * Zawiera mechanizmu kontroli prawidłowości danych
     */
    private void calibrateInstrumentInAP() {
        Dao<registerModel, Integer> registerDao = null;
        QueryBuilder<registerModel, Integer> registerQueryBuilder = null;
        PreparedQuery<registerModel> prepare;
        List<registerModel> result = null;
        try {
            registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), registerModel.class);
            registerQueryBuilder = registerDao.queryBuilder();
            registerQueryBuilder.where().gt("calibrationDate", calibrationDateDatePicker.getValue());
            prepare = registerQueryBuilder.prepare();
            result = registerDao.query(prepare);
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
        if (result.isEmpty()) {
            try {
                // registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), registerModel.class);
                registerDao.create(calibratedInstrument);
                registerQueryBuilder.where().eq("idRegister", calibratedInstrument.getIdRegister() - 1);
                prepare = registerQueryBuilder.prepare();
                result = registerDao.query(prepare);
                calibratedInstrument.setCalibrationDate(calibrationDateDatePicker.getValue().toString());
                if (result.isEmpty()) { //znaczy się ze pierwszy wpis :)
                    calibratedInstrument.setIdRegisterByYear(1);
                    calibratedInstrument.setCardNumber("1-" + year.getYear());
                    registerDao.update(calibratedInstrument);
                } else { //nie jest to pierwszy wpis
                    if (result.get(0).getCardNumber().contains(year.getYear())) {
                        calibratedInstrument.setIdRegisterByYear(result.get(0).getIdRegisterByYear() + 1);
                        calibratedInstrument.setCardNumber(result.get(0).getIdRegisterByYear() + 1 + "-" + year.getYear());
                        if(calibratedInstrument.getInstrument().getClient().getFullName().contains("ENERGOPOMIAR")){
                            calibratedInstrument.setCertificateNumber("EP-"+calibratedInstrument.getCardNumber()+"-P");
                        }
                        registerDao.update(calibratedInstrument);
                    } else {
                        calibratedInstrument.setIdRegisterByYear(1);
                        calibratedInstrument.setCardNumber("1-" + year.getYear());
                        if(calibratedInstrument.getInstrument().getClient().getFullName().contains("ENERGOPOMIAR")){
                            calibratedInstrument.setCertificateNumber("EP-"+calibratedInstrument.getCardNumber()+"-P");
                        }
                        registerDao.update(calibratedInstrument);
                    }
                }
                Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(), storehouseModel.class);
                calibratedInstrumentStorehouse.setCalibrationDate(calibrationDateDatePicker.getValue().toString());
                calibratedInstrumentStorehouse.setUserWhoCalibrate(user);
                storehouseDao.update(calibratedInstrumentStorehouse);
                storehouseMainController.getStorehouseList();
                setCalibratedInstrument(null);
                storehouseMainController.setStorehouseFxElementFromList(null);
                Close.closeVBoxWindow(mainVBox);
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                showAlert.display(e.getMessage());
            }
        }else {
            informationLabel.setText("Data wzorcowania jest wcześniejsza niż ostatnia w rejestrze!");
        }
    }
    /**
     * Metoda służy do dodawania przyrządu do rejestru wzorcowania poza zakresem AP.
     * Zawiera mechanizmu kontroli prawidłowości danych
     */
    private void calibrateInstrumentOutAP(){
        Dao<register2Model, Integer> registerDao = null;
        QueryBuilder<register2Model, Integer> registerQueryBuilder = null;
        PreparedQuery<register2Model> prepare;
        List<register2Model> result = null;
        calibratedInstrument2=new register2Model();
        calibratedInstrument2.setIdStorehouse(calibratedInstrument.getIdStorehouse());
        calibratedInstrument2.setInstrument(calibratedInstrument.getInstrument());
        calibratedInstrument2.setUserWhoCalibrate(calibratedInstrument.getUserWhoCalibrate());
        calibratedInstrument2.setState(calibratedInstrument.getState());
        try {
            registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), register2Model.class);
            registerQueryBuilder = registerDao.queryBuilder();
            registerQueryBuilder.where().gt("calibrationDate", calibrationDateDatePicker.getValue());
            prepare = registerQueryBuilder.prepare();
            result = registerDao.query(prepare);
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
        if (result.isEmpty()) {
            try {
                // registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), registerModel.class);
                registerDao.create(calibratedInstrument2);
                registerQueryBuilder.where().eq("idRegister", calibratedInstrument2.getIdRegister() - 1);
                prepare = registerQueryBuilder.prepare();
                result = registerDao.query(prepare);
                calibratedInstrument2.setCalibrationDate(calibrationDateDatePicker.getValue().toString());
                if (result.isEmpty()) { //znaczy się ze pierwszy wpis :)
                    calibratedInstrument2.setIdRegisterByYear(1);
                    calibratedInstrument2.setCardNumber("0001-" + year.getYear());
                    calibratedInstrument2.setCertificateNumber(getCertificateNumber(calibrationDateDatePicker.getValue().getMonthValue())+"-"+calibratedInstrument2.getCardNumber());
                    registerDao.update(calibratedInstrument2);
                } else { //nie jest to pierwszy wpis
                    if (result.get(0).getCardNumber().contains(year.getYear())) {
                        calibratedInstrument2.setIdRegisterByYear(result.get(0).getIdRegisterByYear() + 1);
                        calibratedInstrument2.setCardNumber(getCardNumber(result.get(0).getIdRegisterByYear() + 1) + "-" + year.getYear());
                        calibratedInstrument2.setCertificateNumber(getCertificateNumber(calibrationDateDatePicker.getValue().getMonthValue())+"-"+calibratedInstrument2.getCardNumber());
                        registerDao.update(calibratedInstrument2);
                    } else {
                        calibratedInstrument2.setIdRegisterByYear(1);
                        calibratedInstrument2.setCardNumber("0001-" + year.getYear());
                        calibratedInstrument2.setCertificateNumber(getCertificateNumber(calibrationDateDatePicker.getValue().getMonthValue())+"-"+calibratedInstrument2.getCardNumber());
                        registerDao.update(calibratedInstrument2);
                    }
                }
                Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(), storehouseModel.class);
                calibratedInstrumentStorehouse.setCalibrationDate(calibrationDateDatePicker.getValue().toString());
                calibratedInstrumentStorehouse.setUserWhoCalibrate(user);
                storehouseDao.update(calibratedInstrumentStorehouse);
                storehouseMainController.getStorehouseList();
                setCalibratedInstrument2(null);
                storehouseMainController.setStorehouseFxElementFromList(null);
                Close.closeVBoxWindow(mainVBox);
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                showAlert.display(e.getMessage());
            }
        }else {
            informationLabel.setText("Data wzorcowania jest wcześniejsza niż ostatnia w rejestrze!");
        }
    }

    /**
     * Metoda służy do tworzenia numeru karty wzorcowania poza zakresem AP
     * @param number
     * @return
     */
    private String getCardNumber(Integer number){
        if(number <= 9){
            return "000"+String.valueOf(number);
        }else if(number > 9 && number <= 99){
            return "00"+ String.valueOf(number);
        }else if (number > 99 && number <=999){
            return "0"+String.valueOf(number);
        }else{
            return String.valueOf(number);
        }
    }

    /**
     * Metoda służy do budowy numeru świadectwa wzorcowania poza zakresem AP
     * @param month
     * @return
     */
    private String getCertificateNumber(Integer month) {
        if (month <= 9) {
            return "0" + String.valueOf(month);
        } else {
            return String.valueOf(month);
        }
    }
    @FXML
    private void cancelAddCalibrateInstrument(){
        Close.closeVBoxWindow(mainVBox);
    }
}
