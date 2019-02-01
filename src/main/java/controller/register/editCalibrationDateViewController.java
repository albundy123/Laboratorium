package controller.register;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.registerModel;
import model.storehouseModel;
import util.Close;
import util.Converter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class editCalibrationDateViewController {
    public editCalibrationDateViewController(){}

    @FXML
    private VBox mainVBox;
    @FXML
    private Label calibrationDateInformationLabel;
    @FXML
    private DatePicker calibrationDateDatePicker;

    public void setCalibrationDateDatePicker(LocalDate calibrationDateDatePicker) {
        this.calibrationDateDatePicker.setValue(calibrationDateDatePicker);
    }

    @FXML
    public void initialize(){
        calibrationDateDatePicker.setConverter(Converter.getConverter());
    }
    private registerModel editedRegisterElement;
    public void setEditedRegisterElement(registerModel editedRegisterElement) {
        this.editedRegisterElement = editedRegisterElement;
    }
    private registerViewController registerMainController;

    public void setRegisterMainController(registerViewController registerMainController) {
        this.registerMainController = registerMainController;
    }
    private storehouseModel editedStorehouseElement;

    public void setEditedStorehouseElement(storehouseModel editedStorehouseElement) {
        this.editedStorehouseElement = editedStorehouseElement;
    }

    @FXML
    public void saveCalibrationDate(){
        if(calibrationDateDatePicker.getValue()!=null && checkCalibrationDate(calibrationDateDatePicker.getValue())) {
            try {
                Dao<registerModel, Integer> registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(),registerModel.class);
                Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(),storehouseModel.class);
                editedRegisterElement.setCalibrationDate(calibrationDateDatePicker.getValue().toString());
                registerDao.update(editedRegisterElement);
                editedStorehouseElement.setCalibrationDate(calibrationDateDatePicker.getValue().toString());
                storehouseDao.update(editedStorehouseElement);
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Close.closeVBoxWindow(mainVBox);
        }else{
            calibrationDateInformationLabel.setText("Nieprawidłowa data wzorcowania");
        }
    }
    @FXML
    public void cancelSaveCalibrationDate(){
        Close.closeVBoxWindow(mainVBox);
    }
    public Boolean checkCalibrationDate(LocalDate CalibrationDate){
        LocalDate next=null;
        LocalDate previous=null;
        LocalDate addDate=null;
        LocalDate leftDate=null;
        Dao<registerModel, Integer> registerDao=null;
        QueryBuilder<registerModel, Integer> registerQueryBuilder=null;
        PreparedQuery<registerModel> prepare=null;
        List<registerModel> result=null;
        try {
            registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(),registerModel.class);
            registerQueryBuilder=registerDao.queryBuilder();
            registerQueryBuilder.where().eq("idRegister", editedRegisterElement.getIdRegister()-1);
            prepare=registerQueryBuilder.prepare();
            result=registerDao.query(prepare);
            if(result.isEmpty()){
                previous=CalibrationDate;
            }else{
                previous=LocalDate.parse(result.get(0).getCalibrationDate());
            }
            registerQueryBuilder.where().eq("idRegister", editedRegisterElement.getIdRegister()+1);
            prepare=registerQueryBuilder.prepare();
            result=registerDao.query(prepare);
            if(result.isEmpty()){
                next=CalibrationDate;
            }else{
                next=LocalDate.parse(result.get(0).getCalibrationDate());
            }
            Dao<storehouseModel, Integer> storehouseDao=DaoManager.createDao(dbSqlite.getConnectionSource(),storehouseModel.class);
            QueryBuilder<storehouseModel, Integer> storehouseQueryBuilder=storehouseDao.queryBuilder();
            storehouseQueryBuilder.where().eq("idStorehouse", editedRegisterElement.getIdStorehouse());
            PreparedQuery<storehouseModel> prepare1=storehouseQueryBuilder.prepare();
            List<storehouseModel> result1=storehouseDao.query(prepare1);
            if(result1.isEmpty()){
                calibrationDateInformationLabel.setText("Niezidentyfikowany błąd");
                System.out.println("Cos tam"+editedRegisterElement.getIdStorehouse());
            }else{
                setEditedStorehouseElement(result1.get(0));
                addDate=LocalDate.parse(result1.get(0).getAddDate());
                if(result1.get(0).getLeftDate().equals("")){
                    leftDate= CalibrationDate;
                }else{
                    leftDate=LocalDate.parse(result1.get(0).getLeftDate());
                }
            }
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if((!CalibrationDate.isBefore(previous)) &&(!CalibrationDate.isAfter(next))&&(!CalibrationDate.isBefore(addDate))&&(!CalibrationDate.isAfter(leftDate))){
            return true;
        }else{
            return false;
        }
    }
}
