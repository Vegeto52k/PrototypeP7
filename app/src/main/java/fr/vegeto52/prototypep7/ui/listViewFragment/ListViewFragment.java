package fr.vegeto52.prototypep7.ui.listViewFragment;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.data.viewModelFactory.ViewModelFactory;
import fr.vegeto52.prototypep7.databinding.FragmentListViewBinding;
import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.model.RestaurantDetails;
import fr.vegeto52.prototypep7.model.User;
import fr.vegeto52.prototypep7.ui.MainActivity;



public class ListViewFragment extends Fragment {




    ListViewViewModel mListViewViewModel;

    Location mLocation;
    List<Restaurant.Results> mRestaurantsList;

    List<User> mUserList;
    RestaurantDetails.Result mRestaurantDetails;

    private FragmentListViewBinding mBinding;
    RecyclerView mRecyclerView;
    private BottomNavigationView mBottomNavigationView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentListViewBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        mRecyclerView = view.findViewById(R.id.recyclerview_list_resto);


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

    private void initViewModel(){
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        mListViewViewModel = new ViewModelProvider(this, viewModelFactory).get(ListViewViewModel.class);
        mListViewViewModel.getListViewMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ListViewViewState>() {
            @Override
            public void onChanged(ListViewViewState listViewViewState) {
                    mLocation = listViewViewState.getLocation();
                    mRestaurantsList = listViewViewState.getResults();
                    mUserList = listViewViewState.getUserList();
                    initRecyclerView();
                    mBinding.listRestoViewEmpty.setVisibility(mRestaurantsList.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        ListRestoViewAdapter listRestoViewAdapter = new ListRestoViewAdapter(mRestaurantsList, mLocation, mUserList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(listRestoViewAdapter);
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
        Collections.sort(mRestaurantsList, new Comparator<Restaurant.Results>() {
            @Override
            public int compare(Restaurant.Results results, Restaurant.Results t1) {
                return results.getName().compareToIgnoreCase(t1.getName());
            }
        });
        ListRestoViewAdapter listRestoViewAdapter = new ListRestoViewAdapter(mRestaurantsList, mLocation, mUserList);
        mRecyclerView.setAdapter(listRestoViewAdapter);
    }

    private void sortByDistance(){
        Collections.sort(mRestaurantsList, new Comparator<Restaurant.Results>() {
            @Override
            public int compare(Restaurant.Results results, Restaurant.Results t1) {
                float distance1 = results.getDistance();
                float distance2 = t1.getDistance();
                return Float.compare(distance1, distance2);
            }
        });
        ListRestoViewAdapter listRestoViewAdapter = new ListRestoViewAdapter(mRestaurantsList, mLocation, mUserList);
        mRecyclerView.setAdapter(listRestoViewAdapter);
    }

    private void sortByRating(){
        Collections.sort(mRestaurantsList, new Comparator<Restaurant.Results>() {
            @Override
            public int compare(Restaurant.Results results, Restaurant.Results t1) {
                double rating1 = results.getRating();
                double rating2 = t1.getRating();
                return Double.compare(rating2, rating1);
            }
        });
        ListRestoViewAdapter listRestoViewAdapter = new ListRestoViewAdapter(mRestaurantsList, mLocation, mUserList);
        mRecyclerView.setAdapter(listRestoViewAdapter);
    }

    private void sortByWorkmates(){
        Collections.sort(mRestaurantsList, new Comparator<Restaurant.Results>() {
            @Override
            public int compare(Restaurant.Results results, Restaurant.Results t1) {
                int workmatesSelected1 = results.getWorkmates_selected();
                int workmatesSelected2 = t1.getWorkmates_selected();
                return Integer.compare(workmatesSelected2, workmatesSelected1);
            }
        });
        ListRestoViewAdapter listRestoViewAdapter = new ListRestoViewAdapter(mRestaurantsList, mLocation, mUserList);
        mRecyclerView.setAdapter(listRestoViewAdapter);
    }

    private void performSearch(String query){
        ListRestoViewAdapter listRestoViewAdapter;
        if (query.length() >= 3){
            List<Restaurant.Results> filteredList = filterRestaurants(query);
            listRestoViewAdapter = new ListRestoViewAdapter(filteredList, mLocation, mUserList);
            mRecyclerView.setAdapter(listRestoViewAdapter);
        } else {
            listRestoViewAdapter = new ListRestoViewAdapter(mRestaurantsList, mLocation, mUserList);
            mRecyclerView.setAdapter(listRestoViewAdapter);
        }
        listRestoViewAdapter.notifyDataSetChanged();
    }

    private List<Restaurant.Results> filterRestaurants(String query){
        List<Restaurant.Results> filteredList = new ArrayList<>();
        for (Restaurant.Results restaurant : mRestaurantsList) {
            if (restaurant.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(restaurant);
            }
        }
        return filteredList;
    }
}