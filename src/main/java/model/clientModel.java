package model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "CLIENTS")
public class clientModel {
    @DatabaseField(generatedId = true)
    private Integer idClient;
    @DatabaseField()
    private String shortName;
    @DatabaseField(dataType = DataType.LONG_STRING)
    private String fullName;
    @DatabaseField
    private String postCode;
    @DatabaseField
    private String city;
    @DatabaseField
    private String street;
    @DatabaseField
    private String houseNumber;
    @DatabaseField
    private String flatNumber;
    @DatabaseField
    private String status;


    public clientModel() {
    }
    public clientModel(Integer idClient, String shortName, String fullName, String postCode, String city, String street, String houseNumber, String flatNumber, String status) {
        this.idClient = idClient;
        this.shortName = shortName;
        this.fullName = fullName;
        this.postCode = postCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.flatNumber = flatNumber;
        this.status = status;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
