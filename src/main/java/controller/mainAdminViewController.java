package controller;

import controller.admin.commonInstrumentViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;



public class mainAdminViewController {
    public  mainAdminViewController() {}


    private static final String COMMON_VIEW = "/admin/commonInstrumentView.fxml";
    private commonInstrumentViewController commonMainController;


    @FXML
    private void initialize(){}
    @FXML
    private void userEdit(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/userView.fxml"));
        try {
            VBox vBox = loader.load();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Użytkownicy");
            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void nameEdit(){
        openCommonWindow("Nazwy przyrządów");
        commonMainController.getNames();
        commonMainController.setValueColumn("Nazwa przyrządu");
        commonMainController.setParameter(1);
    }
    @FXML
    private void typeEdit(){
        openCommonWindow("Typy przyrządów");
        commonMainController.getTypes();
        commonMainController.setValueColumn("Typ przyrządu");
        commonMainController.setParameter(2);
    }
    @FXML
    private void producerEdit(){
        openCommonWindow("Producenci przyrządów");
        commonMainController.getProducers();
        commonMainController.setValueColumn("Producent przyrządu");
        commonMainController.setParameter(3);
    }
    @FXML
    private void rangeEdit(){
        openCommonWindow("Zakresy przyrządów");
        commonMainController.getRanges();
        commonMainController.setValueColumn("Zakres przyrządu");
        commonMainController.setParameter(4);
    }
    @FXML
    private void unitEdit(){
        openCommonWindow("Jednostki wielkości fizycznych");
        commonMainController.getUnits();
        commonMainController.setValueColumn("Jednostki");
        commonMainController.setParameter(5);
    }
    @FXML
    private void yearEdit(){
        openCommonWindow("Lata");
        commonMainController.getYears();
        commonMainController.setValueColumn("Lata");
        commonMainController.setParameter(6);
    }
    private void openCommonWindow(String title){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(COMMON_VIEW));
            VBox vBox = loader.load();
            commonMainController = loader.getController();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(title);
            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
