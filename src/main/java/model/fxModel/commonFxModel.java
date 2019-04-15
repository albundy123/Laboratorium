package model.fxModel;

/**
 * Obiekty klasy commonFxModel są wyświetlana w zakładce administratora. Ponieważ niektóre model danych są zbudowane analogicznie.
 * Dlatego jest jedna klasa, który służy do wyświetlania tych danych.
 */
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
