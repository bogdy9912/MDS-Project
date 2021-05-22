package com.example.mds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder> {
Context context;
    private List<Exercise> exercises;
    ExerciseCallback callback;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ExercisesAdapter(Context context, List<Exercise> exercises, ExerciseCallback callback, ExercisesListner exercisesListner) {
        this.context = context;
        this.exercises = exercises;
        this.callback = callback;
        this.exercisesListner = exercisesListner;
    }

    private ExercisesListner exercisesListner;

    public ExercisesAdapter(List<Exercise> exercises, ExercisesListner exercisesListner,ExerciseCallback exerciseCallback) {
        this.exercises = exercises;
        this.exercisesListner = exercisesListner;
        this.callback=exerciseCallback;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExerciseViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_exercises,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.bindExercise(exercises.get(position));

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public List<Exercise> getSelectedExercises(){
        List<Exercise> selectedExercises=new ArrayList<>();
        for(Exercise exercise:exercises){
            if(exercise.isSelected){
                selectedExercises.add(exercise);
            }
        }
        return selectedExercises;
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout layoutExercise;
        View viewBackground;
        RoundedImageView imageExercise;
        TextView textName,textCreatedBy,textStory;
        RatingBar ratingBar;
        ImageView imageSelected;


         ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutExercise=itemView.findViewById(R.id.layoutExercises);
            viewBackground=itemView.findViewById(R.id.viewBackground);
            imageExercise=itemView.findViewById(R.id.imageExercise);
            textName=itemView.findViewById(R.id.textName);
            textCreatedBy=itemView.findViewById(R.id.textCreateBy);
            textStory=itemView.findViewById(R.id.textStory);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            imageSelected=itemView.findViewById(R.id.imageSelected);

        }



        void bindExercise(final Exercise exercise){
            Picasso.get().load(exercise.getImage()).into(imageExercise);
             textName.setText(exercise.getTitle());
             textCreatedBy.setText(exercise.getAreas());
             textStory.setText(exercise.getDescription());
             ratingBar.setRating(exercise.getRating());
             if(exercise.isSelected){
                 viewBackground.setBackgroundResource(R.drawable.exercise_selected_background);
                 imageSelected.setVisibility(View.VISIBLE);
             }else{
                 viewBackground.setBackgroundResource(R.drawable.exercises_background);
                 imageSelected.setVisibility(View.GONE);
             }
            layoutExercise.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(exercise.isSelected) {
                        viewBackground.setBackgroundResource(R.drawable.exercises_background);
                        imageSelected.setVisibility(View.GONE);
                        exercise.isSelected = false;
                        if (getSelectedExercises().size() == 0) {
                            exercisesListner.onExerciseAction(false);
                        }
                    }else{
                            viewBackground.setBackgroundResource(R.drawable.exercise_selected_background);
                            imageSelected.setVisibility(View.VISIBLE);
                            exercise.isSelected=true;
                            exercisesListner.onExerciseAction(true);
                        }
                    return false;
                    }

            });
             layoutExercise.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                    callback.onExerciseItemClick(getAdapterPosition(),imageExercise,textName,ratingBar,textCreatedBy,textStory);
                 }
             });
        }
    }
}
