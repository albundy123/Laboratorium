package controller.instrument;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import controller.storehouse.newInstrumentViewController;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.instrumentNameModel;
import util.Close;
import util.ConfirmBox;

import java.sql.SQLException;
import java.util.List;

public class newInstrumentNameViewController {
    public newInstrumentNameViewController() {System.out.println("Jestem konstruktorem klasy newInstrumentNameViewController");
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
    private TextField newInstrumentNameTextField;
    @FXML
    private Label newInstrumentNameLabel;

    @FXML
    private void initialize() {
        System.out.println("Jestem funkcją initialize obiektu klasy newInstrumentNameViewController");
    }
    @FXML
    private void addNewName(){
        if(!newInstrumentNameTextField.getText().equals("")) {
            try {
                Dao<instrumentNameModel, Integer> instrumentNameDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentNameModel.class);
                QueryBuilder<instrumentNameModel, Integer> instrumentNameQueryBuilder = instrumentNameDao.queryBuilder();
                instrumentNameQueryBuilder.where().eq("instrumentName", newInstrumentNameTextField.getText());
                PreparedQuery<instrumentNameModel> prepare = instrumentNameQueryBuilder.prepare();
                List<instrumentNameModel> result = instrumentNameDao.query(prepare);
                if (result.isEmpty()) {
                    instrumentNameDao.create(new instrumentNameModel(0, newInstrumentNameTextField.getText()));
                    newInstrumentNameTextField.clear();
                    if(editInstrumentMainController!=null){editInstrumentMainController.getInstrumentNameList();}
                    if(newInstrumentMainController!=null){ newInstrumentMainController.getInstrumentNameList();}
                    Close.closeVBoxWindow(mainVBox);
                } else {
                    newInstrumentNameLabel.setText("Taka nazwa przyrządu już istnieje !");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            newInstrumentNameLabel.setText("Wprowadź prawidłową nazwę przyrządu !");
        }
    }
    @FXML
    private void cancelAddNewName(){
        if(!newInstrumentNameTextField.getText().equals("")){
            if(ConfirmBox.display("Niezapisane dane","Czy na pewno chcesz zamknąć okno ?")){
                Close.closeVBoxWindow(mainVBox);
            }
        }else{
            Close.closeVBoxWindow(mainVBox);
        }
    }
}
