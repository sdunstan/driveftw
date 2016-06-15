package com.stevedunstan.driveftw.calculation;

import java.math.BigDecimal;

/**
 * Created by ea7m on 6/15/2016.
 */
public class TripScoreCalculator {

    //retrieve econ info from shared prefs
    //retrieve the trip info

    public static BigDecimal getTripScore(){
        BigDecimal dollarsForTrip = null;
        double miles = 10.0; //retrieve when ready
        double lifeTimeMiles = 150000.0; //retrieve when ready

        double masterMultiplier = (miles / lifeTimeMiles);
        BigDecimal ownerCost = new BigDecimal("35000");

        dollarsForTrip = BigDecimal.valueOf(masterMultiplier * ownerCost.doubleValue()); //retrieve when ready

        return dollarsForTrip;
    }
}
