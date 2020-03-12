package com.os.imars.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.os.imars.config.Config;

import java.util.List;

/**
 * Created by nitesh on 16/12/16.
 */

public class LocationService extends Service implements LocationListener {

    private LocationManager locationManager;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context mContext = this;
       // App.locationService = LocationService.this;
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        startLocationTracking();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null) {
                location.setTime(System.currentTimeMillis());
                Config.setLatitude(String.valueOf(location.getLatitude()));
                Config.setLongitude(String.valueOf(location.getLongitude()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    public void startLocationTracking() {
        startLocationTracking(getApplicationContext());
    }

    private boolean startLocationTracking(Context context) {
        try {
            if (locationManager == null) {
                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            }
            List<String> providers = locationManager.getProviders(true);
            if (providers.contains(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10 * 1000, 2, this);
            } else {
                locationManager.requestLocationUpdates(getBestProvider(locationManager), 10* 1000, 2, this);
            }
            return true;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    private String getBestProvider(LocationManager locManager) {
        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_HIGH);
        locationCritera.setPowerRequirement(Criteria.POWER_LOW);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setSpeedRequired(false);
        locationCritera.setCostAllowed(true);
        return locManager.getBestProvider(locationCritera, true);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
}
