package monitor_ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import monitors.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        String s;
        Process p;
        int ex=1;
        int exMaven = 1;
        BufferedReader br;
        String checkOS="";
        try{
            p = Runtime.getRuntime().exec("mongod --version");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
            ex = this.installMongoUbuntu();

        }catch (Exception e) {
            ex = this.installMongoUbuntu();
        }
        try{
            p = Runtime.getRuntime().exec("sudo systemctl start mongod");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
            System.out.println(ex);
        }catch (Exception e) {

        }
        try{
            p = Runtime.getRuntime().exec("mvn --version");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            exMaven = p.exitValue();
            p.destroy();
            if(exMaven == 0){
                System.out.println("mv success");
            }
        }catch(Exception e){
            p = Runtime.getRuntime().exec("sudo apt update");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            p.destroy();
            p = Runtime.getRuntime().exec("sudo apt install maven");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            exMaven = p.exitValue();
            p.destroy();
        }
        if (ex != 0) {
            if(exMaven != 0){
                System.out.println("Couldn't install maven");
            }
            System.out.println("Couldn't start mongodb");
        }else {
            if(exMaven != 0){
                System.out.println("Couldn't start maven");
            }else{
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
                primaryStage.setTitle("Linux Monitor");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/sample.fxml"));
                Controller controller = loader.getController();
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            }

        }
    }

    private int installMongoUbuntu(){
        BufferedReader br;
        String s;
        Process p;
        int ex=1;
        try{
            p = Runtime.getRuntime().exec("sudo apt update && sudo apt upgrade");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
            p = Runtime.getRuntime().exec("sudo apt install curl");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
            p = Runtime.getRuntime().exec("wget -qO - https://www.mongodb.org/static/pgp/server-4.4.asc | sudo apt-key add -");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
            p = Runtime.getRuntime().exec("echo \"deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/4.4 multiverse\" | sudo tee /etc/apt/sources.list.d/mongodb-org-4.4.list");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
            System.out.println("try clause installation 21");
            p = Runtime.getRuntime().exec("sudo apt-get update");
            System.out.println("try clause installation 22");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
            p = Runtime.getRuntime().exec("sudo apt-get install -y mongodb-org");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
            if(ex == 0){
                System.out.println("c4 success");
            }
        }catch(Exception e){

        }
        return ex;
    }

    private int installMongoRedHat(){
        BufferedReader br;
        String s;
        Process p;
        int ex=1;
        try{
            FileWriter myWriter = new FileWriter("/etc/yum.repos.d/mongodb-org-4.4.repo");
            myWriter.write("[mongodb-org-4.4]\nname=MongoDB Repository\nbaseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/4.4/x86_64/\ngpgcheck=1\nenabled=1\ngpgkey=https://www.mongodb.org/static/pgp/server-4.4.asc");
            myWriter.close();
            p = Runtime.getRuntime().exec("sudo yum install -y mongodb-org");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
            p = Runtime.getRuntime().exec("sudo yum install -y mongodb-org-4.4.4 mongodb-org-server-4.4.4 mongodb-org-shell-4.4.4 mongodb-org-mongos-4.4.4 mongodb-org-tools-4.4.4");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
            p = Runtime.getRuntime().exec("exclude=mongodb-org,mongodb-org-server,mongodb-org-shell,mongodb-org-mongos,mongodb-org-tools");
            br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            ex = p.exitValue();
            p.destroy();
        }catch(Exception e){

        }
        return ex;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
