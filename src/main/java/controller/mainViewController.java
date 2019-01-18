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
import javafx.scene.layout.VBox;
import model.userModel;

import java.io.IOException;

public class mainViewController {
    public  mainViewController() {System.out.println("Siemanko jestem konstruktorem klasy  mainViewController.");}
    //Scieżki do plików fxml z poszczególnymi widokami
    private static final String STOREHOUSE_VIEW = "/storehouse/storehouseView.fxml";
    private static final String REGISTER_IN_AP_VIEW = "/register/registerView.fxml";
    private static final String REGISTER_OUT_AP_VIEW = "/register2/register2View.fxml";
    private static final String INSTRUMENT_VIEW = "/instrument/instrumentView.fxml";
    private static final String CLIENT_VIEW = "/client/clientView.fxml";
    private static final String USER_VIEW = "/user/userView.fxml";

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
    private Tab storehouseTab;
    @FXML
    private AnchorPane storehouseAnchorPane;
    @FXML
    private VBox storehouseVBox;
    @FXML
    private Tab registerInAPTab;
    @FXML
    private AnchorPane registerInAPAnchorPane;
    @FXML
    private VBox registerInAPVBox;
    @FXML
    private Tab registerOutAPTab;
    @FXML
    private AnchorPane registerOutAPAnchorPane;
    @FXML
    private VBox registerOutAPVBox;
    @FXML
    private Tab instrumentTab;
    @FXML
    private AnchorPane instrumentAnchorPane;
    @FXML
    private VBox instrumentVBox;
    @FXML
    private Tab clientTab;
    @FXML
    private AnchorPane clientAnchorPane;
    @FXML
    private VBox clientVBox;
    @FXML
    private Tab userTab;
    @FXML
    private AnchorPane userAnchorPane;
    @FXML
    private VBox userVBox;

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
        //Ładowanie poszczególnych kart
        storehouseMainController= loadTab(storehouseMainController,storehouseTab,storehouseAnchorPane,storehouseVBox,STOREHOUSE_VIEW);
        storehouseMainController.setMainWindowController(this);
        loadTab(registerInAPMainController,registerInAPTab,registerInAPAnchorPane,registerInAPVBox,REGISTER_IN_AP_VIEW);
        loadTab(registerOutAPMainController,registerOutAPTab,registerOutAPAnchorPane,registerOutAPVBox,REGISTER_OUT_AP_VIEW);
        loadTab(instrumentMainController,instrumentTab,instrumentAnchorPane,instrumentVBox,INSTRUMENT_VIEW);
        loadTab(clientMainController,clientTab,clientAnchorPane,clientVBox,CLIENT_VIEW);
        loadTab(userMainController,userTab,userAnchorPane,userVBox,USER_VIEW);
    }
    private <T> T loadTab(T tabController,Tab tab, AnchorPane anchorPane, VBox vBox,String resource){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        try {
            vBox = loader.load();
            tabController=loader.getController();
            if(tabController!=null) {
                setTabContent(tab,anchorPane,vBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tabController;
    }
    private void setTabContent(Tab tab, AnchorPane anchorPane, VBox vBox){
        anchorPane.getChildren().add(vBox);
        tab.setContent(anchorPane);
        setAnchorPaneConstrains(vBox,0.0);
    }
    private void setAnchorPaneConstrains (VBox vBox, double value){
        AnchorPane.setBottomAnchor(vBox,value);
        AnchorPane.setLeftAnchor(vBox,value);
        AnchorPane.setRightAnchor(vBox,value);
        AnchorPane.setTopAnchor(vBox,value);
    }
}
