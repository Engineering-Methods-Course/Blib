package logic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;

import static client.ClientGUIController.navigateTo;

public class ViewReportsFrameController
{
    @FXML
    private LineChart<String, Number> lineChart;  // Reference to the LineChart
    @FXML
    private CategoryAxis xAxis;  // Reference to the x-axis
    @FXML
    private NumberAxis yAxis;  // Reference to the y-axis
    @FXML
    private RadioButton radioX;  // Reference to the X radio button
    @FXML
    private RadioButton radioY;  // Reference to the Y radio button
    @FXML
    private RadioButton radioZ;  // Reference to the Z radio button
    @FXML
    private Button backButton;  // Reference to the Back button

    /**
     * Handles the Radio Button selection for X, Y, and Z.
     * This updates the chart data based on the selected radio button.
     */
    @FXML
    public void onRadioButtonSelected(ActionEvent actionEvent)
    {
        //todo: change the chart rely on chosen option
    }

    /**
     * Updates the chart for "X" data.
     */
    private void updateChartForX()
    {
        //todo:update the chart to type x
    }

    /**
     * Updates the chart for "Y" data.
     */
    private void updateChartForY()
    {
        //todo:update the chart to type y
    }

    /**
     * Updates the chart for "Z" data.
     */
    private void updateChartForZ()
    {
        //todo:update the chart to type z
    }

    /**
     * Handles the Back button click event.
     *
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during navigation.
     */
    public void backButtonClicked(ActionEvent event) throws Exception
    {
        navigateTo(event, "LibrarianProfileFrame.fxml", "Subscriber.css", "Reports");
    }
}