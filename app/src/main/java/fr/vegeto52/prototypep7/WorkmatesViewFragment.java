package fr.vegeto52.prototypep7;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fr.vegeto52.prototypep7.databinding.FragmentListViewBinding;
import fr.vegeto52.prototypep7.databinding.FragmentWorkmatesViewBinding;
import fr.vegeto52.prototypep7.model.User;
import fr.vegeto52.prototypep7.ui.MainActivity;
import fr.vegeto52.prototypep7.ui.PeopleRestoDetailsAdapter;
import fr.vegeto52.prototypep7.ui.WorkmatesViewAdapter;


public class WorkmatesViewFragment extends Fragment {

    FragmentWorkmatesViewBinding mBinding;
    RecyclerView mRecyclerView;
    List<User> mUserList = new ArrayList<>();
    private BottomNavigationView mBottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentWorkmatesViewBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        mRecyclerView = view.findViewById(R.id.recyclerview_list_workmates);

        initUI();

        return view;
    }

    private void initUI(){
        getUserList();
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
        mUserList.clear();

        if (isAdded() && isVisible()) {
            if (mBottomNavigationView != null) {
                mBottomNavigationView.setVisibility(View.VISIBLE);
            }
        }
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
    }

    private void getUserList(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    User user = documentSnapshot.toObject(User.class);
                    mUserList.add(user);
                }
                initRecyclerView();
                mBinding.listWorkmateViewEmpty.setVisibility(mUserList.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        WorkmatesViewAdapter workmatesViewAdapter = new WorkmatesViewAdapter(mUserList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(workmatesViewAdapter);
    }
}