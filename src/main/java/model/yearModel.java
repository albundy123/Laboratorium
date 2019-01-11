package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "YEARS")
public class yearModel {
    @DatabaseField(generatedId = true)
    private Integer idYear;
    @DatabaseField
    private String year;

    public yearModel() {
    }

    public yearModel(Integer idYear, String year) {
        this.idYear = idYear;
        this.year = year;
    }

    public Integer getIdYear() {
        return idYear;
    }

    public void setIdYear(Integer idYear) {
        this.idYear = idYear;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
