package simulation.fsm;

import simulation.Simulator;

/**
 * Simulation state, State design pattern
 */
public interface SimulationState {
    /**
     * Execute state methods
     * @param context Simulation
     * @param iteration Iteration of the simulation when method called
     */
    void execute(Simulator context, int iteration);
}
