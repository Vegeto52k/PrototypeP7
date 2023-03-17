package fr.vegeto52.prototypep7.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static volatile UserRepository instance;
    private FirebaseFirestore mFirebaseFirestore;

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
}
