package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * Klasa implementująca model danych w tabeli REGISTER2
 */
@DatabaseTable(tableName = "REGISTER2")
public class register2Model {
    @DatabaseField(generatedId = true)
    private Integer idRegister;
    @DatabaseField
    private Integer idRegisterByYear;
    @DatabaseField
    private Integer idStorehouse;
    @DatabaseField
    private String cardNumber;
    @DatabaseField
    private String calibrationDate;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private instrumentModel instrument;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private userModel userWhoCalibrate;
    @DatabaseField
    private String certificateNumber;
    @DatabaseField
    private String documentKind;
    @DatabaseField
    private String state;
    public register2Model() {
    }

    public register2Model(Integer idRegister, Integer idRegisterByYear, Integer idStorehouse, String cardNumber, String calibrationDate, instrumentModel instrument, userModel userWhoCalibrate, String certificateNumber, String documentKind, String state) {
        this.idRegister = idRegister;
        this.idRegisterByYear = idRegisterByYear;
        this.idStorehouse = idStorehouse;
        this.cardNumber = cardNumber;
        this.calibrationDate = calibrationDate;
        this.instrument = instrument;
        this.userWhoCalibrate = userWhoCalibrate;
        this.certificateNumber = certificateNumber;
        this.documentKind = documentKind;
        this.state = state;
    }

    public Integer getIdRegister() {
        return idRegister;
    }

    public void setIdRegister(Integer idRegister) {
        this.idRegister = idRegister;
    }

    public Integer getIdRegisterByYear() {
        return idRegisterByYear;
    }

    public void setIdRegisterByYear(Integer idRegisterByYear) {
        this.idRegisterByYear = idRegisterByYear;
    }

    public Integer getIdStorehouse() {
        return idStorehouse;
    }

    public void setIdStorehouse(Integer idStorehouse) {
        this.idStorehouse = idStorehouse;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCalibrationDate() {
        return calibrationDate;
    }

    public void setCalibrationDate(String calibrationDate) {
        this.calibrationDate = calibrationDate;
    }

    public instrumentModel getInstrument() {
        return instrument;
    }

    public void setInstrument(instrumentModel instrument) {
        this.instrument = instrument;
    }

    public userModel getUserWhoCalibrate() {
        return userWhoCalibrate;
    }

    public void setUserWhoCalibrate(userModel userWhoCalibrate) {
        this.userWhoCalibrate = userWhoCalibrate;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getDocumentKind() {
        return documentKind;
    }

    public void setDocumentKind(String documentKind) {
        this.documentKind = documentKind;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
