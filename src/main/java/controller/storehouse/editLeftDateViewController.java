package controller.storehouse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.storehouseModel;
import util.Close;
import util.ConfirmBox;
import util.Converter;

import java.sql.SQLException;
import java.time.LocalDate;

public class editLeftDateViewController {
    public editLeftDateViewController(){System.out.println("Halo świry jestem kontruktorem klasy editLeftDateViewController"); }

    @FXML
    private VBox mainVBox;
    @FXML
    private DatePicker leftDateDatePicker;
    @FXML
    private Label leftDateInformationLabel;


    public void setLeftDateDatePicker(LocalDate leftDateDatePicker) {
        this.leftDateDatePicker.setValue(leftDateDatePicker);
    }

    private storehouseModel editedStorehouseElement;
    public void setEditedStorehouseElement(storehouseModel editedStorehouseElement) {
        this.editedStorehouseElement = editedStorehouseElement;
    }

    private storehouseViewController storehouseMainController;
    public void setStorehouseMainController(storehouseViewController storehouseMainController) {
        this.storehouseMainController = storehouseMainController;
    }

    @FXML
    private void initialize(){
        System.out.println("Halo świry jestem funkcją initialize klasy editLeftDateViewController");
        leftDateDatePicker.setConverter(Converter.getConverter());
    }

    @FXML
    private void saveLeftDate(){
        if (leftDateDatePicker.getValue() != null){
            if(checkLeftDate()) {
                try {
                    Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(), storehouseModel.class);
                    editedStorehouseElement.setLeftDate(leftDateDatePicker.getValue().toString());
                    storehouseDao.update(editedStorehouseElement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Close.closeVBoxWindow(mainVBox);
                storehouseMainController.getStorehouseList();}
            else{
                leftDateInformationLabel.setText("Nieprawidłowa data wydania !");
            }
        }
        else{
            if(ConfirmBox.display("Pusta data wydania","Czy chcesz zostawić pustą datę wydania")){
                try {
                    Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(), storehouseModel.class);
                    editedStorehouseElement.setLeftDate("");
                    storehouseDao.update(editedStorehouseElement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Close.closeVBoxWindow(mainVBox);
                storehouseMainController.getStorehouseList();
            }
        }
    }
    private Boolean checkLeftDate() {
        LocalDate calibrationDate;
        LocalDate addDate= LocalDate.parse(editedStorehouseElement.getLeftDate());;
        if (!editedStorehouseElement.getCalibrationDate().equals("")) {
            calibrationDate = LocalDate.parse(editedStorehouseElement.getCalibrationDate());
            if (leftDateDatePicker.getValue().isBefore(calibrationDate)) {
                return false;
            } else {
                return true;
            }
        } else {
            if (leftDateDatePicker.getValue().isBefore(addDate)) {
                return false;
            } else {
                return true;
            }
        }
    }
    @FXML
    private void cancelSaveLeftDate(){
        Close.closeVBoxWindow(mainVBox);
    }
}
