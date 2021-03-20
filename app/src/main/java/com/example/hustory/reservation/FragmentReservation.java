package com.example.hustory.reservation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;

public class FragmentReservation extends Fragment implements TabHost.OnTabChangeListener {
    int flag_content = 0;
    int flag;

    PreviousAdapter previousAdapter;
    AfterAdapter afterAdapter;;
    StatusAdapter statusAdapter;
    CardAdapter cardAdapter;

    View view;
    LinearLayout layout_flag;
    LinearLayout tab_previous;
    LinearLayout tab_after;

    LinearLayout layout_status;
    LinearLayout tab_status;
    LinearLayout tab_card;

    TextView intent_reservation;

    private Context mContext;
    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    // firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getRole(uid);

        if (flag == 1) {
            view = inflater.inflate(R.layout.fragment_reservation, container, false);
            init_student();
        } else if (flag == 2) {
            view = inflater.inflate(R.layout.fragment_reservation_professor, container, false);
            init_professor();
        }

        return view;
    }

    // 학생인지 교수인지 알아내는 함수수
   public int getRole (String uid) {
        myRef.child("Member").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("role").toString().equals("학생")){
                    flag = 1;
                }else {
                    flag = 2;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return flag;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init_student() {
        Typeface typeface = getResources().getFont(R.font.jalnan);

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

        // 학생 예약 리스트 생성
        FirebaseDatabase.getInstance().getReference().child("Member").child(uid).child("R_List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                previousAdapter.clear();
                afterAdapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (preArr.size() == 0){
//                        layout_flag.setVisibility(View.VISIBLE);
                    }
                    if(!dataSnapshot.getValue().equals("null")){
//                        layout_flag.setVisibility(View.GONE);
                        HashMap<String, Object> member = (HashMap<String, Object>) snapshot.getValue();
                        professor = member.get("professor").toString();
                        summary = member.get("summary").toString();
                        date = member.get("date").toString();
                        way = member.get("way").toString();
                        place = member.get("place").toString();
                        allow = member.get("allow").toString();
                        key = member.get("key").toString();
                        before_after_data = member.get("before_after_data").toString();
                        if(before_after_data.equals("false")){
                            preArr.add(key);
                            previousAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.professor), professor, "\" "+summary+" \"", date, way, place, allow);
                        }else if(before_after_data.equals("true")){
                            aftArr.add(key);
                            afterAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.professor), professor, "\" "+summary+" \"", date, way, place);
                        }
                        new CompareDate().Compare(preArr, flag);
                    }else {
                        Log.i("compare", "null");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("remove", "없어");
            }
        });

        afterAdapter = new AfterAdapter();
        ListView listview2 = (ListView) view.findViewById(R.id.list_after);
        listview2.setAdapter(afterAdapter);




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
                new SetDialog().SetDialogStudent(view1, preArr, position, layout_flag);
                dialog1.show();
            }
        });

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("after", aftArr.get(position));
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater2 = getLayoutInflater();
                View view1 = inflater2.inflate(R.layout.dialog_after, null);
                builder2.setView(view1);
                final AlertDialog dialog2 = builder2.create();
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                new SetDialog().SetDialogStudent(view1, aftArr, position, layout_flag);
                dialog2.show();
            }
        });


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

//        if (flag_content == 0) {
//            layout_flag.setVisibility(View.GONE);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init_professor() {

        Typeface typeface = getResources().getFont(R.font.jalnan);

        layout_status = (LinearLayout) view.findViewById(R.id.layout_status);

        tab_status = (LinearLayout) view.findViewById(R.id.tab_status);
        tab_card = (LinearLayout) view.findViewById(R.id.tab_card);

        TabHost tab = (TabHost) view.findViewById(android.R.id.tabhost);
        tab.setup();

        TabHost.TabSpec tab1 = tab.newTabSpec("1").setContent(R.id.tab_status).setIndicator("상담현황");
        TabHost.TabSpec tab2 = tab.newTabSpec("2").setContent(R.id.tab_card).setIndicator("관리카드");

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

        statusAdapter = new StatusAdapter();
        ListView listview1 = (ListView) view.findViewById(R.id.list_status);
        listview1.setAdapter(statusAdapter);

        FirebaseDatabase.getInstance().getReference().child("Member").child(uid).child("R_List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                statusAdapter.clear();
                cardAdapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("MainActivity", "ValueEventListener : " + dataSnapshot.getValue());
                    if (dataSnapshot.getValue().equals("null")){
                        layout_status.setVisibility(View.VISIBLE);
                        return;
                    }else{
                        layout_status.setVisibility(View.GONE);
                        HashMap<String, Object> member2 = (HashMap<String, Object>) snapshot.getValue();
                        professor = member2.get("professor").toString();
                        summary = member2.get("summary").toString();
                        date = member2.get("date").toString();
                        way = member2.get("way").toString();
                        place = member2.get("place").toString();
                        allow = member2.get("allow").toString();
                        key = member2.get("key").toString();
                        Log.i("key", key);
                        before_after_data = member2.get("before_after_data").toString();
                        if(before_after_data.equals("false")){
                            Log.i("view", "화면 만들기");
                            preArr.add(key);
                            statusAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.student), professor, "\" "+summary+" \"", date, way, place, allow);
                        }else if(before_after_data.equals("true")){
                            aftArr.add(key);
                            cardAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.student), "홍길동", "대구 ICT산업 혁신아카데미 2021년도 3기");
                        }
                        new CompareDate().Compare(preArr, flag);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("remove", "없어");
            }
        });




        cardAdapter = new CardAdapter();
        ListView listview2 = (ListView) view.findViewById(R.id.list_card);
        listview2.setAdapter(cardAdapter);



        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater1 = getLayoutInflater();
                View view1 = inflater1.inflate(R.layout.dialog_status, null);
                builder1.setView(view1);
                final AlertDialog dialog1 = builder1.create();
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                new SetDialog().SetDialogProfessor(view1, preArr, position, layout_status);

                dialog1.show();

                StatusItem item = (StatusItem) parent.getItemAtPosition(position);
                String allowStr = item.getAllowStr();

                // click event
                Button button = dialog1.findViewById(R.id.button_accept);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(button.getText().toString().equals("수락대기")){
                            new AcceptClick().Click(preArr, position, "FCR1dfS2XqWV7rdpXvrmFsYURU72", "BSSpxq2WkFewZ8gEty9ZDR7nma92");
                            button.setText("상담대기");
                        }
                    }
                });

            }
        });

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater2 = getLayoutInflater();
                View view1 = inflater2.inflate(R.layout.dialog_card, null);
                builder2.setView(view1);
                final AlertDialog dialog2 = builder2.create();
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        });


        mContext = getContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        fab_main = (FloatingActionButton) view.findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton) view.findViewById(R.id.fab_sub1);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen) {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_close);
                    fab_sub1.setClickable(false);
                    isFabOpen = false;
                } else {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_open);
                    fab_sub1.setClickable(true);
                    isFabOpen = true;
                }
            }
        });

//        if (flag_content == 0) {
//            layout_status.setVisibility(View.GONE);
//        }
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
