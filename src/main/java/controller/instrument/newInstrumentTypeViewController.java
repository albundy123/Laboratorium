package controller.instrument;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import controller.storehouse.newInstrumentViewController;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.instrumentTypeModel;
import util.ConfirmBox;

import java.sql.SQLException;
import java.util.List;

public class newInstrumentTypeViewController {
    public newInstrumentTypeViewController() {System.out.println("Jestem konstruktorem klasy newInstrumentTypeViewController");
    }
    private newInstrumentViewController newInstrumentMainController;

    public void setNewInstrumentMainController(newInstrumentViewController newInstrumentMainController) {
        this.newInstrumentMainController = newInstrumentMainController;
    }

    private editInstrumentViewController editInstrumentMainController;

    public void setEditInstrumentMainController(editInstrumentViewController editInstrumentMainController) {
        this.editInstrumentMainController = editInstrumentMainController;
    }

    @FXML
    private VBox mainVBox;
    @FXML
    private Button addNewInstrumentTypeButton;
    @FXML
    private TextField newInstrumentTypeTextField;
    @FXML
    private Button cancelNewInstrumentTypeButton;
    @FXML
    private Label newInstrumentTypeLabel;
    @FXML
    private void initialize() {
        System.out.println("Jestem funkcją initialize obiektu klasy newInstrumentTypeViewController");
    }
    @FXML
    private void addNewType(){
        if(!newInstrumentTypeTextField.getText().equals("")) {
            try {
                Dao<instrumentTypeModel, Integer> instrumentTypeDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentTypeModel.class);
                QueryBuilder<instrumentTypeModel, Integer> instrumentTypeQueryBuilder = instrumentTypeDao.queryBuilder();
                instrumentTypeQueryBuilder.where().eq("instrumentType", newInstrumentTypeTextField.getText());
                PreparedQuery<instrumentTypeModel> prepare = instrumentTypeQueryBuilder.prepare();
                List<instrumentTypeModel> result = instrumentTypeDao.query(prepare);
                if (result.isEmpty()) {
                    instrumentTypeDao.create(new instrumentTypeModel(0, newInstrumentTypeTextField.getText()));
                    newInstrumentTypeTextField.clear();
                    if(editInstrumentMainController!=null){editInstrumentMainController.getInstrumentTypeList();}
                    if(newInstrumentMainController!=null){newInstrumentMainController.getInstrumentTypeList();}
                    Stage window = (Stage) mainVBox.getScene().getWindow();
                    window.close();
                } else {
                    newInstrumentTypeLabel.setText("Taki typ przyrządu już istnieje !");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            newInstrumentTypeLabel.setText("Wprowadź prawidłowy typ przyrządu !");
        }
    }
    @FXML
    private void cancelAddNewType(){
        if(!newInstrumentTypeTextField.getText().equals("")){
            if(ConfirmBox.display("Niezapisane dane","Czy na pewno chcesz zamknąć okno ?")){
                Stage window = (Stage) mainVBox.getScene().getWindow();
                window.close();
            }
        }else{
            Stage window = (Stage) mainVBox.getScene().getWindow();
            window.close();
        }
    }
}
