package monitor_ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import monitors.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        String s;
        Process p;
        int ex=1;
        try{
            p = Runtime.getRuntime().exec("sudo systemctl status mongod");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
        }catch (Exception e) {

        }
        if (ex != 0) {
            System.out.println("Couldn't start mongodb");
        }else {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
            primaryStage.setTitle("Linux Monitor");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/sample.fxml"));
            Controller controller = loader.getController();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
