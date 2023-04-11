package fr.vegeto52.prototypep7.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.data.repository.LocationRepository;
import fr.vegeto52.prototypep7.data.repository.UserRepository;
import fr.vegeto52.prototypep7.databinding.ActivityMainBinding;
import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.model.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    NearbySearchViewModel mNearbySearchViewModel = new NearbySearchViewModel();
    LocationRepository mLocationRepository = new LocationRepository();
    UserRepository mUserRepository = new UserRepository();

    private ActivityMainBinding mBinding;
    private List<Restaurant.Results> mResultsList;

    String mName;
    User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableMyLocation();
        initUI();
        initViewModel();

    //    mUserRepository.createUser("1", "Arthur", "https://static.wikia.nocookie.net/kaamelott-officiel/images/8/85/Arthur.jpg/revision/latest/scale-to-width-down/250?cb=20210629111653&path-prefix=fr");
    //    mUserRepository.updateFavoriteRestoList("ChIJuwXhnAW6j4AR2aWGYgu5cLM", true);
    //    mUserRepository.updateFavoriteRestoList("ChIJOYvCo1W3j4AR1LAifgk13rs", false);
    //    mUserRepository.updateSelectedResto("ChIJOYvCo1W3j4AR1LAifgk13rs", true);
    //    mUserRepository.updateSelectedResto("ChIJK-D4c1a3j4ARz3kpN8rVwFo", true);

    //    mUserRepository.getUsername();
    //    mUserRepository.getUrlPhoto();
        mUserRepository.getPlaceIdResto();

    }

    private void initUI() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
    }

    private void initViewModel() {
        mNearbySearchViewModel = new ViewModelProvider(this).get(NearbySearchViewModel.class);
        mNearbySearchViewModel.getRestaurants().observeForever(new Observer<List<Restaurant.Results>>() {
            @Override
            public void onChanged(List<Restaurant.Results> results) {
                mResultsList = results;
                initRecyclerView();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.recyclerview.setLayoutManager(layoutManager);
        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(mResultsList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mBinding.recyclerview.getContext(), layoutManager.getOrientation());
        mBinding.recyclerview.addItemDecoration(dividerItemDecoration);
        mBinding.recyclerview.setAdapter(restaurantAdapter);
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationRepository.getLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: called--grantResults.length: " + grantResults.length);
        // Si permission, alors location
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationRepository.getLocation();
            }
        }
    }
}