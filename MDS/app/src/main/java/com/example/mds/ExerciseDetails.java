package com.example.mds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class ExerciseDetails extends AppCompatActivity {

    RoundedImageView imgExercise;
    ImageView backArrow;
    TextView textName,textCreateBy,textStory;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        imgExercise=findViewById(R.id.imageExercise);
        ratingBar=findViewById(R.id.ratingBar);
        textName=findViewById(R.id.ExerciseNameDetails);
        textCreateBy=findViewById(R.id.textCreateBy);
        textStory=findViewById(R.id.textStory);

        backArrow=findViewById(R.id.BackToLogin);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Exercise item =(Exercise) getIntent().getExtras().getSerializable("exerciseObject");
        loadExercise(item);
    }

    private void loadExercise(Exercise item) {
        Picasso.get().load(item.getImage()).into(imgExercise);
        ratingBar.setRating(item.getRating());
        textName.setText(item.getTitle());
        textCreateBy.setText(item.getAreas());
        textStory.setText(item.getDescription());


    }
}
