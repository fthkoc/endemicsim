package simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Common definitions implemented in this class
 */
public class Common {
    /**
     * Default population value;
     */
    public static final int DEFAULT_POPULATION = 400;

    // public static final double DEFAULT_SPREADING_FACTOR = 0.8;

    // public static final double DEFAULT_MORTALITY_RATE = 0.2;

    /**
     * How many seconds needs to pass for hospitalize individual
     */
    public static final int TO_HOSPITAL_SEC = 25;

    /**
     * How many seconds needs to pass for discharge individual
     */
    public static final int AT_HOSPITAL_SEC = 10;

    /**
     * Canvas X axis length
     */
    public static final int CANVAS_X = 1000;

    /**
     * Canvas Y axis length
     */
    public static final int CANVAS_Y = 600;

    /**
     * Length of the single box that represents individual in canvas
     */
    public static final int CANVAS_BOX_XY = 5;

    /**
     * Maximum speed value for individual
     */
    public static final int MAX_SPEED = 500;

    /**
     * Maximum social distance value for individual
     */
    public static final int MAX_SOCIAL_DISTANCE = 10;

    /**
     * Maximum interaction time value for individual
     */
    public static final int MAX_INTERACTION_TIME = 5;

    /**
     * Movement directions for individual
     */
    public enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    private static final List<Direction> DIRECTIONS = List.of(Direction.values());
    private static final int DIRECTION_COUNT = DIRECTIONS.size();
    private static final Random RANDOM = new Random();

    /**
     * Get random direction from enum
     *
     * @return Direction
     */
    public static Direction getRandomDirection() {
        return DIRECTIONS.get(RANDOM.nextInt(DIRECTION_COUNT));
    }

    /**
     * Get list of directions except given one
     *
     * @param currentDirection Direction
     * @return List of directions except given one
     */
    public static ArrayList<Direction> getRestOfDirections(Direction currentDirection) {
        var result = new ArrayList<>(Arrays.asList(Direction.values()));
        result.remove(currentDirection);
        return result;
    }

    /**
     * Get random direction from rest of the directions
     *
     * @param currentDirection direction
     * @return random direction except given one
     */
    public static Direction getRandomFromRestOfDirections(Direction currentDirection) {
        var directions = getRestOfDirections(currentDirection);
        return directions.get(RANDOM.nextInt(DIRECTION_COUNT - 1));
    }
}
