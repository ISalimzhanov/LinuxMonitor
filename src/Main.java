import monitors.ProcessMonitor;
import monitors.WindowsMonitor;

public class Main {
    public static void main(String[] args) {
        ProcessMonitor processMonitor = ProcessMonitor.getInstance("iskander");
        WindowsMonitor windowsMonitor = WindowsMonitor.getInstance();
        //processMonitor.start();
        windowsMonitor.start();
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(
                new Thread() {
                    public void run() {
                        //processMonitor.setKeepRunning(false);
                        windowsMonitor.setKeepRunning(false);
                        try {
                            //processMonitor.join();
                            windowsMonitor.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
