package fr.vegeto52.prototypep7.data.viewModelFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import fr.vegeto52.prototypep7.data.repository.FirestoreRepository;
import fr.vegeto52.prototypep7.data.repository.LocationRepository;
import fr.vegeto52.prototypep7.data.repository.NearbySearchRepository;
import fr.vegeto52.prototypep7.data.repository.PlaceDetailsRepository;
import fr.vegeto52.prototypep7.ui.detailsRestaurantFragment.DetailsRestaurantViewModel;
import fr.vegeto52.prototypep7.ui.listViewFragment.ListViewViewModel;
import fr.vegeto52.prototypep7.ui.mapViewFragment.MapViewViewModel;
import fr.vegeto52.prototypep7.ui.workmatesViewFragment.WorkmatesViewViewModel;

/**
 * Created by Vegeto52-PC on 06/07/2023.
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final LocationRepository mLocationRepository;
    private final NearbySearchRepository mNearbySearchRepository;
    private final FirestoreRepository mFirestoreRepository;
    private final PlaceDetailsRepository mPlaceDetailsRepository;

    private static volatile ViewModelFactory sFactory;

    public static ViewModelFactory getInstance(){
        if (sFactory == null){
            synchronized (ViewModelFactory.class){
                if (sFactory == null){
                    sFactory = new ViewModelFactory();
                }
            }
        }
        return sFactory;
    }

    private ViewModelFactory(){
        this.mLocationRepository = new LocationRepository();
        this.mNearbySearchRepository = new NearbySearchRepository();
        this.mFirestoreRepository = new FirestoreRepository();
        this.mPlaceDetailsRepository = new PlaceDetailsRepository();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewViewModel.class)){
            return (T) new MapViewViewModel(mLocationRepository, mNearbySearchRepository, mFirestoreRepository);
        }
        if (modelClass.isAssignableFrom(ListViewViewModel.class)){
            return (T) new ListViewViewModel(mLocationRepository, mNearbySearchRepository, mFirestoreRepository);
        }
        if (modelClass.isAssignableFrom(WorkmatesViewViewModel.class)){
            return (T) new WorkmatesViewViewModel(mFirestoreRepository);
        }
        if (modelClass.isAssignableFrom(DetailsRestaurantViewModel.class)){
            return (T) new DetailsRestaurantViewModel(mFirestoreRepository, mPlaceDetailsRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
