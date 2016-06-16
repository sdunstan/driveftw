package com.stevedunstan.driveftw.calculation;

import com.stevedunstan.driveftw.DriveTelementry;
import com.stevedunstan.driveftw.EconomicData;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ea7m on 6/15/2016.
 */
public class TripScoreCalculator {

    public static final String TRIP_COST = "tripCost";
    public static final String TRIP_SCORE = "tripScore";
    //retrieve econ info from shared prefs
    //retrieve the trip info

    public Map<String, Object> calcScores(List<DriveTelementry> telemetries){
        EconomicData ed = new EconomicData();
        int tripScore = 0;
        BigDecimal tripCost = new BigDecimal("0");
        float revsPerMinuteCollector = 0;
        float revsPerMinuteAvg = .00001f;
        float speedCollector = .00001f;
        float speedAvg = 0;

        String maxEngineRunTime = "00:00:00";

        if(0 < telemetries.size()) {
            for (DriveTelementry t : telemetries) {
                revsPerMinuteCollector += t.getRevPerMinute();
                speedCollector += t.getSpeed();
                maxEngineRunTime = getGreaterString(maxEngineRunTime, t.getEngineRunTime());
            }

            revsPerMinuteAvg = revsPerMinuteCollector / telemetries.size();
            speedAvg = speedCollector / telemetries.size();

            //convert rpm's to revs per hour
            //divide revs/h by miles/hr
            //to convert so that higher score is better, use quotient as divisor
            tripScore = Math.round( 100000 / ( (revsPerMinuteAvg * 60) / speedAvg ) );

            //calculate the trip cost
            if(!"00:00:00".equals(maxEngineRunTime)){
                float miles = calcEstMiles(maxEngineRunTime, speedAvg);
                double masterMultiplier = (miles / (ed.getEstMilesPerYear()* ed.getEstYearsKeep()) );
                BigDecimal ownerCost = new BigDecimal(ed.getTotalCostForOwnership());

                tripCost = BigDecimal.valueOf(masterMultiplier * ownerCost.doubleValue());
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(TRIP_SCORE, Integer.valueOf(tripScore));
        result.put(TRIP_COST, tripCost);
        return result;
    }

    private float calcEstMiles(String runTime, float avgSpeed){
        long runTimeSecs = convertAllToSeconds(runTime);
        float milesPerSecond = avgSpeed/60/60;
        float result = runTimeSecs * milesPerSecond;
        return result;
    }
    private String getGreaterString(String currentMaxString, String contenderString){
        long currentMax = convertAllToSeconds(currentMaxString);
        long contender = convertAllToSeconds(contenderString);
        if(contender>=currentMax){
            return contenderString;
        }else{
            return currentMaxString;
        }
    }
    private long convertAllToSeconds(String time){
        String[] parts = time.split(":");
        long result =
                (Long.getLong(parts[0]) * 60 * 60) +
                (Long.getLong(parts[1]) * 60) +
                Long.getLong(parts[2]);
        return result;
    }

    public BigDecimal getTripCost(EconomicData ed){
        BigDecimal dollarsForTrip = null;
        double miles = 10.0; //retrieve when ready
        double lifeTimeMiles = 150000.0; //retrieve when ready

        double masterMultiplier = (miles / lifeTimeMiles);
        BigDecimal ownerCost = new BigDecimal(ed.getTotalCostForOwnership());

        dollarsForTrip = BigDecimal.valueOf(masterMultiplier * ownerCost.doubleValue()); //retrieve when ready

        return dollarsForTrip;
    }


    public int getTripScore(List<DriveTelementry> telemetries){
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
