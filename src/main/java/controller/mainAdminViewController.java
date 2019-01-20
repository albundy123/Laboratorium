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
    public  mainAdminViewController() {System.out.println("Siemanko jestem konstruktorem klasy  mainAdminViewController.");}


    private static final String COMMON_VIEW = "/admin/commonInstrumentView.fxml";
    private commonInstrumentViewController commonMainController;


    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy mainAdminViewController.");
    }

    @FXML
    private void editUser(){

    }
    @FXML
    private void nameEdit(){
        openCommonWindow("Nazwy przyrządów");
        commonMainController.getNames();
        commonMainController.setValueColumn("Nazwa przyrządu");
    }
    @FXML
    private void typeEdit(){
        openCommonWindow("Typy przyrządów");
        commonMainController.getTypes();
        commonMainController.setValueColumn("Typ przyrządu");
    }
    @FXML
    private void producerEdit(){
        openCommonWindow("Producenci przyrządów");
        commonMainController.getProducers();
        commonMainController.setValueColumn("Producent przyrządu");
    }
    @FXML
    private void rangeEdit(){
        openCommonWindow("Zakresy przyrządów");
        commonMainController.getRanges();
        commonMainController.setValueColumn("Zakres przyrządu");
    }
    @FXML
    private void unitEdit(){
        openCommonWindow("Jednostki wielkości fizycznych");
        commonMainController.getUnits();
        commonMainController.setValueColumn("Jednostki");
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
