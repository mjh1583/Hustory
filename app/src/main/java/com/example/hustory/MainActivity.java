package com.example.hustory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button logoutBTN;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private SharedPreferences auto;
    private SharedPreferences.Editor auto_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        currentUser = mAuth.getCurrentUser();

        Log.i(TAG, "onStart()");
        if(currentUser == null) {
            startLoginActivity();
        }
    }

    private void init() {
        logoutBTN = findViewById(R.id.logoutBTN);

        auto = PreferenceManager.getDefaultSharedPreferences(this);
        auto_editor = auto.edit();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.logoutBTN:
                        mAuth.signOut();
                        auto_editor.clear();
                        auto_editor.apply();
                        startToast("로그아웃했습니다,");
                        startLoginActivity();
                        break;
                }
            }
        };

        logoutBTN.setOnClickListener(onClickListener);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}