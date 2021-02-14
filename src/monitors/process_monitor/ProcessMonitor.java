package monitors.process_monitor;

import packing.Packing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class ProcessMonitor extends Thread {
    private final String username;
    private final HashMap<String, HashMap<String, String>> cash;
    private static final Map<String, ProcessMonitor> instance = new HashMap<>();
    private volatile boolean keepRunning = true;

    private ProcessMonitor(String user) {
        username = user;
        cash = new HashMap<>();
    }

    public static ProcessMonitor getInstance(String user) {
        if (!instance.containsKey(user)) {
            instance.put(user, new ProcessMonitor(user));
        }
        return instance.get(user);
    }

    private static LinkedList<String> get_words(String sentence) {
        LinkedList<String> res = new LinkedList<>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < sentence.length(); ++i) {
            if (sentence.charAt(i) == ' ' && !word.toString().isEmpty()) {
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

    public void packageCash() {
        Packing packager = new Packing("processes.json");
        for (Map.Entry<String, HashMap<String, String>> stringHashMapEntry : this.cash.entrySet()) {
            String name = stringHashMapEntry.getKey();
            packager.packIntoFile(this.cash.get(name));
        }
        this.cash.clear();
    }

    private void transferToCash(LinkedList<Map<String, String>> processes) {
        Packing packager = new Packing("processes.json");
        HashMap<String, String> processData;
        String[] commandArray;
        String[] currentProcesses = new String[processes.size()];
        String processName;

        LinkedList<String> to_delete = new LinkedList<>();
        for (Map.Entry<String, HashMap<String, String>> stringHashMapEntry : this.cash.entrySet()) {
            String name = stringHashMapEntry.getKey();
            if (!Arrays.asList(currentProcesses).contains(name)) {
                packager.packIntoFile(this.cash.get(name));
                to_delete.add(name);
            }
        }
        for (String s : to_delete) {
            this.cash.remove(s);
        }

        float memInitial, memAdditional, cpuInitial, cpuAdditional;
        int num;
        for (int i = 0; i < processes.size(); i++) {
            commandArray = processes.get(i).get("COMMAND").split("/");
            processName = commandArray[commandArray.length - 1];
            currentProcesses[i] = processName;
            if (this.cash.get(processName) == null) {
                processData = new HashMap<>();
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
            } else {
                this.cash.get(processName).put("NUM", String.valueOf(Integer.parseInt(this.cash.get(processName).get("NUM")) + 1));
                num = Integer.parseInt(this.cash.get(processName).get("NUM"));
                memInitial = Float.parseFloat(this.cash.get(processName).get("%MEM"));
                memAdditional = Float.parseFloat(processes.get(i).get("%MEM"));
                cpuInitial = Float.parseFloat(this.cash.get(processName).get("%CPU"));
                cpuAdditional = Float.parseFloat(processes.get(i).get("%CPU"));
                this.cash.get(processName).put("TIME", processes.get(i).get("TIME"));
                this.cash.get(processName).put("%MEM", String.valueOf((memAdditional + memInitial) / num));
                this.cash.get(processName).put("%CPU", String.valueOf((cpuAdditional + cpuInitial) / num));

            }
        }
    }

    public void run() {
        while (this.keepRunning) {
            LinkedList<Map<String, String>> processes = this.getActive();
            this.transferToCash(processes);
        }
        this.packageCash();
    }

    public boolean isKeepRunning() {
        return keepRunning;
    }

    public void setKeepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
    }
}