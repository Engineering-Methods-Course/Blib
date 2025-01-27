package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MonthlyReport implements Serializable {

    private final Date date;
    private ArrayList<ReportEntry> report;

    /**
     * Constructor for the MonthlyReport class.
     *
     * @param date   The date of the report.
     * @param report The report.
     */
    public MonthlyReport(Date date, ArrayList<ReportEntry> report) {
        this.date = date;
        this.report = report;
    }
    /**
     * Constructor for the MonthlyReport class.
     *
     * @param date The date of the report.
     */
    public MonthlyReport(Date date) {
        this.date = date;
        this.report = new ArrayList<>();
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

    /**
     * This method adds a new entry to the report
     *
     * @param entry the entry to be added
     */
    public void addNewEntry(ReportEntry entry) {
        report.add(entry);
    }
}