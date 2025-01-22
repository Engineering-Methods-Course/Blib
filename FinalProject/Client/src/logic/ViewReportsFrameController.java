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
import javafx.util.StringConverter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ViewReportsFrameController {
    // The FXML elements
    @FXML
    public ChoiceBox<String> reportChoiceBox;
    @FXML
    public Button backButton;
    @FXML
    public BarChart<String, Number> BorrowChart;
    @FXML
    public LineChart<String, Number> subscriberStatusesChart;

    public ChoiceBox<Integer> startYear;
    public ChoiceBox<String> startMonth;
    public ChoiceBox<Integer> endYear;
    public ChoiceBox<String> endMonth;
    public RadioButton borrowReportRadio;
    public RadioButton subscriberStatusesReportRadio;
    public Button generateReport;
    private ToggleGroup reportToggleGroup;


    public void initialize() {
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


        // Add event handler to the generateReport button
        generateReport.setOnAction(event -> updateCharts());
    }

    private void switchChartVisibility() {
        if (borrowReportRadio.isSelected()) {
            BorrowChart.setVisible(true);
            subscriberStatusesChart.setVisible(false);
        } else if (subscriberStatusesReportRadio.isSelected()) {
            BorrowChart.setVisible(false);
            subscriberStatusesChart.setVisible(true);
        }
    }

    private void updateCharts() {
        // Helper variables for the message ID and date range
        int messageID = -1;

        // Retrieve the selected start and end month and year
        int startYearValue = (int) startYear.getValue();
        int endYearValue = (int) endYear.getValue();
        Month startMonthValue = Month.valueOf(startMonth.getValue().toUpperCase());
        Month endMonthValue = Month.valueOf(endMonth.getValue().toUpperCase());

        // Create LocalDate objects for the start and end dates
        LocalDate startDate = LocalDate.of(startYearValue, startMonthValue, 1);
        LocalDate endDate = LocalDate.of(endYearValue, endMonthValue, 1);

        // Convert LocalDate to Date
        Date startDateConverted = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateConverted = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Check if the endDate is before the startDate and if so, bring an error message
        if (endDateConverted.before(startDateConverted)) {
            ClientGUIController.showAlert(Alert.AlertType.ERROR, "Wrong date", "End date must be later than start date");
            return;
        }

        // Clear the charts
        BorrowChart.getData().clear();
        subscriberStatusesChart.getData().clear();

        // Update the current report type based on the selected radio button
        if (borrowReportRadio.isSelected()) {
            messageID = 312;
        } else if (subscriberStatusesReportRadio.isSelected()) {
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
    public void generateBorrowTimeReport(ArrayList<MonthlyReport> reportData) {
        // Create an ArrayList<ReportEntry> to store the data in
        ArrayList<ReportEntry> reportEntries = new ArrayList<>();
        for (MonthlyReport monthlyReport : reportData) {
            //adds all the report entries to the list
            reportEntries.addAll(monthlyReport.getReport());
        }

        // Generate the bar chart
        generateBarChart("Actions Per Week", reportEntries);


        // Generate the line chart
        generateLineChart("Returns and Borrows Over Time", reportEntries);
    }

    /**
     * Generates the reports based on the given data.
     *
     * @param reportData The data to generate the reports from.
     */
    public void generateSubscriberStatusReport(ArrayList<MonthlyReport> reportData) {
        // Create an ArrayList<ReportEntry> to store the data in
        ArrayList<ReportEntry> reportEntries = new ArrayList<>();
        for (MonthlyReport monthlyReport : reportData) {
            //adds all the report entries to the list
            reportEntries.addAll(monthlyReport.getReport());
        }

        // Generate the bar chart
        generateBarChart("Status Changes per Week", reportEntries);


        // Generate the line chart
        generateLineChart("Status Changes Over Time", reportEntries);
    }

    /**
     * Generates a bar chart based on the given log entries.
     *
     * @param chartName     The name of the chart.
     * @param reportEntries The log entries to generate the chart from.
     */
    private void generateBarChart(String chartName, ArrayList<ReportEntry> reportEntries) {
        Platform.runLater(() -> {
            // Create new series for each action type
            XYChart.Series<String, Number> borrowSeries = new XYChart.Series<>();
            borrowSeries.setName("Borrows");
            XYChart.Series<String, Number> returnSeries = new XYChart.Series<>();
            returnSeries.setName("Returns");
            XYChart.Series<String, Number> lateReturnSeries = new XYChart.Series<>();
            lateReturnSeries.setName("Late Returns");

            // Create a map to count actions per week
            Map<String, Map<String, Integer>> actionCountMap = new HashMap<>();

            // Define the week formatter
            DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("YYYY-'W'ww");

            // Process the log entries and count the actions per week
            for (ReportEntry entry : reportEntries) {
                LocalDate date = entry.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String week = date.format(weekFormatter);
                String action = entry.getType();

                // Initialize the week map if not already present
                actionCountMap.putIfAbsent(week, new HashMap<>());
                Map<String, Integer> weekMap = actionCountMap.get(week);

                // Count the actions
                weekMap.put(action, weekMap.getOrDefault(action, 0) + 1);
            }

            // Populate the bar chart with the action counts
            for (Map.Entry<String, Map<String, Integer>> weekEntry : actionCountMap.entrySet()) {
                String week = weekEntry.getKey();
                Map<String, Integer> weekMap = weekEntry.getValue();

                borrowSeries.getData().add(new XYChart.Data<>(week, weekMap.getOrDefault("Borrow", 0)));
                returnSeries.getData().add(new XYChart.Data<>(week, weekMap.getOrDefault("Return", 0)));
                lateReturnSeries.getData().add(new XYChart.Data<>(week, weekMap.getOrDefault("Late Return", 0)));
            }

            // Configure the x-axis to handle week values
            CategoryAxis xAxis = (CategoryAxis) BorrowChart.getXAxis();
            xAxis.setLabel("Week");
            xAxis.setCategories(FXCollections.observableArrayList(actionCountMap.keySet()));

            // Configure the y-axis
            NumberAxis yAxis = (NumberAxis) BorrowChart.getYAxis();
            yAxis.setLabel("Count of Actions");

            // Adds the new series to the bar chart
            BorrowChart.getData().addAll(borrowSeries, returnSeries, lateReturnSeries);
            BorrowChart.setTitle(chartName);
        });
    }

    /**
     * Generates a line chart based on the given log entries.
     *
     * @param chartName     The name of the chart.
     * @param reportEntries The log entries to generate the chart from.
     */
    private void generateLineChart(String chartName, ArrayList<ReportEntry> reportEntries) {
        Platform.runLater(() -> {
            // Create a new series for the line chart
            Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

            // Define the date formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Collect all the dates
            Set<String> allDates = new TreeSet<>();

            // Process the log entries
            for (ReportEntry entry : reportEntries) {
                // Get the date and action
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
                for (XYChart.Data<String, Number> data : series.getData()) {
                    if (data.getXValue().equals(weekStart)) {
                        data.setYValue(data.getYValue().intValue() + 1);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    series.getData().add(new XYChart.Data<>(weekStart, 1));
                }
            }

            // Configure the x-axis to handle date values
            CategoryAxis xAxis = (CategoryAxis) subscriberStatusesChart.getXAxis();
            xAxis.setLabel("Date");
            xAxis.setCategories(FXCollections.observableArrayList(allDates));

            // Adds the new series to the line chart
            subscriberStatusesChart.getData().addAll(seriesMap.values());
            subscriberStatusesChart.setTitle(chartName);
        });
    }

    /**
     * Initializes the date picker to only allow month selection.
     *
     * @param datePicker The date picker to initialize.
     */
    private void initializeDatePicker(DatePicker datePicker) {
        // Define the date formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Set the date format
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, formatter);
                } else {
                    return null;
                }
            }
        });

        // Disable all days except the first day of each month and all the dates after the previous month
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Disable all days except the first day of each month and all the dates after the previous month
                setDisable(empty || date.getDayOfMonth() != 1);
            }
        });

        // Set the default value to the first day of the current month
        datePicker.setValue(LocalDate.now().withDayOfMonth(1).minusMonths(1));
    }
}