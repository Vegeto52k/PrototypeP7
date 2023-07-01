package fr.vegeto52.prototypep7.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import fr.vegeto52.prototypep7.data.PlaceDetailsApi;
import fr.vegeto52.prototypep7.data.RetrofitService;
import fr.vegeto52.prototypep7.model.RestaurantDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vegeto52-PC on 28/03/2023.
 */
public class PlaceDetailsRepository {

    private PlaceDetailsApi mPlaceDetailsApi;

    String mPlaceDetails;
    String mKey = "AIzaSyArVUpejXwZw7QhmdFpVY9rHai7Y2adWrI";

    private final MutableLiveData<RestaurantDetails.Result> mPlaceDetailsMutableLiveData = new MutableLiveData<>();

    public PlaceDetailsRepository() {
        mPlaceDetailsApi = RetrofitService.getRetrofitInstance().create(PlaceDetailsApi.class);
    }

    public void getPlaceDetails(String placeId){
        mPlaceDetails = placeId;
        mPlaceDetailsApi.getDetails(mPlaceDetails, mKey).enqueue(new Callback<RestaurantDetails>() {
            @Override
            public void onResponse(Call<RestaurantDetails> call, Response<RestaurantDetails> response) {
                mPlaceDetailsMutableLiveData.setValue(response.body().getResult());
            }

            @Override
            public void onFailure(Call<RestaurantDetails> call, Throwable t) {
                mPlaceDetailsMutableLiveData.setValue(null);
            }
        });
    }

    public LiveData<RestaurantDetails.Result> getPlaceDetailsMutableLiveData(){
        return mPlaceDetailsMutableLiveData;
    }
}
