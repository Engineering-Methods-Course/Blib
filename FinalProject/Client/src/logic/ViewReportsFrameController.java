package logic;

import client.ClientGUIController;
import common.ClientServerMessage;
import common.MonthlyReport;
import common.ReportEntry;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ViewReportsFrameController
{
    // The FXML elements
    @FXML
    public ChoiceBox<String> reportChoiceBox;
    @FXML
    public Button backButton;
    @FXML
    public BarChart<String, Number> BorrowChart;
    @FXML
    public LineChart<String, Number> subscriberStatusesChart;
    @FXML
    public ChoiceBox<Integer> startYear;
    @FXML
    public ChoiceBox<String> startMonth;
    @FXML
    public ChoiceBox<Integer> endYear;
    @FXML
    public ChoiceBox<String> endMonth;
    @FXML
    public RadioButton borrowReportRadio;
    @FXML
    public RadioButton subscriberStatusesReportRadio;
    @FXML
    public Button generateReport;

    /**
     * Initializes the ViewReportsFrameController.
     */
    public void initialize()
    {
        // Initialize the ToggleGroup and add the radio buttons to it
        ToggleGroup reportToggleGroup = new ToggleGroup();
        borrowReportRadio.setToggleGroup(reportToggleGroup);
        subscriberStatusesReportRadio.setToggleGroup(reportToggleGroup);

        // Set the visibility of both charts to false at the start
        BorrowChart.setVisible(false);
        subscriberStatusesChart.setVisible(false);

        // Set the default start year to the current year
        int currentYear = Year.now().getValue();
        ObservableList<Integer> years = FXCollections.observableArrayList(
                IntStream.rangeClosed(currentYear - 5, currentYear).boxed().collect(Collectors.toList())
        );

        // Set the items of the startYear and endYear ChoiceBox elements
        startYear.setItems(years);
        endYear.setItems(years);
        startYear.setValue(currentYear);
        endYear.setValue(currentYear);

        // Create a list of month names
        ObservableList<String> months = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );

        // Set the items of the startMonth and endMonth ChoiceBox elements
        startMonth.setItems(months);
        endMonth.setItems(months);

        // Set the default month to the current month
        startMonth.setValue("January");
        endMonth.setValue("January");
    }

    /**
     * changes the chart's visibility based on the selected radio button
     */
    private void switchChartVisibility()
    {
        // Show the appropriate chart based on the selected radio button
        if (borrowReportRadio.isSelected())
        {
            BorrowChart.setVisible(true);
            subscriberStatusesChart.setVisible(false);
        }
        // Show the appropriate chart based on the selected radio button
        else if (subscriberStatusesReportRadio.isSelected())
        {
            BorrowChart.setVisible(false);
            subscriberStatusesChart.setVisible(true);
        }
    }

    /**
     * updates the report charts on the screen
     */
    public void updateCharts()
    {
        // Helper variables for the message ID and date range
        int messageID = -1;

        // Retrieve the selected start and end month and year
        int startYearValue = startYear.getValue();
        int endYearValue = endYear.getValue();
        Month startMonthValue = Month.valueOf(startMonth.getValue().toUpperCase());
        Month endMonthValue = Month.valueOf(endMonth.getValue().toUpperCase());

        // Create LocalDate objects for the start and end dates
        LocalDate startDate = LocalDate.of(startYearValue, startMonthValue, 1);
        LocalDate endDate = LocalDate.of(endYearValue, endMonthValue, 1);

        // Convert LocalDate to Date
        Date startDateConverted = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateConverted = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Check if the endDate is before the startDate and if so, bring an error message
        if (endDateConverted.before(startDateConverted))
        {
            ClientGUIController.showAlert(Alert.AlertType.ERROR, "Wrong date", "End date must be later than start date");
            return;
        }

        // Clear the charts
        BorrowChart.getData().clear();
        subscriberStatusesChart.getData().clear();

        // Update the current report type based on the selected radio button
        if (borrowReportRadio.isSelected())
        {
            messageID = 312;
        }
        else if (subscriberStatusesReportRadio.isSelected())
        {
            messageID = 314;
        }

        // Request the relevant data from the server
        ClientServerMessage message = new ClientServerMessage(messageID, Arrays.asList(startDateConverted, endDateConverted));
        ClientGUIController.chat.sendToServer(message);

        // Show the appropriate chart based on the selected radio button
        switchChartVisibility();
    }

    /**
     * Generates the reports based on the given data.
     *
     * @param reportData The data to generate the reports from.
     */
    public void generateBorrowTimeReport(ArrayList<MonthlyReport> reportData)
    {
        // Create an ArrayList<ReportEntry> to store the data in
        ArrayList<ReportEntry> reportEntries = new ArrayList<>();
        for (MonthlyReport monthlyReport : reportData)
        {
            //adds all the report entries to the list
            reportEntries.addAll(monthlyReport.getReport());
        }

        // Generate the bar chart
        generateBarChart(reportEntries);
    }

    /**
     * Generates the reports based on the given data.
     *
     * @param reportData The data to generate the reports from.
     */
    public void generateSubscriberStatusReport(ArrayList<MonthlyReport> reportData)
    {
        // Create an ArrayList<ReportEntry> to store the data in
        ArrayList<ReportEntry> reportEntries = new ArrayList<>();
        for (MonthlyReport monthlyReport : reportData)
        {
            //adds all the report entries to the list
            reportEntries.addAll(monthlyReport.getReport());
        }

        // Generate the line chart
        generateLineChart(reportEntries);
    }

    /**
     * Generates a bar chart based on the given log entries.
     *
     * @param reportEntries The log entries to generate the chart from.
     */
    private void generateBarChart(ArrayList<ReportEntry> reportEntries)
    {
        Platform.runLater(() -> {
            // Create new series for each borrow status
            XYChart.Series<String, Number> borrowsSeries = new XYChart.Series<>();
            borrowsSeries.setName("Borrows");
            XYChart.Series<String, Number> returnsSeries = new XYChart.Series<>();
            returnsSeries.setName("Returns");
            XYChart.Series<String, Number> lateReturnsSeries = new XYChart.Series<>();
            lateReturnsSeries.setName("Late Returns");

            // Initialize the maximum value
            int maxValue = 0;

            // Define the week formatter
            DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Map to store counts of each action per week
            Map<String, Map<String, Integer>> weeklyCounts = new HashMap<>();

            // Process the log entries and add the data points directly
            for (ReportEntry entry : reportEntries)
            {
                // Convert the date to a LocalDate
                LocalDate date = entry.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String week = date.format(weekFormatter);

                // Get the action type
                String action = entry.getType();

                // Initialize the week map if not present
                weeklyCounts.putIfAbsent(week, new HashMap<>());
                Map<String, Integer> actionCounts = weeklyCounts.get(week);

                // Increment the count for the action
                actionCounts.put(action, actionCounts.getOrDefault(action, 0) + 1);

                // Update the maximum value
                maxValue = Math.max(maxValue, actionCounts.get(action));
            }

            // Sort the weeks
            List<String> sortedWeeks = new ArrayList<>(weeklyCounts.keySet());
            sortedWeeks.sort(Comparator.comparing(week -> LocalDate.parse(week, weekFormatter)));

            // Add the data points to the series
            for (String week : sortedWeeks)
            {
                // Get the action counts for the week
                Map<String, Integer> actionCounts = weeklyCounts.get(week);

                // Add the data points to the series
                borrowsSeries.getData().add(new XYChart.Data<>(week, actionCounts.getOrDefault("borrow", 0)));
                returnsSeries.getData().add(new XYChart.Data<>(week, actionCounts.getOrDefault("return", 0)));
                lateReturnsSeries.getData().add(new XYChart.Data<>(week, actionCounts.getOrDefault("late return", 0)));
            }

            // Configure the x-axis to handle week values
            CategoryAxis xAxis = (CategoryAxis) BorrowChart.getXAxis();
            xAxis.setLabel("Week");
            xAxis.setCategories(FXCollections.observableArrayList(sortedWeeks));

            // Configure the y-axis
            NumberAxis yAxis = (NumberAxis) BorrowChart.getYAxis();
            yAxis.setLabel("Count of Actions");
            yAxis.setAutoRanging(false);
            yAxis.setTickUnit(1);
            yAxis.setUpperBound(maxValue + 1);

            // Adds the new series to the bar chart
            BorrowChart.getData().addAll(borrowsSeries, returnsSeries, lateReturnsSeries);
            BorrowChart.setTitle("Borrow Statuses Per Week");
        });
    }

    /**
     * Generates a line chart based on the given log entries.
     *
     * @param reportEntries The log entries to generate the chart from.
     */
    private void generateLineChart(ArrayList<ReportEntry> reportEntries)
    {
        Platform.runLater(() -> {
            // Create new series for each action type
            XYChart.Series<String, Number> frozenSeries = new XYChart.Series<>();
            frozenSeries.setName("Frozen");
            XYChart.Series<String, Number> activeSeries = new XYChart.Series<>();
            activeSeries.setName("Active");

            // Initialize the maximum value
            int maxValue = 0;

            // Define the week formatter
            DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Process the log entries and add the data points directly
            for (ReportEntry entry : reportEntries)
            {
                // Convert the date to a LocalDate
                LocalDate date = entry.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String week = date.format(weekFormatter);

                // Get the action type
                String action = entry.getType();

                // Parse the count
                int count = Integer.parseInt(entry.getDescription());

                // Add the data points to the respective series
                if (action.equalsIgnoreCase("frozen"))
                {
                    frozenSeries.getData().add(new XYChart.Data<>(week, count));
                }
                else if (action.equalsIgnoreCase("active"))
                {
                    activeSeries.getData().add(new XYChart.Data<>(week, count));
                }

                // Update the maximum value
                maxValue = Math.max(maxValue, count);
            }

            // Sort the weeks
            List<String> sortedWeeks = frozenSeries.getData().stream()
                    .map(data -> data.getXValue())
                    .sorted(Comparator.comparing(week -> LocalDate.parse(week, weekFormatter)))
                    .collect(Collectors.toList());

            // Configure the x-axis to handle week values
            CategoryAxis xAxis = (CategoryAxis) subscriberStatusesChart.getXAxis();
            xAxis.setLabel("Week");
            xAxis.setCategories(FXCollections.observableArrayList(sortedWeeks));

            // Configure the y-axis
            NumberAxis yAxis = (NumberAxis) subscriberStatusesChart.getYAxis();
            yAxis.setLabel("Count of Actions");
            yAxis.setAutoRanging(false);
            yAxis.setTickUnit(1);
            yAxis.setUpperBound(maxValue + 1);

            // Adds the new series to the line chart
            subscriberStatusesChart.getData().addAll(frozenSeries, activeSeries);
            subscriberStatusesChart.setTitle("Subscriber Statuses Per Week");
        });
    }
}