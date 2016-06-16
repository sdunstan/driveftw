package com.stevedunstan.driveftw.calculation;

import android.support.annotation.NonNull;

import com.stevedunstan.driveftw.DriveTelementry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by computer on 6/15/2016.
 */
public class AchievementCalc  {

   List<DriveTelementry> driveTelemetry;
    List<Achievement> achievements;
    DriveTelementry maxSpeed;
    DriveTelementry minSpeed;
    DriveTelementry maxThrottle;
    DriveTelementry minThrottle;


    List<Achievement> getAchievements(List<DriveTelementry> driveTelementry){
        List<Achievement> achievements = new ArrayList<Achievement>();

            maxSpeed = Collections.max(driveTelementry,new SpeedComparator());
            minSpeed = Collections.min(driveTelementry, new SpeedComparator());
            minThrottle = Collections.min(driveTelementry,new ThrottleComparator());
            maxThrottle = Collections.max(driveTelementry, new ThrottleComparator());

        if(maxSpeed.getSpeed() > 55 ){
            achievements.add(Achievement.SPEED_DEMON);
        }
        if(maxSpeed.getSpeed() <5)
        {
            achievements.add(Achievement.TURTLE);
        }
        if(minThrottle.getThrottle_position() > 60 &&minThrottle.getThrottle_position() <90)
        {
            achievements.add(Achievement.LEADFOOT);
        }
        if(minThrottle.getThrottle_position() > 90 )
        {
            achievements.add(Achievement.PEDALTOTHEMETAL);
        }
        if(maxThrottle.getThrottle_position()< 3){
            achievements.add(Achievement.FEATHERFOOT);
        }
        if(maxSpeed.getSpeed() <5 && maxThrottle.getThrottle_position() < 3){
            achievements.add(Achievement.PARKINGLOT);
        }
        if(maxSpeed.getSpeed()>0){
            achievements.add(Achievement.SEATBELT);
        }

        return achievements;
    }


}
