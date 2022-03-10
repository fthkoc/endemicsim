package simulation.fsm;

import simulation.Simulator;

import java.util.ArrayList;

/**
 * End state of the simulation
 */
public class EndState implements SimulationState {
    @Override
    public void execute(Simulator context, int iteration) {
        System.out.println("iteration:" + iteration + ", EndState::execute() called.");
        context.setIndividuals(new ArrayList<>());
        try {
            var simulationThread = context.getSimulationThread();
            if (simulationThread != null)
                simulationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("iteration:" + iteration + ", EndState::execute() finished.");
    }
}
