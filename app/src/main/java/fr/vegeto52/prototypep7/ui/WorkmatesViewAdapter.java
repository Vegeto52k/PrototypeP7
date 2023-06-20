package fr.vegeto52.prototypep7.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.data.repository.PlaceDetailsRepository;
import fr.vegeto52.prototypep7.model.RestaurantDetails;
import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 21/04/2023.
 */
public class WorkmatesViewAdapter extends RecyclerView.Adapter<WorkmatesViewAdapter.ViewHolder> {

    private String mUrlPhotoPeople;
    List<User> mUserList;


    public WorkmatesViewAdapter(List<User> userList) {
        mUserList = userList;
    }

    @NonNull
    @Override
    public WorkmatesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workmates, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewAdapter.ViewHolder holder, int position) {
        holder.displayWorkmatesView(mUserList.get(position));
        mUrlPhotoPeople = mUserList.get(position).getUrlPhoto();
        Glide.with(holder.mPhotoWorkmate.getContext())
                .load(mUrlPhotoPeople)
                .centerCrop()
                .into(holder.mPhotoWorkmate);

        if (!holder.mPlaceId.isEmpty()){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new DetailsRestaurantFragment();
                    Bundle args = new Bundle();
                    args.putString("placeId", holder.mPlaceId);
                    fragment.setArguments(args);


                    if (fragment != null && view.getContext() instanceof AppCompatActivity) {
                        ((AppCompatActivity) view.getContext()).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        PlaceDetailsRepository mPlaceDetailsRepository = new PlaceDetailsRepository();
        public CircleImageView mPhotoWorkmate;
        public TextView mNameWorkmate;
        String mPlaceId;
        Context mContext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPhotoWorkmate = itemView.findViewById(R.id.photo_workmates);
            mNameWorkmate = itemView.findViewById(R.id.name_workmates);

            mContext = itemView.getContext();
        }

        public void displayWorkmatesView(User user){
            mPlaceId = user.getSelectedResto();
            String fullName = user.getUserName();
            String[] nameParts = fullName.split(" ");
            String firstName = nameParts[0];
            if (!mPlaceId.isEmpty()) {
                mPlaceDetailsRepository.getPlaceDetails(mPlaceId);
                mPlaceDetailsRepository.getRestaurantDetailsMutableLiveData().observeForever(new Observer<RestaurantDetails.Result>() {
                    @Override
                    public void onChanged(RestaurantDetails.Result result) {
                        String eatingAtRestaurant = mContext.getString(R.string.is_eating_at);
                        String formattedString = String.format(eatingAtRestaurant, firstName, result.getName());
                        mNameWorkmate.setText(formattedString);
                    }
                });
            } else {
                String hasNotDecidedYet = mContext.getString(R.string.has_not_decided_yet);
                String formattedString = String.format(hasNotDecidedYet, firstName);
                mNameWorkmate.setText(formattedString);
                mNameWorkmate.setAlpha(0.5f);
            }

        }
    }
}
