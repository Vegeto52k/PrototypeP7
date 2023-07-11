package fr.vegeto52.prototypep7.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import fr.vegeto52.prototypep7.R;
import fr.vegeto52.prototypep7.data.repository.PlaceDetailsRepository;
import fr.vegeto52.prototypep7.model.RestaurantDetails;
import fr.vegeto52.prototypep7.model.User;
import fr.vegeto52.prototypep7.ui.MainActivity;

/**
 * Created by Vegeto52-PC on 29/05/2023.
 */
public class NotificationsService extends FirebaseMessagingService {

    private final int NOTIFICATION_ID = 007;
    private final String NOTIFICATION_TAG = "FIREBASEOC";
    private final String CHANNEL_ID = "MyAppChannel";

    PlaceDetailsRepository mPlaceDetailsRepository = new PlaceDetailsRepository();

    User mUser;
    String mUserRestoSelected;
    String titleText = null;
    String contentText = null;
    List<String> mUserNames = new ArrayList<>();


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getNotification() != null) {
            // Get message sent by Firebase
            RemoteMessage.Notification notification = message.getNotification();
            Log.d("Test de notifications", notification.getBody());
            sendVisualNotification(notification);
        }
    }

    private void sendVisualNotification(RemoteMessage.Notification notification) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mUser = documentSnapshot.toObject(User.class);
                    mUserRestoSelected = documentSnapshot.getString("selectedResto");

                    if (!Objects.equals(mUserRestoSelected, "")) {
                        getNameRestaurant(mUserRestoSelected, new RestaurantNameCallback() {
                            @Override
                            public void onRestaurantNameLoaded(String restaurantName, String restaurantAddress) {
                                titleText = restaurantName;
                                contentText = restaurantAddress;

                                getListName(mUserRestoSelected, new DataListener() {
                                    @Override
                                    public void onDataLoaded(List<String> userNames) {
                                        StringJoiner joiner = new StringJoiner("\n");
                                        for (String userName : userNames){
                                            joiner.add(userName);
                                        }
                                        String joinedNames = joiner.toString();

                                        // Create an Intent that will be shown when user will click on the Notification
                                        Intent intent = new Intent(NotificationsService.this, MainActivity.class);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationsService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                        // Build a Notification object
                                        NotificationCompat.Builder notificationBuilder =
                                                new NotificationCompat.Builder(NotificationsService.this, CHANNEL_ID)
                                                        .setSmallIcon(R.drawable.icon_app)
                                                        .setContentTitle(getResources().getString(R.string.recap_for_lunch))
                                                        //        .setContentText("Voici l'addresse du restaurant : " + contentText + ".")
                                                        .setAutoCancel(true)
                                                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                                        .setContentIntent(pendingIntent);

                                        String infoLunch = getResources().getString(R.string.info_lunch);
                                        String formattedInfoLunch = String.format(infoLunch, titleText, contentText, joinedNames);

                                        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                                                .bigText(formattedInfoLunch);
                                        notificationBuilder.setStyle(bigTextStyle);

                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                        // Support Version >= Android 8
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            CharSequence channelName = "Firebase Messages";
                                            int importance = NotificationManager.IMPORTANCE_HIGH;
                                            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, channelName, importance);
                                            notificationManager.createNotificationChannel(mChannel);
                                        }

                                        // Show notification
                                        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
                                    }
                                });


                            }
                        });
                    }
                }
            }
        });
    }

    public interface RestaurantNameCallback {
        void onRestaurantNameLoaded(String restaurantName, String restaurantAddress);
    }

    private void getNameRestaurant(String placeId, RestaurantNameCallback callback) {
        mPlaceDetailsRepository.getPlaceDetails(placeId);
        mPlaceDetailsRepository.getPlaceDetailsMutableLiveData().observeForever(new Observer<RestaurantDetails.Result>() {
            @Override
            public void onChanged(RestaurantDetails.Result result) {
                if (result != null) {
                    callback.onRestaurantNameLoaded(result.getName(), result.getFormatted_address());
                }
            }
        });
    }

    public interface DataListener{
        void onDataLoaded(List<String> userNames);
    }

    private void getListName(String placeId, DataListener listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("selectedResto", placeId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String userName = documentSnapshot.getString("userName");
                            mUserNames.add(userName);
                        }
                        listener.onDataLoaded(mUserNames);
                    }
                });
    }
}
