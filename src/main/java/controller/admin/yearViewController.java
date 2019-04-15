package controller.admin;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import dbUtil.dbSqlite;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.yearModel;
import util.Close;
import util.ConfirmBox;
import util.showAlert;

import java.sql.SQLException;
import java.util.List;

/**
 * Klasa kontrolera odpowiedzialnego za obsługę okna z wyswietlanymi latami
 */
public class yearViewController {
    public yearViewController() {}

    private yearModel editedYear;

    public void setEditedYear(yearModel editedYear) {
        this.editedYear = editedYear;
    }

    private commonInstrumentViewController commonInstrumentController;

    public void setCommonInstrumentController(commonInstrumentViewController commonInstrumentController) {
        this.commonInstrumentController = commonInstrumentController;
    }
    @FXML
    private VBox mainVBox;
    @FXML
    private TextField yearTextField;

    public void setYearTextField(String year) {
        this.yearTextField.setText(year);
    }

    @FXML
    private Label yearLabel;

    @FXML
    private void initialize() {}

    /**
     * Metoda służy do dodawania nowego roku do tabeli z latami. Na początku każdego roku administrator musi dodawać kolejny rok.
     */
    @FXML
    private void addNewYear(){
        if(!yearTextField.getText().equals("")) {
            try {
                Dao<yearModel, Integer> yearDao = DaoManager.createDao(dbSqlite.getConnectionSource(), yearModel.class);
                QueryBuilder<yearModel, Integer> yearQueryBuilder = yearDao.queryBuilder();
                yearQueryBuilder.where().eq("year", yearTextField.getText());
                PreparedQuery<yearModel> prepare = yearQueryBuilder.prepare();
                List<yearModel> result = yearDao.query(prepare);
                if(editedYear!=null){//edycja roku
                    if (result.isEmpty()) {
                        yearDao.update(new yearModel(editedYear.getIdYear(),yearTextField.getText()));
                        Close.closeVBoxWindow(mainVBox);
                        if(commonInstrumentController!=null){
                            commonInstrumentController.getYears();
                        }
                    }else{
                        if(result.get(0).getIdYear()!=editedYear.getIdYear()){
                            yearLabel.setText("Taki rok już istnieje !");
                        }else{
                            Close.closeVBoxWindow(mainVBox);
                        }
                    }
                }else{
                    if (result.isEmpty()) {//dodawanie nowego roku
                        yearDao.create(new yearModel(0, yearTextField.getText()));
                        yearTextField.clear();
                        Close.closeVBoxWindow(mainVBox);
                        if(commonInstrumentController!=null){
                            commonInstrumentController.getYears();
                        }
                    } else {
                        yearLabel.setText("Taki rok już istnieje !");
                    }
                }
                dbSqlite.closeConnection();
            } catch (SQLException e) {
                showAlert.display(e.getMessage());
            }
        }else{
            yearLabel.setText("Wprowadź prawidłowy rok !");
        }
    }
    @FXML
    private void cancelAddNewYear(){
        if(!yearTextField.getText().equals("")){
            if(ConfirmBox.display("Niezapisane dane","Czy na pewno chcesz zamknąć okno ?")){
                Close.closeVBoxWindow(mainVBox);
            }
        }else{
            Close.closeVBoxWindow(mainVBox);
        }
    }

}
