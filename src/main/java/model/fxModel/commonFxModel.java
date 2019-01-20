package model.fxModel;

public class commonFxModel {
    private Integer idCommon;
    private String commonString;

    public commonFxModel() {
    }

    public commonFxModel(int idCommon, String commonString) {
        this.idCommon = idCommon;
        this.commonString = commonString;
    }

    public int getIdCommon() {
        return idCommon;
    }

    public void setIdCommon(int idCommon) {
        this.idCommon = idCommon;
    }

    public String getCommonString() {
        return commonString;
    }

    public void setCommonString(String commonString) {
        this.commonString = commonString;
    }
}
