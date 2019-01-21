package controller.instrument;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import controller.admin.commonInstrumentViewController;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.unitModel;
import util.Close;
import util.ConfirmBox;

import java.sql.SQLException;
import java.util.List;

public class newInstrumentUnitViewController {
    public newInstrumentUnitViewController() {System.out.println("Jestem konstruktorem klasy newInstrumentUnitViewController");
    }

    private unitModel editedInstrumentUnit;

    public void setEditedInstrumentUnit(unitModel editedInstrumentUnit) {
        this.editedInstrumentUnit = editedInstrumentUnit;
    }
    private commonInstrumentViewController commonInstrumentController;

    public void setCommonInstrumentController(commonInstrumentViewController commonInstrumentController) {
        this.commonInstrumentController = commonInstrumentController;
    }
    @FXML
    private VBox mainVBox;
    @FXML
    private TextField newInstrumentUnitTextField;

    public void setNewInstrumentUnitTextField(String newInstrumentUnit) {
        this.newInstrumentUnitTextField.setText(newInstrumentUnit);
    }
    @FXML
    private Label newInstrumentUnitLabel;

    @FXML
    private void initialize() {
        System.out.println("Jestem funkcją initialize obiektu klasy newInstrumentUnitViewController");
    }

    @FXML
    private void addNewUnit(){
        if(!newInstrumentUnitTextField.getText().equals("")) {
            try {
                Dao<unitModel, Integer> instrumentUnitDao = DaoManager.createDao(dbSqlite.getConnectionSource(), unitModel.class);
                QueryBuilder<unitModel, Integer> instrumentUnitQueryBuilder = instrumentUnitDao.queryBuilder();
                instrumentUnitQueryBuilder.where().eq("unitName", newInstrumentUnitTextField.getText());
                PreparedQuery<unitModel> prepare = instrumentUnitQueryBuilder.prepare();
                List<unitModel> result = instrumentUnitDao.query(prepare);
                if(editedInstrumentUnit!=null){//edycja jednostki
                    if (result.isEmpty()) {
                        instrumentUnitDao.update(new unitModel(editedInstrumentUnit.getIdUnit(),newInstrumentUnitTextField.getText()));
                        Close.closeVBoxWindow(mainVBox);
                        if(commonInstrumentController!=null){
                            commonInstrumentController.getUnits();
                        }
                    }else{
                        if(result.get(0).getIdUnit()!=editedInstrumentUnit.getIdUnit()){
                            newInstrumentUnitLabel.setText("Taka jednostka już istnieje !");
                        }else{
                            Close.closeVBoxWindow(mainVBox);
                        }
                    }
                }else{
                    if (result.isEmpty()) {//dodawanie nowej jednostki
                        instrumentUnitDao.create(new unitModel(0, newInstrumentUnitTextField.getText()));
                        newInstrumentUnitTextField.clear();
                        Close.closeVBoxWindow(mainVBox);
                        if(commonInstrumentController!=null){
                            commonInstrumentController.getUnits();
                        }
                    } else {
                        newInstrumentUnitLabel.setText("Taka jednostka już istnieje !");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            newInstrumentUnitLabel.setText("Wprowadź prawidłową jednostkę !");
        }
    }
    @FXML
    private void cancelAddNewUnit(){
        if(!newInstrumentUnitTextField.getText().equals("")){
            if(ConfirmBox.display("Niezapisane dane","Czy na pewno chcesz zamknąć okno ?")){
                Close.closeVBoxWindow(mainVBox);
            }
        }else{
            Close.closeVBoxWindow(mainVBox);
        }
    }

}
