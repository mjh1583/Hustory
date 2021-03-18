package com.example.hustory.reservation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.hustory.R;
import com.example.hustory.reservation.AfterAdapter;
import com.example.hustory.reservation.PreviousAdapter;
import com.example.hustory.reservation.ReservationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentReservation extends Fragment implements TabHost.OnTabChangeListener {

    PreviousAdapter previousAdapter;
    AfterAdapter afterAdapter;

    View view;
    LinearLayout layout_flag;
    LinearLayout tab_previous;
    LinearLayout tab_after;

    TextView intent_reservation;

    // 다이얼로그 변수
    private Dialog dlg_previous;
    private Dialog dlg_after;


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
    private String key;
    private String contents;
    private String firebase_key;


    public ArrayList<String> preArr = new ArrayList<>();
    public ArrayList<String> aftArr = new ArrayList<>();
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

        // adapter 생성
        previousAdapter = new PreviousAdapter();
        ListView listview1 = (ListView) view.findViewById(R.id.list_previous);
        listview1.setAdapter(previousAdapter);

        afterAdapter = new AfterAdapter();
        ListView listview2 = (ListView) view.findViewById(R.id.list_after);
        listview2.setAdapter(afterAdapter);



        FirebaseDatabase.getInstance().getReference().child("Member").child("v5N3FZkAIxTS2IpVV4ki2yUbRwJ2").child("R_List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                previousAdapter.clear();
                afterAdapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Log.d("MainActivity", "ValueEventListener : " + dataSnapshot.getValue());

                    if (dataSnapshot.getValue().equals("null")){
                        layout_flag.setVisibility(View.VISIBLE);
                        return;
                    }else{
                        layout_flag.setVisibility(View.GONE);
                        HashMap<String, Object> member = (HashMap<String, Object>) snapshot.getValue();
                        professor = member.get("professor").toString();
                        summary = member.get("summary").toString();
                        date = member.get("date").toString();
                        way = member.get("way").toString();
                        place = member.get("place").toString();
                        allow = member.get("allow").toString();
                        key = member.get("key").toString();
                        Log.i("key", key);

                        before_after_data = member.get("before_after_data").toString();
                        if(before_after_data.equals("false")){
                            preArr.add(key);
                            previousAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.professor), professor, "\" "+summary+" \"", date, way, place, allow);
                        }else if(before_after_data.equals("true")){
                            aftArr.add(key);
                            afterAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.professor), professor, "\" "+summary+" \"", date, way, place);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("remove", "없어");
            }
        });

        // 자세히 다이얼로그

        // 자세히 버튼 이벤트 상담 전
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater1 = getLayoutInflater();
                View view1 = inflater1.inflate(R.layout.dialog_previous, null);
                builder1.setView(view1);
                final AlertDialog dialog1 = builder1.create();
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                // dialog 세팅
                TextView text_professor = (TextView) view1.findViewById(R.id.text_professor);
                TextView text_summary = (TextView) view1.findViewById(R.id.text_summary);
                TextView reservation_date = (TextView) view1.findViewById(R.id.reservation_date);
                TextView reservation_way = (TextView) view1.findViewById(R.id.reservation_way);
                TextView reservation_place = (TextView) view1.findViewById(R.id.reservation_place);
                TextView content = (TextView) view1.findViewById(R.id.content);
                TextView reservation_state = (TextView) view1.findViewById(R.id.reservation_state);
                TextView reservation_time = (TextView) view1.findViewById(R.id.reservation_time);
                Log.i("position", ""+position);
                key = preArr.get(position);
                Log.i("key", key);
                myRef.child("Member").child("v5N3FZkAIxTS2IpVV4ki2yUbRwJ2").child("R_List").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                        }
                        else {
                            if (String.valueOf(task.getResult().getValue()).equals("null")){

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
                                    contents = member.get("contents").toString();
                                    before_after_data = member.get("before_after_data").toString();
                                    firebase_key = member.get("key").toString();
                                    time = member.get("time").toString();
                                    if(before_after_data.equals("false") && key.equals(firebase_key)){
                                        text_professor.setText(professor);
                                        text_summary.setText(summary);
                                        reservation_date.setText(date);
                                        reservation_way.setText(way);
                                        reservation_place.setText(place);
                                        content.setText(contents);
                                        reservation_state.setText(allow);
                                        reservation_time.setText(time);
                                    }
                                }
                            }
                        }
                    }
                });
                dialog1.show();
            }
        });


        // 자세히 버튼 이벤트 상담 후
        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater2 = getLayoutInflater();
                View view1 = inflater2.inflate(R.layout.dialog_after, null);
                builder2.setView(view1);
                final AlertDialog dialog2 = builder2.create();
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                // dialog 세팅
                TextView text_professor = (TextView) view1.findViewById(R.id.text_professor);
                TextView text_summary = (TextView) view1.findViewById(R.id.text_summary);
                TextView reservation_date = (TextView) view1.findViewById(R.id.reservation_date);
                TextView reservation_way = (TextView) view1.findViewById(R.id.reservation_way);
                TextView reservation_place = (TextView) view1.findViewById(R.id.reservation_place);
                TextView content = (TextView) view1.findViewById(R.id.content);
                Log.i("position", ""+position);

                key = aftArr.get(position);


                myRef.child("Member").child("v5N3FZkAIxTS2IpVV4ki2yUbRwJ2").child("R_List").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                        }
                        else {
                            if (String.valueOf(task.getResult().getValue()).equals("null")){

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
                                    contents = member.get("contents").toString();
                                    firebase_key = member.get("key").toString();
                                    before_after_data = member.get("before_after_data").toString();
                                    if(before_after_data.equals("true") && key.equals(firebase_key)){
                                        Log.i("key", key);
                                        text_professor.setText(professor);
                                        text_summary.setText(summary);
                                        reservation_date.setText(date);
                                        reservation_way.setText(way);
                                        reservation_place.setText(place);
                                        content.setText(contents);
                                    }
                                }
                            }
                        }
                    }
                });
                dialog2.show();
            }
        });



        // 질문 예약 네비게이션
        mContext = getContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        fab_main = (FloatingActionButton) view.findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton) view.findViewById(R.id.fab_sub1);
        fab_sub2 = (FloatingActionButton) view.findViewById(R.id.fab_sub2);

        fab_sub2.setOnClickListener(new View.OnClickListener() {
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