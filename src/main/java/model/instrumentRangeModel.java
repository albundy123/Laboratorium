package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "INSTRUMENT_RANGES")
public class instrumentRangeModel {
    @DatabaseField(generatedId = true)
    private Integer idInstrumentRange;
    @DatabaseField
    private String instrumentRange;

    public instrumentRangeModel() {
    }

    public instrumentRangeModel(Integer idInstrumentRange, String instrumentRange) {
        this.idInstrumentRange = idInstrumentRange;
        this.instrumentRange = instrumentRange;
    }
    public Integer getIdInstrumentRange() {
        return idInstrumentRange;
    }

    public void setIdInstrumentRange(Integer idInstrumentRange) {
        this.idInstrumentRange = idInstrumentRange;
    }

    public String getInstrumentRange() {
        return instrumentRange;
    }

    public void setInstrumentRange(String instrumentRange) {
        this.instrumentRange = instrumentRange;
    }
}
