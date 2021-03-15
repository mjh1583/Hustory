package com.example.hustory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUPActivity extends AppCompatActivity {

    private static final String TAG = "SignUPActivity";

    private FirebaseAuth mAuth;

    private EditText emailETXT;
    private EditText passwordETXT;
    private EditText passwordCheckETXT;

    private Button signUpBTN;
    private Button loginBTN;


    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();

    private String email = "";
    private String password = "";
    private String passwordCheck = "";

    // 비밀번호 정규식 : 문자 A-Z a-z 0-9 특수문자 : ~!@#$%^&*? 입력가능 8~15자리
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[~!@#$%^&*?])[A-Za-z[0-9]~!@#$%^&*?]{8,15}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 레이아웃 요소 초기화
        init();
    }

    private void init() {
        emailETXT = findViewById(R.id.signUp_emailETXT);
        passwordETXT = findViewById(R.id.signUp_passwordETXT);
        passwordCheckETXT = findViewById(R.id.passwordCheckETXT);

        signUpBTN = findViewById(R.id.signUpBTN);
        loginBTN = findViewById(R.id.loginBTN);

        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.signUpBTN:
                    signUp();
                    break;
                case R.id.loginBTN:
                    startLoginAcivity();
                    break;
            }
        };

        signUpBTN.setOnClickListener(onClickListener);
        loginBTN.setOnClickListener(onClickListener);
    }


    private void signUp() {
        email = emailETXT.getText().toString();
        password = passwordETXT.getText().toString();
        passwordCheck = passwordCheckETXT.getText().toString();

        if(isValidEmail()) {
            if(isValidPwd()) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    // 로그인 성공
                                    Log.i(TAG, "createUserWithEmail : Success");

                                    // firebase에 데이터 저장
                                    HashMap<String, Object> childUpdates = new HashMap<>();
                                    Member member = new Member(email, password);
                                    Map<String, Object> postValue = member.toMap();
                                    childUpdates.put("/Member/" + myRef.child("Member").push().getKey(), postValue);
                                    myRef.updateChildren(childUpdates);

                                    startToast("회원가입을 성공했습니다.");
                                    startLoginAcivity();
                                }
                                else {
                                    // If sign in fails, display a message to the user
                                    Log.i(TAG, "createUserWithEmail : Fail", task.getException());
                                    startToast("이미 가입된 이메일입니다.");
                                }
                            }
                        });
            }
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startLoginAcivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // 비밀번호 유효성 검사
    private boolean isValidPwd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(this, "비밀번호 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!password.equals(passwordCheck)){
            Toast.makeText(this, "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }
}
