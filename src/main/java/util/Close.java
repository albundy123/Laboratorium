package util;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Klasa zawierające metody do zamykania okien.
 */
public class Close {
    public static void closeVBoxWindow(VBox vBox){
        Stage window = (Stage)vBox.getScene().getWindow();
        window.close();
    }
    public static void closeAnchorPaneWindow(AnchorPane anchorPane){
        Stage window = (Stage)anchorPane.getScene().getWindow();
        window.close();
    }
    public static void closeSplitPaneWindow(SplitPane splitPane){
        Stage window = (Stage)splitPane.getScene().getWindow();
        window.close();
    }
}
