package com.stevedunstan.driveftw;

import com.github.pires.obd.commands.ObdCommand;

import java.util.Map;

/**
 * Created by M on 6/15/2016.
 */
public class DriveTelementry {



    private float revPerMinute;
    private float throttle_position;
    private float speed;
    private float engineLoad;
    private String engineRunTime;
    private float fuelLevel;

    public float getThrottle_position() {
        return throttle_position;
    }

    public void setThrottle_position(float throttle_position) {
        this.throttle_position = throttle_position;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getEngineLoad() {
        return engineLoad;
    }

    public void setEngineLoad(float engineLoad) {
        this.engineLoad = engineLoad;
    }

    public String getEngineRunTime() {
        return engineRunTime;
    }

    public void setEngineRunTime(String engineRunTime) {
        this.engineRunTime = engineRunTime;
    }

    public float getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(float fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public float getRevPerMinute() {
        return revPerMinute;
    }

    public void setRevPerMinute(float revPerMinute) {
        this.revPerMinute = revPerMinute;
    }
}
