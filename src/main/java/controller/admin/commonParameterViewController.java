package controller.admin;

import javafx.fxml.FXML;
import model.fxModel.commonFxModel;

public class commonParameterViewController {
    public commonParameterViewController() {System.out.println("Siemanko jestem konstruktorem klasy commonParameterViewController.");}



    private commonFxModel editedElementFromList;

    public void setEditedElementFromList(commonFxModel editedElementFromList) {
        this.editedElementFromList = editedElementFromList;
    }

    @FXML
    private void initialize(){
        System.out.println("Siemanko jestem funkcją initialize klasy commonParameterViewController.");

    }
}
