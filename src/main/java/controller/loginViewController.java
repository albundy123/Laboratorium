package controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.userModel;
import util.Close;
import util.showAlert;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Klasa kontrolera odpowiedzialna za obsługę okna logowania
 */
public class loginViewController {
    public loginViewController() {}

    /*Wstrzykujemy pola z FXMLa, te na których wykonujemy jakieś operacje dlatego @FXML przed każdym */
    @FXML
    private ImageView loginImageView;   /*Kontrolka ImageView do wyświetlania obrazu człowieczka*/
    @FXML
    private TextField loginTextField;   /*Kontrolka TextField do wpisania loginu użytkownika*/
    @FXML
    private PasswordField passwordField;    /*Kontrolka PasswordField do wpisania hasła użytkownika*/
    @FXML
    private Label loginLabel;               /*Kontrolka Label do wyświetlania informacja o nieprawidłowych danych*/
    @FXML
    private AnchorPane mainAnchorPane;      /*Główny kontener AnchorPane, nadrzędny kontener okna logowania*/
    private userModel user;                 /*Pole użytkownik tworzone jest przy logowaniu i następnie pobierane przez następne kontrolery*/
    public userModel getUser() {return user;} /*Setter i Getter*/
    private void setUser(userModel user) {this.user = user;}
    private mainViewController mainWindowController;  /*Obiekt kontrolera okna MainWindow (głównego okna programu)*/

    @FXML
    private void initialize(){
        Image image = new Image("/images/user.jpg");
        loginImageView.setImage(image);
    }
    /**
     * Metoda wywołuje się po wciśnięciu przycisku loguj. Sprawdza czy w bazie danych jest użytkownik o danym logini i haśle
     */
    @FXML
    private void logIn(){
        try {
            Dao<userModel, Integer> userDao= DaoManager.createDao(dbSqlite.getConnectionSource(), userModel.class);
            QueryBuilder<userModel, Integer> userQueryBuilder = userDao.queryBuilder();
            userQueryBuilder.where().eq("login",loginTextField.getText()).and().eq("password",passwordField.getText());
            PreparedQuery<userModel> prepare = userQueryBuilder.prepare();
            List<userModel> result = userDao.query(prepare);
            if(result.isEmpty()) {
                loginLabel.setText("Podałeś nieprawidłowe dane");
            }else{
                setUser(new userModel(result.get(0).getIdUser(),result.get(0).getFirstName(),result.get(0).getLastName(), result.get(0).getLogin(), result.get(0).getPassword(),result.get(0).getPersmissionLevel()));
                Close.closeAnchorPaneWindow(mainAnchorPane);
                loadUserView();         //Otwiera główne okno programu
            }
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }
    }
    @FXML
    private void cancelLogIn(){  //Metoda wywołuje się po wciśnięciu przycisku anuluj
        Close.closeAnchorPaneWindow(mainAnchorPane);
    }

    /**
     * Metoda odpowiedzialna za otwierania głównego okna programu opisanego w pliku mainView.fxml
     */
    private void loadUserView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainView.fxml"));      //Podawanie ścieżki do FXMLA
            SplitPane mainSplitPane = loader.load();                                                    //Ladowanie FXMLA, rownocześnie powoduje utworzenie kontrolera związanego z tym FXMLem
            mainWindowController=loader.getController();                //Przechwytujemy ten kontroler
            if(mainWindowController!=null){
                mainWindowController.setLoginMainController(this);      //mainWindowController zawiera
                mainWindowController.setUser(user);                     //Ustawiamy w mainWindowController obiekt user
                mainWindowController.setUserLoginLabel(user.getLogin());   //Ustawiamy label w oknie MainWindow
                if(user.getPersmissionLevel().equals("user")) {
                    mainWindowController.adminTabDisable();
                }
            }
            showWIndow(mainSplitPane);
        } catch (IOException e) {
            showAlert.display(e.getMessage());
        }
    }
    private void showWIndow(SplitPane mainSplitPane){
        Stage window = new Stage();
        window.setTitle("LABORATORIUM METROLOGICZNE");
        Scene scene = new Scene(mainSplitPane);
        window.setScene(scene);
        window.show();
    }
}
