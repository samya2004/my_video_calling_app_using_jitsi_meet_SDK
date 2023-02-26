package com.example.videocallingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class signupActivity extends AppCompatActivity {
    EditText emailbox, passwordbox, namebox;
    FirebaseAuth auth;
    FirebaseFirestore database;
    Button loginbtn, signbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        database = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        emailbox = findViewById(R.id.emailbox);
        namebox = findViewById(R.id.namebox);
        passwordbox = findViewById(R.id.passwordbox);
        loginbtn = findViewById(R.id.loginbtn);
        signbtn = findViewById(R.id.signupbtn);



        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, email, password;
                email=emailbox.getText().toString();
                password = passwordbox.getText().toString();
                name=namebox.getText().toString();

                User user = new User();
                user.setEmail(email);
                user.setPass(password);
                user.setName(name);
                if(email == null || email.length() == 0|| password ==null|| password.length()==0){
                    Toast.makeText(signupActivity.this, "empty", Toast.LENGTH_SHORT).show();
                }else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                database.collection("User")
                                                .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                startActivity(new Intent(signupActivity.this, loginActivity.class));
                                                finish();
                                            }

                                        });
                                Toast.makeText(signupActivity.this, "Account is created Successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(signupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signupActivity.this, loginActivity.class));
                finish();
            }

        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}