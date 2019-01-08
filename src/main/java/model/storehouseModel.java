package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable (tableName = "STOREHOUSE")
public class storehouseModel {

    @DatabaseField(generatedId = true)
    private Integer idStorehouse;
    @DatabaseField(foreign = true,foreignAutoRefresh = true)
    private instrumentModel instrument;
    @DatabaseField
    private Date addDate;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private userModel user;

    public storehouseModel() {
    }

    public storehouseModel(Integer idStorehouse, instrumentModel instrument, Date addDate, userModel user) {
        this.idStorehouse = idStorehouse;
        this.instrument = instrument;
        this.addDate = addDate;
        this.user = user;
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

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public userModel getUser() {
        return user;
    }

    public void setUser(userModel user) {
        this.user = user;
    }
}
