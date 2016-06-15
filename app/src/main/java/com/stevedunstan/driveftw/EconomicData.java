package com.stevedunstan.driveftw;

import android.content.Context;
import android.content.SharedPreferences;

import com.stevedunstan.driveftw.bluetooth.DeviceManager;

/**
 * Created by computer on 6/15/2016.
 */

public class EconomicData {
    //cost estimates
    public float estMaintCostsPerYear;
    public float insuranceCostsPerYear;
    public float estMilesPerYear;
    public float totalOperatingCostperYear;
    public float estYearsKeep;
    public float costOfVehicle;
    public String userName;

    private static String EST_MAINT_COST_PER_YEAR = "estMaintCostsPerYear";
    private static String INSURANCE_COST_PER_YEAR = "insuranceCostsPerYear";
    private static String EST_MILES_PER_YEAR = "estMilesPerYear";
    private static String TOTAL_OPERATING_COST_PER_YEAR = "totalOperatingCostperYear";
    private static String EST_YEARS_KEEP = "estYearsKeep";
    private static String COST_OF_VEHICLE = "costOfVehicle";
    private static String USER_NAME = "userName";

    public void saveEconData(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(DeviceManager.DRIVE_FTW_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(EST_MAINT_COST_PER_YEAR, estMaintCostsPerYear);
        editor.putFloat(INSURANCE_COST_PER_YEAR, insuranceCostsPerYear);
        editor.putFloat(EST_MILES_PER_YEAR, estMilesPerYear);
        editor.putFloat(TOTAL_OPERATING_COST_PER_YEAR, totalOperatingCostperYear);
        editor.putFloat(EST_YEARS_KEEP, estYearsKeep);
        editor.putFloat(COST_OF_VEHICLE, costOfVehicle);

        editor.commit();
    }
    public void saveUserName(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(DeviceManager.DRIVE_FTW_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public float getEstMaintCostsPerYear() {
        return estMaintCostsPerYear;
    }

    public void setEstMaintCostsPerYear(float estMaintCostsPerYear) {
        this.estMaintCostsPerYear = estMaintCostsPerYear;
    }

    public float getInsuranceCostsPerYear() {
        return insuranceCostsPerYear;
    }

    public void setInsuranceCostsPerYear(float insuranceCostsPerYear) {
        this.insuranceCostsPerYear = insuranceCostsPerYear;
    }

    public float getEstMilesPerYear() {
        return estMilesPerYear;
    }

    public void setEstMilesPerYear(float estMilesPerYear) {
        this.estMilesPerYear = estMilesPerYear;
    }

    public float getTotalOperatingCostperYear() {
        return totalOperatingCostperYear;
    }

    public void setTotalOperatingCostperYear(float totalOperatingCostperYear) {
        this.totalOperatingCostperYear = totalOperatingCostperYear;
    }

    public float getEstYearsKeep() {
        return estYearsKeep;
    }

    public void setEstYearsKeep(float estYearsKeep) {
        this.estYearsKeep = estYearsKeep;
    }

    public float getCostOfVehicle() {
        return costOfVehicle;
    }

    public void setCostOfVehicle(float costOfVehicle) {
        this.costOfVehicle = costOfVehicle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
