package monitor_ui;

import com.mongodb.client.MongoDatabase;
import database.DatabaseManager;
import database.Process;
import database.ProcessDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
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

            ObservableList<Process> processes = FXCollections.observableArrayList();

            ArrayList<Process> list = ProcessDAO.getAllProcesses("19022021");
            processes.addAll(list);

            TableView<Process> table = new TableView<Process>(processes);

            TableColumn<Process, String> user = new TableColumn<Process, String>("USER");
            user.setCellValueFactory(new PropertyValueFactory<Process, String>("user"));
            table.getColumns().add(user);

            TableColumn<Process, String> nameColumn = new TableColumn<Process, String>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<Process, String>("name"));
            table.getColumns().add(nameColumn);

            TableColumn<Process, String> cpu = new TableColumn<Process, String>("CPU");
            cpu.setCellValueFactory(new PropertyValueFactory<Process, String>("cpuUsage"));
            table.getColumns().add(cpu);

            TableColumn<Process, String> memoryUtil = new TableColumn<Process, String>("RAM");
            memoryUtil.setCellValueFactory(new PropertyValueFactory<Process, String>("memoryUtil"));
            table.getColumns().add(memoryUtil);

            TableColumn<Process, String> workTime = new TableColumn<Process, String>("Time");
            workTime.setCellValueFactory(new PropertyValueFactory<Process, String>("workTime"));
            table.getColumns().add(workTime);

            TableColumn<Process, String> procStat = new TableColumn<Process, String>("Stat");
            procStat.setCellValueFactory(new PropertyValueFactory<Process, String>("procStat"));
            table.getColumns().add(procStat);

            TableColumn<Process, String> procRSS = new TableColumn<Process, String>("RSS");
            procRSS.setCellValueFactory(new PropertyValueFactory<Process, String>("procRSS"));
            table.getColumns().add(procRSS);

            TableColumn<Process, String> procTTY = new TableColumn<Process, String>("TTY");
            procTTY.setCellValueFactory(new PropertyValueFactory<Process, String>("procTTY"));
            table.getColumns().add(procTTY);

            TableColumn<Process, String> pid = new TableColumn<Process, String>("pid");
            pid.setCellValueFactory(new PropertyValueFactory<Process, String>("pid"));
            table.getColumns().add(pid);

            TableColumn<Process, String> startTime = new TableColumn<Process, String>("Start time");
            startTime.setCellValueFactory(new PropertyValueFactory<Process, String>("startTime"));
            table.getColumns().add(startTime);

            TableColumn<Process, String> procVSZ = new TableColumn<Process, String>("VSZ");
            procVSZ.setCellValueFactory(new PropertyValueFactory<Process, String>("procVSZ"));
            table.getColumns().add(procVSZ);

            TableColumn<Process, String> command = new TableColumn<Process, String>("command");
            command.setCellValueFactory(new PropertyValueFactory<Process, String>("command"));
            table.getColumns().add(command);


            Stage stage = new Stage();
            stage.setTitle("Stats");

            Scene scene = new Scene(table, 1000, 600);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
