package simulation.individual;

import simulation.Common;
import simulation.Mediator;
import simulation.hospital.Hospital;
import simulation.hospital.Subscriber;

/**
 * Individual entity,  represents a single person in the society
 * Implements Subscriber to subscribe into hospital in Pub/Sub structure
 */
public class Individual implements Subscriber {
    // Fields
    /**
     * a unique number for each individual
     */
    private final int ID;
    /**
     * X coordinate on the canvas
     */
    private int CoordinateX;
    /**
     * Y coordinate on the canvas
     */
    private int CoordinateY;
    /**
     * constant moving speed of an individual, S in [1,5] pixels/second
     */
    private final int Speed;
    /**
     * moving direction of an individual
     */
    private Common.Direction Direction;
    /**
     * the social distance that they practice when they collide with other individuals, D in [0,9] (in pixels)
     */
    private final int SocialDistance;
    /**
     * is the individual in interaction right now
     */
    private boolean IsInInteraction;
    /**
     * how many seconds passed after getting into interaction
     */
    private int InteractedUntil;
    /**
     * time spend in a social interaction, C in [1,5] seconds
     */
    private final int InteractionTime;
    /**
     * is the individual wear a mask or not
     */
    private final boolean IsMasked;
    /**
     * is the individual infected with a disease or not
     */
    private boolean IsInfected;
    /**
     * how many seconds passed after getting infected
     */
    private int InfectedAt;
    /**
     * is the individual hospitalized
     */
    private boolean IsHospitalized;
    /**
     * how many seconds passed after getting hospitalized
     */
    private int HospitalizedAt;
    /**
     * is the individual alive
     */
    private boolean IsAlive;

    // Constructors
    public Individual(int id, int coordinateX, int coordinateY, int speed, Common.Direction direction, int socialDistance, int interactionTime, boolean isMasked, boolean isInfected, int infectedAt) {
        ID = id;
        CoordinateX = coordinateX;
        CoordinateY = coordinateY;
        Speed = speed;
        Direction = direction;
        SocialDistance = socialDistance;
        InteractionTime = interactionTime;
        IsInInteraction = false;
        InteractedUntil = 0;
        IsMasked = isMasked;
        IsInfected = isInfected;
        InfectedAt = infectedAt;
        IsAlive = true;
        IsHospitalized = false;
        HospitalizedAt = 0;
    }

    // Getters and Setters
    public int getID() {
        return ID;
    }

    public int getCoordinateX() {
        return CoordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        CoordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return CoordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        CoordinateY = coordinateY;
    }

    public int getSpeed() {
        return Speed;
    }

    public Common.Direction getDirection() {
        return Direction;
    }

    public void setDirection(Common.Direction direction) {
        Direction = direction;
    }

    public int getSocialDistance() {
        return SocialDistance;
    }

    public int getInteractionTime() {
        return InteractionTime;
    }

    public boolean isInInteraction() {
        return IsInInteraction;
    }

    public void setInInteraction(boolean inInteraction) {
        IsInInteraction = inInteraction;
    }

    public int getInteractedUntil() {
        return InteractedUntil;
    }

    public void setInteractedUntil(int interactedUntil) {
        InteractedUntil = interactedUntil;
    }

    public boolean isMasked() {
        return IsMasked;
    }

    public boolean isInfected() {
        return IsInfected;
    }

    public void setInfected(boolean infected) {
        IsInfected = infected;
    }

    public int getInfectedAt() {
        return InfectedAt;
    }

    public void setInfectedAt(int infectedAt) {
        InfectedAt = infectedAt;
    }

    public boolean isAlive() {
        return IsAlive;
    }

    public void setAlive(boolean alive) {
        IsAlive = alive;
    }

    public boolean isHospitalized() {
        return IsHospitalized;
    }

    public void setHospitalized(boolean hospitalized) {
        IsHospitalized = hospitalized;
    }

    public int getHospitalizedAt() {
        return HospitalizedAt;
    }

    public void setHospitalizedAt(int hospitalizedAt) {
        HospitalizedAt = hospitalizedAt;
    }

    // Helpers

    /**
     * Return the factor value for individuals mask flag
     * @return Mask Factor
     */
    public double getMaskFactor() {
        if (isMasked())
            return 0.2;
        else
            return 1.0;
    }

    /**
     * Update position for one step to individuals direction with its speed
     */
    public void makeMove() {
        switch (getDirection()) {
            case LEFT:
                this.setCoordinateX(getCoordinateX() - getSpeed());
                break;
            case RIGHT:
                this.setCoordinateX(getCoordinateX() + getSpeed());
                break;
            case UP:
                this.setCoordinateY(getCoordinateY() - getSpeed());
                break;
            case DOWN:
                this.setCoordinateY(getCoordinateY() + getSpeed());
                break;
        }
    }

    // Subscriber methods
    @Override
    public void hospitalize(Hospital hospital, int iteration) {
        Mediator.hospitalizeIndividual(this, hospital, iteration);
    }

    @Override
    public void discharge(Hospital hospital, int iteration) {
        Mediator.dischargeIndividual(this, hospital, iteration);
    }

    @Override
    public String toString() {
        return "Individual{" +
                "ID=" + ID +
                ", CoordinateX=" + CoordinateX +
                ", CoordinateY=" + CoordinateY +
                ", Speed=" + Speed +
                ", Direction=" + Direction +
                ", SocialDistance=" + SocialDistance +
                ", IsInInteraction=" + IsInInteraction +
                ", InteractedUntil=" + InteractedUntil +
                ", InteractionTime=" + InteractionTime +
                ", IsMasked=" + IsMasked +
                ", IsInfected=" + IsInfected +
                ", InfectedAt=" + InfectedAt +
                ", IsHospitalized=" + IsHospitalized +
                ", HospitalizedAt=" + HospitalizedAt +
                ", IsAlive=" + IsAlive +
                '}';
    }
}
