package simulation.hospital;

/**
 * Subscriber to hospital, each individual should implement this interface
 * Subscriber in Pub/Sub design
 */
public interface Subscriber {
    /**
     * Hospitalize this subscriber to given hospital
     *
     * @param hospital  Hospital, as a Publisher in Pub/Sub design
     * @param iteration Iteration of the simulation when method called.
     */
    void hospitalize(Hospital hospital, int iteration);

    /**
     * Discharge this subscriber from given hospital
     *
     * @param hospital  Hospital, as a Publisher in Pub/Sub design
     * @param iteration Iteration of the simulation when method called.
     */
    void discharge(Hospital hospital, int iteration);
}
