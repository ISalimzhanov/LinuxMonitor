package database;

// data class that stores process properties
public class ProcessRecord {

    private String name;
    private String cpuUsage;
    private String memoryUtil;
    private String workTime;
    private String user;
    private String procStat;
    private String procRSS;
    private String procTTY;
    private String pid;
    private String startTime;
    private String procVSZ;
    private String command;


    public ProcessRecord(String name, String cpuUsage,
                         String memoryUtil, String workTime,
                         String user, String procStat,
                         String procRSS, String procTTY,
                         String pid, String startTime,
                         String procVSZ, String command) {
        this.name = name;
        this.cpuUsage = cpuUsage;
        this.memoryUtil = memoryUtil;
        this.workTime = workTime;
        this.user = user;
        this.procStat = procStat;
        this.procRSS = procRSS;
        this.procTTY = procTTY;
        this.pid = pid;
        this.startTime = startTime;
        this.procVSZ = procVSZ;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public String getCpuUsage() {
        return cpuUsage;
    }

    public String getMemoryUtil() {
        return memoryUtil;
    }

    public void setMemoryUtil(String memoryUtil) {
        this.memoryUtil = memoryUtil;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProcStat() {
        return procStat;
    }

    public void setProcStat(String procStat) {
        this.procStat = procStat;
    }

    public String getProcRSS() {
        return procRSS;
    }

    public void setProcRSS(String procRSS) {
        this.procRSS = procRSS;
    }

    public String getProcTTY() {
        return procTTY;
    }

    public void setProcTTY(String procTTY) {
        this.procTTY = procTTY;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getProcVSZ() {
        return procVSZ;
    }

    public void setProcVSZ(String procVSZ) {
        this.procVSZ = procVSZ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
