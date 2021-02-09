package monitors.process_monitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.time.LocalTime;

public class ProcessMonitor {
    private final String username;
    private HashMap<String, HashMap<String, String>> cash;
    /*
 0 = {HashMap@1013}  size = 11
 "%MEM" -> "0.1"
 "%CPU" -> "0.0"
 "STAT" -> "Ss"
 "RSS" -> "10836"
 "TTY" -> "?"
 "PID" -> "1586"
 "START" -> "22:55"
 "TIME" -> "0:00"
 "COMMAND" -> "/lib/systemd/systemd"
 "USER" -> "iskander"
 "VSZ" -> "19516"
 "NUM" -> "1"

 NUM - number of checks of a working process for calculating average CPU/RAM utilisation
 */

    public ProcessMonitor(String user) {
        username = user;
    }

    private static LinkedList<String> get_words(String sentence) {
        LinkedList<String> res = new LinkedList<>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < sentence.length(); ++i) {
            if (sentence.charAt(i) == ' ' && !word.toString().isEmpty()) {
                //System.out.println(word.toString());
                res.add(word.toString());
                word = new StringBuilder();
            } else if (sentence.charAt(i) != ' ') {
                char c = sentence.charAt(i);
                word.append(c);
            }
        }
        if (!word.toString().isEmpty())
            res.add(word.toString());
        return res;
    }

    public Map<String, String> processDetails(LinkedList<String> parameters, String info) {
        LinkedList<String> values = get_words(info);
        Map<String, String> res = new HashMap<>();
        for (int i = 0; i < parameters.size(); ++i) {
            res.put(parameters.get(i), values.get(i));
        }
        return res;
    }

    public LinkedList<Map<String, String>> getActive() {
        LinkedList<Map<String, String>> actives = new LinkedList<>();
        try {
            String line;
            Process p = Runtime.getRuntime().exec("ps -aux");
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            line = input.readLine();
            LinkedList<String> parameters = get_words(line);
            while ((line = input.readLine()) != null) {
                Map<String, String> process_info = this.processDetails(parameters, line);
                if (process_info.get("USER").equals(this.username)) {
                    actives.add(process_info);
                }
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return actives;
    }

    private void transferProcessesToCash(LinkedList<Map<String, String>> processes){
        HashMap<String, String> processData;
        String[] commandArray;
        String processName;
        System.out.println(processes.size());
        for(int i=0; i< processes.size(); i++){
            System.out.println("got here");
            commandArray = processes.get(i).get("COMMAND").split("/");
            processName = commandArray[commandArray.length-1];
            processData = new HashMap<String, String>();
            processData.put("%MEM", processes.get(i).get("%MEM"));
            processData.put("%CPU", processes.get(i).get("%CPU"));
            processData.put("STAT", processes.get(i).get("STAT"));
            processData.put("RSS", processes.get(i).get("RSS"));
            processData.put("TTY", processes.get(i).get("TTY"));
            processData.put("PID", processes.get(i).get("PID"));
            processData.put("START", processes.get(i).get("START"));
            processData.put("TIME", processes.get(i).get("TIME"));
            processData.put("COMMAND", processes.get(i).get("COMMAND"));
            processData.put("USER", processes.get(i).get("USER"));
            processData.put("VSZ", processes.get(i).get("VSZ"));
            processData.put("NUM", "1");
            this.cash.put(processName, processData);
        }
    }

    public void run() {
        while (true) {
            long clock = LocalTime.now().toNanoOfDay();
            LinkedList<Map<String, String>> processes = this.getActive();
            System.out.println("got here 0");
            this.transferProcessesToCash(processes);
            //toDo
            System.out.println((LocalTime.now().toNanoOfDay() - clock) / Math.pow(10, 9));
        }
    }
}
