package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "INSTRUMENT_PRODUCERS")
public class instrumentProducerModel {
    @DatabaseField(generatedId = true)
    private Integer idInstrumentProducer;
    @DatabaseField
    private String instrumentProducer;

    public instrumentProducerModel() {
    }

    public instrumentProducerModel(Integer idInstrumentProducer, String instrumentProducer) {
        this.idInstrumentProducer = idInstrumentProducer;
        this.instrumentProducer = instrumentProducer;
    }

    public Integer getIdInstrumentProducer() {
        return idInstrumentProducer;
    }

    public void setIdInstrumentProducer(Integer idInstrumentProducer) {
        this.idInstrumentProducer = idInstrumentProducer;
    }

    public String getInstrumentProducer() {
        return instrumentProducer;
    }

    public void setInstrumentProducer(String instrumentProducer) {
        this.instrumentProducer = instrumentProducer;
    }
}
