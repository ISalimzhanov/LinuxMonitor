package monitors;

import database.ProcessDAO;
import database.ProcessRecord;

import java.net.UnknownHostException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
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

    public static ProcessMonitor getInstance() {
        String user = System.getProperty("user.name");
        if (!instance.containsKey(user)) {
            instance.put(user, new ProcessMonitor(user));
        }
        return instance.get(user);
    }

    public static ProcessMonitor getInstance(String user) {
        if (!instance.containsKey(user)) {
            instance.put(user, new ProcessMonitor(user));
        }
        return instance.get(user);
    }

    public static LinkedList<String> getWords(String sentence) {
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

    public static Map<String, String> getProcessDetails(LinkedList<String> parameters, String processInfo) {
        LinkedList<String> values = getWords(processInfo);
        Map<String, String> res = new HashMap<>();
        for (int i = 0; i < parameters.size(); ++i) {
            res.put(parameters.get(i), values.get(i));
        }
        return res;
    }

    private boolean threshold(Map<String, String> processDetails) {
        return processDetails.get("USER").equals(this.username) && processDetails.get("STAT").charAt(0) == 'R' && Float.parseFloat(processDetails.get("%CPU")) > 0;
    }

    public LinkedList<Map<String, String>> getActive() {
        LinkedList<Map<String, String>> actives = new LinkedList<>();
        try {
            String line;
            Process p = Runtime.getRuntime().exec("ps -aux");
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            line = input.readLine();
            LinkedList<String> parameters = getWords(line);
            while ((line = input.readLine()) != null) {
                Map<String, String> processDetails = getProcessDetails(parameters, line);
                if (this.threshold(processDetails)) {
                    actives.add(processDetails);
                }
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return actives;
    }

    public void storeCashData() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String dateNow = dtf.format(now);
        for (Map.Entry<String, HashMap<String, String>> stringHashMapEntry : this.cash.entrySet()) {
            ProcessRecord pr = new ProcessRecord(stringHashMapEntry.getKey(), stringHashMapEntry.getValue().get("%CPU"), stringHashMapEntry.getValue().get("%MEM"),
                    stringHashMapEntry.getValue().get("TIME"), stringHashMapEntry.getValue().get("USER"),
                    stringHashMapEntry.getValue().get("STAT"), stringHashMapEntry.getValue().get("RSS"),
                    stringHashMapEntry.getValue().get("TTY"), stringHashMapEntry.getValue().get("PID"),
                    stringHashMapEntry.getValue().get("START"), stringHashMapEntry.getValue().get("VSZ"),
                    stringHashMapEntry.getValue().get("COMMAND"));
            try {
                ProcessDAO.saveProcess(pr, dateNow);
            } catch (UnknownHostException ignored) {
            }
        }
        this.cash.clear();
    }

    private void transferToCash(LinkedList<Map<String, String>> processes) {

        HashMap<String, String> processData;
        String[] commandArray;
        String[] currentProcesses = new String[processes.size()];
        String processName;

        float memInitial, memAdditional, cpuInitial, cpuAdditional;
        int num;
        for (int i = 0; i < processes.size(); i++) {
            commandArray = processes.get(i).get("COMMAND").split("/");
            processName = commandArray[commandArray.length - 1];
            currentProcesses[i] = processName;
            if (this.cash.get(processName) == null) {
                processData = (HashMap<String, String>) processes.get(i);
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

        LinkedList<String> toDelete = new LinkedList<>();
        for (Map.Entry<String, HashMap<String, String>> stringHashMapEntry : this.cash.entrySet()) {
            String name = stringHashMapEntry.getKey();
            if (!Arrays.asList(currentProcesses).contains(name)) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime now = LocalDateTime.now();
                String dateNow = dtf.format(now);
                ProcessRecord proc = new ProcessRecord(name, this.cash.get(name).get("%CPU"), this.cash.get(name).get("%MEM"),
                        this.cash.get(name).get("TIME"), this.cash.get(name).get("USER"),
                        this.cash.get(name).get("STAT"), this.cash.get(name).get("RSS"),
                        this.cash.get(name).get("TTY"), this.cash.get(name).get("PID"),
                        this.cash.get(name).get("START"), this.cash.get(name).get("VSZ"),
                        this.cash.get(name).get("COMMAND"));
                try {
                    ProcessDAO.saveProcess(proc, dateNow);
                } catch (UnknownHostException ignored) {
                }
                toDelete.add(name);
            }
        }
        for (String s : toDelete) {
            this.cash.remove(s);
        }
    }

    public void run() {
        while (this.keepRunning) {

            LinkedList<Map<String, String>> processes = this.getActive();
            this.transferToCash(processes);
        }
        this.storeCashData();
    }

    public boolean isKeepRunning() {
        return keepRunning;
    }

    public void setKeepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
    }
}