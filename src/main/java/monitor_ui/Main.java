package monitor_ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import monitors.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        primaryStage.setTitle("Linux Monitor");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/sample.fxml"));
        Controller controller = loader.getController();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) {
        ProcessMonitor processMonitor = ProcessMonitor.getInstance("asanali");
        WindowsMonitor windowsMonitor = WindowsMonitor.getInstance();
        //processMonitor.start();
        windowsMonitor.start();
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(
                new Thread() {
                    public void run() {
                        //processMonitor.setKeepRunning(false);
                        windowsMonitor.setKeepRunning(false);
                        try {
                            //processMonitor.join();
                            windowsMonitor.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
