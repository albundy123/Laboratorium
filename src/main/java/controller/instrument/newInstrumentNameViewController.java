package controller.instrument;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.instrumentNameModel;
import model.userModel;
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
    @FXML
    private VBox mainVBox;
    @FXML
    private Button addNewInstrumentNameButton;
    @FXML
    private TextField newInstrumentNameTextField;
    @FXML
    private Button cancelNewInstrumentNameButton;
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
                    newInstrumentMainController.getInstrumentNameList();
                    Stage window = (Stage) mainVBox.getScene().getWindow();
                    window.close();
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
                Stage window = (Stage) mainVBox.getScene().getWindow();
                window.close();
            }
        }else{
        Stage window = (Stage) mainVBox.getScene().getWindow();
        window.close();
        }
    }
}
