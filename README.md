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
