package com.example.mds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    TextInputEditText email,password,age,fullName;
    Button register;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        email= findViewById(R.id.Email_register);
        password=findViewById(R.id.password_register);
        age=findViewById(R.id.Age_register);
        fullName=findViewById(R.id.fullName_register);
        register=findViewById(R.id.register);
        register.setOnClickListener(this);



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                registerUser();
                break;
        }
    }

    private void registerUser() {

      if(fullName.getText().toString().trim().isEmpty()){
          fullName.setError("Full name is required");
          fullName.requestFocus();
          return;

      }
        if(age.getText().toString().trim().isEmpty()){
            age.setError("Age is required");
            age.requestFocus();
            return;

        }
        if(email.getText().toString().trim().isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher((CharSequence) email.getText().toString().trim()).matches()){
            email.setError("Provide valid email");
            email.requestFocus();
            return;

        }
        if(password.getText().toString().trim().isEmpty()){
            password.setError("Password is required");
            email.requestFocus();
            return;

        }
        if(email.getText().toString().trim().length()<6){
            password.setError("Password must contain at least 6 characters");
            password.requestFocus();
            return;

        }
        final String email_string=email.getText().toString().trim() ;
        String password_string=password.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email_string,password_string)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser=mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid=firebaseUser.getUid();


                            HashMap<String,String> user=new HashMap<>();
                            user.put("id",userid);
                            user.put("email",email_string);
                            user.put("age",age.getText().toString().trim());
                            user.put("fullName",fullName.getText().toString().trim());

                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Intent intent=new Intent(Register.this,MainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Register.this, "Error on database connection!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else{
                            Toast.makeText(Register.this, "Try again with different credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
