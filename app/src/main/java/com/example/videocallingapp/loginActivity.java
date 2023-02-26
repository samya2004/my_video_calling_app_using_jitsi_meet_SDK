package com.example.videocallingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    EditText emailbox, passwordbox;
    Button loginbtn, signbtn;
    FirebaseAuth auth;
    public static final String SHARED_PREFS = "sharedprefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        auth=FirebaseAuth.getInstance();
        checkBox();
        emailbox = findViewById(R.id.emailbox);
        passwordbox = findViewById(R.id.passwordbox);
        loginbtn = findViewById(R.id.loginbtn);
        signbtn = findViewById(R.id.signupbtn);

        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this, signupActivity.class));
                finish();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = emailbox.getText().toString();
                password=passwordbox.getText().toString();

                if(email.length()!=0 && password.length()!=0)
                {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("name", "true");
                                editor.apply();
                                Toast.makeText(loginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(loginActivity.this, dashboardActivity.class));
                                finish();
                            }else {
                                Toast.makeText(loginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(loginActivity.this, "Please give email & password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkBox() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String check = sharedPreferences.getString("name", "");
        if (check.equals("true"))
        {
            startActivity(new Intent(loginActivity.this, dashboardActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}