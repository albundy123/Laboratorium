package util;

        import javafx.geometry.Pos;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.layout.HBox;
        import javafx.scene.layout.VBox;
        import javafx.stage.Modality;
        import javafx.stage.Stage;

/**
 * Klasa zawierająca metodę display do wyświetlania okna dialogowego
 */
public class ConfirmBox {
    static boolean answer;
    public static boolean display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(120);
        Label label = new Label();
        label.setText(message);
        Button yesButton = new Button("Tak");
        Button noButton = new Button("Nie");

        yesButton.setOnAction(e -> {
            answer=true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer=false;
            window.close();
        });
        VBox vBox = new VBox(10);
        HBox hBox = new HBox(10);

        hBox.getChildren().addAll(yesButton,noButton);
        hBox.setAlignment(Pos.CENTER);
        yesButton.setPrefWidth(80);
        noButton.setPrefWidth(80);
        vBox.getChildren().addAll(label,hBox);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();
        return answer;
    }
}