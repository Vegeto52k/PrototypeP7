package fr.vegeto52.prototypep7.ui.mapViewFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.data.viewModelFactory.ViewModelFactory;
import fr.vegeto52.prototypep7.databinding.FragmentMapViewBinding;
import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.model.User;
import fr.vegeto52.prototypep7.ui.detailsRestaurantFragment.DetailsRestaurantFragment;
import fr.vegeto52.prototypep7.ui.MainActivity;



public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    MapViewViewModel mMapViewViewModel;
    FragmentMapViewBinding mBinding;
    private MapView mMapView;
    private GoogleMap mMap;
    LatLng mUserLocation;
    private double mUserLatitude;
    private double mUserLongitude;
    private List<Marker> mMarkerList = new ArrayList<>();
    String mPlaceId;

    ImageButton mButtonCenterMap;
    private BottomNavigationView mBottomNavigationView;
    Location mLocation;
    List<Restaurant.Results> mRestaurantsList;

    List<User> mUserList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMapViewBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mMapView = view.findViewById(R.id.mapview);
        mButtonCenterMap = view.findViewById(R.id.button_center_map);


        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        centerCameraPosition();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initViewModel(){
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        mMapViewViewModel = new ViewModelProvider(this, viewModelFactory).get(MapViewViewModel.class);
        mMapViewViewModel.getMapViewMutableLiveData().observe(getViewLifecycleOwner(), new Observer<MapViewViewState>() {
            @Override
            public void onChanged(MapViewViewState mapViewViewState) {
                mLocation = mapViewViewState.getLocation();
                mRestaurantsList = mapViewViewState.getResults();
                mUserList = mapViewViewState.getUserList();

                addUserMarker();

                Log.d("Vérification MapView", "initViewmodel : " + mLocation + " " + mRestaurantsList + " " + mUserList);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            MainActivity activity = (MainActivity) context;
            mBottomNavigationView = activity.getBottomNavigationView();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (isAdded() && isVisible()) {
            if (mBottomNavigationView != null) {
                mBottomNavigationView.setVisibility(View.VISIBLE);
            }
        }
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        initViewModel();
    }

    private void addUserMarker(){
        mUserLatitude = mLocation.getLatitude();
        mUserLongitude = mLocation.getLongitude();
        mUserLocation = new LatLng(mUserLatitude, mUserLongitude);
        mMap.addMarker(new MarkerOptions().position(mUserLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mUserLocation));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mUserLocation, 17);
        mMap.animateCamera(cameraUpdate);
        markersToResto();
    }

    private void centerCameraPosition(){
        mButtonCenterMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mUserLocation));
            }
        });
    }

    private void markersToResto(){
        List<String> selectedRestos = new ArrayList<>();
        for (User user : mUserList){
            String selectedResto = user.getSelectedResto();
            if (selectedResto != null){
                selectedRestos.add(selectedResto);
            }
        }
        for (Restaurant.Results restaurant : mRestaurantsList){
            mPlaceId = restaurant.getPlace_id();
            LatLng restaurantLocation = new LatLng(restaurant.getGeometry().getLocation().getLat(), restaurant.getGeometry().getLocation().getLng());
            MarkerOptions markerOptions;
            if (selectedRestos.contains(mPlaceId)){
                markerOptions = new MarkerOptions()
                        .position(restaurantLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                markerOptions = new MarkerOptions()
                        .position(restaurantLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(mPlaceId);
            mMarkerList.add(marker);
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                mPlaceId = marker.getTag().toString();
                Fragment fragment = new DetailsRestaurantFragment();
                Bundle args = new Bundle();
                args.putString("placeId", mPlaceId);
                fragment.setArguments(args);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 3){
                    for (Marker marker : mMarkerList){
                        marker.remove();
                    }
                    mMarkerList.clear();
                    performSearch(newText);
                } else {
                    markersToResto();
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void performSearch(String text){
        List<String> selectedRestos = new ArrayList<>();
        for (User user : mUserList){
            String selectedResto = user.getSelectedResto();
            if (selectedResto != null){
                selectedRestos.add(selectedResto);
            }
        }
        for (Restaurant.Results restaurant : mRestaurantsList){
            if (restaurant.getName().toLowerCase().contains(text.toLowerCase())) {
                mPlaceId = restaurant.getPlace_id();
                LatLng restaurantLocation = new LatLng(restaurant.getGeometry().getLocation().getLat(), restaurant.getGeometry().getLocation().getLng());
                MarkerOptions markerOptions;
                if (selectedRestos.contains(mPlaceId)) {
                    markerOptions = new MarkerOptions()
                            .position(restaurantLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else {
                    markerOptions = new MarkerOptions()
                            .position(restaurantLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(mPlaceId);
                mMarkerList.add(marker);
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    mPlaceId = marker.getTag().toString();
                    Fragment fragment = new DetailsRestaurantFragment();
                    Bundle args = new Bundle();
                    args.putString("placeId", mPlaceId);
                    fragment.setArguments(args);

                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
            });
        }
    }
}