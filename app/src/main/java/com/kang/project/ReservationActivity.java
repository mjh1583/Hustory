package com.kang.project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ReservationActivity extends AppCompatActivity {
    // 요일 변수
    private String[] dayArray;
    private boolean before_after_data;

    // 예약수를 세는 변수
    private int i;

    // view 변수
    private EditText text_summary;
    private TextView select_date;
    private TextView select_time;
    private EditText select_place;
    private EditText select_content;
    private Spinner spinner;
    private String key;


    // firebase
    private FirebaseData firebaseData;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<FirebaseData> datas;
    private long consultNum;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reservation);

        init();
    }

    // 매개변수 받아서 Toast
    private void showToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }


    public void init(){
        //요일 변수 선언
        dayArray = getResources().getStringArray(R.array.day_of_week);

        //view 변수 선언
        text_summary = findViewById(R.id.text_summary);
        select_date = findViewById(R.id.select_date);
        select_time = findViewById(R.id.select_time);
        select_place = findViewById(R.id.select_place);
        select_content = findViewById(R.id.select_content);
        spinner = (Spinner)findViewById(R.id.onOffSpiner);




        // 스피너 데이터 선언
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.on_off_line, R.layout.spinner_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // spinner.getSelectedItem().toString() : 스피너 값 가져오는 함수

    }


    // 날짜 정보 고르는 함수.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDatePicker(View view) {
        // Calendar class 변수 선언 년, 월, 일, 요일 후 Calender class에 날짜 set
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        c.set(year, month, day);
        // 지나간 날짜를 선택하지 못하게 하기위해 오늘 날짜를 받아옴.
        long minDate = c.getTime().getTime();

        // DatePickerDialog 선언
        DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int DOF = (dayOfMonth - day) % 7;

//                showToast(year + ":"+ month+1  + ":" +dayOfMonth + ":" + dayArray[dayOfWeek] );
                select_date.setText((month+1)+"."+dayOfMonth+"("+dayArray[dayOfWeek+DOF]+")");
                view.setMinDate(minDate);
            }
        },year,month,day);

        // 지나간 날짜 선택 못하게 min date 설정
        dateDialog.getDatePicker().setMinDate(minDate);
        // dialog 를 띄움.
        dateDialog.show();
    }


    // 시간 정보 고르는 함수.
    public void showTimePicker(View v){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                select_time.setText(hourOfDay + ":" + minute);
                showToast(hourOfDay + ":" + minute);
            }
        }, hour,minute, false);
        timeDialog.show();
    }

    // 예약버튼 클릭 이벤트
    public void onReserve(View v){
        // 총 예약수 세는 함수.
//        myRef.child("Reservation").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                i = 0;
//                if (!task.isSuccessful()) {
//                }
//                else {
//                    if (String.valueOf(task.getResult().getValue()).equals("null")){
//                        return;
//                    }else{
//                        for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
//                            HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
//                            i = Integer.parseInt(member.get("count").toString());
//                            Log.i("count2", ""+i);
//                            ++i;
//                            Log.i("count2", ""+i);
//                            Log.i("count2","snapshot : " + userSnapshot);
//                            Log.i("count2","getKey : " + userSnapshot.getKey());
//                            String key = userSnapshot.getKey();
//                            Log.i("count2","value : " + userSnapshot.child(key).child("value").getValue());
//
//                        }
//                    }
//                }
//            }
//        });

        // 날짜 데이터 만들기
        long id = new Date().getTime();

//        SimpleDateFormat sFormat = new SimpleDateFormat("hh시 mm분 ss초");
//        SimpleDateFormat sFormat2 = new SimpleDateFormat("yyyy년 MM월 dd일");
//        SimpleDateFormat sFormat3 = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat sFormat4 = new SimpleDateFormat("hhmmss");

        DateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH=24h, hh=12h
        String str = df.format(id);
        Log.i("reserve", "HH:mm:ss" + str);

        Date date = new Date(id);
        Log.i("reserve", "date" + date);

//
//        String q_Num = "R" + sFormat3.format(date) + sFormat4.format(date);

        // 예약하기 버튼 클릭시 예약 해서 firebase 에 데이터 전송
        if(!select_date.getText().equals("날짜 설정") && !select_time.getText().equals("시간 설정")  && select_place.getText().length() != 0 && select_content.getText().length() !=0 && text_summary.getText().length() != 0){
            key = "R" + id;
            Log.i("name", String.valueOf(myRef.child("Member").child("v5N3FZkAIxTS2IpVV4ki2yUbRwJ2").child("name").get()));
            Date d = new Date();
            Log.i("date", String.valueOf(d));
            firebaseData = new FirebaseData("v5N3FZkAIxTS2IpVV4ki2yUbRwJ2","홍길동", text_summary.getText().toString(), select_date.getText().toString(),select_time.getText().toString(),spinner.getSelectedItem().toString(),select_place.getText().toString(),"수락대기",select_content.getText().toString(), false, key);
            Map<String, Object> postValue = firebaseData.toMap();
            myRef.child("Member").child("v5N3FZkAIxTS2IpVV4ki2yUbRwJ2").child("R_List").child(key).setValue(postValue);
            Log.i("reserve",""+id);
            finish();

//            myRef.child("Reservation").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if (!task.isSuccessful()) {
//                        Log.i("firebase", "Error getting data", task.getException());
//                    }
//                    else {
//                        Log.i("firebase", String.valueOf(task.getResult().getValue()));
//                        if (String.valueOf(task.getResult().getValue()).equals("null")){
//                            return;
//                        }else{
//                            for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
//                                HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
//                                professor = member.get("professor").toString();
//                                summary = member.get("summary").toString();
//                                date = member.get("date").toString();
//                                way = member.get("way").toString();
//                                place = member.get("place").toString();
//                                allow = member.get("allow").toString();
//                                before_after_data = member.get("before_after_data").toString();
//                                if(before_after_data.equals("false")){
//                                    previousAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.professor), professor, "\" "+summary+" \"", date, way, place, allow);
//                                }
//                            }
//                        }
//
//
//                    }
//                }
//            });
        }

//        // 자식주 세는 함수
//        myRef.child("Reservation").child(firebaseData.uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.i("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    DataSnapshot userSnapshot = (DataSnapshot) task.getResult().getChildren();
//                    consultNum = userSnapshot.getChildrenCount();
//                    Log.i("count", "" + consultNum);
//                }
//            }
//        });

        // 미입력 항목 있을 시 토스트
//        if (dateTXT.getText().equals(date)){
//            showToast("날짜를 설정해 주세요");
//        }else if(timeTXT.getText().equals(time)){
//            showToast("시간을 설정해 주세요");
//        }else if(titleETXT.getText().length() == 0){
//            showToast("제목을 입력해 주세요");
//        }else if(contentsETXT.getText().length() == 0){
//            showToast("내용을 입력해 주세요");
//        }
//        // 예약 현황 화면으로 전환
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.addToBackStack(null);
//        ft.replace(R.id.main_container, beforeFragment);
//        ft.commit();
    }


}