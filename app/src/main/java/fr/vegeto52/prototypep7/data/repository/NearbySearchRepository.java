package fr.vegeto52.prototypep7.data.repository;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

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

    MutableLiveData<List<Restaurant.Results>> mRestaurants = new MutableLiveData<>();

    PlaceDetailsRepository mPlaceDetailsRepository = new PlaceDetailsRepository();


    public void getRestaurantsList(Location location) {

        mCurrentLatitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();

        mLatLng = "" + mCurrentLatitude + "," + mCurrentLongitude;

        mNearbySearchApi = RetrofitService.getRetrofitInstance().create(NearbySearchApi.class);

        Log.d("Test Liste Restaurants", "Coucou" + mCurrentLatitude + " " + mCurrentLongitude);

        mNearbySearchApi.getObjectRestaurant(mLatLng, radius, type, map_key).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {

                mRestaurants.setValue(response.body().getResults());

                mPlaceDetailsRepository.getPlaceDetails("ChIJOYvCo1W3j4AR1LAifgk13rs");

                Log.d("Test Repo", " " + response.body().getResults().size());

                for (Restaurant.Results results : response.body().getResults()) {
                    Log.d("Test ResponseBody", "onResponse: " + results.getName() + " + PlaceId: " + results.getPlace_id());

                //    mPlaceDetailsRepository.getPlaceDetails("ChIJOYvCo1W3j4AR1LAifgk13rs");

                }
                if (!response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
            }
        });
    }

    public MutableLiveData<List<Restaurant.Results>> getListRestaurant(){
        return mRestaurants;
    }
}
