package database;

public class ProcessTab {
    private String name;
    private String status;
    private String start;
    private String PID;


    public ProcessTab(String name, String status, String start, String PID) {
        this.name = name;
        this.status = status;
        this.start = start;
        this.PID = PID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart() {
        return this.start;
    }
    public void setStart(String start) {
        this.start = start;
    }

    public String getPID() {
        return this.PID;
    }
    public void setPID(String PID) {
        this.PID = PID;
    }
}
