package com.stevedunstan.driveftw;

import android.os.Parcel;
import android.os.Parcelable;

import com.stevedunstan.driveftw.calculation.Achievement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Drive implements Parcelable {

    private BigDecimal driveCost;
    private int driveScore;
    private List<Achievement> achievements;

    public BigDecimal getDriveCost() {
        return driveCost;
    }

    public void setDriveCost(BigDecimal driveCost) {
        this.driveCost = driveCost;
    }

    public int getDriveScore() {
        return driveScore;
    }

    public void setDriveScore(int driveScore) {
        this.driveScore = driveScore;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.driveCost);
        dest.writeInt(this.driveScore);
        dest.writeList(this.achievements);
    }

    public Drive() {
    }

    protected Drive(Parcel in) {
        this.driveCost = (BigDecimal) in.readSerializable();
        this.driveScore = in.readInt();
        this.achievements = new ArrayList<Achievement>();
        in.readList(this.achievements, Achievement.class.getClassLoader());
    }

    public static final Parcelable.Creator<Drive> CREATOR = new Parcelable.Creator<Drive>() {
        @Override
        public Drive createFromParcel(Parcel source) {
            return new Drive(source);
        }

        @Override
        public Drive[] newArray(int size) {
            return new Drive[size];
        }
    };

}
