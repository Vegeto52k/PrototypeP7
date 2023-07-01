package fr.vegeto52.prototypep7;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import fr.vegeto52.prototypep7.data.repository.FirestoreRepository;
import fr.vegeto52.prototypep7.data.repository.LocationRepository;
import fr.vegeto52.prototypep7.data.repository.NearbySearchRepository;
import fr.vegeto52.prototypep7.data.repository.PlaceDetailsRepository;

/**
 * Created by Vegeto52-PC on 30/06/2023.
 */
public class ListViewViewModelFactory implements ViewModelProvider.Factory {

    private final LocationRepository mLocationRepository;
    private final NearbySearchRepository mNearbySearchRepository;
    private final FirestoreRepository mFirestoreRepository;
    private final PlaceDetailsRepository mPlaceDetailsRepository;

    private static volatile ListViewViewModelFactory sFactory;

    public static ListViewViewModelFactory getInstance(){
        if (sFactory == null){
            synchronized (ListViewViewModelFactory.class){
                if (sFactory == null){
                    sFactory = new ListViewViewModelFactory();
                }
            }
        }
        return sFactory;
    }

    private ListViewViewModelFactory(){
        this.mLocationRepository = new LocationRepository();
        this.mNearbySearchRepository = new NearbySearchRepository();
        this.mFirestoreRepository = new FirestoreRepository();
        this.mPlaceDetailsRepository = new PlaceDetailsRepository();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListViewViewModel.class)){
            return (T) new ListViewViewModel(mLocationRepository, mNearbySearchRepository, mFirestoreRepository, mPlaceDetailsRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
