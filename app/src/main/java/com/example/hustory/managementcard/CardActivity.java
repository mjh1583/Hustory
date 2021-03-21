package com.example.hustory.managementcard;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hustory.R;
import com.example.hustory.user.m_User;
import com.example.hustory.userInfo.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class CardActivity extends AppCompatActivity {

    private ImageView imageView;
    EditText name,birth,email,education,certificate,experience,carrer;
    Button button_modify;
    private FirebaseStorage storage;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_card);

        imageView = findViewById(R.id.icon_profile);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://hustory-82cc1.appspot.com");
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/" + UserInfo.UID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시

                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });





        name = findViewById(R.id.name);
        birth = findViewById(R.id.birth);
        email = findViewById(R.id.email);
        education = findViewById(R.id.education);
        certificate = findViewById(R.id.certificate);
        experience = findViewById(R.id.experience);
        carrer = findViewById(R.id.career);
        button_modify= findViewById(R.id.button_modify);

        //firebase 정의
        mDatabase = FirebaseDatabase.getInstance().getReference();


        readUser();

        button_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUsername = name.getText().toString();
                String getUserbirth = birth.getText().toString();
                String getUseremail = email.getText().toString();
                String getUsereducation = education.getText().toString();
                String getUsercertificate = certificate.getText().toString();
                String getUserexpercience = experience.getText().toString();
                String getUsercarrer = carrer.getText().toString();



                //hashmap 만들기
                HashMap result = new HashMap<>();
                result.put("name", getUsername);
                result.put("birth", getUserbirth);
                result.put("email", getUseremail);
                result.put("education", getUsereducation);
                result.put("certificate", getUsercertificate);
                result.put("experience", getUserexpercience);
                result.put("carrer", getUsercarrer);

                writeNewUser(UserInfo.UID,getUsername,getUseremail,getUsercertificate,getUserbirth,getUsereducation,getUsercarrer,getUserexpercience);

            }
        });
    }

    private void writeNewUser(String userId, String name, String email , String certificate,String birth, String experience, String education,String carrer) {
        m_User user = new m_User(name,email,certificate,birth, experience,education,carrer);

        mDatabase.child("Member").child(userId).child("management").child("2").setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(CardActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(CardActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void readUser(){
        mDatabase.child("Member").child("management").child("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(m_User.class) != null){
                    m_User post = dataSnapshot.getValue(m_User.class);
                    Log.w("FireBaseData", "getData" + post.toString());
                } else {
                    Toast.makeText(CardActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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