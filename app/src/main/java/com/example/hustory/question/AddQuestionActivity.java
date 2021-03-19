package com.example.hustory.question;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hustory.R;
import com.example.hustory.util.DataStringFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText q_titleETXT;
    private EditText q_contentETXT;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser currentUser;

    private String userId;
    private String title;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        init();
    }

    private void init() {
        q_titleETXT = findViewById(R.id.q_titleETXT);
        q_contentETXT = findViewById(R.id.q_contentETXT);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel:
                q_contentETXT.setText("");
                q_titleETXT.setText("");
                break;
            case R.id.answer_AddBTN:
                title = q_titleETXT.getText().toString();
                content = q_contentETXT.getText().toString();

                if(title.length() > 0 && content.length() > 0) {
                    Date date = new Date();

                    Log.i("시간 : ", String.valueOf(date.getTime()));

                    SimpleDateFormat sFormat = new SimpleDateFormat("HH시 mm분 ss초");
                    SimpleDateFormat sFormat2 = new SimpleDateFormat("yyyy년 MM월 dd일");

                    // Q_Num 을 생성하기 위한 날짜/시간 데이터 처리
                    SimpleDateFormat sFormat3 = new SimpleDateFormat("yyyyMMddHHmmss");

                    String q_Num = "Q" + sFormat3.format(date);

                    QuestionItem data =  new QuestionItem(q_Num, userId,
                            q_titleETXT.getText().toString(),
                            q_contentETXT.getText().toString(),
                            sFormat2.format(date),
                            sFormat.format(date),
                            "0",
                            sFormat3.format(date));

                    Log.i("data", data.toString());
                    myRef.child("Member").child(userId).child("Q_List").child(q_Num).setValue(data);
                    myRef.child("Question").child(q_Num).setValue(data);
                    finish();
                }
                else {
                    makeToast("제목과 내용을 다 입력하세요!");
                }
                break;
        }
    }

}