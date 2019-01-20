package util;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Close {
    public static void closeVBoxWindow(VBox vBox){
        Stage window = (Stage)vBox.getScene().getWindow();
        window.close();
    }
    public static void closeAnchorPaneWindow(AnchorPane anchorPane){
        Stage window = (Stage)anchorPane.getScene().getWindow();
        window.close();
    }
}
