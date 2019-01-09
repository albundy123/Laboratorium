package model.fxModel;

public class registerFxModel {
    private Integer indexOfRegisterModelList;
    private Integer idRegisterByYear;
    private String cardNumber;
    private String calibrationDate;
    private String instrumentName;
    private String serialNumber;
    private String identificationNumber;
    private String client;

    public registerFxModel() {
    }

    public registerFxModel(Integer indexOfRegisterModelList, Integer idRegisterByYear, String cardNumber, String calibrationDate, String instrumentName, String serialNumber, String identificationNumber, String client) {
        this.indexOfRegisterModelList = indexOfRegisterModelList;
        this.idRegisterByYear = idRegisterByYear;
        this.cardNumber = cardNumber;
        this.calibrationDate = calibrationDate;
        this.instrumentName = instrumentName;
        this.serialNumber = serialNumber;
        this.identificationNumber = identificationNumber;
        this.client = client;
    }

    public Integer getIndexOfRegisterModelList() {
        return indexOfRegisterModelList;
    }

    public void setIndexOfRegisterModelList(Integer indexOfRegisterModelList) {
        this.indexOfRegisterModelList = indexOfRegisterModelList;
    }

    public Integer getIdRegisterByYear() {
        return idRegisterByYear;
    }

    public void setIdRegisterByYear(Integer idRegisterByYear) {
        this.idRegisterByYear = idRegisterByYear;
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

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
