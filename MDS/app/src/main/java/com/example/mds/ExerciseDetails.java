package com.example.mds;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ExerciseDetails extends AppCompatActivity {

    RoundedImageView imgExercise;
    ImageView backArrow;
    TextView textName, textCreateBy, textStory;
    RatingBar ratingBar;
    Button videoButton;
    Button pickVideoButton;
    private Uri video;
    private String videoPath;
    public Intent videoFile;
    public ProgressDialog pDialog;
    private static final String ROOT_URL = "http://127.0.0.1:5000/upload";
    private static Context context;

    public static Context getAppContext() {
        return ExerciseDetails.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExerciseDetails.context = getApplicationContext();
        setContentView(R.layout.activity_exercise_details);

        imgExercise = findViewById(R.id.imageExercise);
        ratingBar = findViewById(R.id.ratingBar);
        textName = findViewById(R.id.ExerciseNameDetails);
        textCreateBy = findViewById(R.id.textCreateBy);
        textStory = findViewById(R.id.textStory);
        backArrow = findViewById(R.id.BackToLogin);
        videoButton = findViewById(R.id.recordVideo);
        pickVideoButton = findViewById(R.id.pickVideoButton);


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
        pickVideoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("video/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Video"),  2);
                Intent intent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Downloads.EXTERNAL_CONTENT_URI);
                }
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent,"Select Video"),2);
            }
        });

        Exercise item = (Exercise) getIntent().getExtras().getSerializable("exerciseObject");
        loadExercise(item);
    }
    private Bitmap bitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2) {}
        if (resultCode == RESULT_OK && requestCode == 2) {
            Log.i("meTAG", "inainte de videoPath");
            videoPath = getRealPathFromURI(data.getData());
            Log.i("meTAG", "dupa videoPath");
//            Log.i("meTAG", videoPath);
/*
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uploadBitmap(bitmap);*/

//                    videoFile=data;
            //Encode Video To Strin     g With mig Base64.

            File tempFile = new File(videoPath);
            if (tempFile == null)
                Log.i("meTAG", "ESTE null");
//                    File tempFile = new File(data.getData());
            String encodedString = null;

            InputStream inputStream = null;


            Log.i("meTag", String.valueOf(tempFile.canRead()));
            Log.i("meTag", String.valueOf(tempFile.exists()));


            String pathSec = tempFile.getAbsolutePath();
            Log.i("meTag", pathSec);
            try {
                inputStream = new FileInputStream(tempFile);
            } catch (Exception e) {
                // TODO: handle exception
                Log.i("meTAG", String.valueOf(e));
            }
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = output.toByteArray();
            encodedString = Base64.encodeToString(bytes, Base64.NO_WRAP);
            Log.i("Strng", encodedString);

            sendMessage(encodedString);


        }
        if (resultCode == RESULT_OK && requestCode == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            VideoView videoView = new VideoView(this);
            videoView.setVideoURI(data.getData());
            videoView.start();
            builder.setTitle("Title");
            builder.setPositiveButton("positive", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
//                    videoPath = data.getData().getPath();
                    videoPath = getRealPathFromURI(data.getData());
                    Log.i("meTAG", videoPath);
/*
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uploadBitmap(bitmap);*/

//                    videoFile=data;
                    //Encode Video To Strin     g With mig Base64.

                    File tempFile = new File(videoPath);
                    if (tempFile == null)
                        Log.i("meTAG", "ESTE null");
//                    File tempFile = new File(data.getData());
                    String encodedString = null;

                    InputStream inputStream = null;


                        Log.i("meTag", String.valueOf(tempFile.canRead()));
                        Log.i("meTag", String.valueOf(tempFile.exists()));


                    String pathSec = tempFile.getAbsolutePath();
                    Log.i("meTag", pathSec);
                    try {
                        inputStream = new FileInputStream(tempFile);
                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.i("meTAG", String.valueOf(e));
                    }
                    byte[] bytes;
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    try {
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bytes = output.toByteArray();
                    encodedString = Base64.encodeToString(bytes, Base64.NO_WRAP);
                    Log.i("Strng", encodedString);

                    sendMessage(encodedString);

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

    private void sendMessage(final String token) {
        RequestQueue requestQueue = Volley.newRequestQueue(ExerciseDetails.this);
        String url = "http://192.168.1.2:5000/upload";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TAG", response);
                AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseDetails.context);
                builder.setTitle(response);


//                Dupa ce mesajul a fost postat se trimite inapoi catre Feed cu tokenul JWT
//                Intent intent = new Intent(PostMessage.this, Feed.class);
//                intent.putExtra("token", token);
//                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("meTAG", "este mesaj de eroare");
                Log.i("meTAG", String.valueOf(error));

            }
        }) {
            //            Override pentru getParams pentru a trimite mesajul
//            Override pentru getHeaders pentru a trimite Content-Type,tokenul de auth in header
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video", token);
                Log.i("meTAG", "este in getParams");
                return params;

            }
//
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                params.put("filename", new DataPart(pdfname ,inputData));
//                return params;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                params.put("Authorization", token);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}
