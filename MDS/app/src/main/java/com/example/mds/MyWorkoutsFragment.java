package com.example.mds;

import android.app.ActivityOptions;
import android.content.Intent;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.util.Pair;

public class MyWorkoutsFragment extends Fragment implements ExercisesListner,ExerciseCallback {
    private RecyclerView recyclerView;
    private Button buttonAddToFavourites;
    private List<Exercise> exercises;
    private FirebaseFirestore db;


    public static final String TAG="TAG";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_workouts, container, false);

        // Add the following lines to create RecyclerView
        db=FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.exercisesRecyclerView);

        exercises=new ArrayList<>();

        final ExercisesAdapter exercisesAdapter=new ExercisesAdapter(exercises,this,this);
        recyclerView.setAdapter(exercisesAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        Log.i("TAG",uid);
        db.collection("exercises").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=  queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            Log.i("obiect", String.valueOf(d));
                            Exercise obj=d.toObject(Exercise.class);

                            exercises.add(obj);
                        }
                        exercisesAdapter.notifyDataSetChanged();
                    }
                });





        buttonAddToFavourites=view.findViewById(R.id.buttonFavourites);

        buttonAddToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Exercise> selectedExercises=exercisesAdapter.getSelectedExercises();
                final StringBuilder exercisesNames=new StringBuilder();
                for(int i=0;i<selectedExercises.size();i++){
                    if (i==0){
                        exercisesNames.append(selectedExercises.get(i).title);
                    }else{
                        exercisesNames.append(",").append(selectedExercises.get(i).getTitle());
                    }
                }
                final String[] documentId = new String[1];
                //asynchronously retrieve all documents
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId());
                                       if(document.getData().toString().contains(uid)){
                                           documentId[0] =document.getId();

                                           DocumentReference documentReference=FirebaseFirestore.getInstance().collection("users")
                                                   .document(documentId[0]);

                                           Map<String,Object> map=new HashMap<>();
                                           map.put("favourites",exercisesNames.toString());
                                           documentReference.update(map)
                                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void aVoid) {
                                                           Toast.makeText(getContext(), "Favourites Updated", Toast.LENGTH_SHORT).show();
                                                       }
                                                   })
                                                   .addOnFailureListener(new OnFailureListener() {
                                                       @Override
                                                       public void onFailure(@NonNull Exception e) {
                                                           Log.e(TAG,"onFailure: ",e);
                                                       }
                                                   });
                                       }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });





            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        return view;
    }

    @Override
    public void onExerciseAction(Boolean isSelected) {
        if (isSelected){
            buttonAddToFavourites.setVisibility(View.VISIBLE);
        }else{
            buttonAddToFavourites.setVisibility(View.GONE);
        }
    }

    @Override
    public void onExerciseItemClick(int pos, RoundedImageView img, TextView textName, RatingBar ratingBar, TextView createdBy, TextView story) {
        Intent intent=new Intent(getContext(),ExerciseDetails.class);
        intent.putExtra("exerciseObject",exercises.get(pos));
        Pair<View,String> p1=Pair.create((View) img,"imageExercise");
        Pair<View,String> p2=Pair.create((View) textName,"textName");
        Pair<View,String> p3=Pair.create((View) ratingBar,"ratingBar");
        Pair<View,String> p4=Pair.create((View) createdBy,"textCreateBy");
        Pair<View,String> p5=Pair.create((View) story,"textStory");

        ActivityOptionsCompat optionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),p2,p1,p3,p4,p5);

        startActivity(intent,optionsCompat.toBundle());

    }
}
