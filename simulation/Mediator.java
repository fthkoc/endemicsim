package simulation;

import simulation.disease.Disease;
import simulation.hospital.Hospital;
import simulation.individual.Individual;

import java.util.ArrayList;
import java.util.Random;

/**
 * Mediator class to handle relations between different entities
 */
public class Mediator {
    private static final Random RANDOM = new Random();

    /**
     * Move given individual with the logic given in the project requirements
     *
     * @param individuals Current individual list of the society
     * @param individual  Person to move
     * @param iteration   Iteration of the simulation when method called
     */
    public static void move(ArrayList<Individual> individuals, Individual individual, int iteration) {
        if (individual.isInInteraction()) {
            if (iteration >= individual.getInteractedUntil()) {
                individual.setInInteraction(false);
                individual.setInteractedUntil(0);
                individual.setDirection(Common.getRandomDirection());
                move(individuals, individual, iteration);
            }
        } else {
            if (canIndividualMoveInGivenDirection(individuals, individual, individual.getDirection()))
                individual.makeMove();
            else {
                var directions = getAvailableDirectionsForIndividual(individuals, individual);
                if (!directions.isEmpty()) {
                    individual.setDirection(directions.get(RANDOM.nextInt(directions.size())));
                    move(individuals, individual, iteration);
                }
            }
        }
    }

    /**
     * Move every individual in the given list
     *
     * @param individuals Current individual list of the society
     * @param iteration   Iteration of the simulation when method called
     */
    public static void moveForAll(ArrayList<Individual> individuals, int iteration) {
        for (Individual person : individuals) {
            if (person.isAlive()) {
                move(individuals, person, iteration);
            }
        }
    }

    /**
     * Generates a list of possible directions that individual can make a move
     *
     * @param individuals Current individual list of the society
     * @param individual  Person to check
     * @return List of available directions
     */
    public static ArrayList<Common.Direction> getAvailableDirectionsForIndividual(ArrayList<Individual> individuals, Individual individual) {
        ArrayList<Common.Direction> result = new ArrayList<>();
        for (Common.Direction direction : Common.Direction.values()) {
            if (canIndividualMoveInGivenDirection(individuals, individual, direction))
                result.add(direction);
        }
        return result;
    }

    /**
     * Check individual movement in given direction is legal for the simulation rules
     *
     * @param individuals Current individual list of the society
     * @param individual  Person to check
     * @param direction   Direction to check
     * @return True if individual can make the move
     */
    public static boolean canIndividualMoveInGivenDirection(ArrayList<Individual> individuals, Individual individual, Common.Direction direction) {
        int minX, maxX, minY, maxY;
        switch (direction) {
            case LEFT:
                if (individual.getCoordinateX() - individual.getSpeed() < 0)
                    return false;
                minX = individual.getCoordinateX() - individual.getSpeed() - (Common.CANVAS_BOX_XY * 2);
                minY = individual.getCoordinateY() - Common.CANVAS_BOX_XY;
                maxX = individual.getCoordinateX() - individual.getSpeed() + Common.CANVAS_BOX_XY;
                maxY = individual.getCoordinateY() + Common.CANVAS_BOX_XY;
                break;
            case RIGHT:
                if (individual.getCoordinateX() + individual.getSpeed() > Common.CANVAS_X)
                    return false;
                minX = individual.getCoordinateX() + individual.getSpeed() - Common.CANVAS_BOX_XY;
                minY = individual.getCoordinateY() - Common.CANVAS_BOX_XY;
                maxX = individual.getCoordinateX() + individual.getSpeed() + (Common.CANVAS_BOX_XY * 3);
                maxY = individual.getCoordinateY() + Common.CANVAS_BOX_XY;
                break;
            case UP:
                if (individual.getCoordinateY() - individual.getSpeed() < 0)
                    return false;
                minX = individual.getCoordinateX() - Common.CANVAS_BOX_XY;
                minY = individual.getCoordinateY() - individual.getSpeed() - (Common.CANVAS_BOX_XY * 2);
                maxX = individual.getCoordinateX() + Common.CANVAS_BOX_XY;
                maxY = individual.getCoordinateY() - individual.getSpeed() + Common.CANVAS_BOX_XY;
                break;
            case DOWN:
                if (individual.getCoordinateY() + individual.getSpeed() > Common.CANVAS_Y)
                    return false;
                minX = individual.getCoordinateX() - Common.CANVAS_BOX_XY;
                minY = individual.getCoordinateY() + individual.getSpeed();
                maxX = individual.getCoordinateX() + Common.CANVAS_BOX_XY;
                maxY = individual.getCoordinateY() + individual.getSpeed() + (Common.CANVAS_BOX_XY * 3);
                break;
            default:
                throw new IllegalStateException("canIndividualMoveInCurrentDirection()::Unexpected value: " + individual.getDirection());
        }
        //System.out.println("DEBUG::canIndividualMoveInCurrentDirection()::[" + individual.getID() + "]:(" + individual.getCoordinateX() + "," + individual.getCoordinateY() + "): minX:" + minX + ", minY:" + minY + ", maxX:" + maxX + ", maxY:" + maxY);
        for (Individual person : individuals) {
            if (person.getID() != individual.getID()) {
                if ((person.getCoordinateX() > minX && person.getCoordinateX() < maxX)
                        && (person.getCoordinateY() > minY && person.getCoordinateY() < maxY))
                    return false;
            }
        }
        return true;
    }

    /**
     * Checks given individuals are in social distance or not
     *
     * @param first  One of the individuals to check
     * @param second Other individual to check
     * @return True if they are in social distance of each other
     */
    public static boolean areGivenIndividualsInSocialDistance(Individual first, Individual second) {
        int socialDistance = Math.min(first.getSocialDistance(), second.getSocialDistance());
        int minX = first.getCoordinateX() - socialDistance - Common.CANVAS_BOX_XY;
        int minY = first.getCoordinateY() - socialDistance - Common.CANVAS_BOX_XY;
        int maxX = first.getCoordinateX() + socialDistance + (Common.CANVAS_BOX_XY * 2);
        int maxY = first.getCoordinateY() + socialDistance + (Common.CANVAS_BOX_XY * 2);
        return (second.getCoordinateX() > minX && second.getCoordinateX() < maxX)
                && (second.getCoordinateY() > minY && second.getCoordinateY() < maxY);
    }

    /**
     * Collects list of collisions
     *
     * @param individuals Current individual list of the society
     * @return List of collisions, each collision is an array of individuals with the size of 2
     */
    public static ArrayList<Individual[]> getListOfCollisions(ArrayList<Individual> individuals) {
        ArrayList<Individual[]> result = new ArrayList<>();
        for (Individual person : individuals) {
            if (!person.isInInteraction()) {
                for (Individual other : individuals) {
                    if (!other.isInInteraction()) {
                        if (areGivenIndividualsInSocialDistance(person, other) && person.getID() != other.getID()) {
                            //System.out.println("DEBUG::getListOfCollisions()::[" + person.getID() + "," + other.getID() + "]");
                            result.add(new Individual[]{person, other});
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Handles collision mechanism for the given individuals with given disease parameters
     *
     * @param left      One of the individuals in collision
     * @param right     Other individual in collision
     * @param disease   Simulation disease
     * @param iteration Iteration of the simulation when method called
     */
    public static void handleCollision(Individual left, Individual right, Disease disease, int iteration) {
        if (!left.isInInteraction() && !right.isInInteraction()) {
            int stayTogetherTime = Math.max(left.getInteractionTime(), right.getInteractionTime());
            left.setInInteraction(true);
            left.setInteractedUntil(stayTogetherTime + iteration);
            right.setInInteraction(true);
            right.setInteractedUntil(stayTogetherTime + iteration);
            if ((left.isInfected() && !right.isInfected()) || (!left.isInfected() && right.isInfected())) {
                Individual infected = right, healthy = left;
                if (left.isInfected()) {
                    infected = left;
                    healthy = right;
                }
                int socialDistance = Math.min(left.getSocialDistance(), right.getSocialDistance());
                double infectionProbability = Math.min((disease.getSpreadingFactor() * (1.0 + (stayTogetherTime / 10.0)) * infected.getMaskFactor() * healthy.getMaskFactor() * (1.0 - (socialDistance / 10.0))), 1);
                double randomChance = (RANDOM.nextInt(10) + 1) / 10.0;
                //System.out.println("DEBUG::collision()::infectionProbability from " + infected.getID() + " to " + healthy.getID() + ": " + infectionProbability + ", randomChance:" + randomChance);
                if (infectionProbability >= randomChance) {
                    healthy.setInfected(true);
                    healthy.setInfectedAt(iteration);
                }
            }
        }
    }

    /**
     * Check infection time of the infected individual
     *
     * @param infected  Infected individual
     * @param disease   Simulation disease
     * @param iteration Iteration of the simulation when method called
     */
    public static void checkInfectionTime(Individual infected, Disease disease, int iteration) {
        int infectionMortalityTime = (int) (100 * (1.0 - disease.getMortalityRate()));
        if (iteration > infectionMortalityTime + infected.getInfectedAt())
            infected.setAlive(false);
    }

    /**
     * Check infection time of all individuals in the society
     *
     * @param individuals Current individual list of the society
     * @param disease     Simulation disease
     * @param iteration   Iteration of the simulation when method called
     */
    public static void checkInfectionTimeForAll(ArrayList<Individual> individuals, Disease disease, int iteration) {
        for (Individual person : individuals) {
            if (person.isInfected()) {
                checkInfectionTime(person, disease, iteration);
            }
        }
    }

    /**
     * Hospitalize given individual to given hospital
     *
     * @param individual Infected individual
     * @param hospital   Simulation hospital
     * @param iteration  Iteration of the simulation when method called
     */
    public static void hospitalizeIndividual(Individual individual, Hospital hospital, int iteration) {
        System.out.println("\tDEBUG::iteration:" + iteration + ", hospitalizeIndividual()::called.");
        System.out.println("\tDEBUG::iteration:" + iteration + ", hospitalizeIndividual()::" + individual.toString());
        individual.setHospitalized(true);
        individual.setHospitalizedAt(iteration);
        hospital.attach(individual);
        System.out.println("\tDEBUG::iteration:" + iteration + ", hospitalize()::finished.");

    }

    /**
     * Discharge given individual from given hospital
     *
     * @param individual Healthy individual from hospital
     * @param hospital   Simulation hospital
     * @param iteration  Iteration of the simulation when method called
     */
    public static void dischargeIndividual(Individual individual, Hospital hospital, int iteration) {
        System.out.println("\tDEBUG::iteration:" + iteration + ", discharge()::called.");
        System.out.println("\tDEBUG::iteration:" + iteration + ", discharge()::" + individual.toString());
        individual.setHospitalized(false);
        individual.setHospitalizedAt(0);
        hospital.remove(individual);
        System.out.println("\tDEBUG::iteration:" + iteration + ", discharge()::finished.");
    }

    /**
     * Check infection time of the given individual and hospitalize if needed
     *
     * @param individual Infected individual from the society
     * @param hospital   Simulation hospital
     * @param iteration  Iteration of the simulation when method called
     */
    public static void checkHospitalTime(Individual individual, Hospital hospital, int iteration) {
        if (!individual.isHospitalized() && iteration > individual.getInfectedAt() + Common.TO_HOSPITAL_SEC)
            individual.hospitalize(hospital, iteration);
    }

    /**
     * Check infection time of every individual in the given list
     *
     * @param individuals Current individual list of the society
     * @param hospital    Simulation hospital
     * @param iteration   Iteration of the simulation when method called
     */
    public static void checkHospitalTimeForAll(ArrayList<Individual> individuals, Hospital hospital, int iteration) {
        for (Individual person : individuals) {
            if (person.isAlive() && person.isInfected() && hospital.isThereAnyFreeVentilator()) {
                checkHospitalTime(person, hospital, iteration);
            }
        }
    }
}
