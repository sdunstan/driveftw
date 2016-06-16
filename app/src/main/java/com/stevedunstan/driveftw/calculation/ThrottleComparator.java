package com.stevedunstan.driveftw.calculation;

import com.stevedunstan.driveftw.DriveTelementry;

import java.util.Comparator;

/**
 * Created by computer on 6/15/2016.
 */
public class ThrottleComparator implements Comparator<DriveTelementry> {

    @Override
    public int compare(DriveTelementry lhs, DriveTelementry rhs) {
        Float leftTPS = new Float(lhs.getThrottle_position());
        Float rightTPS = new Float(rhs.getThrottle_position());
        return leftTPS.compareTo(rightTPS);
    }

}