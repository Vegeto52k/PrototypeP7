package fr.vegeto52.prototypep7.data;

import fr.vegeto52.prototypep7.model.Restaurant;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public interface NearbySearchApi {

    // URL : https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=43.408025,5.92382&radius=1500&type=restaurant&key=AIzaSyArVUpejXwZw7QhmdFpVY9rHai7Y2adWrI
    // URL Test : https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=43.408025,5.92382&radius=1500&type=restaurant&fields=rating&key=AIzaSyArVUpejXwZw7QhmdFpVY9rHai7Y2adWrI

    @GET("nearbysearch/json?")
    Call<ResponseBody> getRestaurantsTest(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String map_key
    );

    @GET("nearbysearch/json?")
    Call<Restaurant> getObjectRestaurant(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String map_key
    );
}
