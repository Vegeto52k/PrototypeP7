package fr.vegeto52.prototypep7.data.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import fr.vegeto52.prototypep7.data.MainApplication;
import fr.vegeto52.prototypep7.ui.MainActivity;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public class LocationRepository {

    double mCurrentLatitude;
    double mCurrentLongitude;
    MutableLiveData<Location> mLocationMutableLiveData = new MutableLiveData<>();

    FusedLocationProviderClient mFusedLocationProviderClient;
    MainActivity mMainActivity;
    private static final String TAG = "MainActivity";
    Location mLocation;

    @SuppressLint("MissingPermission")
    public void getLocation() {
        Context context = MainApplication.getApplication();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        Task<Location> task = mFusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLocation = location;
                mCurrentLatitude = mLocation.getLatitude();
                mCurrentLongitude = mLocation.getLongitude();

                mLocationMutableLiveData.setValue(mLocation);

                Log.d("Verif getLocation", "Objectif Atteint" + mLocation);
            }
        });
    }

    public MutableLiveData<Location> getLocationFromRepo() {
        return mLocationMutableLiveData;
    }
}
