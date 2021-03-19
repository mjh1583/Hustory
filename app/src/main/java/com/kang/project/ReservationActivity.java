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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private TextView text_professor;
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
//    private String uid = user.getUid();
    private String profUid;
    private String professor;
    private String student;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reservation);
        init();
    }


    public void init() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        //요일 변수 선언
        dayArray = getResources().getStringArray(R.array.day_of_week);

        //view 변수 선언
        text_professor = findViewById(R.id.text_professor);
        text_summary = findViewById(R.id.text_summary);
        select_date = findViewById(R.id.select_date);
        select_time = findViewById(R.id.select_time);
        select_place = findViewById(R.id.select_place);
        select_content = findViewById(R.id.select_content);
        spinner = (Spinner)findViewById(R.id.onOffSpiner);

        // 이름 설정 함수
        myRef.child("Member").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    profUid = snapshot.child("BSSpxq2WkFewZ8gEty9ZDR7nma92").child("prof").getValue().toString();
                    professor = snapshot.child(profUid).child("name").getValue().toString();
                    student = snapshot.child("BSSpxq2WkFewZ8gEty9ZDR7nma92").child("name").getValue().toString();
                    text_professor.setText(professor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




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
        // 교수 정보 가져오기
//        FirebaseDatabase.getInstance().getReference().child("Member").child("v5N3FZkAIxTS2IpVV4ki2yUbRwJ2").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d("MainActivity", "ValueEventListener : " + dataSnapshot.child("role").getValue());
//                    profUid = dataSnapshot.child("prof").getValue().toString();
//                    Log.i("profUid", profUid);
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("remove", "없어");
//            }
//        });
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
//                showToast(hourOfDay + ":" + minute);
            }
        }, hour,minute, false);
        timeDialog.show();
    }

    // 예약버튼 클릭 이벤트
    public void onReserve(View v){

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


//         예약하기 버튼 클릭시 예약 해서 firebase 에 데이터 전송
        if(!select_date.getText().equals("날짜 설정") && !select_time.getText().equals("시간 설정")  && select_place.getText().length() != 0 && select_content.getText().length() !=0 && text_summary.getText().length() != 0) {
//             로그인한 유저의 이름을 저장

            key =  "" + id;
            Date d = new Date();
            Log.i("date", String.valueOf(d));
            // 학생 데이터 저장
            firebaseData = new FirebaseData("BSSpxq2WkFewZ8gEty9ZDR7nma92", professor, text_summary.getText().toString(), select_date.getText().toString(), select_time.getText().toString(), spinner.getSelectedItem().toString(), select_place.getText().toString(), "수락대기", select_content.getText().toString(), false, key);
            Map<String, Object> postValue = firebaseData.toMap();
            myRef.child("Member").child("BSSpxq2WkFewZ8gEty9ZDR7nma92").child("R_List").child(key).setValue(postValue);

            // 교수 데이터 저장
            firebaseData = new FirebaseData("BSSpxq2WkFewZ8gEty9ZDR7nma92", student, text_summary.getText().toString(), select_date.getText().toString(), select_time.getText().toString(), spinner.getSelectedItem().toString(), select_place.getText().toString(), "수락대기", select_content.getText().toString(), false, key);
            Map<String, Object> postValue2 = firebaseData.toMap();
            myRef.child("Member").child(profUid).child("R_List").child(key).setValue(postValue2);
            finish();
        }

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
    }
}