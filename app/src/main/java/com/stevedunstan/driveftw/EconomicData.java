package com.stevedunstan.driveftw;

/**
 * Created by computer on 6/15/2016.
 *

 //    Estimated yearly maintenance cost (tires, oil changes, etc.)  Cost of vehicle  Number of years you will keep it

 //
 */
public class EconomicData {
    //cost estimates
    double estMaintCostsPerYear;
    double insuranceCostsPerYear;
    double estMilesPerYear;
    double totalOperatingCostperYear;
    double estYearsKeep;
    double costOfVehicle;


    public double getEstMaintCostsPerYear() {
        return estMaintCostsPerYear;
    }

    public void setEstMaintCostsPerYear(double estMaintCostsPerYear) {
        this.estMaintCostsPerYear = estMaintCostsPerYear;
    }

    public double getInsuranceCostsPerYear() {
        return insuranceCostsPerYear;
    }

    public void setInsuranceCostsPerYear(double insuranceCostsPerYear) {
        this.insuranceCostsPerYear = insuranceCostsPerYear;
    }

    public double getEstMilesPerYear() {
        return estMilesPerYear;
    }

    public void setEstMilesPerYear(double estMilesPerYear) {
        this.estMilesPerYear = estMilesPerYear;
    }

    public double getTotalOperatingCostperYear() {
        return totalOperatingCostperYear;
    }

    public void setTotalOperatingCostperYear(double totalOperatingCostperYear) {
        this.totalOperatingCostperYear = totalOperatingCostperYear;
    }

    public double getEstYearsKeep() {
        return estYearsKeep;
    }

    public void setEstYearsKeep(double estYearsKeep) {
        this.estYearsKeep = estYearsKeep;
    }

    public double getCostOfVehicle() {
        return costOfVehicle;
    }

    public void setCostOfVehicle(double costOfVehicle) {
        this.costOfVehicle = costOfVehicle;
    }
}
