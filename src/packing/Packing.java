package packing;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Packing {

    private String file;

    public Packing(String fileName){
        this.file = fileName;
        try {
            File myObj = new File(this.file);
            myObj.createNewFile();
            try(FileWriter writer = new FileWriter(this.file, false);) {
                JSONArray mainJSON = new JSONArray();
                writer.write(mainJSON.toJSONString());
            }catch(IOException e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void changeFile(String fileName){
        file = fileName;
    }

    private String getProcessName(HashMap<String, String> process){
        String[] arrayCommand = process.get("COMMAND").split("/");
        String processName = arrayCommand[arrayCommand.length-1];
        return processName;
    }

    public void packIntoFile(HashMap<String, String> process){
        JSONObject processData = new JSONObject();
        processData.put("%MEM", process.get("%MEM"));
        processData.put("%CPU", process.get("%CPU"));
        processData.put("STAT", process.get("STAT"));
        processData.put("RSS", process.get("RSS"));
        processData.put("TTY", process.get("TTY"));
        processData.put("PID", process.get("PID"));
        processData.put("START", process.get("START"));
        processData.put("TIME", process.get("TIME"));
        processData.put("COMMAND", process.get("COMMAND"));
        processData.put("USER", process.get("USER"));
        processData.put("VSZ", process.get("VSZ"));
        processData.put("NUM", process.get("NUM"));

        JSONParser jsonParser = new JSONParser();
        JSONArray processesWritten;
        try (FileReader reader = new FileReader(this.file))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            processesWritten = (JSONArray) obj;
            //System.out.println(processesWritten);
            //String processName = this.getProcessName(process);
            processesWritten.add(processData);

            try(FileWriter writer = new FileWriter(this.file, false)){
                writer.write(processesWritten.toJSONString());
                writer.flush();
            }catch(IOException e){
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
