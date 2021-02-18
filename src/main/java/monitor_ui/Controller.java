package monitor_ui;

import com.mongodb.client.MongoDatabase;
import database.LocalDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

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
            stage.setTitle("Stats");
            stage.setScene(new Scene(root));
            MongoDatabase db = LocalDatabase.getInstance();
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("proc_name", "lol");
            hm.put("cpu", "90");
            hm.put("time", "102");
            LocalDatabase.addData(hm);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
