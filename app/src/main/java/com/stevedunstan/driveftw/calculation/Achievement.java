package com.stevedunstan.driveftw.calculation;

/**
 * Created by computer on 6/15/2016.
 */
public enum Achievement {
        SPEED_DEMON("1","Speed Demon",5,100,"When a user drove faster than 55mph"),
        TURTLE("2", "Turtle",5,50,"When a user drove less than 5mph"),
        FEATHERFOOT("2","Feather foot",5,50,"When a user drove with the throttle position less than 3"),
        LEADFOOT("3", "Lead foot",15,150,"When a user drove with the throttle position grater than 60 and less than 90"),
        PEDALTOTHEMETAL("4", "Pedal to the Metal",10,200,"When a user drove with the throttle position less than 3"),
        PARKINGLOT("5", "Parking Lot!",10,50,"When a user drove with the throttle position less than 3 and a spee less than 5"),
        SEATBELT("6", "Locked and Loaded", 10,5,"When seatbelt is used");
    public String id;
    public String name;
    public int points;
    public int xp;
    public String description;
Achievement(String id, String name, int points, int xp, String description){
    id = id; //might be machine generated
    name = name;
    points = points;
    xp = xp;
    description= description;


}
    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getXp() {
        return xp;
    }

    public String getDescription() {
        return description;
    }

}
