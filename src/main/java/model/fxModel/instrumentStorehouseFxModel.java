package model.fxModel;

public class instrumentStorehouseFxModel {

    private Integer idStorehouse;
    private String addDate;
    private String addPerson;
    private String calibrationDate;
    private String calibratePerson;
    private String leftDate;
    private String leftPerson;

    public instrumentStorehouseFxModel() {
    }

    public instrumentStorehouseFxModel(Integer idStorehouse, String addDate, String addPerson, String calibrationDate, String calibratePerson, String leftDate, String leftPerson) {
        this.idStorehouse = idStorehouse;
        this.addDate = addDate;
        this.addPerson = addPerson;
        this.calibrationDate = calibrationDate;
        this.calibratePerson = calibratePerson;
        this.leftDate = leftDate;
        this.leftPerson = leftPerson;
    }

    public Integer getIdStorehouse() {
        return idStorehouse;
    }

    public void setIdStorehouse(Integer idStorehouse) {
        this.idStorehouse = idStorehouse;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getAddPerson() {
        return addPerson;
    }

    public void setAddPerson(String addPerson) {
        this.addPerson = addPerson;
    }

    public String getCalibrationDate() {
        return calibrationDate;
    }

    public void setCalibrationDate(String calibrationDate) {
        this.calibrationDate = calibrationDate;
    }

    public String getCalibratePerson() {
        return calibratePerson;
    }

    public void setCalibratePerson(String calibratePerson) {
        this.calibratePerson = calibratePerson;
    }

    public String getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(String leftDate) {
        this.leftDate = leftDate;
    }

    public String getLeftPerson() {
        return leftPerson;
    }

    public void setLeftPerson(String leftPerson) {
        this.leftPerson = leftPerson;
    }
}
