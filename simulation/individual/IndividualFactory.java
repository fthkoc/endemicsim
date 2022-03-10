package simulation.individual;

import java.util.ArrayList;

/**
 * Concrete Factory class for Individuals
 */
public class IndividualFactory implements Factory {
    private final IndividualBuilder builder;

    public IndividualFactory(IndividualBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Individual getIndividualInstance(int iteration, int id) {
        boolean isInfected = builder.generateIsInfectedFlag();
        int infectedAt = 0;
        if (isInfected)
            infectedAt = iteration;

        return new Individual(
                id,
                builder.generateCoordinateX(),
                builder.generateCoordinateY(),
                builder.generateSpeed(),
                builder.generateRandomDirection(),
                builder.generateSocialDistance(),
                builder.generateInteractionTime(),
                builder.generateIsMaskedFlag(),
                isInfected,
                infectedAt
        );
    }

    @Override
    public ArrayList<Individual> getIndividualInstances(int iteration, int currentPopulation, int count) {
        ArrayList<Individual> result = new ArrayList<>();
        for (int i = currentPopulation; i < currentPopulation + count; i++) {
            result.add(getIndividualInstance(iteration, i));
        }
        return result;
    }

    @Override
    public ArrayList<Individual> getIndividualInstancesForInitialization(int count) {
        ArrayList<Individual> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(new Individual(
                    i,
                    builder.generateCoordinateX(),
                    builder.generateCoordinateY(),
                    builder.generateSpeed(),
                    builder.generateRandomDirection(),
                    builder.generateSocialDistance(),
                    builder.generateInteractionTime(),
                    builder.generateIsMaskedFlag(),
                    i == 0,
                    0
            ));
        }
        return result;
    }


}
