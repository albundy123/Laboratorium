package controller.instrument;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
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
import util.ConfirmBox;


import java.sql.SQLException;
import java.util.List;

public class newInstrumentRangeViewController {
    public newInstrumentRangeViewController() {System.out.println("Jestem konstruktorem klasy newInstrumentRangeViewController");
    }
    private newInstrumentViewController newInstrumentMainController;
    public void setNewInstrumentMainController(newInstrumentViewController newInstrumentMainController) {
        this.newInstrumentMainController = newInstrumentMainController;
    }
    private editInstrumentViewController editInstrumentMainController;

    public void setEditInstrumentMainController(editInstrumentViewController editInstrumentMainController) {
        this.editInstrumentMainController = editInstrumentMainController;
    }

    private ObservableList<String> unitObservableList = FXCollections.observableArrayList();
    @FXML
    private VBox mainVBox;
    @FXML
    private Button addNewInstrumentRangeButton;
    @FXML
    private TextField newInstrumentRangeTextField1;
    @FXML
    private TextField newInstrumentRangeTextField2;
    @FXML
    private ComboBox newInstrumentRangeUnitComboBox;
    @FXML
    private Button cancelNewInstrumentRangeButton;
    @FXML
    private Label newInstrumentRangeLabel;
    @FXML
    private void initialize() {
        System.out.println("Jestem funkcją initialize obiektu klasy newInstrumentRangeViewController");
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
                if (result.isEmpty()) {
                    instrumentRangeDao.create(new instrumentRangeModel(0, range));
                    newInstrumentRangeTextField1.clear();
                    newInstrumentRangeTextField2.clear();
                    newInstrumentRangeUnitComboBox.valueProperty().set(null);
                    if(editInstrumentMainController!=null){editInstrumentMainController.getInstrumentRangeList();}
                    if(newInstrumentMainController!=null){newInstrumentMainController.getInstrumentRangeList();}
                    Stage window = (Stage) mainVBox.getScene().getWindow();
                    window.close();
                } else {
                    newInstrumentRangeLabel.setText("Taki zakres pomiarowy już istnieje !");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            newInstrumentRangeLabel.setText("Wprowadź prawidłowo zakres pomiarowy !");
        }
    }
    @FXML
    private void cancelAddNewRange(){
        if((!newInstrumentRangeTextField1.getText().equals(""))&&(!newInstrumentRangeTextField2.getText().equals(""))&& (!newInstrumentRangeUnitComboBox.getSelectionModel().isEmpty())){
            if(ConfirmBox.display("Niezapisane dane","Czy na pewno chcesz zamknąć okno ?")){
                Stage window = (Stage) mainVBox.getScene().getWindow();
                window.close();
            }
        }else{
            Stage window = (Stage) mainVBox.getScene().getWindow();
            window.close();
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
            e.printStackTrace();
        }

    }
}
