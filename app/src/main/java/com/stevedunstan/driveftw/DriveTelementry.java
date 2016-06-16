package com.stevedunstan.driveftw;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by M on 6/15/2016.
 */
public class DriveTelementry implements Parcelable {



    private int revPerMinute;
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

    public int getRevPerMinute() {
        return revPerMinute;
    }

    public void setRevPerMinute(int revPerMinute) {
        this.revPerMinute = revPerMinute;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.revPerMinute);
        dest.writeFloat(this.throttle_position);
        dest.writeFloat(this.speed);
        dest.writeFloat(this.engineLoad);
        dest.writeString(this.engineRunTime);
        dest.writeFloat(this.fuelLevel);
    }

    public DriveTelementry() {
    }

    protected DriveTelementry(Parcel in) {
        this.revPerMinute = in.readInt();
        this.throttle_position = in.readFloat();
        this.speed = in.readFloat();
        this.engineLoad = in.readFloat();
        this.engineRunTime = in.readString();
        this.fuelLevel = in.readFloat();
    }

    public static final Parcelable.Creator<DriveTelementry> CREATOR = new Parcelable.Creator<DriveTelementry>() {
        @Override
        public DriveTelementry createFromParcel(Parcel source) {
            return new DriveTelementry(source);
        }

        @Override
        public DriveTelementry[] newArray(int size) {
            return new DriveTelementry[size];
        }
    };
}
