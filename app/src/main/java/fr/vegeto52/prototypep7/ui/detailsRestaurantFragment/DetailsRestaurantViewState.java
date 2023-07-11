package fr.vegeto52.prototypep7.ui.detailsRestaurantFragment;

import java.util.List;

import fr.vegeto52.prototypep7.model.RestaurantDetails;
import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 05/07/2023.
 */
public class DetailsRestaurantViewState {

    private final List<User> mUserList;
    private final List<RestaurantDetails.Result> mResults;


    public DetailsRestaurantViewState(List<User> userList, List<RestaurantDetails.Result> results){
        mUserList = userList;
        mResults = results;
    }

    public List<User> getUserList() {
        return mUserList;
    }

    public List<RestaurantDetails.Result> getResults() {
        return mResults;
    }
}
