package monitor_ui;

import database.ProcessRecord;
import database.ProcessDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class Controller {


    @FXML private DatePicker datePicker;
    @FXML private Label timer;


    @FXML
    public void initialize() {
        datePicker.setValue(NOW_LOCAL_DATE());
    }


    @FXML
    public void alertExample(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Button clicked");
        alert.showAndWait();
    }

    @FXML
    public void openStats(){
        Parent root;
        try {

            ObservableList<ProcessRecord> processRecords = FXCollections.observableArrayList();
            ArrayList<ProcessRecord> list = ProcessDAO.getAllProcesses(datePicker.getValue().toString());
            processRecords.addAll(list);


            TableView<ProcessRecord> table = new TableView<ProcessRecord>(processRecords);

            TableColumn<ProcessRecord, String> user = new TableColumn<ProcessRecord, String>("USER");
            user.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("user"));
            table.getColumns().add(user);

            TableColumn<ProcessRecord, String> nameColumn = new TableColumn<ProcessRecord, String>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("name"));
            table.getColumns().add(nameColumn);

            TableColumn<ProcessRecord, String> cpu = new TableColumn<ProcessRecord, String>("CPU");
            cpu.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("cpuUsage"));
            table.getColumns().add(cpu);

            TableColumn<ProcessRecord, String> memoryUtil = new TableColumn<ProcessRecord, String>("RAM");
            memoryUtil.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("memoryUtil"));
            table.getColumns().add(memoryUtil);

            TableColumn<ProcessRecord, String> workTime = new TableColumn<ProcessRecord, String>("Time");
            workTime.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("workTime"));
            table.getColumns().add(workTime);

            TableColumn<ProcessRecord, String> procStat = new TableColumn<ProcessRecord, String>("Stat");
            procStat.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("procStat"));
            table.getColumns().add(procStat);

            TableColumn<ProcessRecord, String> procRSS = new TableColumn<ProcessRecord, String>("RSS");
            procRSS.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("procRSS"));
            table.getColumns().add(procRSS);

            TableColumn<ProcessRecord, String> procTTY = new TableColumn<ProcessRecord, String>("TTY");
            procTTY.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("procTTY"));
            table.getColumns().add(procTTY);

            TableColumn<ProcessRecord, String> pid = new TableColumn<ProcessRecord, String>("pid");
            pid.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("pid"));
            table.getColumns().add(pid);

            TableColumn<ProcessRecord, String> startTime = new TableColumn<ProcessRecord, String>("Start time");
            startTime.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("startTime"));
            table.getColumns().add(startTime);

            TableColumn<ProcessRecord, String> procVSZ = new TableColumn<ProcessRecord, String>("VSZ");
            procVSZ.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("procVSZ"));
            table.getColumns().add(procVSZ);

            TableColumn<ProcessRecord, String> command = new TableColumn<ProcessRecord, String>("command");
            command.setCellValueFactory(new PropertyValueFactory<ProcessRecord, String>("command"));
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

    public static LocalDate NOW_LOCAL_DATE (){
        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date , formatter);
    }

}
