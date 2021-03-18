package com.example.hustory.question;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.hustory.MainActivity;
import com.example.hustory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentQuestion extends Fragment {

    View view;

    private Context mContext;
    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser currentUser;
    private String uId;

    private ArrayList<String> key_Arr = new ArrayList<>();
    private ArrayList<String> name_Arr = new ArrayList<>();
    private ArrayList<String> count_Arr = new ArrayList<>();

    QuestionAdapter questionAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_question, container, false);
        init();

        return view;
    }

    public void init() {
        questionAdapter = new QuestionAdapter();
        ListView listview1 = (ListView) view.findViewById(R.id.list_question);
        listview1.setAdapter(questionAdapter);

        TextView new_orderTXT = (TextView) view.findViewById(R.id.new_orderTXT);
        TextView hot_orderTXT = (TextView) view.findViewById(R.id.hot_orderTXT);
        TextView my_orderTXT = (TextView) view.findViewById(R.id.my_orderTXT);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        uId = currentUser.getUid();

        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                // 최신순 버튼을 누르면
                case R.id.new_orderTXT:
                    questionAdapter.clear();
                    key_Arr.clear();
                    name_Arr.clear();

                    // Question에 있는 질문들 다 가져오기
                    myRef.child("Question").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.i("firebase", "Error getting data", task.getException());
                            }
                            else {
                                Log.i("firebase Question", String.valueOf(task.getResult().getValue()));

                                if (String.valueOf(task.getResult().getValue()).equals("null")) {
                                    Log.i("null", "왜 무한루프?123");
                                    questionAdapter.clear();
                                    questionAdapter.notifyDataSetChanged();
                                    return;
                                }
                                else {
                                    // DataSnapshot 자료형을 HashMap으로 가져옴
                                    for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                                        HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                                        String id = member.get("id").toString();
                                        String q_Num = member.get("q_Num").toString();
                                        String q_title = member.get("q_title").toString();
                                        String q_content = member.get("q_content").toString();
                                        String q_date = member.get("q_date").toString();
                                        String q_time = member.get("q_time").toString();
                                        String q_count = member.get("q_count").toString();
                                        String q_like = member.get("q_like").toString();
                                        String q_dislike = member.get("q_dislike").toString();


                                        myRef.child("Member").child(id).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                String name = (String) task.getResult().getValue();
                                                Log.i("name ", name);
                                                name_Arr.add(0, name);
                                            }
                                        });

                                        questionAdapter.addItemDESC(0, id, q_Num, q_title, q_content, q_date, q_time, q_count, q_like, q_dislike);
                                        key_Arr.add(0, q_Num);
                                        count_Arr.add(0, q_count);
                                    }
                                }
                                questionAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    //questionAdapter.notifyDataSetChanged();
                    break;
                case R.id.hot_orderTXT:
                    questionAdapter.clear();
                    key_Arr.clear();
                    name_Arr.clear();
                    break;
                case R.id.my_orderTXT:
                    questionAdapter.clear();
                    key_Arr.clear();
                    name_Arr.clear();

                    myRef.child("Member").child(uId).child("Q_List").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.i("firebase", "Error getting data", task.getException());
                            }
                            else {
                                Log.i("firebase Question ", String.valueOf(task.getResult().getValue()));

                                if (String.valueOf(task.getResult().getValue()).equals("null")) {
                                    Log.i("null", "왜 무한루프?123");
                                    questionAdapter.clear();
                                    questionAdapter.notifyDataSetChanged();
                                    return;
                                }
                                else {
                                    for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                                        HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                                        String id = member.get("id").toString();
                                        String q_Num = member.get("q_Num").toString();
                                        String q_title = member.get("q_title").toString();
                                        String q_content = member.get("q_content").toString();
                                        String q_date = member.get("q_date").toString();
                                        String q_time = member.get("q_time").toString();
                                        String q_count = member.get("q_count").toString();
                                        String q_like = member.get("q_like").toString();
                                        String q_dislike = member.get("q_dislike").toString();

                                        myRef.child("Member").child(id).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                String name = (String) task.getResult().getValue();
                                                Log.i("name ", name);
                                                name_Arr.add(name);
                                            }
                                        });

                                        questionAdapter.addItem(id, q_Num, q_title, q_content, q_date, q_time, q_count, q_like, q_dislike);
                                        key_Arr.add(q_Num);
                                        count_Arr.add(q_count);
                                    }
                                }
                                questionAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;
            }
        };

        new_orderTXT.setOnClickListener(onClickListener);
        hot_orderTXT.setOnClickListener(onClickListener);
        my_orderTXT.setOnClickListener(onClickListener);

        myRef.child("Question").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.i("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.i("firebase Question ", String.valueOf(task.getResult().getValue()));

                    if (String.valueOf(task.getResult().getValue()).equals("null")) {
                        Log.i("null", "왜 무한루프?123");
                        questionAdapter.clear();
                        questionAdapter.notifyDataSetChanged();
                        return;
                    }
                    else {
                        for(DataSnapshot userSnapshot : task.getResult().getChildren()) {
                            HashMap<String, Object> member = (HashMap<String, Object>) userSnapshot.getValue();
                            String id = member.get("id").toString();
                            String q_Num = member.get("q_Num").toString();
                            String q_title = member.get("q_title").toString();
                            String q_content = member.get("q_content").toString();
                            String q_date = member.get("q_date").toString();
                            String q_time = member.get("q_time").toString();
                            String q_count = member.get("q_count").toString();
                            String q_like = member.get("q_like").toString();
                            String q_dislike = member.get("q_dislike").toString();

                            questionAdapter.addItemDESC(0, id, q_Num, q_title, q_content, q_date, q_time, q_count, q_like, q_dislike);
                            key_Arr.add(0, q_Num);
                            count_Arr.add(0, q_count);
                        }
                        questionAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        // 질문 리스트 아이템 클릭 이벤트 리스너
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AnswerActivity.class);
                String q_Num = key_Arr.get(position);
                String name = name_Arr.get(position);

                // 아이템을 클릭하면 조회수 1 증가
                int count = Integer.parseInt(count_Arr.get(position)) + 1;
                Log.i("count", "" + count);
                myRef.child("Member").child(uId).child("Q_List").child(q_Num).child("q_count").setValue(count);
                myRef.child("Question").child(q_Num).child("q_count").setValue(count);

                // 현재 클릭된 아이템의 질문번호(q_Num)과 작성자 이름을 넘겨줌
                intent.putExtra("q_Num", q_Num);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        mContext = getContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        fab_main = (FloatingActionButton) view.findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton) view.findViewById(R.id.fab_sub1);
        fab_sub2 = (FloatingActionButton) view.findViewById(R.id.fab_sub2);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen) {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_close);
                    fab_sub2.startAnimation(fab_close);
                    fab_sub1.setClickable(false);
                    fab_sub2.setClickable(false);
                    isFabOpen = false;
                } else {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_open);
                    fab_sub2.startAnimation(fab_open);
                    fab_sub1.setClickable(true);
                    fab_sub2.setClickable(true);
                    isFabOpen = true;
                }
            }
        });

        fab_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddQuestion();
            }
        });
    } // init()

    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getActivity().getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return view.dispatchTouchEvent(ev);
    }

    private void startAddQuestion() {
        Intent intent = new Intent(getActivity(), AddQuestionActivity.class);
        startActivity(intent);
    }
}
