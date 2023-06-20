package fr.vegeto52.prototypep7.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.vegeto52.prototypep7.model.User;
import retrofit2.http.Body;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static volatile UserRepository instance;
    private FirebaseFirestore mFirebaseFirestore;

    MutableLiveData<List<User>> mListUserMutableLiveData = new MutableLiveData<>();
    List<User> mUserList = new ArrayList<>();

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public void createUser(String uid, String username, String urlPhoto) {

        Map<String, Object> userToCreate = new HashMap<>();
        userToCreate.put("Uid", uid);
        userToCreate.put("Username", username);
        userToCreate.put("UrlPhoto", urlPhoto);

        getUsersCollection().add(userToCreate).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Ajout d'un User", "Ajout réussi");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Ajout d'un User", "Ajout échoué");
            }
        });
    }

    public Task<DocumentSnapshot> getUserData(){
        String uid = "a4wDUUo750MVdFfyu1o4";
        if (uid != null) {
            return getUsersCollection().document(uid).get();
        }else{
            return null;
        }
    }

    public Task<User> getUserDataObject(){
        return getUserData().continueWith(task -> task.getResult().toObject(User.class));
    }

    public void updateFavoriteRestoList(String placeId, Boolean liked){
        String uid = "a4wDUUo750MVdFfyu1o4";
        if (uid != null) {
            if (liked) {
                getUsersCollection().document(uid).update(
                        "FAVORITE_RESTO_LIST",
                        FieldValue.arrayUnion(placeId));
            }else{
                getUsersCollection().document(uid).update(
                        "FAVORITE_RESTO_LIST",
                        FieldValue.arrayRemove(placeId));
            }
        }
    }

    public void updateSelectedResto(String placeId, Boolean selected){
        String uid = "a4wDUUo750MVdFfyu1o4";
        if (uid != null){
            if (selected){
                getUsersCollection().document(uid).update(
                        "selectedResto",
                        FieldValue.delete());
                getUsersCollection().document(uid).update(
                      "selectedResto",
                        FieldValue.arrayUnion(placeId));
            }
        }
    }

    public void getUsername(){
        getUsersCollection().document("a4wDUUo750MVdFfyu1o4").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String username = documentSnapshot.getString("Username");
                            Log.d("Username", "Réussi : " + username);
                        }else{
                            Log.d("Username", "Echec");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Username", "Echec : username non trouvé");
                    }
                });
    }

    public void getUrlPhoto(){
        getUsersCollection().document("a4wDUUo750MVdFfyu1o4").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String urlPhoto = documentSnapshot.getString("UrlPhoto");
                            Log.d("UrlPhoto", "Réussi : " + urlPhoto);
                        }else{
                            Log.d("UrlPhoto", "Echec");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("UrlPhoto", "Echec : UrlPhoto non trouvé");
                    }
                });
    }

    public void getPlaceIdResto(){
        getUsersCollection().document("a4wDUUo750MVdFfyu1o4").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String favoriteRestoList = documentSnapshot.get("FAVORITE_RESTO_LIST").toString();
                            Log.d("Favorite resto list", "Réussi : " + favoriteRestoList);
                        }else{
                            Log.d("Favorite resto list", "Echec");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Favorite resto list", "Echec : username non trouvé");
                    }
                });
    }

    public void getUserList(){
        getUsersCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshots){
                    User user = documentSnapshot.toObject(User.class);
                    mUserList.add(user);
                }
                mListUserMutableLiveData.setValue(mUserList);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public MutableLiveData<List<User>> getListUserFromRepo(String placeId){
        return mListUserMutableLiveData;
    }
}
