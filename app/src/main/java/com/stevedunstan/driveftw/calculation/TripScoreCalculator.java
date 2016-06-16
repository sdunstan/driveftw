package com.stevedunstan.driveftw.calculation;

import com.stevedunstan.driveftw.DriveTelementry;
import com.stevedunstan.driveftw.EconomicData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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


    public int getTripScore(EconomicData ed, List<DriveTelementry> telemetries){
        return tabulateFromList(telemetries);
    }

    private int tabulateFromList(List<DriveTelementry> telemetries){
        /*
        THROTTLE_POSITION = 14.1%
        RPM = 755RPM
        ENGINE_LOAD = 34.9%
        SPEED = 0km/h
        ENGINE_RUNTIME = 00:01:53
        FUEL_LEVEL = 82.0%
        */
        int score = 0;
        float revsPerMinuteCollector = 0;
        float revsPerMinuteAvg = .00001f;
        float speedCollector = .00001f;
        float speedAvg = 0;

        if(0 < telemetries.size()) {
            for (DriveTelementry t : telemetries) {
                revsPerMinuteCollector += t.getRevPerMinute();
                speedCollector += t.getSpeed();
            }

            revsPerMinuteAvg = revsPerMinuteCollector / telemetries.size();
            speedAvg = speedCollector / telemetries.size();

            //convert rpm's to revs per hour
            //divide revs/h by miles/hr
            //to convert so that higher score is better, use quotient as divisor
            score = Math.round( 100000 / ( (revsPerMinuteAvg * 60) / speedAvg ) );

        }

        return score;


    }
}
