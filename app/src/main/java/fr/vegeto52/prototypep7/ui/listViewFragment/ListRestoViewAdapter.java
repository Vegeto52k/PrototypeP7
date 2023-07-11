package fr.vegeto52.prototypep7.ui.listViewFragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.data.repository.LocationRepository;
import fr.vegeto52.prototypep7.data.repository.NearbySearchRepository;
import fr.vegeto52.prototypep7.data.repository.PlaceDetailsRepository;
import fr.vegeto52.prototypep7.model.Restaurant;
import fr.vegeto52.prototypep7.model.RestaurantDetails;
import fr.vegeto52.prototypep7.model.User;
import fr.vegeto52.prototypep7.ui.detailsRestaurantFragment.DetailsRestaurantFragment;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public class ListRestoViewAdapter extends RecyclerView.Adapter<ListRestoViewAdapter.ViewHolder> {


    private static List<Restaurant.Results> mRestaurants;
    private static Location mUserLocation;
    private static RestaurantDetails.Result mRestaurantDetails;
    private static List<User> mUserList;
    private String mPhotoReference;

    String baseUrl = "https://maps.googleapis.com/maps/api/place/photo";
    String width = "?maxwidth=400";
    String photoReference = "&photo_reference=";
    String key ="&key=AIzaSyArVUpejXwZw7QhmdFpVY9rHai7Y2adWrI";



    public ListRestoViewAdapter(List<Restaurant.Results> restaurants, Location location, List<User> userList){
        mRestaurants = restaurants;
        mUserLocation = location;
//        mRestaurantDetails = restaurantDetails;
        mUserList = userList;
    }

    @NonNull
    @Override
    public ListRestoViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRestoViewAdapter.ViewHolder holder, int position) {
        holder.displayRestaurant(mRestaurants.get(position));
        mPhotoReference = mRestaurants.get(position).getPhotos().get(0).getPhoto_reference();
        Glide.with(holder.photoRestaurant.getContext())
                .load(baseUrl + width + photoReference + mPhotoReference + key)
                .centerCrop()
                .into(holder.photoRestaurant);
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

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ListRestoViewAdapter mListRestoViewAdapter;
        PlaceDetailsRepository mPlaceDetailsRepository = new PlaceDetailsRepository();
        LocationRepository mLocationRepository = new LocationRepository();
        NearbySearchRepository mNearbySearchRepository = new NearbySearchRepository();
        RestaurantDetails.Result mRestaurantDetails;
        Location mLocationUser;
        Location mLocation;
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

        Context mContext;



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

            mContext = itemView.getContext();
        }

        public void displayRestaurant(Restaurant.Results results){

            nameRestaurant.setText(results.getName());
            adressRestaurant.setText(results.getVicinity());

            mLatitude = results.getGeometry().getLocation().getLat();
            mLongitude = results.getGeometry().getLocation().getLng();
            mLocation = new Location("");
            mLocation.setLatitude(mLatitude);
            mLocation.setLongitude(mLongitude);

            if (mUserLocation != null){
                float distanceUserRestaurant = mUserLocation.distanceTo(mLocation);
                distance.setText(String.format(Locale.US, "%.0f m", distanceUserRestaurant));
                results.setDistance(distanceUserRestaurant);
            }

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
            mPlaceDetailsRepository.getPlaceDetailsMutableLiveData().observeForever(new Observer<RestaurantDetails.Result>() {
                @Override
                public void onChanged(RestaurantDetails.Result result) {
                   mRestaurantDetails = result;
                    if (mRestaurantDetails.getOpening_hours() != null){
                        List<RestaurantDetails.Periods> openingHours = mRestaurantDetails.getOpening_hours().getPeriods();
                        if (openingHours != null){
                            Calendar calendar = Calendar.getInstance();
                            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                            for (RestaurantDetails.Periods periods : openingHours){
                                if (periods.getOpen().getDay() == dayOfWeek){
                                    String openingTime = periods.getOpen().getTime();
                                    String closingTime = periods.getClose().getTime();
                                    String openingHour = openingTime.substring(0, 2);
                                    String openingMinutes = openingTime.substring(2, 4);
                                    String closingHour = closingTime.substring(0, 2);
                                    String closingMinutes = closingTime.substring(2, 4);

                                    String openingHoursString;
                                    if (Locale.getDefault().getLanguage().equals("fr")){
                                        openingHoursString = "Ouvert de " + openingHour + "h" + openingMinutes + " à " + closingHour + "h" + closingMinutes;
                                    } else {
                                        String formattedOpeningTime = formatTime(openingHour, openingMinutes);
                                        String formattedClosingTime = formatTime(closingHour, closingMinutes);
                                        openingHoursString = "Open from " + formattedOpeningTime + " to " + formattedClosingTime;
                                    }

                                    openHour.setText(openingHoursString);
                                    break;
                                } else {
                                    openHour.setText(mContext.getString(R.string.the_restaurant_is_closed_today));
                                }
                            }
                        }
                    } else {
                        openHour.setText(mContext.getString(R.string.no_information_on_opening_closing_hours));
                    }
                }
            });

        if (mUserList != null){
            int count = 0;
            for (User user : mUserList){
                if (user.getSelectedResto().equals(results.getPlace_id())){
                    count++;
                }
            }
            String numberOfPersonsFormat = mContext.getString(R.string.count_user);
            String numberOfPersonsString = String.format(numberOfPersonsFormat, count);
            numberPerson.setText(numberOfPersonsString);
            results.setWorkmates_selected(count);
        }
    }

        private String formatTime(String hour, String minutes) {
            int hourInt = Integer.parseInt(hour);
            String formattedHour = hourInt > 12 ? String.valueOf(hourInt - 12) : hour;
            String formattedMinutes = minutes;
            String amPm = hourInt >= 12 ? "pm" : "am";

            return formattedHour + ":" + formattedMinutes + amPm;
        }
    }
}
