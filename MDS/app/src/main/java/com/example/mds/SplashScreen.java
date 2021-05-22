package com.example.mds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN=5000;
    //variables
    Animation topAnim,bottomAnim;
    ImageView imageView;
    ProgressBar progressBar;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        textView=findViewById(R.id.textView);
        imageView=findViewById(R.id.imageView);
        progressBar=findViewById(R.id.progressBar);
        imageView.setAnimation(topAnim);
        progressBar.setAnimation(bottomAnim);
        textView.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,Login.class);
                Pair[] pairs=new Pair[1];
                pairs[0]=new Pair<View,String>(imageView,"logo_image");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);

                startActivity(intent,options.toBundle());
                finish();
            }
        },SPLASH_SCREEN);
    }
}
