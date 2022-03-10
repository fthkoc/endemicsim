package simulation.individual;

import simulation.Common;

/**
 * Builder interface for Builder design pattern
 */
public interface IndividualBuilder {
    /**
     * Generate X coordinate for individual
     *
     * @return Valid X coordinate value
     */
    int generateCoordinateX();

    /**
     * Generate Y coordinate for individual
     *
     * @return Valid Y coordinate value
     */
    int generateCoordinateY();

    /**
     * Generate speed value for individual
     *
     * @return Valid speed value
     */
    int generateSpeed();

    /**
     * Generate direction for individual
     *
     * @return Valid direction value
     */
    Common.Direction generateRandomDirection();

    /**
     * Generate social distance value for individual
     *
     * @return Valid social distance value
     */
    int generateSocialDistance();

    /**
     * Generate interaction time value for individual
     *
     * @return Valid interaction time value
     */
    int generateInteractionTime();

    /**
     * Generate is masked flag for individual
     *
     * @return Valid is masked flag value
     */
    boolean generateIsMaskedFlag();

    /**
     * Generate is infected flag value for individual
     *
     * @return Valid is infected flag value
     */
    boolean generateIsInfectedFlag();
}
