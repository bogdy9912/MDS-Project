package com.example.mds;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class ExerciseDetails extends AppCompatActivity {

    RoundedImageView imgExercise;
    ImageView backArrow;
    TextView textName, textCreateBy, textStory;
    RatingBar ratingBar;
    Button videoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        imgExercise = findViewById(R.id.imageExercise);
        ratingBar = findViewById(R.id.ratingBar);
        textName = findViewById(R.id.ExerciseNameDetails);
        textCreateBy = findViewById(R.id.textCreateBy);
        textStory = findViewById(R.id.textStory);
        backArrow = findViewById(R.id.BackToLogin);
        videoButton = findViewById(R.id.recordVideo);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        Exercise item = (Exercise) getIntent().getExtras().getSerializable("exerciseObject");
        loadExercise(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            VideoView videoView = new VideoView(this);
            videoView.setVideoURI(data.getData());
            videoView.start();
            builder.setTitle("Title");
            builder.setPositiveButton("positive", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.setNegativeButton("Negatove", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.setView(videoView).show();
        }
    }

    private void loadExercise(Exercise item) {
        Picasso.get().load(item.getImage()).into(imgExercise);
        ratingBar.setRating(item.getRating());
        textName.setText(item.getTitle());
        textCreateBy.setText(item.getAreas());
        textStory.setText(item.getDescription());


    }
}
