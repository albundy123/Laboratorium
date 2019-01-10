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
    @DatabaseField(foreign=true,foreignAutoRefresh = true)
    private userModel userWhoAdd;
    @DatabaseField
    private String calibrationDate;
    @DatabaseField(foreign=true,foreignAutoRefresh = true)
    private userModel userWhoCalibrate;
    @DatabaseField
    private String leftDate;
    @DatabaseField(foreign=true,foreignAutoRefresh = true)
    private userModel userWhoLeft;
    @DatabaseField(dataType = DataType.LONG_STRING)
    private String remarks;

    public storehouseModel() {
    }

    public storehouseModel(Integer idStorehouse, instrumentModel instrument, String addDate, userModel userWhoAdd, String calibrationDate, userModel userWhoCalibrate, String leftDate, userModel userWhoLeft, String remarks) {
        this.idStorehouse = idStorehouse;
        this.instrument = instrument;
        this.addDate = addDate;
        this.userWhoAdd = userWhoAdd;
        this.calibrationDate = calibrationDate;
        this.userWhoCalibrate = userWhoCalibrate;
        this.leftDate = leftDate;
        this.userWhoLeft = userWhoLeft;
        this.remarks = remarks;
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

    public userModel getUserWhoAdd() {
        return userWhoAdd;
    }

    public void setUserWhoAdd(userModel userWhoAdd) {
        this.userWhoAdd = userWhoAdd;
    }

    public String getCalibrationDate() {
        return calibrationDate;
    }

    public void setCalibrationDate(String calibrationDate) {
        this.calibrationDate = calibrationDate;
    }

    public userModel getUserWhoCalibrate() {
        return userWhoCalibrate;
    }

    public void setUserWhoCalibrate(userModel userWhoCalibrate) {
        this.userWhoCalibrate = userWhoCalibrate;
    }

    public String getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(String leftDate) {
        this.leftDate = leftDate;
    }

    public userModel getUserWhoLeft() {
        return userWhoLeft;
    }

    public void setUserWhoLeft(userModel userWhoLeft) {
        this.userWhoLeft = userWhoLeft;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
