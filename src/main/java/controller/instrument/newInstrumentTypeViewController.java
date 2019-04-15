package controller.instrument;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import controller.admin.commonInstrumentViewController;
import controller.storehouse.newInstrumentViewController;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.instrumentTypeModel;
import util.Close;
import util.ConfirmBox;
import util.showAlert;

import java.sql.SQLException;
import java.util.List;
/**
 * Klasa kontrolera przeznaczonego do obsługi okna do dodawania nowego typu przyrządu newInstrumentTypeView.fxml
 */
public class newInstrumentTypeViewController {
    public newInstrumentTypeViewController() {}

    private newInstrumentViewController newInstrumentMainController;
    public void setNewInstrumentMainController(newInstrumentViewController newInstrumentMainController) {
        this.newInstrumentMainController = newInstrumentMainController;
    }

    private editInstrumentViewController editInstrumentMainController;
    public void setEditInstrumentMainController(editInstrumentViewController editInstrumentMainController) {
        this.editInstrumentMainController = editInstrumentMainController;
    }

    private instrumentTypeModel editedInstrumentType;
    public void setEditedInstrumentType(instrumentTypeModel editedInstrumentType) {
        this.editedInstrumentType = editedInstrumentType;
    }
    private commonInstrumentViewController commonInstrumentController;

    public void setCommonInstrumentController(commonInstrumentViewController commonInstrumentController) {
        this.commonInstrumentController = commonInstrumentController;
    }

    @FXML
    private VBox mainVBox;
    @FXML
    private TextField newInstrumentTypeTextField;

    public void setNewInstrumentTypeTextField(String newInstrumentType) {
        this.newInstrumentTypeTextField.setText(newInstrumentType);
    }

    @FXML
    private Label newInstrumentTypeLabel;

    @FXML
    private void initialize() {}
    @FXML
    private void addNewType(){
        if(!newInstrumentTypeTextField.getText().equals("")) {
            try {
                Dao<instrumentTypeModel, Integer> instrumentTypeDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentTypeModel.class);
                QueryBuilder<instrumentTypeModel, Integer> instrumentTypeQueryBuilder = instrumentTypeDao.queryBuilder();
                instrumentTypeQueryBuilder.where().eq("instrumentType", newInstrumentTypeTextField.getText());
                PreparedQuery<instrumentTypeModel> prepare = instrumentTypeQueryBuilder.prepare();
                List<instrumentTypeModel> result = instrumentTypeDao.query(prepare);
                if(editedInstrumentType!=null){//edycja typu
                    if (result.isEmpty()) {
                        instrumentTypeDao.update(new instrumentTypeModel(editedInstrumentType.getIdInstrumentType(),newInstrumentTypeTextField.getText()));
                        Close.closeVBoxWindow(mainVBox);
                        if(commonInstrumentController!=null){
                            commonInstrumentController.getTypes();
                        }
                    }else{
                        if(result.get(0).getIdInstrumentType()!=editedInstrumentType.getIdInstrumentType()){
                            newInstrumentTypeLabel.setText("Taki typ przyrządu już istnieje !");
                        }else{
                            Close.closeVBoxWindow(mainVBox);
                        }
                    }
                }else{
                    if (result.isEmpty()) {//dodawanie nowego typu
                        instrumentTypeDao.create(new instrumentTypeModel(0, newInstrumentTypeTextField.getText()));
                        newInstrumentTypeTextField.clear();
                        if(editInstrumentMainController!=null){editInstrumentMainController.getInstrumentTypeList();}
                        if(newInstrumentMainController!=null){newInstrumentMainController.getInstrumentTypeList();}
                        Close.closeVBoxWindow(mainVBox);
                        if(commonInstrumentController!=null){commonInstrumentController.getTypes();}
                    } else {
                        newInstrumentTypeLabel.setText("Taki typ przyrządu już istnieje !");
                    }
                }
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                showAlert.display(e.getMessage());
            }
        }else{
            newInstrumentTypeLabel.setText("Wprowadź prawidłowy typ przyrządu !");
        }
    }
    @FXML
    private void cancelAddNewType(){
        if(!newInstrumentTypeTextField.getText().equals("")){
            if(ConfirmBox.display("Niezapisane dane","Czy na pewno chcesz zamknąć okno ?")){
                Close.closeVBoxWindow(mainVBox);
            }
        }else{
            Close.closeVBoxWindow(mainVBox);
        }
    }
}
