package fr.vegeto52.prototypep7.ui.workmatesViewFragment;

import java.util.List;

import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 05/07/2023.
 */
public class WorkmatesViewViewState {

    private final List<User> mUserList;

    public WorkmatesViewViewState(List<User> userList){
        mUserList = userList;
    }

    public List<User> getUserList() {
        return mUserList;
    }
}
