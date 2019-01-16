package controller.register2;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.register2Model;
import model.registerModel;
import model.storehouseModel;
import util.Converter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class editCalibrationDateViewController {
    public editCalibrationDateViewController(){System.out.println("Halo świry jestem kontruktorem klasy editCalibrationDateViewController"); }

    @FXML
    private VBox mainVBox;
    @FXML
    private Button saveCalibrationDateButton;
    @FXML
    private Label calibrationDateInformationLabel;
    @FXML
    private Button cancelSaveCalibrationDateButton;
    @FXML
    private DatePicker calibrationDateDatePicker;

    public void setCalibrationDateDatePicker(LocalDate calibrationDateDatePicker) {
        this.calibrationDateDatePicker.setValue(calibrationDateDatePicker);
    }

    @FXML
    public void initialize(){
        System.out.println("Halo świry jestem funkcją initialize klasy editCalibrationDateViewController");
        calibrationDateDatePicker.setConverter(Converter.getConverter());
    }
    private register2Model editedRegisterElement;
    public void setEditedRegisterElement(register2Model editedRegisterElement) {
        this.editedRegisterElement = editedRegisterElement;
    }
    private register2ViewController registerMainController;

    public void setRegisterMainController(register2ViewController registerMainController) {
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
                Dao<register2Model, Integer> registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(),register2Model.class);
                Dao<storehouseModel, Integer> storehouseDao = DaoManager.createDao(dbSqlite.getConnectionSource(),storehouseModel.class);
                editedRegisterElement.setCalibrationDate(calibrationDateDatePicker.getValue().toString());
                registerDao.update(editedRegisterElement);
                editedStorehouseElement.setCalibrationDate(calibrationDateDatePicker.getValue().toString());
                storehouseDao.update(editedStorehouseElement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Stage window = (Stage) mainVBox.getScene().getWindow();
            window.close();
        }else{
            calibrationDateInformationLabel.setText("Nieprawidłowa data wzorcowania");
        }
    }
    @FXML
    public void cancelSaveCalibrationDate(){
        Stage window = (Stage) mainVBox.getScene().getWindow();
        window.close();
    }
    public Boolean checkCalibrationDate(LocalDate CalibrationDate){
        LocalDate next=null;
        LocalDate previous=null;
        LocalDate addDate=null;
        LocalDate leftDate=null;
        Dao<register2Model, Integer> registerDao=null;
        QueryBuilder<register2Model, Integer> registerQueryBuilder=null;
        PreparedQuery<register2Model> prepare=null;
        List<register2Model> result=null;
        try {
            registerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), register2Model.class);
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
                calibrationDateInformationLabel.setText("Jakiś mega błąd absolutnie");
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
