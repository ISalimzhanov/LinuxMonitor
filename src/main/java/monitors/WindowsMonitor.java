package monitors;

import X.X11Api;
import database.ProcessDAO;
import database.ProcessTabDao;
import database.ProcessTab;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class WindowsMonitor extends Thread {
    private static WindowsMonitor instance = null;
    private final HashMap<String, HashMap<String, String>> cash;
    private volatile boolean keepRunning = true;

    private WindowsMonitor() {
        this.cash = new HashMap<>();
    }

    public static WindowsMonitor getInstance() {
        if (instance == null) {
            instance = new WindowsMonitor();
        }
        return instance;
    }

    public void transferToCash(X11Api.Window[] allWindows, X11Api.Window activeWindow) throws X11Api.X11Exception {
        System.out.println("start transfer");
        String activeWindowTitle = activeWindow.getTitle();
        LinkedList<String> windowTitles = new LinkedList<>();
        windowTitles.add(activeWindowTitle);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String dateNow = dtf.format(now);
        if (!this.cash.containsKey(activeWindowTitle)) {
            HashMap<String, String> windowDetails = new HashMap<>();
            windowDetails.put("status", "FOCUSED");
            windowDetails.put("start", String.valueOf(LocalTime.now()));
            windowDetails.put("PID", activeWindow.getPID().toString());
            this.cash.put(activeWindowTitle, windowDetails);
        }
        for (X11Api.Window window : allWindows) {
            String title = window.getTitle();
            if (title.equals(activeWindowTitle))
                continue;
            windowTitles.add(title);
            if (!this.cash.containsKey(title)) {
                HashMap<String, String> windowData = new HashMap<>();
                windowData.put("status", "NOT FOCUSED");
                windowData.put("start", String.valueOf(LocalTime.now()));
                windowData.put("PID", window.getPID().toString());
                this.cash.put(title, windowData);
            } else if (this.cash.get(title).get("status").equals("FOCUSED")) {
                System.out.println(title);
                ProcessTab pr = new ProcessTab(title, this.cash.get(title).get("status"), this.cash.get(title).get("start"), this.cash.get(title).get("PID"));
                try{
                    ProcessTabDao.saveWindow(pr, dateNow);
                }catch(UnknownHostException e){}
                this.cash.remove(title);
            }
        }

        LinkedList<String> toDelete = new LinkedList<>();
        for (Map.Entry<String, HashMap<String, String>> windowDetails : this.cash.entrySet()) {
            String title = windowDetails.getKey();
            if (!windowTitles.contains(title)) {
                System.out.println(title);//GET PID
                ProcessTab pr = new ProcessTab(title, windowDetails.getValue().get("status"), windowDetails.getValue().get("start"), windowDetails.getValue().get("PID"));
                try{
                    ProcessTabDao.saveWindow(pr, dateNow);
                    System.out.println("db saving");
                }catch(UnknownHostException e){}
                toDelete.add(title);
            }
        }
        for (String s : toDelete) {
            this.cash.remove(s);
        }
    }

    public void storeCashData() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String dateNow = dtf.format(now);
        for (Map.Entry<String, HashMap<String, String>> stringHashMapEntry : this.cash.entrySet()) {
            ProcessTab pr = new ProcessTab(stringHashMapEntry.getKey(), stringHashMapEntry.getValue().get("status"), stringHashMapEntry.getValue().get("start"), stringHashMapEntry.getValue().get("PID"));
            try{
                ProcessTabDao.saveWindow(pr, dateNow);
            }catch(UnknownHostException e){};
        }
        this.cash.clear();
    }


    public void run() {
        X11Api.Display display = new X11Api.Display();
        while (this.keepRunning) {
            try {
                X11Api.Window[] allWindows = display.getWindows();
                X11Api.Window activeWindow = display.getActiveWindow();
                this.transferToCash(allWindows, activeWindow);
            } catch (X11Api.X11Exception e) {
                e.printStackTrace();
            }
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