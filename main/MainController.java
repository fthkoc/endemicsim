package main;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import simulation.Common;
import simulation.Simulator;
import simulation.fsm.PauseState;
import simulation.individual.Individual;

import java.util.Random;

/**
 * Controller class for the UI, handles UI with the simulation instance
 */
public class MainController {
    @FXML
    private Canvas canvas;
    @FXML
    private LineChart<Integer, Double> chart;
    @FXML
    private TextField populationField;
    @FXML
    private TextField spreadingFactorField;
    @FXML
    private TextField mortalityRateField;
    @FXML
    private TextField insertIndividualsField;
    @FXML
    private TextField totalCountField;
    @FXML
    private TextField healthyCountField;
    @FXML
    private TextField infectedCountField;
    @FXML
    private TextField hospitalizedCountField;
    @FXML
    private TextField casualtyCountField;
    @FXML
    private TextField averageSocialDistanceField;
    @FXML
    private TextField maskUsagePercentageField;
    @FXML
    private TextField currentAverageSocialDistanceField;
    @FXML
    private TextField currentMaskUsagePercentageField;
    /**
     * Simulator object, back-end of the program
     */
    private Simulator simulator;

    public MainController() {
        simulator = new Simulator(0, 0.0, 0.0);
    }

    /**
     * Initialize simulation with given input from user
     */
    private void initializeSimulation() {
        Random random = new Random();
        int population = Common.DEFAULT_POPULATION;
        double spreadingFactor = (random.nextInt(5) + 6) / 10.0;
        double mortalityRate = (random.nextInt(9) + 1) / 10.0;
        if (!populationField.getText().isBlank() || !populationField.getText().isEmpty())
            population = Integer.parseInt(populationField.getText());
        else
            populationField.setText(Integer.toString(population));
        if (!spreadingFactorField.getText().isBlank() || !spreadingFactorField.getText().isEmpty())
            spreadingFactor = Double.parseDouble(spreadingFactorField.getText());
        else
            spreadingFactorField.setText(Double.toString(spreadingFactor));
        if (!mortalityRateField.getText().isBlank() || !mortalityRateField.getText().isEmpty())
            mortalityRate = Double.parseDouble(mortalityRateField.getText());
        else
            mortalityRateField.setText(Double.toString(mortalityRate));
        this.simulator = new Simulator(population, spreadingFactor, mortalityRate);
        initializeChart();
    }

    /**
     * Initialize simulation and start to run
     */
    @FXML
    protected void startSimulation() {
        System.out.println("iteration:" + simulator.getIteration() + ", MainController::startSimulation() called.");
        endSimulation();
        initializeSimulation();
        simulator.run();
        System.out.println("iteration:" + simulator.getIteration() + ", MainController::startSimulation() finished.");
    }

    /**
     * Pause simulation if it is running, Run it again if it is on pause state
     */
    @FXML
    protected void pauseSimulation() {
        System.out.println("iteration:" + simulator.getIteration() + ", MainController::pauseSimulation() called.");
        if (simulator.getState().getClass().equals(PauseState.class))
            simulator.run();
        else
            simulator.pause();
        System.out.println("iteration:" + simulator.getIteration() + ", MainController::pauseSimulation() finished.");
    }

    /**
     * End simulation and clear UI
     */
    @FXML
    protected void endSimulation() {
        System.out.println("iteration:" + simulator.getIteration() + ", MainController::endSimulation() called.");
        simulator.end();
        clearCanvas();
        clearFields();
        clearChart();
        System.out.println("iteration:" + simulator.getIteration() + ", MainController::endSimulation() finished.");
    }

    /**
     * Add random individuals to the simulator
     */
    @FXML
    protected void addBulkIndividuals() {
        System.out.println("iteration:" + simulator.getIteration() + ", MainController::addBulkIndividuals() called.");
        int population;
        if (insertIndividualsField.getText().isBlank() || insertIndividualsField.getText().isBlank())
            population = 0;
        else
            population = Integer.parseInt(insertIndividualsField.getText());
        simulator.addBulkIndividuals(population);
        System.out.println("iteration:" + simulator.getIteration() + ", MainController::addBulkIndividuals() finished.");
    }

    /**
     * Add an individual to the simulator
     */
    @FXML
    protected void addIndividual() {
        System.out.println("iteration:" + simulator.getIteration() + ", MainController::addIndividual() called.");
        simulator.addIndividual();
        System.out.println("iteration:" + simulator.getIteration() + ", MainController::addIndividual() finished.");
    }

    /**
     * Update canvas
     */
    public void updateCanvas() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawCanvasBorders();
        drawIndividuals();
    }

    /**
     * Draw a border around canvas
     */
    private void drawCanvasBorders() {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setStroke(Color.BLACK);
        context.setLineWidth(1);
        context.moveTo(0, 0);
        context.lineTo(0, canvas.getHeight());
        context.stroke();
        context.moveTo(0, canvas.getHeight());
        context.lineTo(canvas.getWidth(), canvas.getHeight());
        context.stroke();
        context.moveTo(0, 0);
        context.lineTo(canvas.getWidth(), 0);
        context.stroke();
        context.moveTo(canvas.getWidth(), 0);
        context.lineTo(canvas.getWidth(), canvas.getHeight());
        context.stroke();
    }

    /**
     * Draw each individual in the simulator to the canvas
     */
    private void drawIndividuals() {
        var context = canvas.getGraphicsContext2D();
        var individuals = simulator.getIndividuals();
        for (Individual person : individuals) {
            if (person.isAlive() && !person.isHospitalized()) {
                if (person.isInfected())
                    context.setFill(Color.RED);
                else
                    context.setFill(Color.GREEN);
                context.fillRect(person.getCoordinateX(), person.getCoordinateY(), 5, 5);
            }
        }
    }

    /**
     * Clear canvas
     */
    public void clearCanvas() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Update fields with simulator data
     */
    public void updateCountStatistics() {
        if (simulator.getIndividuals().size() > 0) {
            totalCountField.setText(Integer.toString((int) simulator.getTotalCount()));
            healthyCountField.setText(Integer.toString((int) simulator.getHealthyCount()));
            infectedCountField.setText(Integer.toString((int) simulator.getInfectedCount()));
            hospitalizedCountField.setText(Integer.toString((int) simulator.getHospitalizedCount()));
            casualtyCountField.setText(Integer.toString((int) simulator.getCasualtyCount()));
            averageSocialDistanceField.setText(Double.toString(simulator.getAverageSocialDistance()));
            maskUsagePercentageField.setText(Double.toString(simulator.getMaskUsagePercentage()));
            currentAverageSocialDistanceField.setText(Double.toString(simulator.getCurrentAverageSocialDistance()));
            currentMaskUsagePercentageField.setText(Double.toString(simulator.getCurrentMaskUsagePercentage()));
        }
    }

    /**
     * Clear fields
     */
    public void clearFields() {
        totalCountField.setText("");
        healthyCountField.setText("");
        infectedCountField.setText("");
        hospitalizedCountField.setText("");
        casualtyCountField.setText("");
        averageSocialDistanceField.setText("");
        maskUsagePercentageField.setText("");
        currentAverageSocialDistanceField.setText("");
        currentMaskUsagePercentageField.setText("");
    }

    /**
     * Initialize line chart
     */
    private void initializeChart() {
        chart.setCreateSymbols(false);
        XYChart.Series<Integer, Double> casualty = new XYChart.Series<>();
        casualty.setName("Casualty");
        chart.getData().add(casualty);
        XYChart.Series<Integer, Double> infected = new XYChart.Series<>();
        infected.setName("Infected");
        chart.getData().add(infected);
        XYChart.Series<Integer, Double> healthy = new XYChart.Series<>();
        healthy.setName("Healthy");
        chart.getData().add(healthy);
        XYChart.Series<Integer, Double> hospitalized = new XYChart.Series<>();
        hospitalized.setName("Hospitalized");
        chart.getData().add(hospitalized);
        XYChart.Series<Integer, Double> averageSocialDistance = new XYChart.Series<>();
        averageSocialDistance.setName("Average Social Distance");
        chart.getData().add(averageSocialDistance);
        XYChart.Series<Integer, Double> maskUsagePercentage = new XYChart.Series<>();
        maskUsagePercentage.setName("Mask Usage Percentage");
        chart.getData().add(maskUsagePercentage);
    }

    /**
     * Update chart data
     */
    public void updateChart() {
        if (chart.getData().size() == 6) {
            chart.getData().get(0).getData().add(new XYChart.Data<>(simulator.getIteration(), simulator.getCasualtyCount()));
            chart.getData().get(1).getData().add(new XYChart.Data<>(simulator.getIteration(), simulator.getInfectedCount()));
            chart.getData().get(2).getData().add(new XYChart.Data<>(simulator.getIteration(), simulator.getHealthyCount()));
            chart.getData().get(3).getData().add(new XYChart.Data<>(simulator.getIteration(), simulator.getHospitalizedCount()));
            chart.getData().get(4).getData().add(new XYChart.Data<>(simulator.getIteration(), simulator.getCurrentAverageSocialDistance()));
            chart.getData().get(5).getData().add(new XYChart.Data<>(simulator.getIteration(), simulator.getCurrentMaskUsagePercentage()));
        }
    }

    /**
     * Clear chart data
     */
    public void clearChart() {
        chart.getData().clear();
    }
}
