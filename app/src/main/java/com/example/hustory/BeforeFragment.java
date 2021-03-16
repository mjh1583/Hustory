package com.example.hustory;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BeforeFragment extends ListFragment {
    // data 형성
    private ArrayList<BeforeData> datas = new ArrayList<>();
    private BeforeDataAdapter adapter;
    private String date;
    private String professor;
    private String contents;
    private String place;
    private String accept;
    private String onOff;


    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid;



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        init();

        myRef.child("Reservation").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.i("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.i("firebase", String.valueOf(task.getResult().getValue()));
                    if (String.valueOf(task.getResult().getValue()).equals("null")){
                        Log.i("null", "왜 무한루프?");
                        return;
                    }else{
                        for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                            HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                            date = member.get("date").toString();
                            professor = member.get("professor").toString();
                            contents = member.get("contents").toString();
                            place = member.get("place").toString();
                            accept = member.get("accept").toString();
                            onOff = member.get("onOff").toString();
                            datas.add(new BeforeData(professor, date, onOff, place, accept, contents));
                            adapter = new BeforeDataAdapter(getActivity(), R.layout.activity_before_data, datas);
                            setListAdapter(adapter);
                        }
                    }


                }
            }
        });







    }

    public void init(){
        if (user != null)
        {
            uid = user.getUid();
        }
        datas.clear();
    }
}
