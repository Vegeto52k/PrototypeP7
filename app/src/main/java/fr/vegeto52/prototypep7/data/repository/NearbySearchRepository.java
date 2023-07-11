package fr.vegeto52.prototypep7.data.repository;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import fr.vegeto52.prototypep7.data.NearbySearchApi;
import fr.vegeto52.prototypep7.data.RetrofitService;
import fr.vegeto52.prototypep7.model.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public class NearbySearchRepository {

    private NearbySearchApi mNearbySearchApi;
    double mCurrentLatitude;
    double mCurrentLongitude;

    String mLatLng;
    int radius = 1500;
    String type = "restaurant";
    String map_key = "AIzaSyArVUpejXwZw7QhmdFpVY9rHai7Y2adWrI";

    private final MutableLiveData<List<Restaurant.Results>> mListMutableLiveData = new MutableLiveData<>();
    LocationRepository mLocationRepository = new LocationRepository();

    public NearbySearchRepository() {

    }

//    public void getNearBySearch(Location location) {
//        mNearbySearchApi = RetrofitService.getRetrofitInstance().create(NearbySearchApi.class);
//        mLocationRepository.getLocationMutableLiveData().observeForever(new Observer<Location>() {
//            @Override
//            public void onChanged(Location location) {
//                mCurrentLatitude = location.getLatitude();
//                mCurrentLongitude = location.getLongitude();
//
//                mLatLng = "" + mCurrentLatitude + "," + mCurrentLongitude;
//
//                mNearbySearchApi.getObjectRestaurant(mLatLng, radius, type, map_key).enqueue(new Callback<Restaurant>() {
//                    @Override
//                    public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
//                        mListMutableLiveData.setValue(response.body().getResults());
//                    }
//
//                    @Override
//                    public void onFailure(Call<Restaurant> call, Throwable t) {
//                        mListMutableLiveData.setValue(null);
//                    }
//                });
//            }
//        });
//    }

    public void getNearBySearch(Location location) {
        mNearbySearchApi = RetrofitService.getRetrofitInstance().create(NearbySearchApi.class);
        mCurrentLatitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();

        mLatLng = "" + mCurrentLatitude + "," + mCurrentLongitude;

        mNearbySearchApi.getObjectRestaurant(mLatLng, radius, type, map_key).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                mListMutableLiveData.setValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                mListMutableLiveData.setValue(null);
            }
        });
    }

    public LiveData<List<Restaurant.Results>> getNearBySearchMutableLiveData() {
        return mListMutableLiveData;
    }
}
