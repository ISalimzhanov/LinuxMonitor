package packing;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Unpacking {
    private String file;

    public Unpacking(String fileName){
        this.file = fileName;
    }

    public void changeFile(String fileName){
        file = fileName;
    }

    private String getProcessName(HashMap<String, String> process){
        String[] arrayCommand = process.get("COMMAND").split("/");
        String processName = arrayCommand[arrayCommand.length-1];
        return processName;
    }

    private HashMap<String,String> parseProcesses(JSONObject process){
        HashMap<String, String> processMap = new HashMap<>();
        processMap.put("%MEM", process.get("%MEM").toString());
        processMap.put("%CPU", process.get("%CPU").toString());
        processMap.put("STAT", process.get("STAT").toString());
        processMap.put("RSS", process.get("RSS").toString());
        processMap.put("TTY", process.get("TTY").toString());
        processMap.put("PID", process.get("PID").toString());
        processMap.put("START", process.get("START").toString());
        processMap.put("TIME", process.get("TIME").toString());
        processMap.put("COMMAND", process.get("COMMAND").toString());
        processMap.put("USER", process.get("USER").toString());
        processMap.put("VSZ", process.get("VSZ").toString());
        processMap.put("NUM", process.get("NUM").toString());
        return processMap;
    }


    public ArrayList<HashMap<String, String>> getProcessData(String processName){
        JSONArray processData = new JSONArray();

        JSONParser jsonParser = new JSONParser();
        ArrayList<HashMap<String, String>> data = new ArrayList<>();

        try (FileReader reader = new FileReader("employees.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray processes = (JSONArray) obj;
            System.out.println(processes);

            //Iterate over employee array
            for(int i=0; i<processes.size(); i++){
                if(processName.equals( this.getProcessName(this.parseProcesses( (JSONObject) processes.get(i) )) )){
                    data.add( this.parseProcesses( (JSONObject) processes.get(i) ) );
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }



    public ArrayList<HashMap<String, String>> getData(){
        JSONArray processData = new JSONArray();

        JSONParser jsonParser = new JSONParser();
        ArrayList<HashMap<String, String>> data = new ArrayList<>();

        try (FileReader reader = new FileReader("employees.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray processes = (JSONArray) obj;
            System.out.println(processes);

            //Iterate over employee array
            processes.forEach( proc -> data.add( this.parseProcesses( (JSONObject) proc ) ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }
}
