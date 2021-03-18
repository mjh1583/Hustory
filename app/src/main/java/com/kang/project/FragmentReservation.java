package com.kang.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FragmentReservation extends Fragment implements TabHost.OnTabChangeListener {

    PreviousAdapter previousAdapter;
    AfterAdapter afterAdapter;

    View view;
    LinearLayout layout_flag;
    LinearLayout tab_previous;
    LinearLayout tab_after;

    TextView intent_reservation;

    private Context mContext;
    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;



    // firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private String professor;
    private String summary;
    private String date;
    private String way;
    private String place;
    private String allow;
    private String before_after_data;
    private String contents;
    private String time;
    private String uid;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_reservation, container, false);
        init();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init() {
        Typeface typeface = getResources().getFont(R.font.jalnan);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        layout_flag = (LinearLayout) view.findViewById(R.id.layout_flag);
        intent_reservation = (TextView) view.findViewById(R.id.intent_reservation);

        intent_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReservationActivity.class);
                startActivity(intent);
            }
        });

        tab_previous = (LinearLayout) view.findViewById(R.id.tab_previous);
        tab_after = (LinearLayout) view.findViewById(R.id.tab_after);

        TabHost tab = (TabHost) view.findViewById(android.R.id.tabhost);
        tab.setup();

        TabHost.TabSpec tab1 = tab.newTabSpec("1").setContent(R.id.tab_previous).setIndicator("상담전");
        TabHost.TabSpec tab2 = tab.newTabSpec("2").setContent(R.id.tab_after).setIndicator("상담후");

        tab.addTab(tab1);
        tab.addTab(tab2);
        tab.setOnTabChangedListener(this);

        for (int i = 0; i < tab.getTabWidget().getChildCount(); i++) {
            TextView tp = (TextView) tab.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tp.setTextColor(Color.parseColor("#FFC1C1C1"));
            tp.setTypeface(typeface);

            tab.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FAFAFA"));
        }

        TextView tp_select = (TextView) tab.getTabWidget().getChildAt(tab.getCurrentTab()).findViewById(android.R.id.title);
        tp_select.setTextColor(Color.parseColor("#FAFAFA"));

        tab.getTabWidget().setCurrentTab(0);
        tab.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#2F5596"));


        previousAdapter = new PreviousAdapter();
        ListView listview1 = (ListView) view.findViewById(R.id.list_previous);
        listview1.setAdapter(previousAdapter);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef.addChildEventListener(childEventListener);

        myRef.child("Reservation").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.i("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.i("firebase", String.valueOf(task.getResult().getValue()));
                    if (String.valueOf(task.getResult().getValue()).equals("null")){
                        layout_flag.setVisibility(View.VISIBLE);
                        Log.i("null", "왜 무한루프?123");

                        Log.i("count",myRef.child("Reservation").orderByChild("allow").toString());
                        return;
                    }else{
                        layout_flag.setVisibility(View.GONE);
                        for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                            HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                            professor = member.get("professor").toString();
                            summary = member.get("summary").toString();
                            date = member.get("date").toString();
                            way = member.get("way").toString();
                            place = member.get("place").toString();
                            allow = member.get("allow").toString();
                            before_after_data = member.get("before_after_data").toString();
                            if(before_after_data.equals("false")){
                                previousAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.professor), professor, "\" "+summary+" \"", date, way, place, allow);
                            }
                        }
                    }
                }
            }
        });
// previous 예제
//        previousAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.professor), "홍길동", "연봉 협상에 대하여", "02.18(목)", "온라인", "ZOOM", "수락 대기");


        afterAdapter = new AfterAdapter();
        ListView listview2 = (ListView) view.findViewById(R.id.list_after);
        listview2.setAdapter(afterAdapter);


        myRef.child("Reservation").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.i("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.i("firebase", String.valueOf(task.getResult().getValue()));
                    if (String.valueOf(task.getResult().getValue()).equals("null")){
                        Log.i("null", "왜 무한루프?123");
                        Log.i("count", "111");
                        Log.i("count",myRef.child("Reservation").orderByChild("allow").toString());
                        return;
                    }else{
                        for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                            HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                            professor = member.get("professor").toString();
                            summary = member.get("summary").toString();
                            date = member.get("date").toString();
                            way = member.get("way").toString();
                            place = member.get("place").toString();
                            allow = member.get("allow").toString();
                            before_after_data = member.get("before_after_data").toString();
                            if(before_after_data.equals("true")){
                                afterAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.professor), professor, "\" "+summary+" \"", date, way, place);
                            }
                            Log.i("firebase", String.valueOf(task.getResult().getValue()));
                            Log.i("count",myRef.child("Reservation").orderByChild("allow").toString());
//                            listview1.setAdapter(previousAdapter);
//                            datas.add(new BeforeData(professor, date, onOff, place, accept, contents));
//                            adapter = new BeforeDataAdapter(getActivity(), R.layout.activity_before_data, datas);
//                            setListAdapter(adapter);
                        }
                    }


                }
            }
        });

//        afterAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.professor), "홍길동", "취업 가능 여부", "01.12(화)", "오프라인", "테크카페");

        mContext = getContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        fab_main = (FloatingActionButton) view.findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton) view.findViewById(R.id.fab_sub1);
        fab_sub2 = (FloatingActionButton) view.findViewById(R.id.fab_sub2);

        fab_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReservationActivity.class);
                startActivity(intent);
            }
        });

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen) {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_close);
                    fab_sub2.startAnimation(fab_close);
                    fab_sub1.setClickable(false);
                    fab_sub2.setClickable(false);
                    isFabOpen = false;
                } else {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_open);
                    fab_sub2.startAnimation(fab_open);
                    fab_sub1.setClickable(true);
                    fab_sub2.setClickable(true);
                    isFabOpen = true;
                }
            }
        });

    }

    @Override
    public void onTabChanged(String tabId) {
        TabHost tab = (TabHost) view.findViewById(android.R.id.tabhost);
        TextView tp_select = (TextView) tab.getTabWidget().getChildAt(tab.getCurrentTab()).findViewById(android.R.id.title);

        for(int i = 0; i < tab.getTabWidget().getChildCount(); i++) {
            TextView tp = (TextView) tab.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tp.setTextColor(Color.parseColor("#FFC1C1C1"));

            tab.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FAFAFA"));
        }

        tp_select.setTextColor(Color.parseColor("#FAFAFA"));
        tab.getTabWidget().getChildAt(tab.getCurrentTab()).setBackgroundColor(Color.parseColor("#2F5596"));
    }
}