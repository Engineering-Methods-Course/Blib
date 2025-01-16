package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.ReportEntry;
import common.MonthlyReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static client.ClientGUIController.navigateTo;

public class ViewReportsFrameController
{
    @FXML
    public DatePicker startRangePicker;
    @FXML
    public DatePicker endRangePicker;
    @FXML
    public ChoiceBox<String> reportChoiceBox;
    @FXML
    public Button backButton;
    @FXML
    public BarChart<String, Number> barChart;
    @FXML
    public PieChart pieChart;
    @FXML
    public LineChart<String, Number> lineChart;

    /**
     * Initializes the ViewReportsFrameController.
     */
    public void initialize()
    {
        // Add the report options to the ChoiceBox.
        reportChoiceBox.getItems().addAll("Borrow Times", "Subscriber Status");

        // Set the default report to display.
        reportChoiceBox.getSelectionModel().selectFirst();

        // Add a listener to handle changes in the selected item.
        reportChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateCharts());

        // Add a listener to handle changes in the start date.
        startRangePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateCharts());

        // Add a listener to handle changes in the end date.
        endRangePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateCharts());
    }

    private void updateCharts()
    {
        // helper variables for the message ID and date range
        int messageID = -1;
        Date startDate = Date.from(startRangePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endRangePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        // clear the charts
        barChart.getData().clear();
        pieChart.getData().clear();
        lineChart.getData().clear();

        // update the current report type
        if (reportChoiceBox.getValue().equals("Borrow Times"))
        {
            messageID = 312;
        }
        else if (reportChoiceBox.getValue().equals("Subscriber Status"))
        {
            messageID = 316;
        }

        // request the relevant data from the server
        ClientServerMessage message = new ClientServerMessage(messageID, Arrays.asList(startDate, endDate));
        ClientGUIController.chat.sendToServer(message);
    }

    public void generateBorrowTimeReport(ArrayList<MonthlyReport> reportData)
    {
        // helper Date object for easier reading
        Date date;

        // Create an ArrayList<ReportEntry> to store the data in
        ArrayList<ReportEntry> logEntries = new ArrayList<>();
        for (MonthlyReport monthlyReport : reportData)
        {
            for (ArrayList<String> entry : monthlyReport.getLog())
            {
                // Create a new ReportEntry object and add it to the list
                date = Date.from(LocalDateTime.parse(entry.get(0), DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(ZoneId.systemDefault()).toInstant());
                logEntries.add(new ReportEntry(date, entry.get(1), entry.get(2)));
            }
        }

        // Generate the bar chart
        generateBarChart("Actions Per Day", logEntries);

        // Generate the pie chart
        generatePieChart("Action Distribution", logEntries);

        // Generate the line chart
        generateLineChart("Actions Over Time", logEntries);
    }

    public void generateSubscriberStatusReport(ArrayList<MonthlyReport> reportData)
    {
        // helper Date object for easier reading
        Date date;

        // Create an ArrayList<ReportEntry> to store the data in
        ArrayList<ReportEntry> logEntries = new ArrayList<>();
        for (MonthlyReport monthlyReport : reportData)
        {
            for (ArrayList<String> entry : monthlyReport.getLog())
            {
                // Create a new ReportEntry object and add it to the list
                date = Date.from(LocalDateTime.parse(entry.get(0), DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(ZoneId.systemDefault()).toInstant());
                logEntries.add(new ReportEntry(date, entry.get(1), entry.get(2)));
            }
        }

        // Generate the bar chart
        generateBarChart("Accounts Frozen", logEntries);

        // Generate the pie chart
        generatePieChart("Action Distribution", logEntries);

        // Generate the line chart
        generateLineChart("Actions Over Time", logEntries);
    }

    /**
     * Handles the Back button click event.
     *
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "/gui/LibrarianProfileFrame.fxml", "/gui/Subscriber.css", "Librian Profile");
    }

    /**
     * Generates a bar chart based on the given log entries.
     *
     * @param chartName  The name of the chart.
     * @param logEntries The log entries to generate the chart from.
     */
    private void generateBarChart(String chartName, ArrayList<ReportEntry> logEntries) {
        // Create a new series for the bar chart
        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
        barSeries.setName(chartName);

        // Create a map to count actions per day
        Map<String, Map<String, Integer>> actionCountMap = new HashMap<>();

        // Define the date formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        // Process the log entries
        for (ReportEntry entry : logEntries) {
            String date = entry.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);
            String action = entry.getType();

            // Initialize the date map if not already present
            actionCountMap.putIfAbsent(date, new HashMap<>());
            Map<String, Integer> dateMap = actionCountMap.get(date);

            // Count the actions
            dateMap.put(action, dateMap.getOrDefault(action, 0) + 1);
        }

        // Populate the bar chart with the action counts
        for (Map.Entry<String, Map<String, Integer>> dateEntry : actionCountMap.entrySet()) {
            String date = dateEntry.getKey();
            for (Map.Entry<String, Integer> actionEntry : dateEntry.getValue().entrySet()) {
                String action = actionEntry.getKey();
                Integer count = actionEntry.getValue();
                barSeries.getData().add(new XYChart.Data<>(date + " (" + action + ")", count));
            }
        }

        // Add the series to the bar chart
        barChart.getData().add(barSeries);
    }

    /**
     * Generates a pie chart based on the given log entries.
     *
     * @param chartName  The name of the chart.
     * @param logEntries The log entries to generate the chart from.
     */
    private void generatePieChart(String chartName, ArrayList<ReportEntry> logEntries) {
        // Create a map to count different types of actions
        Map<String, Integer> actionCountMap = new HashMap<>();

        // Process the log entries
        for (ReportEntry entry : logEntries) {
            String action = entry.getType();

            // Count the actions
            actionCountMap.put(action, actionCountMap.getOrDefault(action, 0) + 1);
        }

        // Populate the pie chart with the action counts
        for (Map.Entry<String, Integer> entry : actionCountMap.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // Set the name for the pie chart
        pieChart.setTitle(chartName);
    }

    /**
     * Generates a line chart based on the given log entries.
     *
     * @param chartName  The name of the chart.
     * @param logEntries The log entries to generate the chart from.
     */
    private void generateLineChart(String chartName, ArrayList<ReportEntry> logEntries) {
        // Create a new series for the line chart
        Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

        // Define the date formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        // Process the log entries
        for (ReportEntry entry : logEntries) {
            String date = entry.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);
            String action = entry.getType();

            // Initialize series if not already present
            seriesMap.putIfAbsent(action, new XYChart.Series<>());
            seriesMap.get(action).setName(action);

            // Count the actions
            XYChart.Series<String, Number> series = seriesMap.get(action);
            boolean found = false;
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getXValue().equals(date)) {
                    data.setYValue(data.getYValue().intValue() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                series.getData().add(new XYChart.Data<>(date, 1));
            }
        }

        // Add the series to the line chart
        lineChart.getData().addAll(seriesMap.values());

        // Set the name for the line chart
        lineChart.setTitle(chartName);
    }
}