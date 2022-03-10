package simulation;

import simulation.disease.Disease;
import simulation.fsm.*;
import simulation.hospital.Hospital;
import simulation.individual.Factory;
import simulation.individual.Individual;
import simulation.individual.IndividualFactory;
import simulation.individual.RandomIndividualBuilder;

import java.util.ArrayList;

/**
 * Main class for simulation, handles all the work in the back-end side, offers an API for GUI
 */
public class Simulator {
    /**
     * Disease
     */
    private final Disease disease;
    /**
     * Hospital
     */
    private final Hospital hospital;
    /**
     * Iteration of the simulation when method called.
     */
    private int iteration;
    /**
     * Thread for ProcessState, to run infinite Game Loop inside
     */
    private Thread simulationThread;
    /**
     * Current State of the simulation
     */
    private SimulationState state;
    /**
     * Population
     */
    private int population;
    /**
     * Individual list of the society
     */
    private ArrayList<Individual> individuals;

    // Constructors
    public Simulator(int population, double spreadingFactor, double mortalityRate) {
        this.population = population;
        this.simulationThread = null;
        this.disease = new Disease(spreadingFactor, mortalityRate);
        this.hospital = new Hospital(population);
        this.individuals = new ArrayList<>();
        this.state = new InitializationState();
        this.state.execute(this, 0);
    }

    /**
     * Run process state of the simulation in a new thread
     */
    public void run() {
        System.out.println("iteration:" + iteration + ", Simulator::run() called.");
        this.setState(new ProcessState());
        simulationThread = new Thread(this::simulationProcessLoop);
        simulationThread.start();
        System.out.println("iteration:" + iteration + ", Simulator::run() finished.");
    }

    /**
     * Pause simulation by updating state
     */
    public void pause() {
        System.out.println("iteration:" + iteration + ", Simulator::pause() called.");
        this.setState(new PauseState());
        this.state.execute(this, iteration);
        iteration++;
        System.out.println("iteration:" + iteration + ", Simulator::pause() finished.");
    }

    /**
     * End simulation by updating state
     */
    public void end() {
        System.out.println("iteration:" + iteration + ", Simulator::end() called.");
        this.setState(new EndState());
        this.state.execute(this, iteration);
        iteration++;
        System.out.println("iteration:" + iteration + ", Simulator::end() finished.");
    }

    /**
     * ProcessState infinite loop, each step updates simulation for 1 iteration
     */
    protected void simulationProcessLoop() {
        System.out.println("iteration:" + iteration + ", Simulator::simulationProcessLoop() called.");
        final long[] lastCallTime = {System.nanoTime()};
        while (isSimulationRunning()) {
            double elapsedTime = (System.nanoTime() - lastCallTime[0]) / 1000000000.0;
            if (elapsedTime > 1.0) {
                this.state.execute(this, iteration);
                iteration++;
                System.out.println("DEBUG::simulationProcessLoop()::iteration_" + iteration + ", took " + (((System.nanoTime() - lastCallTime[0]) / 1000000000.0) - 1.0) + " seconds");
                lastCallTime[0] = System.nanoTime();
            }
        }
        System.out.println("iteration:" + iteration + ", Simulator::simulationProcessLoop() finished.");
    }

    /**
     * Check simulation state, is it on one of the running states
     *
     * @return true if current state is InitializationState or ProcessState
     */
    public boolean isSimulationRunning() {
        return this.state.getClass().equals(InitializationState.class) || this.state.getClass().equals(ProcessState.class);
    }

    /**
     * Add number of individuals to the society, each with random parameters
     *
     * @param count Number of individuals to add
     */
    public void addBulkIndividuals(int count) {
        System.out.println("iteration:" + iteration + ", Simulator::addBulkIndividuals() started.");
        Factory factory = new IndividualFactory(new RandomIndividualBuilder());
        this.individuals.addAll(factory.getIndividualInstances(getIteration(), getPopulation(), count));
        this.population += count;
        System.out.println("iteration:" + iteration + ", Simulator::addBulkIndividuals() finished.");
    }

    /**
     * Add an individual to the society with random parameters
     */
    public void addIndividual() {
        System.out.println("iteration:" + iteration + ", Simulator::addIndividual() started.");
        Factory factory = new IndividualFactory(new RandomIndividualBuilder());
        this.individuals.add(factory.getIndividualInstance(getIteration(), getPopulation()));
        this.population++;
        System.out.println("iteration:" + iteration + ", Simulator::addIndividual() finished.");
    }

    // Getters and Setters
    public int getIteration() {
        return iteration;
    }

    public Thread getSimulationThread() {
        return simulationThread;
    }

    public int getPopulation() {
        return population;
    }

    public Disease getDisease() {
        return disease;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }

    public SimulationState getState() {
        return state;
    }

    public void setState(SimulationState state) {
        this.state = state;
    }

    public double getTotalCount() {
        return population;
    }

    public double getHealthyCount() {
        double count = 0;
        for (Individual person : individuals) {
            if (person.isAlive() && !person.isInfected() && !person.isHospitalized())
                count++;
        }
        return count;
    }

    public double getInfectedCount() {
        double count = 0;
        for (Individual person : individuals) {
            if (person.isAlive() && person.isInfected() && !person.isHospitalized())
                count++;
        }
        return count;
    }

    public double getHospitalizedCount() {
        double count = 0.0;
        for (Individual person : individuals) {
            if (person.isAlive() && person.isHospitalized())
                count++;
        }
        return count;
    }

    public double getCasualtyCount() {
        double count = 0.0;
        for (Individual person : individuals) {
            if (!person.isAlive())
                count++;
        }
        return count;
    }

    public double getAverageSocialDistance() {
        int average = 0;
        for (Individual person : individuals) {
            average += person.getSocialDistance();
        }
        return (double) average / individuals.size();
    }

    public double getMaskUsagePercentage() {
        int count = 0;
        for (Individual person : individuals) {
            if (person.isMasked())
                count++;
        }
        return (double) count / individuals.size() * 100;
    }

    public double getCurrentAverageSocialDistance() {
        int average = 0;
        for (Individual person : individuals) {
            if (person.isAlive())
                average += person.getSocialDistance();
        }
        return (double) average / (getHealthyCount() + getInfectedCount());
    }

    public double getCurrentMaskUsagePercentage() {
        int count = 0;
        for (Individual person : individuals) {
            if (person.isAlive())
                if (person.isMasked())
                    count++;
        }
        return (double) count / (getHealthyCount() + getInfectedCount()) * 100;
    }
}
