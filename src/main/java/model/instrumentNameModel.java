package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * Klasa implementująca model danych w tabeli INSTRUMENT_NAMES
 */
@DatabaseTable(tableName = "INSTRUMENT_NAMES")
public class instrumentNameModel {
    @DatabaseField(generatedId = true)
    private Integer idInstrumentName;
    @DatabaseField
    private String instrumentName;

    public instrumentNameModel() {
    }

    public instrumentNameModel(Integer idInstrumentName, String instrumentName) {
        this.idInstrumentName = idInstrumentName;
        this.instrumentName = instrumentName;
    }

    public Integer getIdInstrumentName() {
        return idInstrumentName;
    }

    public void setIdInstrumentName(Integer idInstrumentName) {
        this.idInstrumentName = idInstrumentName;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }
}
