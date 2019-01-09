package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "INSTRUMENTS")
public class instrumentModel {
    @DatabaseField(generatedId = true)
    private Integer idInstrument;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private instrumentNameModel instrumentName;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private instrumentTypeModel instrumentType;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private instrumentProducerModel instrumentProducer;
    @DatabaseField
    private String serialNumber;
    @DatabaseField
    private String identificationNumber;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private instrumentRangeModel instrumentRange;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private clientModel client;

    public instrumentModel() {
    }

    public instrumentModel(Integer idInstrument, instrumentNameModel instrumentName, instrumentTypeModel instrumentType, instrumentProducerModel instrumentProducer, String serialNumber, String identificationNumber, instrumentRangeModel instrumentRange, clientModel client) {
        this.idInstrument = idInstrument;
        this.instrumentName = instrumentName;
        this.instrumentType = instrumentType;
        this.instrumentProducer = instrumentProducer;
        this.serialNumber = serialNumber;
        this.identificationNumber = identificationNumber;
        this.instrumentRange = instrumentRange;
        this.client = client;
    }

    public Integer getIdInstrument() {
        return idInstrument;
    }

    public void setIdInstrument(Integer idInstrument) {
        this.idInstrument = idInstrument;
    }

    public instrumentNameModel getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(instrumentNameModel instrumentName) {
        this.instrumentName = instrumentName;
    }

    public instrumentTypeModel getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(instrumentTypeModel instrumentType) {
        this.instrumentType = instrumentType;
    }

    public instrumentProducerModel getInstrumentProducer() {
        return instrumentProducer;
    }

    public void setInstrumentProducer(instrumentProducerModel instrumentProducer) {
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

    public instrumentRangeModel getInstrumentRange() {
        return instrumentRange;
    }

    public void setInstrumentRange(instrumentRangeModel instrumentRange) {
        this.instrumentRange = instrumentRange;
    }

    public clientModel getClient() {
        return client;
    }

    public void setClient(clientModel client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "instrumentModel{" +
                "idInstrument=" + idInstrument +
                ", instrumentName=" + instrumentName +
                ", instrumentType=" + instrumentType +
                ", instrumentProducer=" + instrumentProducer +
                ", serialNumber='" + serialNumber + '\'' +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", instrumentRange=" + instrumentRange +
                ", client=" + client +
                '}';
    }
}
