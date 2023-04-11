package fr.vegeto52.prototypep7.ui;

import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.type.TimeOfDay;

import java.util.Calendar;
import java.util.List;

import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.data.repository.LocationRepository;
import fr.vegeto52.prototypep7.data.repository.PlaceDetailsRepository;
import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.model.RestaurantDetails;

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

    PlaceDetailsViewModel mPlaceDetailsViewModel = new PlaceDetailsViewModel();


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
        Glide.with(holder.photoRestaurant.getContext())
                .load(baseUrl + width + photoReference + mphotoReference + key)
                .into(holder.photoRestaurant);


    }

    @Override
    public int getItemCount() {
        Log.d("Taille liste", " " + mRestaurants.size());
        return mRestaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        RestaurantAdapter mRestaurantAdapter;
        PlaceDetailsViewModel mPlaceDetailsViewModel = new PlaceDetailsViewModel();
        PlaceDetailsRepository mPlaceDetailsRepository = new PlaceDetailsRepository();
        LocationRepository mLocationRepository = new LocationRepository();
        RestaurantDetails.Result mRestaurantDetails;
        Location mLocation;
        Location mLocationUser;
        double mLatitude;
        double mLongitude;
        String mPlaceId;
        double mRating;

        public TextView nameRestaurant;
        public TextView adressRestaurant;
        public ImageView photoRestaurant;
        public TextView openHour;
        public TextView distance;
        public TextView numberPerson;
        ImageView iconPerson;
        ImageView iconStarRating1;
        ImageView iconStarRating2;
        ImageView iconStarRating3;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameRestaurant = itemView.findViewById(R.id.name_restaurant);
            adressRestaurant = itemView.findViewById(R.id.adress_restaurant);
            photoRestaurant = itemView.findViewById(R.id.photo_restaurant);
            openHour = itemView.findViewById(R.id.open_hour);
            distance = itemView.findViewById(R.id.distance);
            numberPerson = itemView.findViewById(R.id.number_person);
            iconPerson = itemView.findViewById(R.id.icon_person);
            iconStarRating1 = itemView.findViewById(R.id.icon_star_rating_1);
            iconStarRating2 = itemView.findViewById(R.id.icon_star_rating_2);
            iconStarRating3 = itemView.findViewById(R.id.icon_star_rating_3);
        }

        public ViewHolder linkAdapter(RestaurantAdapter adapter){
            this.mRestaurantAdapter = adapter;
            return this;
        }

        public void displayRestaurant(Restaurant.Results results){

            nameRestaurant.setText(results.getName());
            adressRestaurant.setText(results.getVicinity());

            mLatitude = results.getGeometry().getLocation().getLat();
            mLongitude = results.getGeometry().getLocation().getLng();
            mLocation = new Location("");
            mLocation.setLatitude(mLatitude);
            mLocation.setLongitude(mLongitude);
            mLocationRepository.getLocationFromRepo().observeForever(new Observer<Location>() {
                @Override
                public void onChanged(Location location) {
                    if (location != null) {
                        mLocationUser = location;
                        float distanceUserRestaurant = mLocationUser.distanceTo(mLocation);
                        distance.setText(String.format("%.0f m", distanceUserRestaurant));
                    }
                }
            });
            mLocationRepository.getLocation();

            mRating = results.getRating();
            if (mRating <= 1.25){
                iconStarRating1.setVisibility(View.GONE);
                iconStarRating2.setVisibility(View.GONE);
                iconStarRating3.setVisibility(View.GONE);
            } else if (mRating > 1.25 && mRating <= 2.5) {
                iconStarRating2.setVisibility(View.GONE);
                iconStarRating3.setVisibility(View.GONE);
            } else if (mRating > 2.5 && mRating <= 3.75) {
                iconStarRating3.setVisibility(View.GONE);
            }

            mPlaceId = results.getPlace_id();
            mPlaceDetailsRepository.getPlaceDetails(mPlaceId);
            mPlaceDetailsRepository.getRestaurantDetailsMutableLiveData().observeForever(new Observer<RestaurantDetails.Result>() {
                @Override
                public void onChanged(RestaurantDetails.Result result) {
                   Log.d("Coucou2", "Je suis là !");
                   mRestaurantDetails = result;
                    if (result.getOpening_hours() != null){
                        List<RestaurantDetails.Periods> openingHours = result.getOpening_hours().getPeriods();
                        if (openingHours != null){
                            Calendar calendar = Calendar.getInstance();
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                            for (RestaurantDetails.Periods periods : openingHours){
                                if (periods.getOpen().getDay() == dayOfWeek){
                                    openHour.setText("Ouvert de " + periods.getOpen().getTime() + " à " + periods.getClose().getTime());
                                    break;
                                }
                            }
                        }
                    }
                }
            });



        //    openHour.setText();

        //    numberPerson.setText();



        }
    }
}
