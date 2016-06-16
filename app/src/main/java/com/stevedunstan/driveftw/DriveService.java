package com.stevedunstan.driveftw;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.engine.LoadCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.engine.RuntimeCommand;
import com.github.pires.obd.commands.engine.ThrottlePositionCommand;
import com.github.pires.obd.commands.fuel.ConsumptionRateCommand;
import com.github.pires.obd.commands.fuel.FuelLevelCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.stevedunstan.driveftw.calculation.Achievement;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DriveService extends IntentService {
    public static final String DRIVE_TELEMETRY_EVENT = "com.stevedunstan.driveftw.action.DRIVE_TELEMETRY";
    public static final String DRIVE_TELEMETRY_STOPPED_EVENT = "com.stevedunstan.driveftw.action.DRIVE_TELEMETRY_STOPPED";
    public static final String DRIVE_TELEMETRY_ERROR = "ERROR";

    private static final String ACTION_START_DRIVE = "com.stevedunstan.driveftw.action.START_DRIVE";
    private static final String EXTRA_ODB2_ADDRESS = "com.stevedunstan.driveftw.extra.ADDRESS";
    private static final String TAG = DriveService.class.getName();
    public static final String RPM = "RPM";
    public static final String ENGINE_RUNTIME= "ENGINE_RUNTIME";
    public static final String SPEED = "SPEED";
    public static final String THROTTLE_POSITION = "THROTTLE_POSITION";
    public static final String ENGINE_LOAD = "ENGINE_LOAD";
    public static final String FUEL_LEVEL="FUEL_LEVEL";
    public static final String FUEL_CONSUMPTION = "FUEL_CONSUMPTION";
    private static final int NOTIFICATION_ID = 31415;


    public DriveService() {
        super("DriveService");
    }

    public static void startActionDrive(Context context, String bluetoothAddress) {
        Intent intent = new Intent(context, DriveService.class);
        intent.setAction(ACTION_START_DRIVE);
        intent.putExtra(EXTRA_ODB2_ADDRESS, bluetoothAddress);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_DRIVE.equals(action)) {
                final String bluetoothAddress = intent.getStringExtra(EXTRA_ODB2_ADDRESS);
                handleActionStartDrive(bluetoothAddress);
            }
        }
    }

    private void handleActionStartDrive(String bluetoothAddress) {
        BluetoothSocket socket = null;
        try {
            Log.d(TAG, "Getting BT adapter.");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Log.d(TAG, "Getting BT device at address " + bluetoothAddress);
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothAddress);

            // well known bluetooth UUID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            Log.d(TAG, "creating socket at well known UUID");
            socket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

            Log.d(TAG, "connecting to BT socket");
            socket.connect();
            initializeODB2(socket);
            collectTelemetry(socket);
        }
        catch (Throwable throwable) {
            reportFatalError(throwable); // restart?
        }
        finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void initializeODB2(BluetoothSocket socket) throws IOException, InterruptedException {
        Log.d(TAG, "Setting echo off");
        sendCommand(new EchoOffCommand(), socket, null, null);
        Log.d(TAG, "Setting line feed off");
        new LineFeedOffCommand().run(socket.getInputStream(), socket.getOutputStream());
        Log.d(TAG, "Setting timeout");
        new TimeoutCommand(250).run(socket.getInputStream(), socket.getOutputStream());
        Log.d(TAG, "Selecting protocol (auto)");
        new SelectProtocolCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream());
        Log.d(TAG, "Sensor configured");
    }

    private void sendCommand(ObdCommand command, BluetoothSocket socket, Map<String, ObdCommand> telemetry, String commandName) throws IOException, InterruptedException {
        try {
            command.run(socket.getInputStream(), socket.getOutputStream());
            if (telemetry != null && commandName != null) {
                telemetry.put(commandName, command);
            }
        }
        catch(RuntimeException exc) {
            Log.e(TAG, "Retrying, command failed result was " + command.getResult());
            try {
                command.run(socket.getInputStream(), socket.getOutputStream());
                if (telemetry != null && commandName != null) {
                    telemetry.put(commandName, command);
                }
            }
            catch (RuntimeException exc2) {
                Log.e(TAG, "Retry failed. Giving up. Command failed result was " + command.getResult());
            }
        }
    }


    private void collectTelemetry(BluetoothSocket socket) throws InterruptedException, IOException {
        ArrayList<DriveTelementry> driveTelementryList = new ArrayList<>();

        while(socket.isConnected()) {
            Thread.sleep(5000);

            Map<String, ObdCommand >  telemetry = new HashMap<>();

            RPMCommand rpm = new RPMCommand();
            ConsumptionRateCommand consumptionRateCommand = new ConsumptionRateCommand();
            RuntimeCommand engineRuntime = new RuntimeCommand();
            SpeedCommand speedCommand = new SpeedCommand();
            ThrottlePositionCommand throttlePositionCommand = new ThrottlePositionCommand();
            LoadCommand engineLoadCommand = new LoadCommand();
            FuelLevelCommand fuelLevelCommand = new FuelLevelCommand();

            Log.d(TAG, "Running RPM command");
            sendCommand(rpm, socket, telemetry, RPM);

            Log.d(TAG, "Running Engine Runtime");
            sendCommand(engineRuntime, socket, telemetry, ENGINE_RUNTIME);

            Log.d(TAG, "Running Speed command");
            sendCommand(speedCommand, socket, telemetry, SPEED);

            Log.d(TAG, "Running Throttle position");
            sendCommand(throttlePositionCommand, socket, telemetry, THROTTLE_POSITION);


            Log.d(TAG, "Running Engined Load command");
            sendCommand(engineLoadCommand, socket, telemetry, ENGINE_LOAD);

            Log.d(TAG, "Running Fuel Level command");
            sendCommand(fuelLevelCommand, socket, telemetry, FUEL_LEVEL);


            Log.d(TAG, "Running fuel consumption");
            sendCommand(consumptionRateCommand, socket, telemetry, FUEL_CONSUMPTION);

            driveTelementryList.add(addTelemetryToList(telemetry));
        }

        createNotification(saveDrive(driveTelementryList));
    }

    private Drive saveDrive(ArrayList<DriveTelementry> driveTelementryList) {
        // TODO: use real data.
        Database db = new Database();
        Drive drive = new Drive();
        drive.setDriveCost(new BigDecimal("3.22"));
        drive.setDriveScore(31415);
        // TODO: call the achievements calculator instead of this hard codeded mess.
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(Achievement.FEATHERFOOT);
        achievements.add(Achievement.PARKINGLOT);
        drive.setAchievements(achievements);
        db.saveDrive(drive);

        return drive;
    }

    private void createNotification(Drive drive) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Drive Completed")
                        .setContentText("You just completed a drive! Tap here to view your score.");
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("DRIVE", drive); // now all the main activity has to do is get this and display it

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private DriveTelementry addTelemetryToList(Map<String, ObdCommand> telemetry) {

        DriveTelementry driveTelementry = new DriveTelementry();

        // TODO: get rid of these
        float speed,engineLoad,fuelLevel;
        Float speedy = Float.parseFloat(telemetry.get(SPEED).getCalculatedResult());
        Float engineLoady = Float.parseFloat(telemetry.get(ENGINE_LOAD).getCalculatedResult());
        Float fuelLevely = Float.parseFloat(telemetry.get(FUEL_LEVEL).getCalculatedResult());

        // Do them all like this with the try/catch
        float throttlePosition = 0.0f;
        try {
            throttlePosition = ((ThrottlePositionCommand) telemetry.get(THROTTLE_POSITION)).getPercentage();
            driveTelementry.setThrottle_position(throttlePosition);
        }
        catch (Throwable t) {
        }

        int rpm = 0;
        try {
            rpm = ((RPMCommand)telemetry.get(RPM)).getRPM();
            driveTelementry.setRevPerMinute(rpm);
        }
        catch (Throwable t) {
        }

        // TODO: get rid of this
        if(speedy != null){
            speed = speedy.floatValue();
        }else {
            speed = 0;
        }
        if(engineLoady != null){
            engineLoad = engineLoady.floatValue();
        }else{
            engineLoad =0;
        }
        if(fuelLevely != null) {
            fuelLevel = fuelLevely.floatValue();
        }else{
            fuelLevel=0;
        }

        // TODO: get rid of this too.
        driveTelementry.setEngineRunTime(telemetry.get(ENGINE_RUNTIME).getFormattedResult());
        driveTelementry.setSpeed(speed);
        driveTelementry.setEngineLoad(engineLoad);
        driveTelementry.setFuelLevel(fuelLevel);

        return  driveTelementry;
    }

    private void reportTelemetry(Map<String, String> telemetry) {
        Intent telemetryIntent = new Intent(DRIVE_TELEMETRY_EVENT);
        for(String key : telemetry.keySet()) {
            String value = telemetry.get(key);
            Log.i(TAG, key + " = " + value);
            telemetryIntent.putExtra(key, value);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(telemetryIntent);
    }

    private void reportFatalError(Throwable throwable) {
        Log.e(TAG, "Error in drive service.", throwable);
        Intent telemetryIntent = new Intent(DRIVE_TELEMETRY_STOPPED_EVENT);
        telemetryIntent.putExtra(DRIVE_TELEMETRY_ERROR, throwable.getMessage());
        LocalBroadcastManager.getInstance(this).sendBroadcast(telemetryIntent);
    }

}
