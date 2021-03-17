package com.example.hustory.question;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hustory.R;

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
}
