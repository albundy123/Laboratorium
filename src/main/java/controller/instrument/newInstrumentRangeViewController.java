package controller.instrument;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import controller.admin.commonInstrumentViewController;
import controller.storehouse.newInstrumentViewController;
import dbUtil.dbSqlite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.instrumentRangeModel;
import model.unitModel;
import util.Close;
import util.ConfirmBox;
import util.showAlert;


import java.sql.SQLException;
import java.util.List;
/**
 * Klasa kontrolera przeznaczonego do obsługi okna do dodawania nowego zakresu pomiarowego przyrządu newInstrumentRangeView.fxml
 */
public class newInstrumentRangeViewController {
    public newInstrumentRangeViewController() {}

    private newInstrumentViewController newInstrumentMainController;
    public void setNewInstrumentMainController(newInstrumentViewController newInstrumentMainController) {
        this.newInstrumentMainController = newInstrumentMainController;
    }
    private editInstrumentViewController editInstrumentMainController;
    public void setEditInstrumentMainController(editInstrumentViewController editInstrumentMainController) {
        this.editInstrumentMainController = editInstrumentMainController;
    }
    private ObservableList<String> unitObservableList = FXCollections.observableArrayList();

    private instrumentRangeModel editedInstrumentRange;

    public void setEditedInstrumentRange(instrumentRangeModel editedInstrumentRange) {
        this.editedInstrumentRange = editedInstrumentRange;
    }
    private commonInstrumentViewController commonInstrumentController;

    public void setCommonInstrumentController(commonInstrumentViewController commonInstrumentController) {
        this.commonInstrumentController = commonInstrumentController;
    }
    @FXML
    private VBox mainVBox;
    @FXML
    private TextField newInstrumentRangeTextField1;
    public void setNewInstrumentRangeTextField1(String newInstrumentRange) {
        this.newInstrumentRangeTextField1.setText(newInstrumentRange);
    }
    @FXML
    private TextField newInstrumentRangeTextField2;
    public void setNewInstrumentRangeTextField2(String newInstrumentRange) {
        this.newInstrumentRangeTextField2.setText(newInstrumentRange);
    }
    @FXML
    private ComboBox newInstrumentRangeUnitComboBox;
    public void setNewInstrumentRangeUnitComboBox(String newInstrumentRangeUnit) {
        this.newInstrumentRangeUnitComboBox.setValue(newInstrumentRangeUnit);
    }

    @FXML
    private Label newInstrumentRangeLabel;

    @FXML
    private void initialize() {
        getUnitList();
        newInstrumentRangeUnitComboBox.setItems(unitObservableList);
    }
    @FXML
    private void addNewRange(){
        if((!newInstrumentRangeTextField1.getText().equals(""))&&(!newInstrumentRangeTextField2.getText().equals(""))&& (!newInstrumentRangeUnitComboBox.getSelectionModel().isEmpty())) {
            try {
                String range = "("+newInstrumentRangeTextField1.getText()+" do "+newInstrumentRangeTextField2.getText()+") "+newInstrumentRangeUnitComboBox.getValue();
                Dao<instrumentRangeModel, Integer> instrumentRangeDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentRangeModel.class);
                QueryBuilder<instrumentRangeModel, Integer> instrumentRangeQueryBuilder = instrumentRangeDao.queryBuilder();
                instrumentRangeQueryBuilder.where().eq("instrumentRange", range);
                PreparedQuery<instrumentRangeModel> prepare = instrumentRangeQueryBuilder.prepare();
                List<instrumentRangeModel> result = instrumentRangeDao.query(prepare);
                if(editedInstrumentRange!=null){//edytujemy zakres
                    if (result.isEmpty()) {
                        range = "("+newInstrumentRangeTextField1.getText()+" do "+newInstrumentRangeTextField2.getText()+") "+newInstrumentRangeUnitComboBox.getValue();
                        instrumentRangeDao.update(new instrumentRangeModel(editedInstrumentRange.getIdInstrumentRange(),range));
                        Close.closeVBoxWindow(mainVBox);
                        if(commonInstrumentController!=null){
                            commonInstrumentController.getRanges();
                        }
                    }else{
                        if(result.get(0).getIdInstrumentRange()!=editedInstrumentRange.getIdInstrumentRange()){
                            newInstrumentRangeLabel.setText("Taki zakres pomiarowy już istnieje !");
                        }else{
                            Close.closeVBoxWindow(mainVBox);
                        }
                    }
                }else{
                    if (result.isEmpty()) {
                        instrumentRangeDao.create(new instrumentRangeModel(0, range));
                        newInstrumentRangeTextField1.clear();
                        newInstrumentRangeTextField2.clear();
                        newInstrumentRangeUnitComboBox.valueProperty().set(null);
                        if(editInstrumentMainController!=null){editInstrumentMainController.getInstrumentRangeList();}
                        if(newInstrumentMainController!=null){newInstrumentMainController.getInstrumentRangeList();}
                        Close.closeVBoxWindow(mainVBox);
                        if(commonInstrumentController!=null){
                            commonInstrumentController.getRanges();
                        }
                    } else {
                        newInstrumentRangeLabel.setText("Taki zakres pomiarowy już istnieje !");
                    }
                }
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                showAlert.display(e.getMessage());
            }
        }else{
            newInstrumentRangeLabel.setText("Wprowadź prawidłowo zakres pomiarowy !");
        }
    }
    @FXML
    private void cancelAddNewRange(){
        if((!newInstrumentRangeTextField1.getText().equals(""))&&(!newInstrumentRangeTextField2.getText().equals(""))&& (!newInstrumentRangeUnitComboBox.getSelectionModel().isEmpty())){
            if(ConfirmBox.display("Niezapisane dane","Czy na pewno chcesz zamknąć okno ?")){
                Close.closeVBoxWindow(mainVBox);
            }
        }else{
            Close.closeVBoxWindow(mainVBox);
        }
    }
    public void getUnitList(){
        try {
            unitObservableList.clear();
            Dao<unitModel, Integer> unitDao = DaoManager.createDao(dbSqlite.getConnectionSource(),unitModel.class);
            List<unitModel> unitList = unitDao.queryForAll();
            unitList.forEach(unit ->{
                unitObservableList.add(unit.getUnitName());
            });
            dbSqlite.closeConnection();
        } catch (SQLException e) {
            showAlert.display(e.getMessage());
        }

    }
}
