package com.example.hustory;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ReservingActivity extends AppCompatActivity {
    // 매개변수 받아서 Toast
    private void showToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    // 요일 array
    private String[] dayArray;


    // 뷰 멤버변수
    private TextView dateTXT;

    // 상담 방법 스피너 변수
    private Spinner spinner;
    private String[] arrData;
    private ArrayAdapter<String> arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserving);

        init();

    }

    public void init(){
        // 뷰 변수 선언
        dateTXT = findViewById(R.id.dateTXT);

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

    }

    // 예약 버툰 클릭 이벤트
    public void onClick(View v){
        showToast("click");
        finish();
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

                showToast(year + ":"+ month+1  + ":" +dayOfMonth + ":" + dayArray[dayOfWeek] );
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
                showToast(hourOfDay + ":" + minute);
            }
        }, hour,minute, false);
        timeDialog.show();
    }


}