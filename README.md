# LinuxMonitor


### GUI component 
This component is responsible for the application interface.
We used JavaFx for creation of the GUI part of Linux Monitor project. We can see time for which application is running and choose the date to see processes information as table for this date.

There are three classes and one fxml for the layout:

* TrackerTimer: <br /> 
*This class holds time and state of timer*  
* Controller: <br />
*This class is for interaction logic with user. It is responsible for click listeners and table creation with processes information.* 
* Main: <br />
*Main class for running program. It loads the main layout.*
 

### Database component
This component is responsible for storing information about processes. We used MongoDB for it. Database stores information of processes resource utilization for particular date.
 
* DatabaseManager: <br /> 
*Singleton manager for the database* 
* ProcessDao: <br />
*This class contains queries for process information addition and getting of all processes information for particular date.*   
* ProcessProperties: <br />
*Enum class with process properties*
* ProcessRecord: <br />
*Data class which is stores such variables as RAM and other resource utilization*
* ProcessTab: <br /> 
*Data class which is stores data variables for the tab*
* ProcessTabDao: <br />
*This class contains queries for tab information addition and getting of all tab information for particular date*
* ProcessTabProperties: <br />
*Enum class with tab properties*

### Monitor component 
This component is responsible for the monitoring processes and windows and consist of two classes:
* ProcessMonitor: 
*This class keep track of processes and store process's data to DB when process or programm is finished*  
* WindowMonitor:
*This class keep track of windows and store window's data to DB when window is close/focus changed or programm is finished*

#### ProcessMonitor:
We track following process's data:
* %MEM - amount of memory usage in percents 
* %CPU - amount of cpu usage in percents
* STAT - state of the process:
    * D Uninterruptible sleep (usually IO)
    * R Running or runnable (on run queue)
    * S Interruptible sleep (waiting for an event to complete)
    * T Stopped, either by a job control signal or because it is being traced.  
    * W paging (not valid since the 2.6.xx kernel)
    * X dead (should never be seen)
    * Z Defunct ("zombie") process, terminated but not reaped by its parent. 
* PID - ID of the process
* START - when process was started
* TIME - cumulative CPU time
* VSZ - virtual memory usage of entire process (in KiB)
* COMMAND - by which command was started 

Note: ProcessMonitor will track only thoose processes that were initiated by the user that initiated ProcessMonitor  

For monitoring processes, command 'ps -aux' is queried periodically and result of command is parsed. When command 'ps -aux' is quired, 
*  All new processes's data stored in the the cash
*  If process's data already in the cash it will be updated
*  If there is a data of process in the cash, but it is not in 'ps -aux' results then process will be considered as turned off and data in cash will be stored in 

#### WindowMonitor:
We track following window's data:
* Title - title of the window
* Focused/Not Focused - status of the window
* PID - ID of process of window
* START - when window was created or focused of window changed

For window monitoring we use X11 and JNA. In details, we used and tested code from contribution: https://github.com/java-native-access/jna/blob/master/contrib/x11/src/jnacontrib/x11/api/X.java (class Display and class Windows is tested manually). 

If focus changed/new window was created/some window was closed then:
* We check cash data and if focus changed or window was closed, window's will be stored in DB
* If window is not in cash then we store its data into cash
