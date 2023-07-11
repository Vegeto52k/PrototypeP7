package fr.vegeto52.prototypep7.ui.detailsRestaurantFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 20/04/2023.
 */
public class PeopleRestoDetailsAdapter extends RecyclerView.Adapter<PeopleRestoDetailsAdapter.ViewHolder> {

    List<User> mUserList;
    private String mUrlPhotoPeople;

    public PeopleRestoDetailsAdapter(List<User> userList) {
        mUserList = userList;
    }

    @NonNull
    @Override
    public PeopleRestoDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_people_resto_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleRestoDetailsAdapter.ViewHolder holder, int position) {
        holder.displayPeopleRestoDetails(mUserList.get(position));
        mUrlPhotoPeople = mUserList.get(position).getUrlPhoto();
        Glide.with(holder.mPhotoPeople.getContext())
                .load(mUrlPhotoPeople)
                .centerCrop()
                .into(holder.mPhotoPeople);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView mPhotoPeople;
        public TextView mNamePeople;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPhotoPeople = itemView.findViewById(R.id.photo_workmates);
            mNamePeople = itemView.findViewById(R.id.name_workmates);
        }

        public void displayPeopleRestoDetails(User user){
            String fullName = user.getUserName();
            String[] nameParts = fullName.split(" ");
            String firstName = nameParts[0];
            mNamePeople.setText(firstName + " is joining!");
        }
    }
}


