package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "REGISTER")
public class registerModel {
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
    @DatabaseField
    private String certificateNumber;

    public registerModel() {
    }

    public registerModel(Integer idRegister, Integer idRegisterByYear, Integer idStorehouse, String cardNumber, String calibrationDate, instrumentModel instrument, String certificateNumber) {
        this.idRegister = idRegister;
        this.idRegisterByYear = idRegisterByYear;
        this.idStorehouse = idStorehouse;
        this.cardNumber = cardNumber;
        this.calibrationDate = calibrationDate;
        this.instrument = instrument;
        this.certificateNumber = certificateNumber;
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

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    @Override
    public String toString() {
        return "registerModel{" +
                "idRegister=" + idRegister +
                ", idRegisterByYear=" + idRegisterByYear +
                ", idStorehouse=" + idStorehouse +
                ", cardNumber='" + cardNumber + '\'' +
                ", calibrationDate='" + calibrationDate + '\'' +
                ", instrument=" + instrument +
                ", certificateNumber='" + certificateNumber + '\'' +
                '}';
    }
}
