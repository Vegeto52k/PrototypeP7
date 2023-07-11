package fr.vegeto52.prototypep7.ui.mapViewFragment;

import android.location.Location;

import java.util.List;

import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 04/07/2023.
 */
public class MapViewViewState {

    private final Location mLocation;
    private final List<Restaurant.Results> mResults;
    private final List<User> mUserList;

    public MapViewViewState(Location location, List<Restaurant.Results> results, List<User> userList) {
        mLocation = location;
        mResults = results;
        mUserList = userList;
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
}
