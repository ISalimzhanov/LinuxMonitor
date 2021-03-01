package monitor_ui;

import database.ProcessRecord;
import database.ProcessDAO;
import database.ProcessTab;
import database.ProcessTabDao;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import monitor_ui.timer.TrackerTimer;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class Controller {


    @FXML private DatePicker datePicker;
    @FXML private Label timer;
    @FXML private Button mainButton;
    private Timeline timeline;

    private TrackerTimer trackerTimer = new TrackerTimer();


    @FXML
    public void initialize() {
        datePicker.setValue(NOW_LOCAL_DATE());
    }


    @FXML
    public void startTimer(){

        timeline = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                trackerTimer.change(timer);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        mainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(trackerTimer.timerOn) {
                    timeline.play();
                    trackerTimer.timerOn = false;
                    mainButton.setText("Stop");
                } else {
                    timeline.pause();
                    trackerTimer.timerOn = true;
                    mainButton.setText("Start");
                }
            }
        });
    }

    @FXML
    public void openWindowStats(){
        Parent root;
        try {

            ObservableList<ProcessTab> processTabs = FXCollections.observableArrayList();
            ArrayList<ProcessTab> list = ProcessTabDao.getAllWindows(datePicker.getValue().toString());
            processTabs.addAll(list);


            TableView<ProcessTab> table = new TableView<ProcessTab>(processTabs);

            TableColumn<ProcessTab, String> nameColumn = new TableColumn<ProcessTab, String>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<ProcessTab, String>("name"));
            table.getColumns().add(nameColumn);

            TableColumn<ProcessTab, String> stat = new TableColumn<ProcessTab, String>("Status");
            stat.setCellValueFactory(new PropertyValueFactory<ProcessTab, String>("status"));
            table.getColumns().add(stat);

            TableColumn<ProcessTab, String> start = new TableColumn<ProcessTab, String>("Start");
            start.setCellValueFactory(new PropertyValueFactory<ProcessTab, String>("start"));
            table.getColumns().add(start);

            TableColumn<ProcessTab, String> PID = new TableColumn<ProcessTab, String>("PID");
            PID.setCellValueFactory(new PropertyValueFactory<ProcessTab, String>("PID"));
            table.getColumns().add(PID);


            Stage stage = new Stage();
            stage.setTitle("Windows Stats");

            Scene scene = new Scene(table, 1000, 600);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void openStats(){
        Parent root;
        try {

            ObservableList<ProcessRecord> processRecords = FXCollections.observableArrayList();
            ArrayList<ProcessRecord> list = ProcessDAO.getAllProcesses(datePicker.getValue().toString());
            processRecords.addAll(list);


            TableView<ProcessRecord> table = new TableView<ProcessRecord>(processRecords);


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
