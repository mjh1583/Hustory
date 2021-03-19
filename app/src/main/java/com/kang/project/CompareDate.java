package com.kang.project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class CompareDate {
    private FirebaseData firebaseData;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //    private String uid = user.getUid();
    private Long createTime;
    private Long currentTime;


    public void Compare (ArrayList<String> arr) {
        Log.i("compare", "start");
        if(arr.size()>0){
            for (int i = 0; i<arr.size(); i++){
                Log.i("compare", "i : " + i);
                createTime = Long.parseLong(arr.get(i));
                currentTime = new Date().getTime();
                Log.i("compare", "비교" + createTime.compareTo(currentTime));
                if (createTime.compareTo(currentTime) < 0){
                    myRef.child("Member").child("BSSpxq2WkFewZ8gEty9ZDR7nma92").child("R_List").child(arr.get(i)).child("before_after_data").setValue("true");
                }else{
                    return;
                }
            }
        }
    }
}
