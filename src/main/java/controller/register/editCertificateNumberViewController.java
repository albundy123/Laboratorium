package controller.register;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.registerModel;
import util.Close;

import java.sql.SQLException;

public class editCertificateNumberViewController {
    public editCertificateNumberViewController() {System.out.println("Halo świry jestem kontruktorem klasy editCertificateNumberViewController");
    }

    @FXML
    private VBox mainVBox;
    @FXML
    private TextField certificateNumberTextField;
    @FXML
    private ComboBox<String> documentKindComboBox;
    @FXML
    private Label certificateNumberInformationLabel;

    @FXML
    public void initialize(){
        System.out.println("Halo świry jestem funkcją initialize klasy editCertificateNumberViewController");
        documentKindComboBox.getItems().addAll("Świadectwo wzorcowania","Protokół odmowy wzorcowania");
        documentKindComboBox.setValue("Świadectwo wzorcowania");
    }
    private registerModel editedRegisterElement;
    public void setEditedRegisterElement(registerModel editedRegisterElement) {
        this.editedRegisterElement = editedRegisterElement;
    }
    private registerViewController registerMainController;

    public void setRegisterMainController(registerViewController registerMainController) {
        this.registerMainController = registerMainController;
    }
    @FXML
    public void saveCertificateNumber(){
        if(certificateNumberTextField.getText().contains(editedRegisterElement.getCardNumber())) {
            editedRegisterElement.setCertificateNumber(certificateNumberTextField.getText());
            if(documentKindComboBox.getValue().equals("Świadectwo wzorcowania")){
                editedRegisterElement.setDocumentKind("ŚW");
            }else{
                editedRegisterElement.setDocumentKind("PO");
            }
            try {
                Dao<registerModel, Integer> registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(),registerModel.class);
                registerDao.update(editedRegisterElement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Close.closeVBoxWindow(mainVBox);
        }else{
            certificateNumberInformationLabel.setText("Nieprawidłowy numer świadectwa/protokołu");
        }
    }
    @FXML
    public void cancelSaveCertificateNumber(){
        Close.closeVBoxWindow(mainVBox);
    }
}
