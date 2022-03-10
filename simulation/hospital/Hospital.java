package simulation.hospital;

import simulation.Common;
import simulation.individual.Individual;

import java.util.ArrayList;

/**
 * Hospital entity, Publisher in Pub/Sub design
 */
public class Hospital {
    /**
     * List of individuals that are hospitalized
     */
    private final ArrayList<Individual> patients;
    /**
     * Ventilator count of the hospital
     */
    private int ventilatorCount;

    // Constructors
    public Hospital(int population) {
        ventilatorCount = population / 100;
        patients = new ArrayList<>();
    }

    // Getters and Setters
    public int getPatientCount() {
        return patients.size();
    }

    public int getVentilatorCount() {
        return ventilatorCount;
    }

    /**
     * Perform hospital stuff on patients
     *
     * @param population current population of the society
     * @param iteration  Iteration of the simulation when method called.
     */
    public void handlePatients(int population, int iteration) {
        int updatedVentilatorCount = population / 100;
        if (updatedVentilatorCount > ventilatorCount)
            ventilatorCount = updatedVentilatorCount;
        for (Individual patient : patients)
            curePatient(patient);
        /*
        for (Individual patient : patients)
            System.out.println("\tDEBUG::iteration:" + iteration + ", handlePatients()::patient::" + patient.toString());
        */
        notifyAllObservers(iteration);
    }

    /**
     * Add this individual as a patient
     *
     * @param patient Incoming patient
     */
    public void attach(Individual patient) {
        patients.add(patient);
    }

    /**
     * Remove this patient from hospital
     *
     * @param patient Outgoing patient
     */
    public void remove(Individual patient) {
        patients.remove(patient);
    }

    /**
     * Notify all patients about new iteration, discharge them if needed
     *
     * @param iteration Iteration of the simulation when method called.
     */
    public void notifyAllObservers(int iteration) {
        ArrayList<Individual> dischargeList = new ArrayList<>();
        for (Individual patient : patients)
            if ((!patient.isAlive()) || (iteration > patient.getHospitalizedAt() + Common.AT_HOSPITAL_SEC))
                dischargeList.add(patient);
        for (Individual patient : dischargeList)
            patient.discharge(this, iteration);
    }

    /**
     * Perform stuff to cure patient
     *
     * @param patient Patient
     */
    private void curePatient(Individual patient) {
        patient.setInfected(false);
        patient.setInfectedAt(0);
    }

    /**
     * Is there any available ventilator right now
     *
     * @return true if hospital can accept new patients
     */
    public boolean isThereAnyFreeVentilator() {
        return this.getVentilatorCount() > this.getPatientCount();
    }
}
