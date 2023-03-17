package fr.vegeto52.prototypep7.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.model.Restaurant;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {


    private List<Restaurant.Results> mRestaurants;
    private String mphotoReference;

    String baseUrl = "https://maps.googleapis.com/maps/api/place/photo";
    String width = "?maxwidth=400";
    String photoReference = "&photo_reference=";
    String key ="&key=AIzaSyArVUpejXwZw7QhmdFpVY9rHai7Y2adWrI";


    public RestaurantAdapter(List<Restaurant.Results> restaurants){
        mRestaurants = restaurants;
    }

    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, int position) {
        holder.displayRestaurant(mRestaurants.get(position));
        mphotoReference = mRestaurants.get(position).getPhotos().get(0).getPhoto_reference();
        Glide.with(holder.photo.getContext())
                .load(baseUrl + width + photoReference + mphotoReference + key)
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        Log.d("Taille liste", " " + mRestaurants.size());
        return mRestaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        RestaurantAdapter mRestaurantAdapter;

        public TextView name;
        public TextView adress;
        public ImageView photo;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_restaurant);
            adress = itemView.findViewById(R.id.adress_restaurant);
            photo = itemView.findViewById(R.id.photo_restaurant);
        }

        public ViewHolder linkAdapter(RestaurantAdapter adapter){
            this.mRestaurantAdapter = adapter;
            return this;
        }

        public void displayRestaurant(Restaurant.Results results){
            name.setText(results.getName());
            adress.setText(results.getVicinity());
        }
    }
}
