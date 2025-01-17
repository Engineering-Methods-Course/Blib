package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.ReportEntry;
import common.MonthlyReport;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
        // Initialize the date pickers to only allow month selection
        initializeDatePicker(startRangePicker);
        initializeDatePicker(endRangePicker);

        // Add the report options to the ChoiceBox.
        reportChoiceBox.getItems().addAll("Borrow Times", "Subscriber Status");

        // Add a listener to handle changes in the selected item.
        reportChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateCharts());

        // Add a listener to handle changes in the start date.
        startRangePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateCharts());

        // Add a listener to handle changes in the end date.
        endRangePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateCharts());

        // Set the default report to display.
        reportChoiceBox.getSelectionModel().selectFirst();
    }

    /**
     * Updates the charts based on the selected report type and date range.
     */
    private void updateCharts()
    {
        // helper variables for the message ID and date range
        int messageID = -1;
        Date startDate = Date.from(startRangePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endRangePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        // checks if the endDatePicker's date is later than the startDatePicker and if so, brings an error message
        if(endDate.before(startDate))
        {
            ClientGUIController.showAlert(Alert.AlertType.ERROR, "Wrong date", "End date must be later than start date");
            return;
        }

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

    /**
     * Generates the reports based on the given data.
     *
     * @param reportData The data to generate the reports from.
     */
    public void generateBorrowTimeReport(ArrayList<MonthlyReport> reportData)
    {

        /////////////////////////// TESTING ///////////////////////////
        // Create an ArrayList of MonthlyReport objects
        ArrayList<MonthlyReport> monthlyReports = new ArrayList<>();

        // Create 5 ReportEntry objects
        Calendar calendar = Calendar.getInstance();

        calendar.set(2023, Calendar.OCTOBER, 1);
        ReportEntry reportEntry1 = new ReportEntry(calendar.getTime(), "borrow", "Book borrowed 1");

        calendar.set(2023, Calendar.OCTOBER, 10);
        ReportEntry reportEntry2 = new ReportEntry(calendar.getTime(), "return", "Book returned 1");

        calendar.set(2023, Calendar.OCTOBER, 3);
        ReportEntry reportEntry3 = new ReportEntry(calendar.getTime(), "borrow", "Book borrowed 2");

        calendar.set(2023, Calendar.OCTOBER, 1);
        ReportEntry reportEntry4 = new ReportEntry(calendar.getTime(), "return", "Book returned 2");

        calendar.set(2023, Calendar.OCTOBER, 5);
        ReportEntry reportEntry5 = new ReportEntry(calendar.getTime(), "borrow", "Book borrowed 3");

        calendar.set(2023, Calendar.NOVEMBER, 5);
        ReportEntry reportEntry6 = new ReportEntry(calendar.getTime(), "return", "Book borrowed 3");

        // Create a list of ReportEntry objects
        ArrayList<ReportEntry> reportEntriesTest = new ArrayList<>();
        reportEntriesTest.add(reportEntry1);
        reportEntriesTest.add(reportEntry2);
        reportEntriesTest.add(reportEntry3);
        reportEntriesTest.add(reportEntry4);
        reportEntriesTest.add(reportEntry5);
        reportEntriesTest.add(reportEntry6);

        // Create a MonthlyReport and add the ReportEntry list to it
        MonthlyReport monthlyReportTest = new MonthlyReport(new Date(), reportEntriesTest);

        // Add the MonthlyReport to the ArrayList
        monthlyReports.add(monthlyReportTest);
        ////////////////////////////////////////////////////////////////

        // Create an ArrayList<ReportEntry> to store the data in
        ArrayList<ReportEntry> reportEntries = new ArrayList<>();
        for (MonthlyReport monthlyReport : monthlyReports)
        {
            //adds all the report entries to the list
            reportEntries.addAll(monthlyReport.getReport());
        }


        // Generate the bar chart
        generateBarChart("Actions Per Week", reportEntries);

        // Generate the pie chart
        generatePieChart("Borrows vs. Returns", reportEntries);

        // Generate the line chart
        generateLineChart("Returns and Borrows Over Time", reportEntries);
    }

    /**
     * Generates the reports based on the given data.
     *
     * @param reportData The data to generate the reports from.
     */
    public void generateSubscriberStatusReport(ArrayList<MonthlyReport> reportData)
    {
        /////////////////////////// TESTING ///////////////////////////
        // Create an ArrayList of MonthlyReport objects
        ArrayList<MonthlyReport> monthlyReports = new ArrayList<>();

        // Create 5 ReportEntry objects
        Calendar calendar = Calendar.getInstance();

        calendar.set(2023, Calendar.OCTOBER, 1);
        ReportEntry reportEntry1 = new ReportEntry(calendar.getTime(), "Frozen", "Book borrowed 1");

        calendar.set(2023, Calendar.OCTOBER, 10);
        ReportEntry reportEntry2 = new ReportEntry(calendar.getTime(), "Frozen", "Book returned 1");

        calendar.set(2023, Calendar.OCTOBER, 3);
        ReportEntry reportEntry3 = new ReportEntry(calendar.getTime(), "Registered", "Book borrowed 2");

        calendar.set(2023, Calendar.OCTOBER, 1);
        ReportEntry reportEntry4 = new ReportEntry(calendar.getTime(), "Unfrozen", "Book returned 2");

        calendar.set(2023, Calendar.OCTOBER, 5);
        ReportEntry reportEntry5 = new ReportEntry(calendar.getTime(), "Unfrozen", "Book borrowed 3");

        calendar.set(2023, Calendar.NOVEMBER, 5);
        ReportEntry reportEntry6 = new ReportEntry(calendar.getTime(), "Registered", "Book borrowed 3");

        // Create a list of ReportEntry objects
        ArrayList<ReportEntry> reportEntriesTest = new ArrayList<>();
        reportEntriesTest.add(reportEntry1);
        reportEntriesTest.add(reportEntry2);
        reportEntriesTest.add(reportEntry3);
        reportEntriesTest.add(reportEntry4);
        reportEntriesTest.add(reportEntry5);
        reportEntriesTest.add(reportEntry6);

        // Create a MonthlyReport and add the ReportEntry list to it
        MonthlyReport monthlyReportTest = new MonthlyReport(new Date(), reportEntriesTest);

        // Add the MonthlyReport to the ArrayList
        monthlyReports.add(monthlyReportTest);
        ////////////////////////////////////////////////////////////////

        // Create an ArrayList<ReportEntry> to store the data in
        ArrayList<ReportEntry> reportEntries = new ArrayList<>();
        for (MonthlyReport monthlyReport : monthlyReports)
        {
            //adds all the report entries to the list
            reportEntries.addAll(monthlyReport.getReport());
        }

        // Generate the bar chart
        generateBarChart("Status Changes per Week", reportEntries);

        // Generate the pie chart
        generatePieChart("Freezing vs. Registering vs. Unfreezing", reportEntries);

        // Generate the line chart
        generateLineChart("Status Changes Over Time", reportEntries);
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
     * @param reportEntries The log entries to generate the chart from.
     */
    private void generateBarChart(String chartName, ArrayList<ReportEntry> reportEntries) {
        Platform.runLater(() -> {
            // Create a new series for the bar chart
            XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
            barSeries.setName(chartName);

            // Create a map to count actions per day
            Map<String, Map<String, Integer>> actionCountMap = new HashMap<>();

            // Define the date formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Process the log entries and count the actions per week
            for (ReportEntry entry : reportEntries) {
                LocalDate date = entry.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate startOfWeek = date.with(DayOfWeek.SUNDAY);
                String weekStart = startOfWeek.format(formatter);
                String action = entry.getType();

                // Initialize the date map if not already present
                actionCountMap.putIfAbsent(weekStart, new HashMap<>());
                Map<String, Integer> dateMap = actionCountMap.get(weekStart);

                // Count the actions
                dateMap.put(action, dateMap.getOrDefault(action, 0) + 1);
            }

            // Populate the bar chart with the action counts
            for (Map.Entry<String, Map<String, Integer>> dateEntry : actionCountMap.entrySet()) {
                String date = dateEntry.getKey();
                for (Map.Entry<String, Integer> actionEntry : dateEntry.getValue().entrySet()) {
                    String action = actionEntry.getKey();
                    Integer count = actionEntry.getValue();
                    barSeries.getData().add(new XYChart.Data<>(date, count));
                }
            }

            // Configure the x-axis to handle date values
            CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
            xAxis.setLabel("Date");
            xAxis.setCategories(FXCollections.observableArrayList(actionCountMap.keySet()));

            // Configure the y-axis
            NumberAxis yAxis = (NumberAxis) barChart.getYAxis();
            yAxis.setLabel("Count of Actions");

            // Clear existing data and add the new series to the bar chart
            barChart.getData().clear();
            barChart.getData().add(barSeries);
            barChart.setTitle(chartName);
        });
    }

    /**
     * Generates a pie chart based on the given log entries.
     *
     * @param chartName     The name of the chart.
     * @param reportEntries The log entries to generate the chart from.
     */
    private void generatePieChart(String chartName, ArrayList<ReportEntry> reportEntries)
    {
        Platform.runLater(() -> {
            // Create a map to count different types of actions
            Map<String, Integer> actionCountMap = new HashMap<>();

            // Process the log entries
            for (ReportEntry entry : reportEntries)
            {
                String action = entry.getType();

                // Count the actions
                actionCountMap.put(action, actionCountMap.getOrDefault(action, 0) + 1);
            }

            // Populate the pie chart with the action counts
            for (Map.Entry<String, Integer> entry : actionCountMap.entrySet())
            {
                pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

            // Set the name for the pie chart
            pieChart.setTitle(chartName);
        });
    }

    /**
     * Generates a line chart based on the given log entries.
     *
     * @param chartName  The name of the chart.
     * @param reportEntries The log entries to generate the chart from.
     */
    private void generateLineChart(String chartName, ArrayList<ReportEntry> reportEntries)
    {
        Platform.runLater(() -> {
            // Create a new series for the line chart
            Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

            // Define the date formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Collect all the dates
            Set<String> allDates = new TreeSet<>();

            // Process the log entries
            for (ReportEntry entry : reportEntries)
            {
                LocalDate date = entry.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate startOfWeek = date.with(DayOfWeek.SUNDAY);
                String weekStart = startOfWeek.format(formatter);
                String action = entry.getType();

                // Add the date to the set of all dates
                allDates.add(weekStart);

                // Initialize series if not already present
                seriesMap.putIfAbsent(action, new XYChart.Series<>());
                seriesMap.get(action).setName(action);

                // Count the actions
                XYChart.Series<String, Number> series = seriesMap.get(action);
                boolean found = false;
                for (XYChart.Data<String, Number> data : series.getData())
                {
                    if (data.getXValue().equals(weekStart))
                    {
                        data.setYValue(data.getYValue().intValue() + 1);
                        found = true;
                        break;
                    }
                }
                if (!found)
                {
                    series.getData().add(new XYChart.Data<>(weekStart, 1));
                }
            }

            // Configure the x-axis to handle date values
            CategoryAxis xAxis = (CategoryAxis) lineChart.getXAxis();
            xAxis.setLabel("Date");
            xAxis.setCategories(FXCollections.observableArrayList(allDates));

            // Clear existing data and add the new series to the line chart
            lineChart.getData().clear();
            lineChart.getData().addAll(seriesMap.values());
            lineChart.setTitle(chartName);
        });
    }

    /**
     * Initializes the date picker to only allow month selection.
     *
     * @param datePicker The date picker to initialize.
     */
    private void initializeDatePicker(DatePicker datePicker) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Disable all days except the first day of each month and all the dates after the previous month
                setDisable(empty || date.getDayOfMonth() != 1 || date.isAfter(LocalDate.now().withDayOfMonth(1).minusDays(1)));
            }
        });
        // Set the default value to the first day of the current month
        datePicker.setValue(LocalDate.now().withDayOfMonth(1).minusMonths(1));
    }
}