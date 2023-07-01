package fr.vegeto52.prototypep7.data.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import fr.vegeto52.prototypep7.data.MainApplication;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public class LocationRepository {

    private final MutableLiveData<Location> mLocationMutableLiveData = new MutableLiveData<>();

    FusedLocationProviderClient mFusedLocationProviderClient;

    public LocationRepository() {
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        Context context = MainApplication.getApplication();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        Task<Location> task = mFusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLocationMutableLiveData.setValue(location);
            }
        });
    }

    public LiveData<Location> getLocationMutableLiveData() {
        return mLocationMutableLiveData;
    }
}
