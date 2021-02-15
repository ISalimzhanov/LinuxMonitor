import monitors.ProcessMonitor;
import monitors.test.ProcessMonitorTest;

public class Main {
    public static void main(String[] args) {
        ProcessMonitorTest test = new ProcessMonitorTest();
        test.run();
        /*ProcessMonitor p = ProcessMonitor.getInstance("iskander");
        p.start();
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(
                new Thread() {
                    public void run() {
                        p.setKeepRunning(false);
                        try {
                            p.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );*/
    }
}
