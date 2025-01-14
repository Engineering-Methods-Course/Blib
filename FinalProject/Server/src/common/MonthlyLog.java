package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthlyLog implements Serializable {

    private final Date date;
    private final List<ArrayList<String>> log;

    public MonthlyLog(Date date, List<ArrayList<String>> log) {
        this.date = date;
        this.log = log;
    }

    /**
     * This method returns the date
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * This method returns the log
     *
     * @return the log
     */
    public List<ArrayList<String>> getLog() {
        return log;
    }


}
