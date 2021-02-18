package monitor_ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    public void alertExample(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Button clicked");
        alert.showAndWait();
    }

    @FXML
    public void openStats(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/stats.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Day information");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
