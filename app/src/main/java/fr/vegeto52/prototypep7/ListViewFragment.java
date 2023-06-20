package fr.vegeto52.prototypep7;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.vegeto52.prototypep7.data.repository.LocationRepository;
import fr.vegeto52.prototypep7.data.repository.UserRepository;
import fr.vegeto52.prototypep7.databinding.FragmentListViewBinding;
import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.ui.ListRestoViewAdapter;
import fr.vegeto52.prototypep7.ui.MainActivity;
import fr.vegeto52.prototypep7.ui.NearbySearchViewModel;


public class ListViewFragment extends Fragment {

    NearbySearchViewModel mNearbySearchViewModel = new NearbySearchViewModel();
    LocationRepository mLocationRepository = new LocationRepository();
    UserRepository mUserRepository = new UserRepository();

    private FragmentListViewBinding mBinding;
    private List<Restaurant.Results> mResultsList;
    RecyclerView mRecyclerView;
    private BottomNavigationView mBottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentListViewBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        mRecyclerView = view.findViewById(R.id.recyclerview_list_resto);

        enableMyLocation();
        initViewModel();


        return view;
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
    public void onResume() {
        super.onResume();

        if (isAdded() && isVisible()) {
            if (mBottomNavigationView != null) {
                mBottomNavigationView.setVisibility(View.VISIBLE);
            }
        }
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
    }

    private void initViewModel() {
        mNearbySearchViewModel = new ViewModelProvider(this).get(NearbySearchViewModel.class);
        mNearbySearchViewModel.getRestaurants().observeForever(new Observer<List<Restaurant.Results>>() {
            @Override
            public void onChanged(List<Restaurant.Results> results) {
                mResultsList = results;
                initRecyclerView();
                mBinding.listRestoViewEmpty.setVisibility(results.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        ListRestoViewAdapter listRestoViewAdapter = new ListRestoViewAdapter(mResultsList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(listRestoViewAdapter);
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationRepository.getLocation();
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
                mLocationRepository.getLocation();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        inflater.inflate(R.menu.menu_sort, menu);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    performSearch(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort_name:
                sortByName();
                return true;
            case R.id.sort_distance:
                sortByDistance();
                return true;
            case R.id.sort_rating:
                sortByRating();
                return true;
            case R.id.sort_workmates:
                sortByWorkmates();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sortByName(){
        Collections.sort(mResultsList, new Comparator<Restaurant.Results>() {
            @Override
            public int compare(Restaurant.Results results, Restaurant.Results t1) {
                return results.getName().compareToIgnoreCase(t1.getName());
            }
        });
        ListRestoViewAdapter listRestoViewAdapter = new ListRestoViewAdapter(mResultsList);
        mRecyclerView.setAdapter(listRestoViewAdapter);
    }

    private void sortByDistance(){
        Collections.sort(mResultsList, new Comparator<Restaurant.Results>() {
            @Override
            public int compare(Restaurant.Results results, Restaurant.Results t1) {
                float distance1 = results.getDistance();
                float distance2 = t1.getDistance();
                return Float.compare(distance1, distance2);
            }
        });
        ListRestoViewAdapter listRestoViewAdapter = new ListRestoViewAdapter(mResultsList);
        mRecyclerView.setAdapter(listRestoViewAdapter);
    }

    private void sortByRating(){
        Collections.sort(mResultsList, new Comparator<Restaurant.Results>() {
            @Override
            public int compare(Restaurant.Results results, Restaurant.Results t1) {
                double rating1 = results.getRating();
                double rating2 = t1.getRating();
                return Double.compare(rating2, rating1);
            }
        });
        ListRestoViewAdapter listRestoViewAdapter = new ListRestoViewAdapter(mResultsList);
        mRecyclerView.setAdapter(listRestoViewAdapter);
    }

    private void sortByWorkmates(){
        Collections.sort(mResultsList, new Comparator<Restaurant.Results>() {
            @Override
            public int compare(Restaurant.Results results, Restaurant.Results t1) {
                int workmatesSelected1 = results.getWorkmates_selected();
                int workmatesSelected2 = t1.getWorkmates_selected();
                return Integer.compare(workmatesSelected2, workmatesSelected1);
            }
        });
        ListRestoViewAdapter listRestoViewAdapter = new ListRestoViewAdapter(mResultsList);
        mRecyclerView.setAdapter(listRestoViewAdapter);
    }

    private void performSearch(String query){
        ListRestoViewAdapter listRestoViewAdapter;
        if (query.length() >= 3){
            List<Restaurant.Results> filteredList = filterRestaurants(query);
            listRestoViewAdapter = new ListRestoViewAdapter(filteredList);
            mRecyclerView.setAdapter(listRestoViewAdapter);
        } else {
            listRestoViewAdapter = new ListRestoViewAdapter(mResultsList);
            mRecyclerView.setAdapter(listRestoViewAdapter);
        }
        listRestoViewAdapter.notifyDataSetChanged();
    }

    private List<Restaurant.Results> filterRestaurants(String query){
        List<Restaurant.Results> filteredList = new ArrayList<>();
        for (Restaurant.Results restaurant : mResultsList) {
            if (restaurant.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(restaurant);
            }
        }
        return filteredList;
    }
}