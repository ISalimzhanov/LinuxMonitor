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
                LinkedList<String> words = getWords(line);
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

            testDuration--;
            Map<String, String> details = this.getStressDetails();
            if (details != null) {
                try {
                    System.out.println("killed");
                    Runtime.getRuntime().exec(String.format("sudo kill %s",
                            details.get("PID")));
                    TimeUnit.SECONDS.sleep(5);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                this.cash.add((HashMap<String, String>) details);
            }
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Unpacking unpacking = new Unpacking("processes.json");
        LinkedList<HashMap<String, String>> data = unpacking.getProcessData("stress");
        assert (data.equals(this.cash));
        System.out.println("OK");
    }
}
