package monitor_ui.timer;

import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class TrackerTimer {

    public int mins = 0, secs = 0, millis = 0;
    public boolean timerOn = true;

    public void change(Label text) {
        if(millis == 1000) {
            secs++;
            millis = 0;
        }
        if(secs == 60) {
            mins++;
            secs = 0;
        }
        text.setText((((mins/10) == 0) ? "0" : "") + mins + ":"
                + (((secs/10) == 0) ? "0" : "") + secs + ":"
                + (((millis/10) == 0) ? "00" : (((millis/100) == 0) ? "0" : "")) + millis++);
    }


}
