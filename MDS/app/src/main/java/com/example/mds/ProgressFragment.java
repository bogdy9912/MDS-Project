package com.example.mds;

import android.content.Intent;
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
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressFragment extends Fragment  implements ExercisesListner,ExerciseCallback  {


    private static final String TAG ="CEVA" ;
    private RecyclerView recyclerView;
    private List<Exercise> exercises;
    private FirebaseFirestore db;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        // Add the following lines to create RecyclerView
        db= FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.exercisesRecyclerView);

        exercises=new ArrayList<>();

        final ExercisesAdapter exercisesAdapter=new ExercisesAdapter(exercises,this,this);
        recyclerView.setAdapter(exercisesAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();




        final String[] favouriteExercices = new String[1];
        //asynchronously retrieve all documents
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getData().toString().contains(uid)){
                                    favouriteExercices[0]=document.getString("favourites");
                                    final String[] favouriteExercicesVector = favouriteExercices[0].split(",");
                                Log.d(TAG, "caca"+ favouriteExercicesVector[1]);



                                db.collection("exercises").get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                List<DocumentSnapshot> list=  queryDocumentSnapshots.getDocuments();
                                                for(DocumentSnapshot d:list){
                                                    Log.i("exercitii", String.valueOf(d));
                                                    Exercise obj=d.toObject(Exercise.class);
                                                    for(int i=0;i<favouriteExercicesVector.length;i++){
                                                        if(favouriteExercicesVector[i].equals(obj.getTitle())){
                                                            exercises.add(obj);
                                                        }else {
                                                            continue;
                                                        }
                                                    }

                                                }
                                                exercisesAdapter.notifyDataSetChanged();
                                            }
                                        });
                                }
//
//

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        return view;
    }

    @Override
    public void onExerciseAction(Boolean isSelected) {

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
