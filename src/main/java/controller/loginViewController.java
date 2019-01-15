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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.userModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class loginViewController {
    public loginViewController() {System.out.println("Siemanko jestem konstruktorem klasy loginViewController.");}

    @FXML
    private ImageView loginImageView;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelLoginButton;
    @FXML
    private Label loginLabel;
    @FXML
    private AnchorPane mainAnchorPane;


    private userModel user;

    public userModel getUser() {
        return user;
    }

    private mainViewController mainWindowController;
    @FXML
    private void initialize(){
        Image image = new Image("/images/user.jpg");
        loginImageView.setImage(image);
    }
    @FXML
    private void loguj(){
        try {
            Dao<userModel, Integer> userDao= DaoManager.createDao(dbSqlite.getConnectionSource(), userModel.class);
            QueryBuilder<userModel, Integer> userQueryBuilder = userDao.queryBuilder();
            userQueryBuilder.where().eq("login",loginTextField.getText()).and().eq("password",passwordField.getText());
            PreparedQuery<userModel> prepare = userQueryBuilder.prepare();
            List<userModel> result = userDao.query(prepare);
            if(result.isEmpty()) {
                loginLabel.setText("Podałeś nieprawidłowe dane");
            }else{
                loginLabel.setText("Wow działa !");
                user=new userModel(result.get(0).getIdUser(),result.get(0).getFirstName(),result.get(0).getLastName(), result.get(0).getLogin(), result.get(0).getPassword(),result.get(0).getPersmissionLevel());
                Stage window = (Stage) mainAnchorPane.getScene().getWindow();
                window.close();
                loadUserView();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void anuluj(){
        Stage window = (Stage) mainAnchorPane.getScene().getWindow();
        window.close();
    }
    private void loadUserView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/mainView.fxml"));
            SplitPane mainSplitPane = loader.load();
            System.out.println("This "+this);
            mainWindowController=loader.getController();
            if(mainWindowController!=null){
                mainWindowController.setLoginMainController(this);
                mainWindowController.setUser(user);
                System.out.println("This "+this);
                mainWindowController.setUserLoginLabel(user.getLogin());
                System.out.println("Skonczylem ladowanko");
            }
            Stage window = new Stage();
            window.setTitle("Użytkownicy");
            Scene scene = new Scene(mainSplitPane);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
