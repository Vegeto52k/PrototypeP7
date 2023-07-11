package fr.vegeto52.prototypep7.ui.workmatesViewFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.vegeto52.prototypep7.data.repository.FirestoreRepository;
import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 05/07/2023.
 */
public class WorkmatesViewViewModel extends ViewModel {

    private final MediatorLiveData<WorkmatesViewViewState> mMediatorLiveData = new MediatorLiveData<>();

    public WorkmatesViewViewModel(FirestoreRepository firestoreRepository){
        LiveData<List<User>> selectedRestaurantsList = firestoreRepository.getListMutableLiveData();

        mMediatorLiveData.addSource(selectedRestaurantsList, users -> combine(users));
    }

    private void combine(List<User> selectedRestaurantsList){
        if (selectedRestaurantsList != null){
            mMediatorLiveData.setValue(new WorkmatesViewViewState(selectedRestaurantsList));
        }
    }

    public LiveData<WorkmatesViewViewState> getWorkmatesViewMutableLiveData(){
        return mMediatorLiveData;
    }
}
