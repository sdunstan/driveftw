package com.stevedunstan.driveftw;

import android.content.Context;
import android.content.SharedPreferences;

import com.stevedunstan.driveftw.bluetooth.DeviceManager;

import java.util.HashMap;
import java.util.Map;

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

    private Context context;

    private static String EST_MAINT_COST_PER_YEAR = "estMaintCostsPerYear";
    private static String INSURANCE_COST_PER_YEAR = "insuranceCostsPerYear";
    private static String EST_MILES_PER_YEAR = "estMilesPerYear";
    private static String TOTAL_OPERATING_COST_PER_YEAR = "totalOperatingCostperYear";
    private static String EST_YEARS_KEEP = "estYearsKeep";
    private static String COST_OF_VEHICLE = "costOfVehicle";
    private static String USER_NAME = "userName";

    public EconomicData(){}
    public EconomicData(Context ctx){this.context = ctx;}

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

    public Map<String, Float> retrieveSharedPrefValues(){
        Map<String, Float> result = new HashMap<String, Float>();
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(DeviceManager.DRIVE_FTW_SHARED_PREFERENCES, 0);
        result.put(EST_MAINT_COST_PER_YEAR, sharedPreferences.getFloat(EST_MAINT_COST_PER_YEAR, 0));
        result.put(INSURANCE_COST_PER_YEAR, sharedPreferences.getFloat(INSURANCE_COST_PER_YEAR, 0));
        result.put(TOTAL_OPERATING_COST_PER_YEAR, sharedPreferences.getFloat(TOTAL_OPERATING_COST_PER_YEAR, 0));
        result.put(COST_OF_VEHICLE, sharedPreferences.getFloat(COST_OF_VEHICLE, 0));

        result.put(EST_MILES_PER_YEAR, sharedPreferences.getFloat(EST_MILES_PER_YEAR, 0));
        result.put(EST_YEARS_KEEP, sharedPreferences.getFloat(EST_YEARS_KEEP, 0));
        return result;
    }
    public float getTotalCostForOwnership(){
        Map<String, Float> storedPrefVals = retrieveSharedPrefValues();
        float yearlies = storedPrefVals.get(EST_MAINT_COST_PER_YEAR) +
                storedPrefVals.get(INSURANCE_COST_PER_YEAR) +
                storedPrefVals.get(TOTAL_OPERATING_COST_PER_YEAR);
        return (yearlies * storedPrefVals.get(EST_YEARS_KEEP)) + storedPrefVals.get(COST_OF_VEHICLE);
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
