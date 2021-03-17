package com.kang.project;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentMain extends Fragment {
    int flag = 1;

    View view;
    ImageView image_top;
    ViewPager viewPager;
    TextView text_summary;
    SwipeAdapter adapter;

    ImageView image_professor;
    TextView main_professor;
    TextView title_professor;
    TextView main_summary;
    TextView go_reservation;

    LinearLayout layout_reservation;
    //LinearLayout layout_question;

    private Context mContext;
    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);
        init();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init() {
        //image_top = (ImageView) view.findViewById(R.id.image_top);
        text_summary = (TextView) view.findViewById(R.id.main_summary);
        viewPager = (ViewPager) view.findViewById(R.id.image_banner);

        image_professor = (ImageView) view.findViewById(R.id.image_professor);
        main_professor = (TextView) view.findViewById(R.id.main_professor);
        title_professor = (TextView) view.findViewById(R.id.title_professor);
        main_summary = (TextView) view.findViewById(R.id.main_summary);
        go_reservation = (TextView) view.findViewById(R.id.go_reservation);

        layout_reservation = (LinearLayout) view.findViewById(R.id.reservation);
        //layout_question = (LinearLayout) view.findViewById(R.id.question);

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

        //image_top.getLayoutParams().height = height/2;
        //image_top.requestLayout();

        //layout_reservation.getLayoutParams().height = height/5;
        //layout_reservation.requestLayout();

        //layout_question.getLayoutParams().height = height/5;
        //layout_question.requestLayout();

        String str = "\" " + String.valueOf(text_summary.getText()) + " \"";
        text_summary.setText(str);

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

        if (flag == 1) {
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
            layoutParams1.topMargin = 30;
            text_no.setLayoutParams(layoutParams1);

            TextView text_go = new TextView(getContext());
            text_go.setText("새로운 예약을 진행하세요");
            text_go.setTextSize(12);
            text_go.setGravity(Gravity.CENTER);
            layout_reservation.addView(text_go);

            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) text_go.getLayoutParams();
            layoutParams2.bottomMargin = 30;
            text_go.setLayoutParams(layoutParams2);

            Typeface typeface = getResources().getFont(R.font.jalnan);
            text_no.setTypeface(typeface);
            text_go.setTypeface(typeface);
        }
    }
}