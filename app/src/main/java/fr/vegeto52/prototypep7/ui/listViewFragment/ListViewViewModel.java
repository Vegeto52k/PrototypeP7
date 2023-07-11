package fr.vegeto52.prototypep7.ui.listViewFragment;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.vegeto52.prototypep7.data.repository.FirestoreRepository;
import fr.vegeto52.prototypep7.data.repository.LocationRepository;
import fr.vegeto52.prototypep7.data.repository.NearbySearchRepository;
import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 27/06/2023.
 */
public class ListViewViewModel extends ViewModel {

    private final MediatorLiveData<ListViewViewState> mMediatorLiveData = new MediatorLiveData<>();

    public ListViewViewModel(LocationRepository locationRepository, NearbySearchRepository nearbySearchRepository, FirestoreRepository firestoreRepository){
        LiveData<Location> location = locationRepository.getLocationMutableLiveData();
        LiveData<List<Restaurant.Results>> restaurant = nearbySearchRepository.getNearBySearchMutableLiveData();
        LiveData<List<User>> selectedRestaurantsList = firestoreRepository.getListMutableLiveData();

        mMediatorLiveData.addSource(location, location1 -> {
            if (location1 != null){
                nearbySearchRepository.getNearBySearch(location1);
            }
            combine(location1, restaurant.getValue(), selectedRestaurantsList.getValue());
        });
        mMediatorLiveData.addSource(restaurant, resultsList -> combine(location.getValue(), resultsList, selectedRestaurantsList.getValue()));
        mMediatorLiveData.addSource(selectedRestaurantsList, users -> combine(location.getValue(), restaurant.getValue(), users));

    }

    private void combine(Location location,List<Restaurant.Results> listRestaurants, List<User> selectedRestaurantsList){
        if (location != null && listRestaurants != null && selectedRestaurantsList != null){
            mMediatorLiveData.setValue(new ListViewViewState(location, listRestaurants, selectedRestaurantsList));
        }
    }

    public LiveData<ListViewViewState> getListViewMutableLiveData(){
        return mMediatorLiveData;
    }
}
