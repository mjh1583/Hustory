package com.example.hustory.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hustory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FindPwActivity extends AppCompatActivity {

    private static final String TAG = "FindIdActivity";

    private EditText find_Pw_find_nameETXT;
    private EditText find_Pw_find_phoneETXT;
    private EditText find_Pw_find_IdETXT;

    private TextView find_Pw_find_Id_ResultTXT;

    private Button go_loginBTN;
    private Button find_Pw_find_PwBTN;

    private FirebaseDatabase db;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;

    private String find_name;
    private String find_phone;
    private String find_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        init();
    }

    private void init() {
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        mAuth = FirebaseAuth.getInstance();

        find_Pw_find_nameETXT = findViewById(R.id.find_Pw_find_nameETXT);
        find_Pw_find_phoneETXT = findViewById(R.id.find_Pw_find_phoneETXT);
        find_Pw_find_IdETXT = findViewById(R.id.find_Pw_find_IdETXT);

        find_Pw_find_Id_ResultTXT = findViewById(R.id.find_Pw_find_Id_ResultTXT);

        go_loginBTN = findViewById(R.id.go_loginBTN);
        find_Pw_find_PwBTN = findViewById(R.id.find_Pw_find_PwBTN);

        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.find_Pw_find_PwBTN:
                    find_name = find_Pw_find_nameETXT.getText().toString();
                    find_phone = find_Pw_find_phoneETXT.getText().toString();
                    find_email = find_Pw_find_IdETXT.getText().toString();

                    myRef.child("Member").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.i("firebase", "Error getting data", task.getException());
                            }
                            else {
                                Log.i("firebase", String.valueOf(task.getResult().getValue()));

                                for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                                    HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();

                                    String name = member.get("name").toString();
                                    String phone = member.get("phone").toString();
                                    String email = member.get("email").toString();

                                    if(name.equals(find_name) && phone.equals(find_phone) && email.equals(find_email)) {
                                        find_Pw_find_Id_ResultTXT.setText("비밀번호는 " + member.get("pw").toString() + " 입니다.");
                                    }
                                }
                                if(find_Pw_find_Id_ResultTXT.getText().length() > 0) {
                                    startToast("비밀번호를 찾았습니다.");
                                }
                                else {
                                    startToast("아이디, 휴대전화, 이름을 다시 확인해주세요.");
                                }
                            }
                        }
                    });
                    break;
                case R.id.go_loginBTN:
                    startLoginActivity();
                    break;
                default:
                    break;
            }
        };
        find_Pw_find_PwBTN.setOnClickListener(onClickListener);
        go_loginBTN.setOnClickListener(onClickListener);
    }

    private void startToast(String message) {
        Toast.makeText(FindPwActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void startLoginActivity() {
        Intent intent =  new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}