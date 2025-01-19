package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import common.*;
import logic.DBController;

public class MainDontPush {

/*
    public static void main(String[] args) {
        Connection conn = null;
        try {
            DBController db = DBController.getInstance();

            Calendar calendar = Calendar.getInstance();
            calendar.set(2025, Calendar.JANUARY, 1);
            Date date = calendar.getTime();
            // Add monthly report
          /*  MonthlyReport monthlyReport = new MonthlyReport(date);
            Blob monthlyReportBlob = db.convertMonthlyReportToBlob(monthlyReport);

            String newReportQuery = "INSERT INTO monthly_report (details, report_type) VALUES (?, ?)";
            PreparedStatement newReportStatement = db.conn.prepareStatement(newReportQuery);
            newReportStatement.setBlob(1, monthlyReportBlob);
            newReportStatement.setString(2, "subscriberStatuses");
            newReportStatement.executeUpdate();


            MonthlyReport monthlyReport2 = new MonthlyReport(date);
            Blob monthlyReportBlob2 = db.convertMonthlyReportToBlob(monthlyReport2);

            String newReportQuery2 = "INSERT INTO monthly_report (details, report_type) VALUES (?, ?)";
            PreparedStatement newReportStatement2 = db.conn.prepareStatement(newReportQuery2);
            newReportStatement2.setBlob(1, monthlyReportBlob2);
            newReportStatement2.setString(2, "borrowTime");
            newReportStatement2.executeUpdate();*/


            /*String query = "SELECT * FROM monthly_report WHERE ready_for_export = 0";
           PreparedStatement statement = db.conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Blob blob = rs.getBlob("details");
                MonthlyReport monthlyReport = (MonthlyReport) db.convertBlobToMonthlyReport(blob);
                System.out.println("MonthlyReport: " + monthlyReport.getDate());
                ArrayList<ReportEntry> report = monthlyReport.getReport();
                for (ReportEntry entry : report) {
                    System.out.println("ReportEntry: " + entry.getDate() + " " + entry.getType() + " " + entry.getDescription());
                }
            }




            System.out.println("MonthlyReport inserted successfully!");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }*/
}
