package fr.vegeto52.prototypep7.model;

import javax.annotation.Nullable;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public class User {

    private String mUid;
    private String mUserName;
    @Nullable
    private String mUrlPhoto;


    //Constructor
    public User(String uid, String userName, @androidx.annotation.Nullable String urlPhoto) {
        mUid = uid;
        mUserName = userName;
        mUrlPhoto = urlPhoto;
    }

    //Getters
    public String getUid() {
        return mUid;
    }

    public String getUserName() {
        return mUserName;
    }

    @androidx.annotation.Nullable
    public String getUrlPhoto() {
        return mUrlPhoto;
    }


    //Setters
    public void setUid(String uid) {
        mUid = uid;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setUrlPhoto(@androidx.annotation.Nullable String urlPhoto) {
        mUrlPhoto = urlPhoto;
    }
}


