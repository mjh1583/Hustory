package com.example.hustory.question;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hustory.R;
import com.example.hustory.userInfo.UserInfo;
import com.example.hustory.util.DataStringFormat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AnswerActivity extends AppCompatActivity {

    AnswerAdapter answerAdapter;
    Button button_answer;

    private static final String TAG = "AnswerActivity";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private String userId;

    private ArrayList<String> arr = new ArrayList<>();

    private String q_Num;
    private String name;
    private String writerId;

    // 다이얼 로그 뷰 가져오기
    private EditText answerETXT;
    private Button ok;

    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer);
        userId = currentUser.getUid();

        init();
    }

    public void init() {
        answerAdapter = new AnswerAdapter();
        ListView listView = (ListView) findViewById(R.id.list_answer);
        listView.setAdapter(answerAdapter);

        // 답변할 질문의 뷰 정의
        TextView question_name = findViewById(R.id.question_name);
        TextView q_person_name = findViewById(R.id.q_person_name);
        TextView question_time = findViewById(R.id.question_time);
        TextView question_content = findViewById(R.id.question_content);

        Intent intent = getIntent();

        // 앞의 질문 아이템 포지션 값 가져옴
        q_Num = intent.getStringExtra("q_Num");
        name = intent.getStringExtra("name");
        writerId = intent.getStringExtra("writerId");

        Log.i("q_Num ", q_Num);
        Log.i("name ", name);

        // 답변을 달아줄 질문 가져와서 생성
        myRef.child("Question").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.i("!task.isSuccessful() ", q_Num);
                }
                else {
                    if (String.valueOf(task.getResult().getValue()).equals("null")) {
                        Log.i("null ", q_Num);
                        Log.i("null ", String.valueOf(task.getResult().getValue()));
                        answerAdapter.notifyDataSetChanged();
                        return;
                    }
                    else {
                        for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                            Log.i(TAG, userSnapshot.toString());
                            HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                            String q_title = member.get("q_title").toString();
                            String q_content = member.get("q_content").toString();
                            String q_diffTime = member.get("q_diffTime").toString();

                            String q_date = member.get("q_date").toString();
                            String firebase_q_Num = member.get("q_Num").toString();

                            Log.i("firebase_q_Num ", firebase_q_Num);

                            if(q_Num.equals(firebase_q_Num)){
                                question_name.setText(q_title);
                                q_person_name.setText(name);
                                q_diffTime = DataStringFormat.CreateDataWithCheck(q_diffTime);
                                question_time.setText(q_diffTime);
                                question_content.setText(q_content);
                            }
                        }
                        answerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        // 질문에 달린 답변들을 가져와서 생성
        myRef.child("Member").child(writerId).child("Q_List").child(q_Num).child("A_List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                answerAdapter.clear();
                //Log.i(TAG, "AnswerActivity : onDataChange()");

                if (snapshot != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.i(TAG, dataSnapshot.toString());
                        HashMap<String, Object> member = (HashMap<String, Object>) dataSnapshot.getValue();
                        String id = member.get("id").toString();
                        String a_Num = member.get("a_Num").toString();
                        String a_content = member.get("a_content").toString();
                        String a_date = member.get("a_date").toString();
                        String a_time = member.get("a_time").toString();
                        String a_writer = member.get("a_writer").toString();
                        String a_diffTime = member.get("a_diffTime").toString();

                        answerAdapter.addItem(a_Num, a_content, a_date, a_time, a_writer, id, a_diffTime);
                    }
                    answerAdapter.notifyDataSetChanged();
                }
                else {
                    Log.i(TAG, "AnswerActivity : onDataChange() fail Firebase null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 답변 테스트 데이터
        //answerAdapter.addItem("프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다.", "홍길동", "1분 전");
        //answerAdapter.addItem("프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다. 프로젝트 평가 요소가 궁금합니다.", "홍길동", "5분 전");

        button_answer = (Button) findViewById(R.id.button_answer);
        button_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼 로그 만들고 보여주기
                AlertDialog.Builder builder = new AlertDialog.Builder(AnswerActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_question, null);
                builder.setView(view);
                dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                // 다이얼 로그 초기화 및 클릭 리스너 생성
                initAnswerDialog();
            }
        });



        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    private void initAnswerDialog() {
        // 다이얼 로그의 뷰 정의
        answerETXT = dialog.findViewById(R.id.answerETXT);
        ok = dialog.findViewById(R.id.answer_AddBTN);

        // 답변하기 버튼 클릭 시
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 답변 텍스트 뷰가 작성되어 있다면
                if(answerETXT.getText().length() > 0) {
                    Date date = new Date();

                    Log.i("시간 : ", String.valueOf(date.getTime()));

                    SimpleDateFormat sFormat = new SimpleDateFormat("HH시 mm분 ss초");
                    SimpleDateFormat sFormat2 = new SimpleDateFormat("yyyy년 MM월 dd일");
                    SimpleDateFormat sFormat3 = new SimpleDateFormat("yyyyMMddHHmmss");

                    String a_Num = "A" + sFormat3.format(date);

                    AnswerItem data = new AnswerItem(a_Num,
                            answerETXT.getText().toString(),
                            sFormat2.format(date),
                            sFormat.format(date),
                            UserInfo.CUR_USER_NAME,
                            userId,
                            sFormat3.format(date));

                    // 답변을 지정된 경로에 저장
                    myRef.child("Member").child(writerId).child("Q_List").child(q_Num).child("A_List").child(a_Num).setValue(data);
                    dialog.cancel();
                }
                else {
                    startToast("답변을 작성하고 버튼을 클릭하세요!");
                }
            }
        });
    }

    public void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
