package model.fxModel;
/**
 * Obiekty klasy registerFxModel są wyświetlane w kontrolerze TableView w zakładce Rejestr w zakresie AP i Rejest poza zakresem AP
 */
public class registerFxModel {
    private Integer indexOfRegisterModelList;
    private Integer idRegisterByYear;
    private String cardNumber;
    private String calibrationDate;
    private String instrumentName;
    private String serialNumber;
    private String identificationNumber;
    private String client;
    private String calibratePerson;
    private String certificateNumber;
    private String documentKind;
    private String state;

    public registerFxModel() {
    }

    public registerFxModel(Integer indexOfRegisterModelList, Integer idRegisterByYear, String cardNumber, String calibrationDate, String instrumentName, String serialNumber, String identificationNumber, String client, String calibratePerson, String certificateNumber, String documentKind, String state) {
        this.indexOfRegisterModelList = indexOfRegisterModelList;
        this.idRegisterByYear = idRegisterByYear;
        this.cardNumber = cardNumber;
        this.calibrationDate = calibrationDate;
        this.instrumentName = instrumentName;
        this.serialNumber = serialNumber;
        this.identificationNumber = identificationNumber;
        this.client = client;
        this.calibratePerson = calibratePerson;
        this.certificateNumber = certificateNumber;
        this.documentKind = documentKind;
        this.state = state;
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

    public String getCalibratePerson() {
        return calibratePerson;
    }

    public void setCalibratePerson(String calibratePerson) {
        this.calibratePerson = calibratePerson;
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
