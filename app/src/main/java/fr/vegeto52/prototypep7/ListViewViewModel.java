package fr.vegeto52.prototypep7;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.vegeto52.prototypep7.data.repository.FirestoreRepository;
import fr.vegeto52.prototypep7.data.repository.LocationRepository;
import fr.vegeto52.prototypep7.data.repository.NearbySearchRepository;
import fr.vegeto52.prototypep7.data.repository.PlaceDetailsRepository;
import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.model.RestaurantDetails;
import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 27/06/2023.
 */
public class ListViewViewModel extends ViewModel {

    private final MediatorLiveData<ListViewViewState> mMediatorLiveData = new MediatorLiveData<>();

    public ListViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository, FirestoreRepository firestoreRepository, PlaceDetailsRepository placeDetailsRepository){
        LiveData<Location> location = locationRepository.getLocationMutableLiveData();
        LiveData<List<Restaurant.Results>> restaurant = nearbySearchRepository.getNearBySearchMutableLiveData();
        LiveData<List<User>> selectedRestaurantsList = firestoreRepository.getListMutableLiveData();
        LiveData<RestaurantDetails.Result> restaurantDetails = placeDetailsRepository.getPlaceDetailsMutableLiveData();

        mMediatorLiveData.addSource(location, location1 -> combine(location1, restaurant.getValue(), selectedRestaurantsList.getValue(), restaurantDetails.getValue()));
        mMediatorLiveData.addSource(restaurant, resultsList -> combine(location.getValue(), resultsList, selectedRestaurantsList.getValue(), restaurantDetails.getValue()));
        mMediatorLiveData.addSource(selectedRestaurantsList, users -> combine(location.getValue(), restaurant.getValue(), users, restaurantDetails.getValue()));
        mMediatorLiveData.addSource(restaurantDetails, result -> combine(location.getValue(), restaurant.getValue(), selectedRestaurantsList.getValue(), result));

        Log.d("ViewModel 1", " " + mMediatorLiveData + " " + location + " " + restaurant + " " + selectedRestaurantsList + " " + restaurantDetails);
    }

    private void combine(Location location,List<Restaurant.Results> listRestaurants, List<User> selectedRestaurantsList, RestaurantDetails.Result result){
        Log.d("ViewModel 2", " " + mMediatorLiveData + " " + location + " " + listRestaurants + " " + selectedRestaurantsList + " " + result);
        if (location != null && listRestaurants != null && selectedRestaurantsList != null && result != null){
            mMediatorLiveData.setValue(new ListViewViewState(location, listRestaurants, selectedRestaurantsList, result));
            Log.d("ViewModel 3", " " + mMediatorLiveData + " " + location + " " + listRestaurants + " " + selectedRestaurantsList + " " + result);
        }
    }

    public LiveData<ListViewViewState> getListViewMutableLiveData(){
        return mMediatorLiveData;
    }
}
