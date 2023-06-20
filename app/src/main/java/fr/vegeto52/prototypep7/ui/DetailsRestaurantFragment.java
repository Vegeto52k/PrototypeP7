package fr.vegeto52.prototypep7.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.data.repository.PlaceDetailsRepository;
import fr.vegeto52.prototypep7.data.repository.UserRepository;
import fr.vegeto52.prototypep7.databinding.FragmentDetailsRestaurantBinding;
import fr.vegeto52.prototypep7.model.RestaurantDetails;
import fr.vegeto52.prototypep7.model.User;

public class DetailsRestaurantFragment extends Fragment {

    PlaceDetailsRepository mPlaceDetailsRepository = new PlaceDetailsRepository();
    UserRepository mUserRepository = new UserRepository();
    private FragmentDetailsRestaurantBinding mBinding;

    RestaurantDetails mRestaurantDetails = new RestaurantDetails();
    private boolean iconCheckedRestoSelected;
    private boolean iconLikeDislikeResto = true;
    RecyclerView mRecyclerViewPeopleRestoDetails;
    List<User> mUserList = new ArrayList<>();
    String mUserRestoSelected;
    List<String> mUserFavoriteResto = new ArrayList<>();

    String mPlaceId;
    BottomNavigationView mBottomNavigationView;
    ImageView mPhotoRestoReference;
    TextView mNameResto;
    TextView mAdressResto;
    ImageView mStarRating1;
    ImageView mStarRating2;
    ImageView mStarRating3;
    ImageButton mRestoSelected;
    Button mCallResto;
    Button mLikeResto;
    Button mWebsiteResto;
    RecyclerView mRecyclerView;


    String baseUrl = "https://maps.googleapis.com/maps/api/place/photo";
    String width = "?maxwidth=400";
    String photoReference = "&photo_reference=";
    String key ="&key=AIzaSyArVUpejXwZw7QhmdFpVY9rHai7Y2adWrI";


 //   @Override
 //   public void onCreate(@Nullable Bundle savedInstanceState) {
 //       super.onCreate(savedInstanceState);
 //       setHasOptionsMenu(false);
 //   }
//
 //   @Override
 //   public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
 //       super.onCreateOptionsMenu(menu, inflater);
 //       menu.clear();
 //   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        mBinding = FragmentDetailsRestaurantBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();


        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view);
        mPhotoRestoReference = view.findViewById(R.id.photo_restaurant_details);
        mNameResto = view.findViewById(R.id.name_restaurant_details);
        mAdressResto = view.findViewById(R.id.adress_restaurant_details);
        mStarRating1 = view.findViewById(R.id.icon_star_rating_details_1);
        mStarRating2 = view.findViewById(R.id.icon_star_rating_details_2);
        mStarRating3 = view.findViewById(R.id.icon_star_rating_details_3);
        mRestoSelected = view.findViewById(R.id.check_resto_selected);
        mCallResto = view.findViewById(R.id.call_resto_details);
        mLikeResto = view.findViewById(R.id.like_resto_details);
        mWebsiteResto = view.findViewById(R.id.website_resto_details);
        mRecyclerView = view.findViewById(R.id.recyclerview_list_people_resto_details);

        Bundle args = getArguments();
        if (args != null){
            mPlaceId = args.getString("placeId");
        }
        initUi();
        return view;
    }

    private void initUi(){
        mPlaceDetailsRepository.getPlaceDetails(mPlaceId);
        mPlaceDetailsRepository.getRestaurantDetailsMutableLiveData().observeForever(new Observer<RestaurantDetails.Result>() {
            @Override
            public void onChanged(RestaurantDetails.Result result) {
            //    ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
                mBottomNavigationView.setVisibility(View.GONE);
                mNameResto.setText(result.getName());
                mAdressResto.setText(result.getVicinity());
                photoReferenceView(result);
                starRatingUi(result);
                restoSelectedUi(result);
                callButton(result);
                likeDislikeButton(result);
                websiteButton(result);
                checkUserSelectedResto();
            }
        });
    }



    private void photoReferenceView(RestaurantDetails.Result result){
        String url = result.getPhotos().get(0).getPhoto_reference();
        Glide.with(this)
                .load(baseUrl + width + photoReference + url + key)
                .centerCrop()
                .into(mPhotoRestoReference);
    }

    private void starRatingUi(RestaurantDetails.Result result){
        double rating;
        rating = result.getRating();
        if (rating <= 1.25){
            mStarRating1.setVisibility(View.GONE);
            mStarRating2.setVisibility(View.GONE);
            mStarRating3.setVisibility(View.GONE);
        } else if (rating > 1.25 && rating <= 2.5) {
            mStarRating2.setVisibility(View.GONE);
            mStarRating3.setVisibility(View.GONE);
        } else if (rating > 2.5 && rating <= 3.75) {
            mStarRating3.setVisibility(View.GONE);
        }
    }

    private void restoSelectedUi(RestaurantDetails.Result result){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(currentUserId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                mUserRestoSelected = documentSnapshot.getString("selectedResto");
                if (mUserRestoSelected != null && mUserRestoSelected.equals(result.getPlace_id())) {
                    mRestoSelected.setImageResource(R.drawable.baseline_check_circle_24);
                    iconCheckedRestoSelected = true;
                } else {
                    mRestoSelected.setImageResource(R.drawable.baseline_cancel_24);
                    iconCheckedRestoSelected = false;
                }
            }
        })
                .addOnFailureListener(e -> {
                    Log.e("TAG", " ", e);
                });

        String placeId = result.getPlace_id();

        mRestoSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iconCheckedRestoSelected == true) {
                    db.collection("users").document(currentUserId).update("selectedResto", "");
                    mRestoSelected.setImageResource(R.drawable.baseline_cancel_24);
                    iconCheckedRestoSelected = false;
                    checkUserSelectedResto();
                } else {
                    db.collection("users").document(currentUserId).update("selectedResto", placeId);
                    mRestoSelected.setImageResource(R.drawable.baseline_check_circle_24);
                    iconCheckedRestoSelected = true;
                    checkUserSelectedResto();
                }
            }
        });
    }

    private void callButton(RestaurantDetails.Result result){
        mCallResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = result.getFormatted_phone_number();
                if (phoneNumber != null) {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                    startActivity(dialIntent);
                } else {
                    Toast toast = Toast.makeText(getContext(), getString(R.string.no_phone_number_provided), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void likeDislikeButton(RestaurantDetails.Result result){
        mPlaceId = result.getPlace_id();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(currentUserId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                mUserFavoriteResto = (List<String>) documentSnapshot.get("FAVORITE_RESTO_LIST");
                if (mUserFavoriteResto.contains(mPlaceId)){
                    mLikeResto.setText(getResources().getString(R.string.like));
                    mLikeResto.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.baseline_star_like_24,0,0);
                    iconLikeDislikeResto = false;
                } else {
                    mLikeResto.setText(getResources().getString(R.string.dislike));
                    mLikeResto.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.baseline_star_outline_24,0,0);
                    iconLikeDislikeResto = true;
                }
            }
        });
        mLikeResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iconLikeDislikeResto){
                    mUserFavoriteResto.add(mPlaceId);
                    db.collection("users").document(currentUserId).update("FAVORITE_RESTO_LIST", mUserFavoriteResto);
                    mLikeResto.setText(getResources().getString(R.string.like));
                    mLikeResto.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.baseline_star_like_24,0,0);
                    Toast toast = Toast.makeText(getContext(), getResources().getString(R.string.restaurant_added_to_favorites), Toast.LENGTH_SHORT);
                    toast.show();
                    iconLikeDislikeResto = false;
                } else {
                    mUserFavoriteResto.remove(mPlaceId);
                    db.collection("users").document(currentUserId).update("FAVORITE_RESTO_LIST", mUserFavoriteResto);
                    mLikeResto.setText(getResources().getString(R.string.dislike));
                    mLikeResto.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.baseline_star_outline_24,0,0);
                    Toast toast = Toast.makeText(getContext(), getResources().getString(R.string.restaurant_removed_from_favorites), Toast.LENGTH_SHORT);
                    toast.show();
                    iconLikeDislikeResto = true;
                }
            }
        });
    }

    private void websiteButton(RestaurantDetails.Result result) {
        mWebsiteResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = result.getWebsite();
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } else {
                //    Intent intent = new Intent(Intent.ACTION_VIEW);
                //    intent.setData(Uri.parse("https://twitter.com/home"));
                //    startActivity(intent);
                    Toast toast = Toast.makeText(getContext(), getResources().getString(R.string.no_website_provided), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void checkUserSelectedResto(){
        mUserList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String selectedResto = documentSnapshot.getString("selectedResto");
                    if (selectedResto != null && selectedResto.equals(mPlaceId)){
                        User user = documentSnapshot.toObject(User.class);
                        mUserList.add(user);
                    }
                }
                initRecyclerView();
            }
        });
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        PeopleRestoDetailsAdapter peopleRestoDetailsAdapter = new PeopleRestoDetailsAdapter(mUserList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(peopleRestoDetailsAdapter);
    }
}