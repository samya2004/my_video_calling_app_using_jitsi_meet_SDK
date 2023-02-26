package com.example.videocallingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class dashboardActivity extends AppCompatActivity {
    EditText roomcode;
    Button joinbtn, sharebtn, logout;
    public static final String SHARED_PREFS = "sharedprefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        roomcode =findViewById(R.id.roomname);
        joinbtn = findViewById(R.id.joinbtn);
        sharebtn = findViewById(R.id.sharebtn);
        logout = findViewById(R.id.logout);

        URL serverURL;
        try {
            serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions =
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)
                            .setFeatureFlag("call-integration.enabled", false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roomcode.getText().toString().length()!=0)
                {
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(roomcode.getText().toString())
                            .setFeatureFlag("call-integration.enabled", false)
                            .build();
                    JitsiMeetActivity.launch(dashboardActivity.this, options);
                }
                else {
                    Toast.makeText(dashboardActivity.this, "Please enter a code", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roomcode.getText().toString().length()!=0)
                {
                    String string = roomcode.getText().toString();
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_TEXT, string);
                    i.setType("text/plain");
                    startActivity(i);
                }
                else {
                    Toast.makeText(dashboardActivity.this, "Please enter a code", Toast.LENGTH_SHORT).show();
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(dashboardActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", "false");
                editor.apply();
                startActivity(new Intent(dashboardActivity.this, loginActivity.class));
                finish();
            }
        });
    }

}