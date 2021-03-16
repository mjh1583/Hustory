package com.example.hustory;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Fragment 관련 변수
    FragmentManager manager;
    BeforeFragment beforeFragment;
    AfterFragment afterFragment;

    //멤버 변수
    private TextView reserveTXT;
    private Button beforeBTN, afterBTN;

    // 매개변수 받아서 Toast
    private void showToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fragment 관련 변수선언
        manager = getSupportFragmentManager();

        beforeFragment = new BeforeFragment();
        afterFragment = new AfterFragment();

        FragmentTransaction ft = manager.beginTransaction();
        ft.addToBackStack(null);
        ft.add(R.id.main_container, beforeFragment);
        ft.commit();

        init();
    }

    public void init(){
        reserveTXT = findViewById(R.id.reserveTXT);
        beforeBTN = findViewById(R.id.beforeBTN);
        afterBTN = findViewById(R.id.afterBTN);

    }

    // Fragment 이동 함수
    public void fragmentClick (View v){

        if(v == beforeBTN){
            if(!beforeFragment.isVisible()){
                FragmentTransaction ft = manager.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.main_container, beforeFragment);
                ft.commit();
            }
        }else if(v == afterBTN){
                if(!afterFragment.isVisible()){
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.main_container, afterFragment);
                    ft.commit();
                }
            }
        }


    public void onClick(View v){
        Intent moveINT = new Intent(MainActivity.this, ReservingActivity.class);

        // (2) Intent 전송 ==> startActivity()
        startActivity(moveINT);
    }


}