package fr.vegeto52.prototypep7.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.databinding.FragmentDetailsRestaurantBinding;
import fr.vegeto52.prototypep7.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding mBinding;
    private BottomNavigationView mBottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view);

        mBottomNavigationView.setVisibility(View.GONE);

        return view;
    }

    //TODO Gestion Notifications à faire
    private void managementNotifications(){

    }
}