package fr.vegeto52.prototypep7.ui.detailsRestaurantFragment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.List;

import fr.vegeto52.prototypep7.data.repository.FirestoreRepository;
import fr.vegeto52.prototypep7.data.repository.PlaceDetailsRepository;
import fr.vegeto52.prototypep7.model.RestaurantDetails;
import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 05/07/2023.
 */
public class DetailsRestaurantViewModel extends ViewModel {

    private final MediatorLiveData<DetailsRestaurantViewState> mMediatorLiveData = new MediatorLiveData<>();
    private MutableLiveData<List<RestaurantDetails.Result>> mListLiveData = new MutableLiveData<>();

    List<RestaurantDetails.Result> mRestaurantList = new ArrayList<>();


    public DetailsRestaurantViewModel(FirestoreRepository firestoreRepository, PlaceDetailsRepository placeDetailsRepository){

        LiveData<List<User>> usersList = firestoreRepository.getListMutableLiveData();
        LiveData<List<RestaurantDetails.Result>> listRestaurantDetails = mListLiveData;

        mMediatorLiveData.addSource(usersList, userList -> combine(userList, listRestaurantDetails.getValue()));
        mMediatorLiveData.addSource(listRestaurantDetails, results -> combine(usersList.getValue(), results));
    }

    private void combine(List<User> userList, List<RestaurantDetails.Result> result){
        if (userList != null){
            mMediatorLiveData.setValue(new DetailsRestaurantViewState(userList, result));
        }
    }

    public void getPlaceDetails(String placeId){
        PlaceDetailsRepository placeDetailsRepository = new PlaceDetailsRepository();
        boolean checkPlaceId = true;
        if (mRestaurantList.isEmpty()){
            placeDetailsRepository.getPlaceDetails(placeId);
            placeDetailsRepository.getPlaceDetailsMutableLiveData().observeForever(new Observer<RestaurantDetails.Result>() {
                @Override
                public void onChanged(RestaurantDetails.Result result) {
                    mRestaurantList.add(result);
                    mListLiveData.setValue(mRestaurantList);
                    Log.d("Vérification 4", "Mediator " + mRestaurantList.toString());
                }
            });
        } else {
            for (RestaurantDetails.Result restaurant : mRestaurantList){
                if (restaurant.getPlace_id().equals(placeId)){
                    if (checkPlaceId == true){
                        Log.d("Vérification 3", "Mediator " + mRestaurantList.toString());
                        checkPlaceId = false;
                    }
                }
            }
            if (checkPlaceId == true){
                placeDetailsRepository.getPlaceDetails(placeId);
                placeDetailsRepository.getPlaceDetailsMutableLiveData().observeForever(new Observer<RestaurantDetails.Result>() {
                    @Override
                    public void onChanged(RestaurantDetails.Result result) {
                        mRestaurantList.add(result);
                        mListLiveData.setValue(mRestaurantList);
                        Log.d("Vérification 2", "Mediator " + mRestaurantList.toString());
                    }
                });
            }
        }
    }

    public void getPlaceDetails2(String placeId){
        PlaceDetailsRepository placeDetailsRepository = new PlaceDetailsRepository();
        Log.d("Vérification 3", "Mediator " + mRestaurantList.toString());
        for (RestaurantDetails.Result restaurant : mRestaurantList){
            if (!restaurant.getPlace_id().equals(placeId)){
                placeDetailsRepository.getPlaceDetails(placeId);
                placeDetailsRepository.getPlaceDetailsMutableLiveData().observeForever(new Observer<RestaurantDetails.Result>() {
                    @Override
                    public void onChanged(RestaurantDetails.Result result) {
                        mRestaurantList.add(result);
                        mListLiveData.setValue(mRestaurantList);
                        Log.d("Vérification 2", "Mediator " + mRestaurantList.toString());
                    }
                });
            }
        }
    }

    public LiveData<DetailsRestaurantViewState> getUsersListMutableLiveData(){
        Log.d("Vérification ", "Mediator " + mRestaurantList.toString());
        return mMediatorLiveData;
    }
}
