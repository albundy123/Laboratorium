package main;

import dbUtil.dbSqlite;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

       // dbSqlite.initDatabase();

       // FXMLLoader loader = new FXMLLoader(getClass().getResource("/register/registerView.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/storehouseView.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/instrument/instrumentView.fxml"));
       // FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/clientView.fxml"));
        VBox vBox = loader.load();
        Scene scene = new Scene(vBox);

       /* FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/loginView.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);*/
        primaryStage.setScene(scene);
        primaryStage.setTitle("Logowanie");
        primaryStage.show();
    }
}
