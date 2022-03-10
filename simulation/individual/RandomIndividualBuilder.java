package simulation.individual;

import simulation.Common;

import java.util.Random;

/**
 * Concrete builder class to use in factory, uses random calculation in project requirements
 */
public class RandomIndividualBuilder implements IndividualBuilder {
    private static final Random RANDOM = new Random();

    @Override
    public int generateCoordinateX() {
        return RANDOM.nextInt(Common.CANVAS_X);
    }

    @Override
    public int generateCoordinateY() {
        return RANDOM.nextInt(Common.CANVAS_Y);
    }

    @Override
    public int generateSpeed() {
        return RANDOM.nextInt(Common.MAX_SPEED) + 1;
    }

    @Override
    public Common.Direction generateRandomDirection() {
        return Common.getRandomDirection();
    }

    @Override
    public int generateSocialDistance() {
        return RANDOM.nextInt(Common.MAX_SOCIAL_DISTANCE);
    }

    @Override
    public int generateInteractionTime() {
        return RANDOM.nextInt(Common.MAX_INTERACTION_TIME) + 1;
    }

    @Override
    public boolean generateIsMaskedFlag() {
        return RANDOM.nextInt(2) % 2 == 0;
    }

    @Override
    public boolean generateIsInfectedFlag() {
        return RANDOM.nextInt(2) % 2 == 0;
    }
}
