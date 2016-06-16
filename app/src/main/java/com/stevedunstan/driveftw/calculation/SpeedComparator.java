package com.stevedunstan.driveftw.calculation;

import com.stevedunstan.driveftw.DriveTelementry;

import java.util.Comparator;

/**
 * Created by computer on 6/15/2016.
 */
public class SpeedComparator implements Comparator<DriveTelementry> {

    @Override
    public int compare(DriveTelementry lhs, DriveTelementry rhs) {
        Float leftSpeed = new Float(lhs.getSpeed());
        Float rightSpeed = new Float(rhs.getSpeed());
        return leftSpeed.compareTo(rightSpeed);
    }

}
