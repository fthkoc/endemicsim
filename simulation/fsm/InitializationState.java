package simulation.fsm;

import simulation.Simulator;
import simulation.individual.Factory;
import simulation.individual.IndividualFactory;
import simulation.individual.RandomIndividualBuilder;

/**
 * Initialize state of the simulation
 */
public class InitializationState implements SimulationState {
    @Override
    public void execute(Simulator context, int iteration) {
        System.out.println("iteration:" + iteration + ", InitializationState::execute() called.");
        Factory factory = new IndividualFactory(new RandomIndividualBuilder());
        context.setIndividuals(factory.getIndividualInstancesForInitialization(context.getPopulation()));
        System.out.println("iteration:" + iteration + ", InitializationState::execute() finished.");
    }
}
