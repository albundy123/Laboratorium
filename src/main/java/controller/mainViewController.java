package controller;

import controller.instrument.instrumentViewController;
import controller.register.registerViewController;
import controller.register2.register2ViewController;
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

    @FXML
    private Label userLoginLabel;
    public void setUserLoginLabel(String userLoginLabel) {
        this.userLoginLabel.setText(userLoginLabel);
    }

    private userModel user;
    public void setUser(userModel user) {
        this.user = user;
    }
    public userModel getUser() {
        return user;
    }


//Deklaracje wszystkich potrzebnych elementów do załadowania wszystkich widoków
    @FXML
    private VBox storehouseVBox;
    @FXML
    private Tab storehouseTab;
    @FXML
    private AnchorPane storehouseAnchorPane;
    @FXML
    private VBox registerInAPVBox;
    @FXML
    private Tab registerInAPTab;
    @FXML
    private AnchorPane registerInAPAnchorPane;
    @FXML
    private VBox registerOutAPVBox;
    @FXML
    private Tab registerOutAPTab;
    @FXML
    private AnchorPane registerOutAPAnchorPane;
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

    public void userTabDisable(){
        userTab.setDisable(true);
    }
    private loginViewController loginMainController;

    public void setLoginMainController(loginViewController loginMainController) {
        this.loginMainController = loginMainController;
    }

    private storehouseViewController storehouseMainController;
    private registerViewController registerInAPMainController;
    private register2ViewController registerOutAPMainController;
    private instrumentViewController instrumentMainController;
    private clientViewController clientMainController;
    private userViewController userMainController;

    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy mainViewController.");
        loadStorehouseTab();
        loadRegisterInAPTab();
        loadRegisterOutAPTab();
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
                storehouseMainController.setMainWindowController(this);
                setTabContent(storehouseTab,storehouseAnchorPane,storehouseVBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadRegisterInAPTab(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/register/registerView.fxml"));
        try {
            registerInAPVBox = loader.load();
            registerInAPMainController=loader.getController();
            if(registerInAPMainController!=null) {
                setTabContent(registerInAPTab,registerInAPAnchorPane,registerInAPVBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadRegisterOutAPTab(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/register2/register2View.fxml"));
        try {
            registerOutAPVBox = loader.load();
            registerOutAPMainController=loader.getController();
            if(registerOutAPMainController!=null) {
                setTabContent(registerOutAPTab,registerOutAPAnchorPane,registerOutAPVBox);
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
                setTabContent(instrumentTab,instrumentAnchorPane,instrumentVBox);
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
                setTabContent(clientTab,clientAnchorPane,clientVBox);
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
                setTabContent(userTab,userAnchorPane,userVBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setTabContent(Tab tab, AnchorPane anchorPane, VBox vBox){
        anchorPane.getChildren().add(vBox);
        tab.setContent(anchorPane);
        setAnchorPaneConstrains(vBox,0.0);
    }
    private void setAnchorPaneConstrains (VBox node, double value){
        AnchorPane.setBottomAnchor(node,value);
        AnchorPane.setLeftAnchor(node,value);
        AnchorPane.setRightAnchor(node,value);
        AnchorPane.setTopAnchor(node,value);
    }
}
