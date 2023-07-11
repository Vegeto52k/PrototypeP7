package fr.vegeto52.prototypep7.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fr.vegeto52.prototypep7.model.User;

/**
 * Created by Vegeto52-PC on 27/06/2023.
 */
public class FirestoreRepository {

    private static final String COLLECTION_NAME = "users";
    List<User> mUserList = new ArrayList<>();
    private final MutableLiveData<List<User>> mListMutableLiveData = new MutableLiveData<>();

    public FirestoreRepository() {
        getListUsers();
    }

    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public void getListUsers(){
        getUsersCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    for (DocumentSnapshot documentSnapshot : documents){
                        User user = documentSnapshot.toObject(User.class);
                        String uid = documentSnapshot.getId();
                        user.setUid(uid);
                        mUserList.add(user);
                    }
                    mListMutableLiveData.setValue(mUserList);
                }
            }
        });
    }

    public LiveData<List<User>> getListMutableLiveData(){
        return mListMutableLiveData;
    }
}
