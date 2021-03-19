package com.kang.project.reservation;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kang.project.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SetDialog {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String professor;
    private String summary;
    private String date;
    private String way;
    private String place;
    private String allow;
    private String before_after_data;
    private String key;
    private String contents;
    private String firebase_key;
    private String time;

    public void SetDialogStudent(View view1, ArrayList<String> arr, int position, LinearLayout layout) {
        // dialog 세팅

        key = arr.get(position);

        // view 세팅
        myRef.child("Member").child("BSSpxq2WkFewZ8gEty9ZDR7nma92").child("R_List").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    if (String.valueOf(task.getResult().getValue()).equals("null")){
                        return;
                    }else{
                        layout.setVisibility(View.GONE);
                        for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                            HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                            professor = member.get("professor").toString();
                            summary = member.get("summary").toString();
                            date = member.get("date").toString();
                            way = member.get("way").toString();
                            place = member.get("place").toString();
                            allow = member.get("allow").toString();
                            contents = member.get("contents").toString();
                            before_after_data = member.get("before_after_data").toString();
                            firebase_key = member.get("key").toString();
                            time = member.get("time").toString();
                            if(before_after_data.equals("false") && key.equals(firebase_key)){
                                TextView text_professor = (TextView) view1.findViewById(R.id.text_professor);
                                TextView text_summary = (TextView) view1.findViewById(R.id.text_summary);
                                TextView reservation_date = (TextView) view1.findViewById(R.id.reservation_date);
                                TextView reservation_way = (TextView) view1.findViewById(R.id.reservation_way);
                                TextView reservation_place = (TextView) view1.findViewById(R.id.reservation_place);
                                TextView content = (TextView) view1.findViewById(R.id.content);
                                TextView reservation_state = (TextView) view1.findViewById(R.id.reservation_state);
                                TextView reservation_time = (TextView) view1.findViewById(R.id.reservation_time);
                                text_professor.setText(professor);
                                text_summary.setText(summary);
                                reservation_date.setText(date);
                                reservation_way.setText(way);
                                reservation_place.setText(place);
                                content.setText(contents);
                                reservation_state.setText(allow);
                                reservation_time.setText(time);
                            }else {
                                TextView text_professor = (TextView) view1.findViewById(R.id.text_professor);
                                TextView text_summary = (TextView) view1.findViewById(R.id.text_summary);
                                TextView reservation_date = (TextView) view1.findViewById(R.id.reservation_date);
                                TextView reservation_way = (TextView) view1.findViewById(R.id.reservation_way);
                                TextView reservation_place = (TextView) view1.findViewById(R.id.reservation_place);
                                TextView content = (TextView) view1.findViewById(R.id.content);
                                text_professor.setText(professor);
                                text_summary.setText(summary);
                                reservation_date.setText(date);
                                reservation_way.setText(way);
                                reservation_place.setText(place);
                                content.setText(contents);
                            }
                        }
                    }
                }
            }
        });
    }

    public void SetDialogProfessor(View view1, ArrayList<String> arr, int position, LinearLayout layout) {
        // dialog 세팅

        key = arr.get(position);

        // view 세팅
        myRef.child("Member").child("BSSpxq2WkFewZ8gEty9ZDR7nma92").child("R_List").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    if (String.valueOf(task.getResult().getValue()).equals("null")){

                    }else{
                        layout.setVisibility(View.GONE);
                        for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                            HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                            professor = member.get("professor").toString();
                            summary = member.get("summary").toString();
                            date = member.get("date").toString();
                            way = member.get("way").toString();
                            place = member.get("place").toString();
                            allow = member.get("allow").toString();
                            contents = member.get("contents").toString();
                            before_after_data = member.get("before_after_data").toString();
                            firebase_key = member.get("key").toString();
                            time = member.get("time").toString();
                            Log.i("check", "no set : " + allow);
                            if(before_after_data.equals("false") && key.equals(firebase_key)){

                                TextView name_student = (TextView) view1.findViewById(R.id.name_student);
                                TextView text_summary = (TextView) view1.findViewById(R.id.text_summary);
                                TextView reservation_date = (TextView) view1.findViewById(R.id.reservation_date);
                                TextView reservation_way = (TextView) view1.findViewById(R.id.reservation_way);
                                TextView reservation_place = (TextView) view1.findViewById(R.id.reservation_place);
                                TextView content = (TextView) view1.findViewById(R.id.content);
                                Button button_accept = (Button) view1.findViewById(R.id.button_accept);
                                TextView reservation_time = (TextView) view1.findViewById(R.id.reservation_time);
                                name_student.setText(professor);
                                text_summary.setText(summary);
                                reservation_date.setText(date);
                                reservation_way.setText(way);
                                reservation_place.setText(place);
                                content.setText(contents);
                                button_accept.setText(allow);
                                reservation_time.setText(time);
                                Log.i("check", "set button : " + button_accept.getText());
                            }else {
//                                TextView text_professor = (TextView) view1.findViewById(R.id.text_professor);
//                                TextView text_summary = (TextView) view1.findViewById(R.id.text_summary);
//                                TextView reservation_date = (TextView) view1.findViewById(R.id.reservation_date);
//                                TextView reservation_way = (TextView) view1.findViewById(R.id.reservation_way);
//                                TextView reservation_place = (TextView) view1.findViewById(R.id.reservation_place);
//                                TextView content = (TextView) view1.findViewById(R.id.content);
//                                text_professor.setText(professor);
//                                text_summary.setText(summary);
//                                reservation_date.setText(date);
//                                reservation_way.setText(way);
//                                reservation_place.setText(place);
//                                content.setText(contents);
                            }
                        }
                    }
                }
            }
        });
    }


}
