package com.example.mds;

import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

public interface ExerciseCallback {

    void onExerciseItemClick(int pos, RoundedImageView img, TextView textName, RatingBar ratingBar, TextView createdBy, TextView story);
}
