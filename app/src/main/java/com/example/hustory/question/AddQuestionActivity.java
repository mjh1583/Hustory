package com.example.hustory.question;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.hustory.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText q_titleETXT;
    private EditText q_contentETXT;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser currentUser;

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
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.removeBTN:
                q_contentETXT.setText("");
                q_titleETXT.setText("");
                break;
            case R.id.addBTN:
                Date date = new Date();

                SimpleDateFormat sFormat = new SimpleDateFormat("hh시 mm분 ss초");
                SimpleDateFormat sFormat2 = new SimpleDateFormat("yyyy년 MM월 dd일");

                QuestionItem data =  new QuestionItem(0, currentUser.getUid(),
                        q_titleETXT.getText().toString(),
                        q_contentETXT.getText().toString(),
                        sFormat2.format(date),
                        sFormat.format(date),
                        0, 0, 0
                );

                Log.i("data", data.toString());
                break;
        }
    }

}