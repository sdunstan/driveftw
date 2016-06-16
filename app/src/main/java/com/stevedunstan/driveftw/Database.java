package com.stevedunstan.driveftw;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Database {

    private DatabaseReference dbReference;

    public Database() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
    }

    public void saveLogData(DriveTelemetry telemetry) {
        String logKey = dbReference.child("log").push().getKey();

        Map<String,Object> logMap = new HashMap<>();
        logMap.put("log/" + logKey, telemetry.toMap());

        dbReference.updateChildren(logMap);
    }

    public void saveDrive(Drive drive) {
        String driveKey = dbReference.child("drives").push().getKey();

        Map<String,Object> drivesMap = new HashMap<>();
        drivesMap.put("drives/" + driveKey, drive);
        
        dbReference.updateChildren(drivesMap);
    }

    // TODO: rip this out when this object is ready
    private class DriveTelemetry {
        public Map<String, Object> toMap() {
            return null;
        }
    }

}
