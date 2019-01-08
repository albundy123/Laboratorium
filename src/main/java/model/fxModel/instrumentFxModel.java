package model.fxModel;


import com.j256.ormlite.field.DatabaseField;
import model.*;

public class instrumentFxModel {
    private Integer indexOfInstrumentModelList;
    private Integer idInstrument;
    private String instrumentName;
    private String instrumentType;
    private String instrumentProducer;
    private String serialNumber;
    private String identificationNumber;
    private String instrumentRange;
    private String client;

    public instrumentFxModel() {
    }

    public instrumentFxModel(Integer indexOfInstrumentModelList, Integer idInstrument, String instrumentName, String instrumentType, String instrumentProducer, String serialNumber, String identificationNumber, String instrumentRange, String client) {
        this.indexOfInstrumentModelList = indexOfInstrumentModelList;
        this.idInstrument = idInstrument;
        this.instrumentName = instrumentName;
        this.instrumentType = instrumentType;
        this.instrumentProducer = instrumentProducer;
        this.serialNumber = serialNumber;
        this.identificationNumber = identificationNumber;
        this.instrumentRange = instrumentRange;
        this.client = client;
    }

    public Integer getIndexOfInstrumentModelList() {
        return indexOfInstrumentModelList;
    }

    public void setIndexOfInstrumentModelList(Integer indexOfInstrumentModelList) {
        this.indexOfInstrumentModelList = indexOfInstrumentModelList;
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
}
