package controller;

import controller.instrument.instrumentViewController;
import controller.register.registerViewController;
import controller.storehouse.storehouseViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.userModel;
import model.yearModel;

import java.io.IOException;

public class mainViewController {
    public  mainViewController() {System.out.println("Siemanko jestem konstruktorem klasy  mainViewController.");}

    private userModel user;
    private yearModel year;

    @FXML
    private Label userLoginLabel;

    public void setUser(userModel user) {
        this.user = user;
    }

    public void setYear(yearModel year) {
        this.year = year;
    }



    @FXML
    private VBox storehouseVBox;
    @FXML
    private Tab storehouseTab;
    @FXML
    private AnchorPane storehouseAnchorPane;
    @FXML
    private VBox registerAPVBox;
    @FXML
    private Tab registerAPTab;
    @FXML
    private AnchorPane registerAPAnchorPane;
    @FXML
    private VBox instrumentVBox;
    @FXML
    private Tab instrumentTab;
    @FXML
    private AnchorPane instrumentAnchorPane;
    @FXML
    private VBox clientVBox;
    @FXML
    private Tab clientTab;
    @FXML
    private AnchorPane clientAnchorPane;
    @FXML
    private VBox userVBox;
    @FXML
    private Tab userTab;
    @FXML
    private AnchorPane userAnchorPane;









    private storehouseViewController storehouseMainController;
    private registerViewController registerAPMainController;
    private instrumentViewController instrumentMainController;
    private clientViewController clientMainController;
    private userViewController userMainController;

    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy mainViewController.");
        loadStorehouseTab();
        loadRegisterAPTab();
        loadInstrumentTab();
        loadClientTab();
        loadUserTab();
    }
    private void loadStorehouseTab(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/storehouse/storehouseView.fxml"));
        try {
            storehouseVBox = loader.load();
            storehouseMainController=loader.getController();
            if(storehouseMainController!=null) {
                storehouseAnchorPane.getChildren().add(storehouseVBox);
                storehouseTab.setContent(storehouseAnchorPane);
                setAnchorPaneConstrains(storehouseVBox,0.0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadRegisterAPTab(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/register/registerView.fxml"));
        try {
            registerAPVBox = loader.load();
            registerAPMainController=loader.getController();
            if(registerAPMainController!=null) {
                registerAPAnchorPane.getChildren().add(registerAPVBox);
                registerAPTab.setContent(registerAPAnchorPane);
                setAnchorPaneConstrains(registerAPVBox,0.0);
                VBox.setVgrow(registerAPVBox, Priority.ALWAYS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadInstrumentTab(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/instrument/instrumentView.fxml"));
        try {
            instrumentVBox = loader.load();
            instrumentMainController=loader.getController();
            if(instrumentMainController!=null) {
                instrumentAnchorPane.getChildren().add(instrumentVBox);
                instrumentTab.setContent(instrumentAnchorPane);
                setAnchorPaneConstrains(instrumentVBox,0.0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadClientTab(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/clientView.fxml"));
        try {
            clientVBox = loader.load();
            clientMainController=loader.getController();
            if(clientMainController!=null) {
                clientAnchorPane.getChildren().add(clientVBox);
                clientTab.setContent(clientAnchorPane);
                setAnchorPaneConstrains(clientVBox,0.0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadUserTab(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/userView.fxml"));
        try {
            userVBox = loader.load();
            userMainController=loader.getController();
            if(userMainController!=null) {
                userAnchorPane.getChildren().add(userVBox);
                userTab.setContent(userAnchorPane);
                setAnchorPaneConstrains(userVBox,0.0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setAnchorPaneConstrains (VBox node, double value){
        AnchorPane.setBottomAnchor(node,value);
        AnchorPane.setLeftAnchor(node,value);
        AnchorPane.setRightAnchor(node,value);
        AnchorPane.setTopAnchor(node,value);
    }
}
