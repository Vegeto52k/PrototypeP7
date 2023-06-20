package fr.vegeto52.prototypep7.ui;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.vegeto52.prototypep7.data.repository.LocationRepository;
import fr.vegeto52.prototypep7.data.repository.PlaceDetailsRepository;
import fr.vegeto52.prototypep7.data.repository.UserRepository;
import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.model.RestaurantDetails;
import fr.vegeto52.prototypep7.model.RestaurantDetailsViewState;
import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 29/03/2023.
 */
public class PlaceDetailsViewModel extends ViewModel {

    PlaceDetailsRepository mPlaceDetailsRepository = new PlaceDetailsRepository();
    UserRepository mUserRepository = new UserRepository();
    LocationRepository mLocationRepository = new LocationRepository();
    MediatorLiveData mMediatorLiveData = new MediatorLiveData();


    Location mUserLocation;
    double mCurrentLatitude;
    double mCurrentLongitude;
    RestaurantDetails.Result mRestaurantDetails;
    List<User> mUserList;


    String mPlaceId;


    public void getRestaurantDetails(String placeId){

        mPlaceDetailsRepository.getPlaceDetails(placeId);
        mPlaceDetailsRepository.getRestaurantDetailsMutableLiveData().observeForever(new Observer<RestaurantDetails.Result>() {
            @Override
            public void onChanged(RestaurantDetails.Result result) {

            }
        });
    }

    private void loadData(String placeId){
        MutableLiveData<RestaurantDetails.Result> restaurantDetailsMutableLiveData = mPlaceDetailsRepository.getRestaurantDetailsMutableLiveData();
        MutableLiveData<List<User>> userList = mUserRepository.getListUserFromRepo();

        mMediatorLiveData.addSource(userList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
               combine(restaurantDetailsMutableLiveData.getValue(), users);
            }
        });
        mMediatorLiveData.addSource(restaurantDetailsMutableLiveData, mRestaurantDetails -> combine((RestaurantDetails.Result) mRestaurantDetails, userList.getValue()));
    }

    private void combine(RestaurantDetails.Result restaurantDetails, List<User> userList){
        if (restaurantDetails != null && userList != null){
            mMediatorLiveData.postValue(new RestaurantDetailsViewState(restaurantDetails, userList));
        }
    }

    public MutableLiveData<RestaurantDetailsViewState> getDetailsViewLiveData(String placeId){
        loadData(placeId);
        return mMediatorLiveData;
    }

    public Location UserLocation(){
        mLocationRepository.getLocationFromRepo().observeForever(new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                mUserLocation = location;
            }
        });
        Log.d("Test PDVM",  " " + mUserLocation);
        return mUserLocation;
    }


    public void getPhoneNumber(){

    }

    public void getRating(){

    }

    public void getWebsite(){

    }

    public void getOpeningHours(){

    }
}
