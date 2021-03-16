package com.example.hustory;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FindIdActivity extends AppCompatActivity {

    private static final String TAG = "FindIdActivity";

    private EditText find_nameETXT;
    private EditText find_phoneETXT;

    private TextView find_Id_ResultTXT;

    private Button find_IdBTN;
    private Button find_PwBTN;

    private FirebaseDatabase db;
    private DatabaseReference myRef;

    private String find_name;
    private String find_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        init();
    }

    private void init() {
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();

        find_nameETXT = findViewById(R.id.find_nameETXT);
        find_phoneETXT = findViewById(R.id.find_phoneETXT);

        find_Id_ResultTXT = findViewById(R.id.find_Id_ResultTXT);

        find_IdBTN = findViewById(R.id.find_IdBTN);
        find_PwBTN = findViewById(R.id.find_PwBTN);

        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.find_IdBTN:
                    find_name = find_nameETXT.getText().toString();
                    find_phone = find_phoneETXT.getText().toString();

                    myRef.child("Member").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.i("firebase", "Error getting data", task.getException());
                            }
                            else {
                                //Log.i("firebase", String.valueOf(task.getResult().getValue()));

                                for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                                    HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();

                                    String name = member.get("name").toString();
                                    String phone = member.get("phone").toString();

                                    if(name.equals(find_name) && phone.equals(find_phone)) {
                                        find_Id_ResultTXT.setText("아이디는 " + member.get("email").toString() + " 입니다.");
                                    }
                                }
                                if(find_Id_ResultTXT.getText().length() > 0) {
                                    startToast("아이디를 찾았습니다.");
                                }
                                else {
                                    startToast("이름과 휴대전화와 일치하는 아이디가 없습니다.");
                                }
                            }
                        }
                    });
                    break;
                case R.id.find_PwBTN:
                    startFindPwActivity();
                    break;
                default:
                    break;
            }
        };

        find_IdBTN.setOnClickListener(onClickListener);
        find_PwBTN.setOnClickListener(onClickListener);
    }

    private void startToast(String message) {
        Toast.makeText(FindIdActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void startFindPwActivity() {
        Intent intent =  new Intent(this, FindPwActivity.class);
        startActivity(intent);
        finish();
    }
}