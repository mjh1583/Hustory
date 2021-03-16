package com.example.hustory;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReservingActivity extends AppCompatActivity {
    // Fragment 관련 변수
    FragmentManager manager;
    BeforeFragment beforeFragment;

    // 매개변수 받아서 Toast
    private void showToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    // 요일 array
    private String[] dayArray;


    // 뷰 멤버변수
    private TextView dateTXT;
    private TextView timeTXT;
    private EditText titleETXT;
    private EditText contentsETXT;

    // 상담 방법 스피너 변수
    private Spinner spinner;
    private String[] arrData;
    private ArrayAdapter<String> arrayAdapter;

    // firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid;
    private ArrayList<BeforeData> datas;
    private long consultNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserving);

        init();

    }

    public void init(){
        // 뷰 변수 선언
        dateTXT = findViewById(R.id.dateTXT);
        timeTXT = findViewById(R.id.timeTXT);
        titleETXT = findViewById(R.id.titleETXT);
        contentsETXT = findViewById(R.id.contentsETXT);

        //요일 변수 선언
        dayArray = getResources().getStringArray(R.array.day_of_week);

        // 스피너 데이터 선언
        arrData = getResources().getStringArray(R.array.on_off_line);
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrData);
        spinner = (Spinner)findViewById(R.id.onOffSpiner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //

        // uid 생성
        if (user != null)
        {
            uid = user.getUid();
        }
    }

    // 예약 버튼 클릭 이벤트
    public void onClick(View v){
        // spinner 값 받아오기.
        String text = spinner.getSelectedItem().toString();
        String date = getResources().getString(R.string.date);
        String time = getResources().getString(R.string.time);

//        myRef.child("Reservation").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

        if(!dateTXT.getText().equals(date) && !timeTXT.getText().equals(time)  && titleETXT.getText().length() != 0 && contentsETXT.getText().length() !=0){





            datas = new ArrayList<BeforeData>();
            HashMap<String, Object> childUpdates = new HashMap<>();
            BeforeData beforeData = new BeforeData("홍길동", dateTXT.getText().toString(), text, "ZOOM", "수락대기", titleETXT.getText().toString());
            Map<String, Object> postValue = beforeData.toMap();
//        childUpdates.put("1",postValue);
//        BeforeData beforeData2 = new BeforeData("홍", "02.18(목)", "온라인", "ZOOM", "수락대기", "연봉협상에대하여");
//        Map<String, Object> postValue2 = beforeData2.toMap();
//        childUpdates.put("2",postValue2);

            Log.d("firebase", (String) postValue.get("professor"));


//            childUpdates.put("/Reservation/" + myRef.child("Reservation").push().getKey(), postValue);
            myRef.updateChildren(childUpdates);


            myRef.child("Reservation").child(uid).setValue(postValue);


            finish();
        }
        if (dateTXT.getText().equals(date)){
            showToast("날짜를 설정해 주세요");
        }else if(timeTXT.getText().equals(time)){
            showToast("시간을 설정해 주세요");
        }else if(titleETXT.getText().length() == 0){
            showToast("제목을 입력해 주세요");
        }else if(contentsETXT.getText().length() == 0){
            showToast("내용을 입력해 주세요");
        }

        FragmentTransaction ft = manager.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.main_container, beforeFragment);
        ft.commit();
    }

    // Dialog 이벤트 처리
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClickCalender(View v){

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
                dateTXT.setText((month+1)+"."+dayOfMonth+"("+dayArray[dayOfWeek+DOF]+")");
            }
        },year,month,day);

        // 지나간 날짜 선택 못하게 min date 설정
        dateDialog.getDatePicker().setMinDate(minDate);
        // dialog 를 띄움.
        dateDialog.show();
    }

    // 시간설정 이벤트 처리
    public void onClickTimer(View v){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeTXT.setText(hourOfDay + ":" + minute);
                showToast(hourOfDay + ":" + minute);
            }
        }, hour,minute, false);
        timeDialog.show();
    }




}