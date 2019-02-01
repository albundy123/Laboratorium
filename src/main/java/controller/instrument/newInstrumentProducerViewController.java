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
import model.instrumentProducerModel;
import util.Close;
import util.ConfirmBox;


import java.sql.SQLException;
import java.util.List;

public class newInstrumentProducerViewController {
    public newInstrumentProducerViewController() {}

    private newInstrumentViewController newInstrumentMainController;
    public void setNewInstrumentMainController(newInstrumentViewController newInstrumentMainController) {
        this.newInstrumentMainController = newInstrumentMainController;
    }

    private editInstrumentViewController editInstrumentMainController;
    public void setEditInstrumentMainController(editInstrumentViewController editInstrumentMainController) {
        this.editInstrumentMainController = editInstrumentMainController;
    }

    private instrumentProducerModel editedInstrumentProducer;

    public void setEditedInstrumentProducer(instrumentProducerModel editedInstrumentProducer) {
        this.editedInstrumentProducer = editedInstrumentProducer;
    }

    private commonInstrumentViewController commonInstrumentController;

    public void setCommonInstrumentController(commonInstrumentViewController commonInstrumentController) {
        this.commonInstrumentController = commonInstrumentController;
    }
    @FXML
    private VBox mainVBox;
    @FXML
    private TextField newInstrumentProducerTextField;

    public void setNewInstrumentProducerTextField(String newInstrumentProducer) {
        this.newInstrumentProducerTextField.setText(newInstrumentProducer);
    }

    @FXML
    private Label newInstrumentProducerLabel;

    @FXML
    private void initialize() {}
    @FXML
    private void addNewProducer(){
        if(!newInstrumentProducerTextField.getText().equals("")) {
            try {
                Dao<instrumentProducerModel, Integer> instrumentProducerDao = DaoManager.createDao(dbSqlite.getConnectionSource(), instrumentProducerModel.class);
                QueryBuilder<instrumentProducerModel, Integer> instrumentProducerQueryBuilder = instrumentProducerDao.queryBuilder();
                instrumentProducerQueryBuilder.where().eq("instrumentProducer", newInstrumentProducerTextField.getText());
                PreparedQuery<instrumentProducerModel> prepare = instrumentProducerQueryBuilder.prepare();
                List<instrumentProducerModel> result = instrumentProducerDao.query(prepare);
                if (editedInstrumentProducer != null) {//edycja producenta
                    if (result.isEmpty()) {
                        instrumentProducerDao.update(new instrumentProducerModel(editedInstrumentProducer.getIdInstrumentProducer(),newInstrumentProducerTextField.getText()));
                        Close.closeVBoxWindow(mainVBox);
                        if(commonInstrumentController!=null){
                            commonInstrumentController.getProducers();
                        }
                    }else{
                        if(result.get(0).getIdInstrumentProducer()!=editedInstrumentProducer.getIdInstrumentProducer()){
                            newInstrumentProducerLabel.setText("Taki producent przyrządu już istnieje !");
                        }else{
                            Close.closeVBoxWindow(mainVBox);
                        }
                    }
                }else{
                    if (result.isEmpty()) {
                        instrumentProducerDao.create(new instrumentProducerModel(0, newInstrumentProducerTextField.getText()));
                        newInstrumentProducerTextField.clear();
                        if(editInstrumentMainController!=null){editInstrumentMainController.getInstrumentProducerList();}
                        if(newInstrumentMainController!=null){newInstrumentMainController.getInstrumentProducerList();}
                        Close.closeVBoxWindow(mainVBox);
                        if(commonInstrumentController!=null){
                            commonInstrumentController.getProducers();
                        }
                    } else {
                        newInstrumentProducerLabel.setText("Taki producent przyrządu już istnieje !");
                    }
                }
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            newInstrumentProducerLabel.setText("Wprowadź prawidłowy typ przyrządu !");
        }
    }
    @FXML
    private void cancelAddNewProducer(){
        if(!newInstrumentProducerTextField.getText().equals("")){
            if(ConfirmBox.display("Niezapisane dane","Czy na pewno chcesz zamknąć okno ?")){
                Close.closeVBoxWindow(mainVBox);
            }
        }else{
            Close.closeVBoxWindow(mainVBox);
        }
    }
}
