package com.example.security1stex;

import static android.content.Context.BATTERY_SERVICE;
import static android.content.Context.CAMERA_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class AppManager {
    private boolean isConnectedToWifi = false;
    private boolean isFlashlight = false;
    private int batteryPrecent = 0;
    private String userName;
    String allowed="Lichow";
    private ArrayList<String> allowedNames = new ArrayList<>(Arrays.asList("Liri's iPhone", "Lichow"));
    public static AppManager appManager;

    public AppManager() {
    }

    public static AppManager getInstance() {
        if (appManager == null) {
            appManager = new AppManager();
        }
        return appManager;
    }
    public void checkPermissions(AppCompatActivity activity,Context context) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }
    public boolean isConnectedToWifi(AppCompatActivity activity,Context context) {
        checkPermissions(activity,context);
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String name="original";
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (wifiInfo != null  && wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {

                name= wifiInfo.getSSID(); // This will return the Wi-Fi network name
            }
        }
        else name= null; // Return null if Wi-Fi is not enabled or not connected

        //if (name.trim().equals("Lichow")) isConnectedToWifi = true;
        if (name.contains(allowed)) isConnectedToWifi = true;

        return isConnectedToWifi;
    }

    public void setConnectedToWifi(boolean connectedToWifi) {
        isConnectedToWifi = connectedToWifi;
    }

    public boolean isFlashlight(Context context) {

        CameraManager CM = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String CID = CM.getCameraIdList()[0];

            CM.registerTorchCallback(new CameraManager.TorchCallback() {
                @Override
                public void onTorchModeChanged(String CID, boolean enabled) {
                    super.onTorchModeChanged(CID, enabled);
                    if (!enabled) {
                        isFlashlight = false;
                    } else {
                        isFlashlight = true;
                    }
                }
            }, new Handler());

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return isFlashlight;
    }

    public void setFlashlight(boolean flashlight) {
        isFlashlight = flashlight;
    }

    public int getBatteryPrecent(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {

            BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
            return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        } else {

            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);

            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

            double batteryPct = level / (double) scale;

            return (int) (batteryPct * 100);
        }
    }

    public void setBatteryPrecent(int batteryPrecent) {
        this.batteryPrecent = batteryPrecent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
