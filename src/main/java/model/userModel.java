package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "USERS")
public class userModel {
//Konfiguracja kolumn tabeli
    @DatabaseField(generatedId = true)
    private Integer idUser;
    @DatabaseField
    private String firstName;
    @DatabaseField
    private String lastName;
    @DatabaseField
    private String login;
    @DatabaseField
    private String password;
    @DatabaseField
    private String persmissionLevel; //admin,User
//Konstruktor bezparametrowy
    public userModel() {
    }
//Konstuktor z parametrami
    public userModel(Integer idUser, String firstName, String lastName, String login, String password, String persmissionLevel) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.persmissionLevel = persmissionLevel;
    }
//Gettery i Settery
    public Integer getIdUser() {return idUser;}
    public void setIdUser(Integer idUser) {this.idUser = idUser; }
    public String getFirstName() {return firstName; }
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public String getLogin() {return login;}
    public void setLogin(String login) {this.login = login;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public String getPersmissionLevel() {return persmissionLevel;}
    public void setPersmissionLevel(String persmissionLevel) {this.persmissionLevel = persmissionLevel;}
}
