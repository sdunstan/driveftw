package com.stevedunstan.driveftw.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.Map;
import java.util.Set;

public class DeviceManager {
    private static final String DRIVE_FTW_SHARED_PREFERENCES = "com.stevedunstan.drivefwt.DRIVE_FTW_SP";
    private static final String DRIVE_FTW_BLUETOOTH_DEVICE_NAME = "BLUETOOTH_DEVICE_NAME";
    private static final String DRIVE_FTW_BLUETOOTH_DEVICE = "BLUETOOTH_DEVICE";
    private static final String TAG = DeviceManager.class.getName();


    public synchronized static void showListOfDevices(final Context context) throws DeviceException {
        Log.d(TAG,"Going into show list of devices");
        Map<String,Device> deviceMap = getDeviceAddresses(context);
        if (!deviceMap.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            final Device[] devices = deviceMap.values().toArray(new Device[deviceMap.size()]);
            ArrayAdapter<Device> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice);
            adapter.addAll(devices);

            builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Device device = devices[i];
                    saveBluetoothDevice(context, device);
                }
            });

            builder.setTitle("Bluetooth Devices");
            builder.show();
        }
        else {
            throw new DeviceException("No Bluetooth devices are available.");
        }
    }

    public static synchronized String getBlueoothDevice(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DRIVE_FTW_SHARED_PREFERENCES, 0);
        return sharedPreferences.getString(DRIVE_FTW_BLUETOOTH_DEVICE, null);
    }

    private static Map<String,Device> getDeviceAddresses(Context context) {
        Map<String, Device> addresses = new java.util.HashMap<>();



        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter != null) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            for (BluetoothDevice device : pairedDevices) {
                Log.d(TAG, "Found device " + device.getName());
                addresses.put(device.getAddress(), new Device(device.getAddress(), device.getName()));
            }
        }
        return addresses;
    }

    private static void saveBluetoothDevice(Context ctx, Device device) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(DRIVE_FTW_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DRIVE_FTW_BLUETOOTH_DEVICE, device.address);
        editor.putString(DRIVE_FTW_BLUETOOTH_DEVICE_NAME, device.name);
        editor.commit();
    }

}
