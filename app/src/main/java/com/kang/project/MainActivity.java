package com.kang.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentMain fragmentMain = new FragmentMain();
    private FragmentReservation fragmentReservation = new FragmentReservation();
    private FragmentMy fragmentMy = new FragmentMy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Project);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.mainItem:
                    transaction.replace(R.id.frameLayout, fragmentMain).commitAllowingStateLoss();
                    break;
                case R.id.searchItem:
                    transaction.replace(R.id.frameLayout, fragmentReservation).commitAllowingStateLoss();
                    break;
                case R.id.reservationItem:
                    transaction.replace(R.id.frameLayout, fragmentReservation).commitAllowingStateLoss();
                    break;
                case R.id.myItem:
                    transaction.replace(R.id.frameLayout, fragmentMy).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    public void init() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentMain).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }
}