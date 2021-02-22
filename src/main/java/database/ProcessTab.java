package database;

public class ProcessTab {
    private String name;
    private String status;
    private String start;


    public ProcessTab(String name, String status, String start) {
        this.name = name;
        this.status = status;
        this.start = start;
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
}
