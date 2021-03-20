package com.example.hustory.reservation;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AcceptClick {
    private FirebaseData firebaseData;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public void Click(ArrayList<String> arr, int position, String uid, String student_id) {
        myRef.child("Member").child(uid).child("R_List").child(arr.get(position)).child("allow").setValue("상담대기");
        myRef.child("Member").child(student_id).child("R_List").child(arr.get(position)).child("allow").setValue("상담대기");
        Log.i("long",arr.get(position));
    }
}
