package fr.vegeto52.prototypep7.ui;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.vegeto52.prototypep7.data.repository.LocationRepository;
import fr.vegeto52.prototypep7.data.repository.NearbySearchRepository;
import fr.vegeto52.prototypep7.model.Restaurant;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public class NearbySearchViewModel extends ViewModel {

    NearbySearchRepository mNearbySearchRepository = new NearbySearchRepository();
    LocationRepository mLocationRepository = new LocationRepository();

    public NearbySearchViewModel() {
        getListRestaurantName();
    }

    public void getListRestaurantName() {

        mLocationRepository.getLocation();
        Log.d("Verif Search", "For moment, it's ok");
        mLocationRepository.getLocationFromRepo().observeForever(new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                mNearbySearchRepository.getRestaurantsList(location);
            }
        });
    }

    public MutableLiveData<List<Restaurant.Results>> getRestaurants(){
        return mNearbySearchRepository.getListRestaurant();
    }
}
