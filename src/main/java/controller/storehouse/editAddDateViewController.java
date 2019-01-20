package controller.storehouse;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.sun.org.apache.xpath.internal.operations.Bool;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.storehouseModel;
import util.Close;
import util.Converter;

import java.sql.SQLException;
import java.time.LocalDate;

public class editAddDateViewController {
    public editAddDateViewController() {
        System.out.println("Halo świry jestem kontruktorem klasy editAddDateViewController");
    }

    @FXML
    private VBox mainVBox;
    @FXML
    private DatePicker addDateDatePicker;
    @FXML
    private Label addDateInformationLabel;

    public void setAddDateDatePicker(LocalDate addDateDatePicker) {
        this.addDateDatePicker.setValue(addDateDatePicker);
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
    private void initialize() {
        System.out.println("Halo świry jestem funkcją initialize klasy editAddDateViewController");
        addDateDatePicker.setConverter(Converter.getConverter());
    }
    @FXML
    private void saveAddDate() {
        if (addDateDatePicker.getValue() != null&&checkAddDate()) {
            try {
                Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(), storehouseModel.class);
                editedStorehouseElement.setAddDate(addDateDatePicker.getValue().toString());
                storehouseDao.update(editedStorehouseElement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Close.closeVBoxWindow(mainVBox);
            storehouseMainController.getStorehouseList();
        }else{
            addDateInformationLabel.setText("Nieprawidłowa data przyjęcia !");
        }
    }
    private Boolean checkAddDate() {
        LocalDate calibrationDate;
        LocalDate leftDate;
        if (!editedStorehouseElement.getCalibrationDate().equals("")) {
            calibrationDate = LocalDate.parse(editedStorehouseElement.getCalibrationDate());
            if (addDateDatePicker.getValue().isAfter(calibrationDate)) {
                return false;
            } else {
                return true;
            }
        } else {
            if (!editedStorehouseElement.getLeftDate().equals("")) {
                leftDate = LocalDate.parse(editedStorehouseElement.getLeftDate());
                if (addDateDatePicker.getValue().isAfter(leftDate)) {
                    return false;
                } else {
                    return true;
                }
            }

        }
        return true;
    }
    @FXML
    private void cancelSaveAddDate() {
        Close.closeVBoxWindow(mainVBox);
    }
}

