package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthlyReport implements Serializable {

    private final Date date;
    private final ArrayList<ReportEntry> report;

    public MonthlyReport(Date date, ArrayList<ReportEntry> report) {
        this.date = date;
        this.report = report;
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
    public ArrayList<ReportEntry> getReport() {
        return report;
    }
}