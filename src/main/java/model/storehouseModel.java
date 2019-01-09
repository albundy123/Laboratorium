package model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import static com.j256.ormlite.field.DataType.*;

@DatabaseTable (tableName = "STOREHOUSE")
public class storehouseModel {

    @DatabaseField(generatedId = true)
    private Integer idStorehouse;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private instrumentModel instrument;
    @DatabaseField
    private String addDate;
    @DatabaseField
    private String calibrationDate;
    @DatabaseField
    private String leftDate;

    public storehouseModel() {
    }

    public storehouseModel(Integer idStorehouse, instrumentModel instrument, String addDate, String calibrationDate, String leftDate) {
        this.idStorehouse = idStorehouse;
        this.instrument = instrument;
        this.addDate = addDate;
        this.calibrationDate = calibrationDate;
        this.leftDate = leftDate;
    }

    public Integer getIdStorehouse() {
        return idStorehouse;
    }

    public void setIdStorehouse(Integer idStorehouse) {
        this.idStorehouse = idStorehouse;
    }

    public instrumentModel getInstrument() {
        return instrument;
    }

    public void setInstrument(instrumentModel instrument) {
        this.instrument = instrument;
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
