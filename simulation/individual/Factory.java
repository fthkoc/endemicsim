package simulation.individual;

import java.util.ArrayList;

/**
 * Factory interface for future factory implementations, Factory design pattern
 */
public interface Factory {
    /**
     * Get single instance of Individual with given id
     *
     * @param iteration Iteration of the simulation when method called
     * @param id        ID of the Individual instance
     * @return an Individual instance
     */
    Individual getIndividualInstance(int iteration, int id);

    /**
     * Get list of Individual instance with the size of count
     *
     * @param iteration Iteration of the simulation when method called
     * @param currentPopulation Current population of the society, need for unique ID creations
     * @param count             number of instances
     * @return List of Individual instances
     */
    ArrayList<Individual> getIndividualInstances(int iteration, int currentPopulation, int count);

    /**
     * Get list of Individual instances with the size of count
     * needed for initializing simulation only with 1 infected individual
     *
     * @param count             number of instances
     * @return List of Individual instances
     */
    ArrayList<Individual> getIndividualInstancesForInitialization(int count);
}
