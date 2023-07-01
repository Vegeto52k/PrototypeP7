package fr.vegeto52.prototypep7;

import android.location.Location;

import java.util.List;

import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.model.RestaurantDetails;
import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 28/06/2023.
 */
public class ListViewViewState {

    private final Location mLocation;
    private final List<Restaurant.Results> mResults;
    private final List<User> mUserList;
    private final RestaurantDetails.Result mResult;

    public ListViewViewState(Location location, List<Restaurant.Results> results, List<User> userList, RestaurantDetails.Result result) {
        mLocation = location;
        mResults = results;
        mUserList = userList;
        mResult = result;
    }

    public Location getLocation() {
        return mLocation;
    }

    public List<Restaurant.Results> getResults() {
        return mResults;
    }

    public List<User> getUserList() {
        return mUserList;
    }

    public RestaurantDetails.Result getResult() {
        return mResult;
    }
}
