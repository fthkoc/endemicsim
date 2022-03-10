package simulation.fsm;

import simulation.Common;
import simulation.Mediator;
import simulation.Simulator;
import simulation.individual.Individual;

/**
 * Process state of the simulation
 */
public class ProcessState implements SimulationState {
    @Override
    public void execute(Simulator context, int iteration) {
        System.out.println("iteration:" + iteration + ", ProcessState::execute() called.");
        // Make moves
        Mediator.moveForAll(context.getIndividuals(), iteration);
        // Check collisions
        var collisions = Mediator.getListOfCollisions(context.getIndividuals());
        //      if collisions exists, handle collisions
        if (!collisions.isEmpty())
            for (Individual[] collision : collisions)
                Mediator.handleCollision(collision[0], collision[1], context.getDisease(), iteration);
        // Check hospital
        var hospital = context.getHospital();
        Mediator.checkHospitalTimeForAll(context.getIndividuals(), hospital, iteration);
        hospital.handlePatients(context.getPopulation(), iteration);
        /*
        Thread hospitalThread = new Thread(() -> {
            var hospital = context.getHospital();
            Mediator.checkHospitalTimeForAll(context.getIndividuals(), hospital, iteration);
            hospital.handlePatients(context.getPopulation(), iteration);
        });
        hospitalThread.start();
        */
        // Check died ones
        Mediator.checkInfectionTimeForAll(context.getIndividuals(), context.getDisease(), iteration);
        System.out.println("iteration:" + iteration + ", ProcessState::execute() finished.");
    }

    // Print list of individuals
    private void printIndividuals(Simulator context, int iteration) {
        System.out.println("DEBUG::iteration: " + iteration + ", ProcessState::execute()::Moved individuals:");
        for (Individual person : context.getIndividuals()) {
            System.out.println("DEBUG::iteration: " + iteration + ", " + person.toString());
        }
    }

    // Check is there any overlapping individuals
    private void checkOverlaps(Simulator context, int iteration) {
        for (Individual individual : context.getIndividuals()) {
            for (Individual overlapped : context.getIndividuals()) {
                if (individual.getID() != overlapped.getID() && !individual.isInInteraction() && !overlapped.isInInteraction()) {
                    int minX = individual.getCoordinateX() - Common.CANVAS_BOX_XY;
                    int minY = individual.getCoordinateY() - Common.CANVAS_BOX_XY;
                    int maxX = individual.getCoordinateX() + Common.CANVAS_BOX_XY;
                    int maxY = individual.getCoordinateY() + Common.CANVAS_BOX_XY;
                    if (overlapped.getCoordinateX() > minX && overlapped.getCoordinateX() < maxX
                            && (overlapped.getCoordinateY() > minY && overlapped.getCoordinateY() < maxY)) {
                        System.out.println("DEBUG::OVERLAP::[" + individual.getID() + "]:(" + individual.getCoordinateX() + "," + individual.getCoordinateY() + ")-[" + overlapped.getID() + "]:(" + overlapped.getCoordinateX() + "," + overlapped.getCoordinateY() + ")");
                        System.out.println("DEBUG::" + individual.toString());
                        System.out.println("DEBUG::" + Mediator.getAvailableDirectionsForIndividual(context.getIndividuals(), individual));
                        System.out.println("DEBUG::" + overlapped.toString());
                        System.out.println("DEBUG::" + Mediator.getAvailableDirectionsForIndividual(context.getIndividuals(), overlapped));
                    }
                }
            }
        }
    }
}
