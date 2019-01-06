package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "UNITS")
public class unitModel {
    @DatabaseField(generatedId = true)
    private Integer idUnit;
    @DatabaseField
    private String unitName;

    public unitModel() {
    }

    public unitModel(Integer idUnit, String unitName) {
        this.idUnit = idUnit;
        this.unitName = unitName;
    }

    public Integer getIdUnit() {
        return idUnit;
    }

    public void setIdUnit(Integer idUnit) {
        this.idUnit = idUnit;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
