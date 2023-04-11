package fr.vegeto52.prototypep7.model;

import java.util.List;

/**
 * Created by Vegeto52-PC on 07/04/2023.
 */
public class RestaurantDetailsViewState {

    private final RestaurantDetails.Result mRestaurantDetails;
    private final List<User> mUserList;

    public RestaurantDetailsViewState(RestaurantDetails.Result restaurantDetails, List<User> userList) {
        mRestaurantDetails = restaurantDetails;
        mUserList = userList;
    }

    public RestaurantDetails.Result getRestaurantDetails(){
        return mRestaurantDetails;
    }
    public List<User> getUserList(){
        return mUserList;
    }
}
