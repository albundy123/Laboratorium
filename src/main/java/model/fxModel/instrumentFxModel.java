package model.fxModel;


import com.j256.ormlite.field.DatabaseField;
import model.*;

public class instrumentFxModel {
    private Integer idIntrument;
    private instrumentNameModel instrumentName;
    private instrumentTypeModel instrumentType;
    private instrumentProducerModel instrumentProducer;
    private String serialNumber;
    private String identificationNumber;
    private instrumentRangeModel instrumentRange;
    private clientModel client;

    public instrumentFxModel() {
    }

    public instrumentFxModel(Integer idIntrument, instrumentNameModel instrumentName, instrumentTypeModel instrumentType, instrumentProducerModel instrumentProducer, String serialNumber, String identificationNumber, instrumentRangeModel instrumentRange, clientModel client) {
        this.idIntrument = idIntrument;
        this.instrumentName = instrumentName;
        this.instrumentType = instrumentType;
        this.instrumentProducer = instrumentProducer;
        this.serialNumber = serialNumber;
        this.identificationNumber = identificationNumber;
        this.instrumentRange = instrumentRange;
        this.client = client;
    }

}
