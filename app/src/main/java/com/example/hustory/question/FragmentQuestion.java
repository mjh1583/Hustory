package com.example.hustory.question;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hustory.MainActivity;
import com.example.hustory.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentQuestion extends Fragment {

    View view;

    private Context mContext;
    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser currentUser;

    QuestionAdapter questionAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_question, container, false);
        init();

        return view;
    }

    public void init() {

        questionAdapter = new QuestionAdapter();
        ListView listview1 = (ListView) view.findViewById(R.id.list_question);
        listview1.setAdapter(questionAdapter);

        TextView new_orderTXT = (TextView) view.findViewById(R.id.new_orderTXT);
        TextView hot_orderTXT = (TextView) view.findViewById(R.id.hot_orderTXT);
        TextView my_orderTXT = (TextView) view.findViewById(R.id.my_orderTXT);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.new_orderTXT:

                    break;
                case R.id.hot_orderTXT:

                    break;
                case R.id.my_orderTXT:

                    break;
            }
        };

        new_orderTXT.setOnClickListener(onClickListener);
        hot_orderTXT.setOnClickListener(onClickListener);
        my_orderTXT.setOnClickListener(onClickListener);


        questionAdapter.addItem("프로젝트 평가 요소가 궁금합니다.", "홍길동", "1분 전", 0);
        questionAdapter.addItem("프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다.", "홍길동", "25분 전", 5);


        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), AnswerActivity.class);
                startActivity(intent);

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

        fab_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddQuestion();
            }
        });

    } // init()

    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getActivity().getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return view.dispatchTouchEvent(ev);
    }

    private void startAddQuestion() {
        Intent intent = new Intent(getActivity(), AddQuestionActivity.class);
        startActivity(intent);
    }
}
