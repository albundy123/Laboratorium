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
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class loginViewController {
    public loginViewController() {}

    //Wstrzykujemy pola z FXMLa, te na których wykonujemy jakieś operacje dlatego @FXML przed każdym
    @FXML
    private ImageView loginImageView;   //Kontrolka ImageView do wyświetlania obrazu człowieczka
    @FXML
    private TextField loginTextField;   //Kontrolka TextField do wpisania loginu użytkownika
    @FXML
    private PasswordField passwordField;    //Kontrolka PasswordField do wpisania hasła użytkownika
    @FXML
    private Label loginLabel;               //Kontrolka Label do wyświetlania informacja o nieprawidłowych danych
    @FXML
    private AnchorPane mainAnchorPane;      //Główny kontener AnchorPane, nadrzędny kontener okna logowania
    //Pola
    private userModel user;                 //Pole użytkownik tworzone jest przy logowaniu i następnie pobierane przez następne kontrolery
    public userModel getUser() {return user;} //Setter i Getter
    private void setUser(userModel user) {this.user = user;}
    private mainViewController mainWindowController;  //Obiekt kontrolera okna MainWindow (głównego okna programu)

    @FXML
    private void initialize(){  //Funkcja initialize wykonuje się bezpośrednio po konstruktorze nie będę tego powtarzał we wszystkich klasach więc napiszę tutaj. Można tutaj konfigurować różne rzeczy.
        Image image = new Image("/images/user.jpg");
        loginImageView.setImage(image);
    }
    @FXML
    private void logIn(){  //Metoda wywołuje się po wciśnięciu przycisku loguj.
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void cancelLogIn(){  //Metoda wywołuje się po wciśnięciu przycisku anuluj
        Close.closeAnchorPaneWindow(mainAnchorPane);
    }
    private void loadUserView(){  //Otwieranie głównego okna programu
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainView.fxml"));      //Podawanie ścieżki do FXMLA
        try {
            SplitPane mainSplitPane = loader.load();                                                    //Ładowanie FXMLA, równocześnie powoduje utworzenie kontrolera związanego z tym FXMLem
            mainWindowController=loader.getController();                //Przechwytujemy ten kontroler
            if(mainWindowController!=null){
                mainWindowController.setLoginMainController(this);      //mainWindowController zawiera
                mainWindowController.setUser(user);                     //Ustawiamy w mainWindowController obiekt user
                mainWindowController.setUserLoginLabel(user.getLogin());   //Ustawiamy label w oknie MainWindow
                if(user.getPersmissionLevel().equals("user")) {
                    mainWindowController.adminTabDisable();
                }
                System.out.println("Skonczylem ladowanko"); //Wyswietli dopiero jak zbuduje całe główne okno wraz z zakładkami
            }
            showWIndow(mainSplitPane);
        } catch (IOException e) {
            e.printStackTrace();
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
