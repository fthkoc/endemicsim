package simulation.disease;

/**
 * Disease entity
 */
public class Disease {
    /**
     * constant spreading factor R in [0.5, 1.0]
     */
    private final double SpreadingFactor;
    /**
     * constant mortality rate Z in [0.1, 0.9]
     */
    private final double MortalityRate;

    public Disease(double spreadingFactor, double mortalityRate) {
        SpreadingFactor = spreadingFactor;
        MortalityRate = mortalityRate;
    }

    public double getSpreadingFactor() {
        return SpreadingFactor;
    }

    public double getMortalityRate() {
        return MortalityRate;
    }

    @Override
    public String toString() {
        return "Disease{" + "SpreadingFactor=" + SpreadingFactor + ", MortalityRate=" + MortalityRate + "}";
    }
}
