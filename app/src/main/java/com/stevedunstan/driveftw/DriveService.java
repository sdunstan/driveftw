package com.stevedunstan.driveftw;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.engine.RuntimeCommand;
import com.github.pires.obd.commands.engine.ThrottlePositionCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.github.pires.obd.exceptions.ResponseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DriveService extends IntentService {
    public static final String DRIVE_TELEMETRY_EVENT = "com.stevedunstan.driveftw.action.DRIVE_TELEMETRY";
    public static final String DRIVE_TELEMETRY_STOPPED_EVENT = "com.stevedunstan.driveftw.action.DRIVE_TELEMETRY_STOPPED";
    public static final String DRIVE_TELEMETRY_ERROR = "ERROR";

    private static final String ACTION_START_DRIVE = "com.stevedunstan.driveftw.action.START_DRIVE";
    private static final String EXTRA_ODB2_ADDRESS = "com.stevedunstan.driveftw.extra.ADDRESS";
    private static final String TAG = DriveService.class.getName();
    private static final String RPM = "RPM";
    private static final String ENGINE_RUNTIME= "ENGINE_RUNTIME";
    private static final String SPEED = "SPEED";
    private static final String THROTTLE_POSITION = "THROTTLE_POSITION";
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
        sendCommand(new EchoOffCommand(), socket);
        Log.d(TAG, "Setting line feed off");
        new LineFeedOffCommand().run(socket.getInputStream(), socket.getOutputStream());
        Log.d(TAG, "Setting timeout");
        new TimeoutCommand(250).run(socket.getInputStream(), socket.getOutputStream());
        Log.d(TAG, "Selecting protocol (auto)");
        new SelectProtocolCommand(ObdProtocols.AUTO).run(socket.getInputStream(), socket.getOutputStream());
        Log.d(TAG, "Sensor configured");
    }

    private void sendCommand(ObdCommand command, BluetoothSocket socket) throws IOException, InterruptedException {
        try {
            command.run(socket.getInputStream(), socket.getOutputStream());
        }
        catch(ResponseException exc) {
            Log.e(TAG, "Retrying, command failed result was " + command.getResult());
            command.run(socket.getInputStream(), socket.getOutputStream());
        }
    }


    private void collectTelemetry(BluetoothSocket socket) throws InterruptedException, IOException {
        RPMCommand rpm = new RPMCommand();
        Map<String, String> telemetry = new HashMap<>();
        RuntimeCommand engineRuntime = new RuntimeCommand();
        SpeedCommand speedCommand = new SpeedCommand();
        ThrottlePositionCommand throttlePositionCommand = new ThrottlePositionCommand();

            Log.d(TAG, "Running RPM command");
            rpm.run(socket.getInputStream(), socket.getOutputStream());
            telemetry.put(RPM, rpm.getFormattedResult());
            Log.d(TAG, "Running Engine Runtime");
            engineRuntime.run(socket.getInputStream(), socket.getOutputStream());
            telemetry.put(ENGINE_RUNTIME, engineRuntime.getFormattedResult());
            Log.d(TAG, "Running Speed command");
            speedCommand.run(socket.getInputStream(), socket.getOutputStream());
            telemetry.put(SPEED,speedCommand.getFormattedResult());
            throttlePositionCommand.run(socket.getInputStream(),socket.getOutputStream());
            Log.d(TAG,"Running Throttle position");
            telemetry.put(THROTTLE_POSITION, throttlePositionCommand.getFormattedResult());
        

            reportTelemetry(telemetry);
        }
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
