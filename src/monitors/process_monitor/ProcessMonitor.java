package monitors.process_monitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.time.LocalTime;

public class ProcessMonitor {
    private final String username;

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

    public void run() {
        while (true) {
            long clock = LocalTime.now().toNanoOfDay();
            LinkedList<Map<String, String>> processes = this.getActive();
            //toDo
            System.out.println((LocalTime.now().toNanoOfDay() - clock) / Math.pow(10, 9));
        }
    }
}
