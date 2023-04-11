package fr.vegeto52.prototypep7.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import fr.vegeto52.prototypep7.data.PlaceDetailsApi;
import fr.vegeto52.prototypep7.data.RetrofitService;
import fr.vegeto52.prototypep7.model.RestaurantDetails;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vegeto52-PC on 28/03/2023.
 */
public class PlaceDetailsRepository {

    PlaceDetailsApi mPlaceDetailsApi;

    String mPlaceDetails;
    String mPhoneNumber;
    double mRating;
    String mWebsite;
    String mOpeningHours;
    String mKey = "AIzaSyArVUpejXwZw7QhmdFpVY9rHai7Y2adWrI";

    MutableLiveData<RestaurantDetails.Result> mRestaurantDetailsMutableLiveData = new MutableLiveData<>();


    public void getPlaceDetails(String placeId){

        Log.d("Coucou4", "Je suis là, ici !");

        mPlaceDetails = placeId;

        mPlaceDetailsApi = RetrofitService.getRetrofitInstance().create(PlaceDetailsApi.class);
        mPlaceDetailsApi.getDetails(mPlaceDetails, mKey).enqueue(new Callback<RestaurantDetails>() {
            @Override
            public void onResponse(Call<RestaurantDetails> call, Response<RestaurantDetails> response) {
                Log.d("Coucou3", "Je suis là, ici !");
                mRestaurantDetailsMutableLiveData.setValue(response.body().getResult());
            }

            @Override
            public void onFailure(Call<RestaurantDetails> call, Throwable t) {
                Log.d("Coucou5", "Je suis là, ici !" + t.getMessage());
            }
        });
    }

    public MutableLiveData<RestaurantDetails.Result> getRestaurantDetailsMutableLiveData(){
        return mRestaurantDetailsMutableLiveData;
    }
}