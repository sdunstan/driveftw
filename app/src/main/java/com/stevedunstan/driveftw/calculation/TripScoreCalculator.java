package com.stevedunstan.driveftw.calculation;

import com.stevedunstan.driveftw.EconomicData;

import java.math.BigDecimal;

/**
 * Created by ea7m on 6/15/2016.
 */
public class TripScoreCalculator {

    //retrieve econ info from shared prefs
    //retrieve the trip info


    public BigDecimal getTripCost(EconomicData ed){
        BigDecimal dollarsForTrip = null;
        double miles = 10.0; //retrieve when ready
        double lifeTimeMiles = 150000.0; //retrieve when ready

        double masterMultiplier = (miles / lifeTimeMiles);
        BigDecimal ownerCost = new BigDecimal(ed.getTotalCostForOwnership());

        dollarsForTrip = BigDecimal.valueOf(masterMultiplier * ownerCost.doubleValue()); //retrieve when ready

        return dollarsForTrip;
    }

    /*
    public int getTripScore(EconomicData ed){
        int scoreForTrip;
        int accumulatedScoreTotal;
        int tripsCounted;
        //calculate mean of the accumulated scores
        float meanScore = accumulatedScoreTotal/tripsCounted;
        score =
        return scoreForTrip;
    }
    */
    public int getTripScore(EconomicData ed){
        return 97;
    }
}
