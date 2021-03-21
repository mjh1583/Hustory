package com.example.hustory.managementcard;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hustory.R;
import com.example.hustory.user.User;
import com.example.hustory.user.User3;
import com.example.hustory.userInfo.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LetterActivity extends AppCompatActivity {

    EditText background,character,ict_experience,motivation;
    Button button_modify1;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_letter);

        init();

        background = findViewById(R.id.background);
        character = findViewById(R.id.character);
        ict_experience = findViewById(R.id.ict_experience);
        motivation = findViewById(R.id.motivation);
        button_modify1= findViewById(R.id.button_modify);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        background = (EditText)findViewById(R.id.background);
        character = (EditText)findViewById(R.id.character);
        ict_experience = (EditText)findViewById(R.id.ict_experience);
        motivation = (EditText)findViewById(R.id.motivation);
        button_modify1= findViewById(R.id.button_modify);

        readUser();

        button_modify1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUserbackground = background.getText().toString();
                String getUsercharacter = character.getText().toString();
                String getUserict_experience = ict_experience.getText().toString();
                String getUsermotivation = motivation.getText().toString();




                //hashmap 만들기
                HashMap result = new HashMap<>();
                result.put("background", getUserbackground);
                result.put("character", getUsercharacter);
                result.put("ict_experience", getUserict_experience);
                result.put("motivation", getUsermotivation);


                writeNewUser(UserInfo.UID,getUserbackground,getUsercharacter,getUserict_experience,getUsermotivation);

            }
        });
    }

    @Override
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

    private void writeNewUser(String userId, String background, String character , String ict_experience,String motivation) {
        User3 user = new User3(background,character,ict_experience,motivation);

        mDatabase.child("Member").child(userId).child("management").child("3").setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(LetterActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(LetterActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void readUser(){
        mDatabase.child("Member").child("management").child("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(User.class) != null){
                    User post = dataSnapshot.getValue(User.class);
                    Log.w("FireBaseData", "getData" + post.toString());
                } else {
                    Toast.makeText(LetterActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void init() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}