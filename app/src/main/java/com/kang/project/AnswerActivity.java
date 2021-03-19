package com.kang.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class AnswerActivity extends AppCompatActivity {

    AnswerAdapter answerAdapter;
    Button button_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer);

        init();
    }

    public void init() {
        answerAdapter = new AnswerAdapter();
        ListView listView = (ListView) findViewById(R.id.list_answer);
        listView.setAdapter(answerAdapter);

        answerAdapter.addItem("프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다.", "홍길동", "1분 전");
        answerAdapter.addItem("프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다.", "홍길동", "5분 전");
        answerAdapter.addItem("프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다.", "홍길동", "5분 전");
        answerAdapter.addItem("프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다.", "홍길동", "5분 전");
        answerAdapter.addItem("프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다.", "홍길동", "5분 전");

        button_answer = (Button) findViewById(R.id.button_answer);
        button_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AnswerActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_question, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
