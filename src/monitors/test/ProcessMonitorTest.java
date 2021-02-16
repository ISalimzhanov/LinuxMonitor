package monitors.test;

import monitors.ProcessMonitor;
import packing.Unpacking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static monitors.ProcessMonitor.getProcessDetails;
import static monitors.ProcessMonitor.getWords;

public class ProcessMonitorTest {
    LinkedList<HashMap<String, String>> cash = new LinkedList<>();

    public ProcessMonitorTest() {
    }

    public Map<String, String> getStressDetails() {
        try {
            String line;
            Process p = Runtime.getRuntime().exec("ps -aux");
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            line = input.readLine();
            LinkedList<String> parameters = getWords(line);
            while ((line = input.readLine()) != null) {
                Map<String, String> details = getProcessDetails(parameters, line);
                if (!details.get("USER").equals("iskander") || !details.get("STAT").equals("R")) {
                    continue;
                }
                if (details.get("COMMAND").equals("stress")) {
                    return details;
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean monitoringIsCorrect() {
        Unpacking unpacking = new Unpacking("processes.json");
        LinkedList<HashMap<String, String>> data = unpacking.getProcessData("stress");
        if (data.size() != this.cash.size())
            return false;
        for (int i = 0; i < data.size(); ++i) {
            HashMap<String, String> processDetails = data.get(i);
            HashMap<String, String> stressDetails = this.cash.get(i);
            if (!processDetails.get("COMMAND").equals(stressDetails.get("COMMAND")))
                return false;
            if (!processDetails.get("START").equals(stressDetails.get("START")))
                return false;
            if (Float.parseFloat(processDetails.get("%CPU")) > 0.8)
                return false;
        }
        return true;
    }

    public void run() {
        ProcessMonitor pm = ProcessMonitor.getInstance("iskander");
        pm.start();
        Random random = new Random();
        int testDuration = 2;
        while (testDuration != 0) {
            int interval = Math.abs(random.nextInt() % 10) + 5;
            try {
                System.out.println(String.format("started for %s", interval));
                Runtime.getRuntime().exec("stress --cpu 1");
                TimeUnit.SECONDS.sleep(interval);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            Map<String, String> details = this.getStressDetails();
            if (details != null) {
                try {
                    Runtime.getRuntime().exec(String.format("kill %s",
                            details.get("PID")));
                    System.out.println("killed");
                    TimeUnit.SECONDS.sleep(5);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                this.cash.add((HashMap<String, String>) details);
            }
            testDuration--;
        }
        try {
            pm.setKeepRunning(false);
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert (this.monitoringIsCorrect());
        System.out.println("OK");
    }
}
