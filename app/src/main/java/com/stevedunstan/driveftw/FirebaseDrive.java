package com.stevedunstan.driveftw;

import com.google.firebase.database.Exclude;
import com.stevedunstan.driveftw.calculation.Achievement;

import java.math.BigDecimal;
import java.util.List;

public class FirebaseDrive {
    private String driveCost;
    private int driveScore;
    private List<String> achievements;

    public String getDriveCost() {
        return driveCost;
    }

    public void setDriveCost(String driveCost) {
        this.driveCost = driveCost;
    }

    public int getDriveScore() {
        return driveScore;
    }

    public void setDriveScore(int driveScore) {
        this.driveScore = driveScore;
    }

    public List<String> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<String> achievements) {
        this.achievements = achievements;
    }

    @Exclude
    public Drive toDrive() {
        Drive drive = new Drive();
        drive.setDriveScore(getDriveScore());
        drive.setDriveCost(new BigDecimal(getDriveCost()));
        for(String achievement : getAchievements()) {
            Achievement anAchievement = Achievement.valueOf(achievement);
            drive.getAchievements().add(anAchievement);
        }
        return drive;
    }
}
