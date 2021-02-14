import monitors.process_monitor.ProcessMonitor;

public class Main {
    public static void main(String[] args) {
        ProcessMonitor p = ProcessMonitor.getInstance("iskander");
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
        );
    }
}
