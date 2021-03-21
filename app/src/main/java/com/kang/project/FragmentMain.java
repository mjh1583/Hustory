package com.kang.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kang.project.question.FragmentQuestion;
import com.kang.project.reservation.CompareDate;
import com.kang.project.reservation.GetRole;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FragmentMain extends Fragment {

    EditText edit_question;
    ImageView go_question;

    View view;
    ViewPager viewPager;
    TextView text_summary;
    SwipeAdapter adapter;

    ImageView image_professor;
    TextView main_professor;
    TextView title_professor;
    TextView main_summary;
    TextView go_reservation;

    LinearLayout layout_reservation;

    private Context mContext;
    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    private  String professor;
    private String student;
    private  String summary;
    private  String before_after_data;
    private  String reservedate;
    private String reserve_month;
    private String reserve_day;
    Date date = new Date(2021,12,31,00,00);
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    String testDate = format.format(date);
    private  long check_reservedate;

    private FragmentQuestion fragmentQuestion = new FragmentQuestion();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (GetRole.FLAG == 1) {
            view = inflater.inflate(R.layout.fragment_main, container, false);
            init_student();
        } else if (GetRole.FLAG == 2) {
            view = inflater.inflate(R.layout.fragment_main_professor, container, false);
            init_professor();
        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init_student() {
        check_reservedate = Long.parseLong(testDate);
        text_summary = (TextView) view.findViewById(R.id.main_summary);
        viewPager = (ViewPager) view.findViewById(R.id.image_banner);

        image_professor = (ImageView) view.findViewById(R.id.image_professor);
        main_professor = (TextView) view.findViewById(R.id.main_professor);
        title_professor = (TextView) view.findViewById(R.id.title_professor);
        main_summary = (TextView) view.findViewById(R.id.main_summary);
        go_reservation = (TextView) view.findViewById(R.id.go_reservation);
        FirebaseDatabase.getInstance().getReference().child("Member").child(uid).child("R_List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> member = (HashMap<String, Object>) snapshot.getValue();
                        student = member.get("student").toString();
                        summary = member.get("summary").toString();
                        before_after_data = member.get("before_after_data").toString();
                        reservedate = member.get("reservedate").toString();
                        reserve_month = member.get("reserve_month").toString();
                        reserve_day = member.get("reserve_day").toString();
                        long currentTime = new Date().getTime();

                        if(before_after_data.equals("false") && Long.valueOf(reservedate).compareTo(check_reservedate) < 0){
                            Log.i("test", "크다"+reservedate);
                            main_professor.setText(student);
                            main_summary.setText(summary);
                            go_reservation.setText(CreateDataWithCheck(reserve_month, reserve_day));
                            check_reservedate = Long.valueOf(reservedate);

                        }else {
                            Log.i("test", "작다"+Long.valueOf(reservedate).compareTo(check_reservedate));
                        }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("remove", "없어");
            }
        });

        layout_reservation = (LinearLayout) view.findViewById(R.id.reservation);

        adapter = new SwipeAdapter(getContext());
        viewPager.setAdapter(adapter);

        int dpValue = 35;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        viewPager.getLayoutParams().height = height/3;
        viewPager.requestLayout();

        String str = "\" " + String.valueOf(text_summary.getText()) + " \"";
        text_summary.setText(str);

        edit_question = (EditText) view.findViewById(R.id.edit_question);
        go_question = (ImageView) view.findViewById(R.id.go_question);

        go_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                FragmentQuestion fragment = new FragmentQuestion();
                Bundle bundle = new Bundle();
                bundle.putString("KEY_SEARCH", String.valueOf(edit_question.getText()));
                fragment.setArguments(bundle);

                transaction.replace(R.id.frameLayout, fragment);
                transaction.commit();
            }
        });

        mContext = getContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        fab_main = (FloatingActionButton) view.findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton) view.findViewById(R.id.fab_sub1);
        fab_sub2 = (FloatingActionButton) view.findViewById(R.id.fab_sub2);

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

        if (GetRole.CONTENT_FLAG == 1) {
            image_professor.setVisibility(View.GONE);
            main_professor.setVisibility(View.GONE);
            title_professor.setVisibility(View.GONE);
            main_summary.setVisibility(View.GONE);
            go_reservation.setVisibility(View.GONE);

            TextView text_no = new TextView(getContext());
            text_no.setText("진행중인 예약 내역이 없습니다.");
            text_no.setTextColor(Color.parseColor("#FFFFFF"));
            text_no.setTextSize(14);
            text_no.setGravity(Gravity.CENTER);
            layout_reservation.addView(text_no);

            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) text_no.getLayoutParams();
            layoutParams1.topMargin = 80;
            text_no.setLayoutParams(layoutParams1);

            TextView text_go = new TextView(getContext());
            text_go.setText("새로운 예약을 진행하세요");
            text_go.setTextSize(12);
            text_go.setGravity(Gravity.CENTER);
            layout_reservation.addView(text_go);

            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) text_go.getLayoutParams();
            layoutParams2.bottomMargin = 80;
            text_go.setLayoutParams(layoutParams2);

            Typeface typeface = getResources().getFont(R.font.jalnan);
            text_no.setTypeface(typeface);
            text_go.setTypeface(typeface);
        }
    }

    public static String CreateDataWithCheck(String reserve_month, String reserve_day) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String msg = "D-" + (Integer.parseInt(reserve_day) - day);
        Log.i("msg", msg);


        return msg;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init_professor() {
        check_reservedate = Long.parseLong(testDate);
        text_summary = (TextView) view.findViewById(R.id.main_summary);
        viewPager = (ViewPager) view.findViewById(R.id.image_banner);

        image_professor = (ImageView) view.findViewById(R.id.image_professor);
        main_professor = (TextView) view.findViewById(R.id.main_professor);
        title_professor = (TextView) view.findViewById(R.id.title_professor);
        main_summary = (TextView) view.findViewById(R.id.main_summary);
        go_reservation = (TextView) view.findViewById(R.id.go_reservation);

        image_professor = (ImageView) view.findViewById(R.id.image_professor);
        main_professor = (TextView) view.findViewById(R.id.main_professor);
        title_professor = (TextView) view.findViewById(R.id.title_professor);
        main_summary = (TextView) view.findViewById(R.id.main_summary);
        go_reservation = (TextView) view.findViewById(R.id.go_reservation);
        FirebaseDatabase.getInstance().getReference().child("Member").child(uid).child("R_List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> member = (HashMap<String, Object>) snapshot.getValue();
                    professor = member.get("professor").toString();
                    summary = member.get("summary").toString();
                    before_after_data = member.get("before_after_data").toString();
                    reservedate = member.get("reservedate").toString();
                    reserve_month = member.get("reserve_month").toString();
                    reserve_day = member.get("reserve_day").toString();
                    long currentTime = new Date().getTime();

                    if(before_after_data.equals("false") && Long.valueOf(reservedate).compareTo(check_reservedate) < 0){
                        Log.i("test", "크다"+reservedate);
                        main_professor.setText(professor);
                        main_summary.setText(summary);
                        go_reservation.setText(CreateDataWithCheck(reserve_month, reserve_day));
                        check_reservedate = Long.valueOf(reservedate);

                    }else {
                        Log.i("test", "작다"+Long.valueOf(reservedate).compareTo(check_reservedate));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("remove", "없어");
            }
        });

        layout_reservation = (LinearLayout) view.findViewById(R.id.reservation);

        adapter = new SwipeAdapter(getContext());
        viewPager.setAdapter(adapter);

        int dpValue = 35;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        viewPager.getLayoutParams().height = height/3;
        viewPager.requestLayout();

        String str = "\" " + String.valueOf(text_summary.getText()) + " \"";
        text_summary.setText(str);

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

        if (GetRole.CONTENT_FLAG == 1) {
            image_professor.setVisibility(View.GONE);
            main_professor.setVisibility(View.GONE);
            title_professor.setVisibility(View.GONE);
            main_summary.setVisibility(View.GONE);
            go_reservation.setVisibility(View.GONE);

            TextView text_no = new TextView(getContext());
            text_no.setText("진행중인 예약 내역이 없습니다.");
            text_no.setTextColor(Color.parseColor("#FFFFFF"));
            text_no.setTextSize(14);
            text_no.setGravity(Gravity.CENTER);
            layout_reservation.addView(text_no);

            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) text_no.getLayoutParams();
            layoutParams1.topMargin = 80;
            text_no.setLayoutParams(layoutParams1);

            TextView text_go = new TextView(getContext());
            text_go.setText("새로운 예약을 진행하세요");
            text_go.setTextSize(12);
            text_go.setGravity(Gravity.CENTER);
            layout_reservation.addView(text_go);

            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) text_go.getLayoutParams();
            layoutParams2.bottomMargin = 80;
            text_go.setLayoutParams(layoutParams2);

            Typeface typeface = getResources().getFont(R.font.jalnan);
            text_no.setTypeface(typeface);
            text_go.setTypeface(typeface);
        }
    }
}