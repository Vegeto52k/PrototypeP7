package fr.vegeto52.prototypep7.data;

import fr.vegeto52.prototypep7.model.RestaurantDetails;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Vegeto52-PC on 27/03/2023.
 */
public interface PlaceDetailsApi {

    //Exemple URL : https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJOYvCo1W3j4AR1LAifgk13rs&rating%2Cformatted_phone_number%2Crating%2Ccurrent_opening_hours&key=AIzaSyArVUpejXwZw7QhmdFpVY9rHai7Y2adWrI
    //https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJxeChJ3VuyRIR7fS-pJl6hXY&fields=formatted_phone_number,rating,website,opening_hours/periods/open/time&key=AIzaSyArVUpejXwZw7QhmdFpVY9rHai7Y2adWrI

    @GET("details/json?")
    Call<RestaurantDetails> getDetails(
            @Query("place_id") String placeId,
            @Query("fields")String fields,
            @Query("key") String map_key
    );
}
