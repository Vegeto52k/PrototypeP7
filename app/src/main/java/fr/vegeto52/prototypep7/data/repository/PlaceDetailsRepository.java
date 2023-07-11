package fr.vegeto52.prototypep7.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import fr.vegeto52.prototypep7.data.PlaceDetailsApi;
import fr.vegeto52.prototypep7.data.RetrofitService;
import fr.vegeto52.prototypep7.model.Restaurant;
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
    String mFields = "website,vicinity,rating,place_id,photos/photo_reference,opening_hours/periods/open/time,opening_hours/periods/open/day,opening_hours/periods/close/time,name,icon,formatted_phone_number,formatted_address";

    private final MutableLiveData<RestaurantDetails.Result> mPlaceDetailsMutableLiveData = new MutableLiveData<>();
    NearbySearchRepository mNearbySearchRepository = new NearbySearchRepository();

    public PlaceDetailsRepository() {

    }

    public void getPlaceDetails(String placeId){
        mPlaceDetailsApi = RetrofitService.getRetrofitInstance().create(PlaceDetailsApi.class);
        mPlaceDetails = placeId;
        mPlaceDetailsApi.getDetails(mPlaceDetails, mFields, mKey).enqueue(new Callback<RestaurantDetails>() {
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

    public void getListPlaceDetails(){
        mPlaceDetailsApi = RetrofitService.getRetrofitInstance().create(PlaceDetailsApi.class);
        mNearbySearchRepository.getNearBySearchMutableLiveData().observeForever(new Observer<List<Restaurant.Results>>() {
            @Override
            public void onChanged(List<Restaurant.Results> resultsList) {
                for (Restaurant.Results restaurant : resultsList){
                    mPlaceDetails = restaurant.getPlace_id();
                    mPlaceDetailsApi.getDetails(mPlaceDetails, mFields, mKey).enqueue(new Callback<RestaurantDetails>() {
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
            }
        });
    }

    public LiveData<RestaurantDetails.Result> getPlaceDetailsMutableLiveData(){
        return mPlaceDetailsMutableLiveData;
    }
}
