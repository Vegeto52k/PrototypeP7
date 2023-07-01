package fr.vegeto52.prototypep7.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.vegeto52.prototypep7.AuthenticationActivity;
import fr.vegeto52.prototypep7.ListViewFragment;
import fr.vegeto52.prototypep7.MapViewFragment;
import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.WorkmatesViewFragment;
import fr.vegeto52.prototypep7.data.repository.LocationRepository;
import fr.vegeto52.prototypep7.data.repository.UserRepository;
import fr.vegeto52.prototypep7.databinding.ActivityMainBinding;
import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.model.User;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    NearbySearchViewModel mNearbySearchViewModel = new NearbySearchViewModel();
    LocationRepository mLocationRepository = new LocationRepository();
    UserRepository mUserRepository = new UserRepository();

    private ActivityMainBinding mBinding;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;


    private ImageView mPhotoUserNav;
    private TextView mUsernameNav;
    private TextView mMailUserNav;
    ImageView mBackgroundNav;

    String mName;
    User mUser;

    private BottomNavigationView mBottomNavigationView;

    public BottomNavigationView getBottomNavigationView(){
        return mBottomNavigationView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableMyLocation();
//        initUI();
//        initViewModel();

    //    mUserRepository.createUser("1", "Arthur", "https://static.wikia.nocookie.net/kaamelott-officiel/images/8/85/Arthur.jpg/revision/latest/scale-to-width-down/250?cb=20210629111653&path-prefix=fr");
    //    mUserRepository.updateFavoriteRestoList("ChIJuwXhnAW6j4AR2aWGYgu5cLM", true);
    //    mUserRepository.updateFavoriteRestoList("ChIJOYvCo1W3j4AR1LAifgk13rs", false);
    //    mUserRepository.updateSelectedResto("ChIJOYvCo1W3j4AR1LAifgk13rs", true);
    //    mUserRepository.updateSelectedResto("ChIJK-D4c1a3j4ARz3kpN8rVwFo", true);

    //    mUserRepository.getUsername();
    //    mUserRepository.getUrlPhoto();
    //    mUserRepository.getPlaceIdResto();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            initUI();
        } else {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Si permission, alors location
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initUI();
            }
        }
    }

    private void initUI() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);
        mToolbar = findViewById(R.id.activity_main_toolbar);
        mNavigationView = findViewById(R.id.navigation_drawer);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);

        View hearderView = mNavigationView.getHeaderView(0);

        mPhotoUserNav = hearderView.findViewById(R.id.photo_user_nav_header);
        mUsernameNav = hearderView.findViewById(R.id.username_nav_header);
        mMailUserNav = hearderView.findViewById(R.id.mail_user_nav_header);
        mBackgroundNav = hearderView.findViewById(R.id.background_nav_header);


        Glide.with(this)
                .load("https://media.istockphoto.com/id/1018141890/fr/photo/deux-verres-%C3%A0-vin-vides-assis-dans-un-restaurant-par-un-chaud-apr%C3%A8s-midi-ensoleill%C3%A9.jpg?s=612x612&w=0&k=20&c=gajFyzYO1pxyLkm-P7l9yrj2vm_x1JqC3NMDXiRl46A=")
                .transform(new ColorFilterTransformation(Color.argb(100, 0, 0, 0)), new BlurTransformation(25))
                .into(mBackgroundNav);

        initToolbar();
        initInfoUser();
        itemNavSelected();


    //    ActionBar actionBar = getSupportActionBar();
    //    actionBar.setDisplayHomeAsUpEnabled(true);
    //    actionBar.setHomeAsUpIndicator(R.drawable.icon_menu_24);

        Fragment fragment = null;
        fragment = new MapViewFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        badgeSelected();


    }

    private void initToolbar(){
        setSupportActionBar(mToolbar);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
//        actionBar.show();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    mDrawerLayout.closeDrawer(GravityCompat.START);
//                } else {
//                    mDrawerLayout.openDrawer(GravityCompat.START);
//                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
//                }
//            }
//        });
    }

    private void itemNavSelected(){
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_nav_your_lunch:
                        itemNavYourLunchSelected();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.item_nav_settings:
                        itemNavSettingsSelected();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.item_nav_logout:
                        itemNavLogOutSelected();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void itemNavYourLunchSelected(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    mUser = documentSnapshot.toObject(User.class);

                    if (mUser.getSelectedResto() != null) {
                        Fragment fragment = new DetailsRestaurantFragment();
                        Bundle args = new Bundle();
                        args.putString("placeId", mUser.getSelectedResto());
                        fragment.setArguments(args);

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.no_restaurant_selected), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }

    private void itemNavSettingsSelected(){
        Fragment fragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void itemNavLogOutSelected(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(account != null){
            GoogleSignIn.getClient(MainActivity.this, gso).signOut();
        } else if (accessToken != null && !accessToken.isExpired()) {
            LoginManager.getInstance().logOut();
        }
        Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }

    private void badgeSelected(){
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.action_map_view:
                        fragment = new MapViewFragment();
                        break;
                    case R.id.action_list_view:
                        fragment = new ListViewFragment();
                        break;
                    case R.id.action_workmates:
                        fragment = new WorkmatesViewFragment();
                        break;
                }

                if (fragment != null){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
                return true;
            }
        });
    }

    private void initInfoUser(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    mUser = documentSnapshot.toObject(User.class);

                    mMailUserNav.setText(mUser.getAdressMail());
                    mUsernameNav.setText(mUser.getUserName());
                    Glide.with(MainActivity.this).load(mUser.getUrlPhoto()).into(mPhotoUserNav);
                }
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_search_view, menu);
//
//        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchMenuItem.getActionView();
//        searchView.setQueryHint("Type here to search");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//               if (newText.length() >= 3){
//                   Log.d("Test de la searchview", "Coucou, ça fonctionne !");
//               }
//               return true;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }
}