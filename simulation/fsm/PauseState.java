package simulation.fsm;

import simulation.Simulator;

/**
 * Pause state of the simulation
 */
public class PauseState implements SimulationState {
    @Override
    public void execute(Simulator context, int iteration) {
        System.out.println("iteration:" + iteration + ", PauseState::execute() called.");
        try {
            var simulationThread = context.getSimulationThread();
            if (simulationThread != null)
                simulationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("iteration:" + iteration + ", PauseState::execute() finished.");
    }
}
