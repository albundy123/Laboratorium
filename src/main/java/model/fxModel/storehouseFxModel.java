package model.fxModel;

import java.util.Date;

public class storehouseFxModel {
    private Integer indexOfStorehouseModelList;
    private Integer idInstrument;
    private String instrumentName;
    private String instrumentType;
    private String instrumentProducer;
    private String serialNumber;
    private String identificationNumber;
    private String instrumentRange;
    private String client;
    private String addDate;
    private String calibrationDate;
    private String leftDate;

    public storehouseFxModel() {
    }

    public storehouseFxModel(Integer indexOfStorehouseModelList, Integer idInstrument, String instrumentName, String instrumentType, String instrumentProducer, String serialNumber, String identificationNumber, String instrumentRange, String client, String addDate, String calibrationDate, String leftDate) {
        this.indexOfStorehouseModelList = indexOfStorehouseModelList;
        this.idInstrument = idInstrument;
        this.instrumentName = instrumentName;
        this.instrumentType = instrumentType;
        this.instrumentProducer = instrumentProducer;
        this.serialNumber = serialNumber;
        this.identificationNumber = identificationNumber;
        this.instrumentRange = instrumentRange;
        this.client = client;
        this.addDate = addDate;
        this.calibrationDate = calibrationDate;
        this.leftDate = leftDate;
    }

    public Integer getIndexOfStorehouseModelList() {
        return indexOfStorehouseModelList;
    }

    public void setIndexOfStorehouseModelList(Integer indexOfStorehouseModelList) {
        this.indexOfStorehouseModelList = indexOfStorehouseModelList;
    }

    public Integer getIdInstrument() {
        return idInstrument;
    }

    public void setIdInstrument(Integer idInstrument) {
        this.idInstrument = idInstrument;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getInstrumentProducer() {
        return instrumentProducer;
    }

    public void setInstrumentProducer(String instrumentProducer) {
        this.instrumentProducer = instrumentProducer;
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

    public String getInstrumentRange() {
        return instrumentRange;
    }

    public void setInstrumentRange(String instrumentRange) {
        this.instrumentRange = instrumentRange;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getCalibrationDate() {
        return calibrationDate;
    }

    public void setCalibrationDate(String calibrationDate) {
        this.calibrationDate = calibrationDate;
    }

    public String getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(String leftDate) {
        this.leftDate = leftDate;
    }
}
