package com.example.hustory.managementcard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hustory.MentorActivity;
import com.example.hustory.R;
import com.example.hustory.login.LoginActivity;
import com.example.hustory.managementcard.CardActivity;
import com.example.hustory.managementcard.LetterActivity;
import com.example.hustory.user.User;
import com.example.hustory.userInfo.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class FragmentMy extends Fragment {
    int flag = 1;

    Button button_card;
    Button button_letter;
    Button button_mentor;

    View view;

    private final int GALLERY_CODE = 10;
    private ImageView imageview;
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;
    private TextView save;
    private TextView delete;
    private TextView logout;

    private TextView version_student;
    EditText my_school, my_major, my_company_1, my_company_2, my_company_3, name_student, my_agency, my_spot, my_department;

    private Uri filePath;

    private Context mContext;
    private FloatingActionButton fab_main, fab_sub1, fab_sub2;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    private FirebaseAuth mAuth;
    private FirebaseUser cuurentUser;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    private SharedPreferences auto;
    private SharedPreferences.Editor auto_editor;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i("getRole", "" + flag);
        getRole(UserInfo.UID);

        if (flag == 1) {
            view = inflater.inflate(R.layout.fragment_my, container, false);
            init_student();
        } else {
            view = inflater.inflate(R.layout.fragment_professor, container, false);
            init_professor();
        }

        return view;
    }

    public int getRole (String uid) {
        myRef.child("Member").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("role").toString().equals("학생")){
                    Log.i("role", "" + flag);
                    flag = 1;
                }else {
                    Log.i("role", "" + flag);
                    flag = 2;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return flag;
    }


    public void init_student() {
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

        button_card = (Button) view.findViewById(R.id.button_card);
        button_letter = (Button) view.findViewById(R.id.button_letter);

        button_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CardActivity.class);
                startActivity(intent);
            }
        });

        button_letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LetterActivity.class);
                startActivity(intent);
            }
        });


        auto = PreferenceManager.getDefaultSharedPreferences(getActivity());
        auto_editor = auto.edit();
        mAuth = FirebaseAuth.getInstance();
        cuurentUser = mAuth.getCurrentUser();

        TextView logoutTXTBTN = view.findViewById(R.id.logoutTXTBTN);

        logoutTXTBTN.setOnClickListener(v -> {
            Log.i("LogOut", "log------------out");

            mAuth.signOut();
            auto_editor.clear();
            auto_editor.apply();
            startLoginActivity();
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        my_school = (EditText) view.findViewById(R.id.my_school);
        my_major = (EditText) view.findViewById(R.id.my_major);
        my_company_1 = (EditText) view.findViewById(R.id.my_company_1);
        my_company_2 = (EditText) view.findViewById(R.id.my_company_2);
        my_company_3 = (EditText) view.findViewById(R.id.my_company_3);

        version_student=(TextView)view.findViewById(R.id.version_student);
        save = (TextView) view.findViewById(R.id.save);
        button_card = (Button) view.findViewById(R.id.button_card);
        button_letter = (Button) view.findViewById(R.id.button_letter);

        imageview = (ImageView) view.findViewById(R.id.icon_student);

        delete = (TextView)view.findViewById(R.id.delete);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUsermy_school = my_school.getText().toString();
                String getUsermy_major = my_major.getText().toString();
                String getUsermy_company_1 = my_company_1.getText().toString();
                String getUsermy_company_2 = my_company_2.getText().toString();
                String getUsermy_company_3 = my_company_3.getText().toString();
                String getUserversion_student = version_student.getText().toString();
                uploadFile();

                //hashmap 만들기
                HashMap result = new HashMap<>();
                result.put("my_school", getUsermy_school);
                result.put("my_major", getUsermy_major);
                result.put("my_company_1", getUsermy_company_1);
                result.put("my_company_2", getUsermy_company_2);
                result.put("my_company_3", getUsermy_company_3);
                result.put("ersion_student", getUserversion_student);


                writeNewUser(UserInfo.UID, getUsermy_school, getUsermy_major, getUsermy_company_1, getUsermy_company_2, getUsermy_company_3,getUserversion_student);

            }
        });
    } //init_student()

    public void init_professor() {
        name_student = (EditText) view.findViewById(R.id.name_student);
        my_agency = (EditText) view.findViewById(R.id.my_agency);
        my_department = (EditText) view.findViewById(R.id.my_department);
        my_spot = (EditText) view.findViewById(R.id.my_spot);
        button_mentor = (Button) view.findViewById(R.id.button_mentor);
        button_mentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MentorActivity.class);
                startActivity(intent);

            }
        });

        mContext = getContext();
        fab_open = AnimationUtils.loadAnimation(mContext, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(mContext, R.anim.fab_close);
        fab_main = (FloatingActionButton) view.findViewById(R.id.fab_main);
        fab_sub1 = (FloatingActionButton) view.findViewById(R.id.fab_sub1);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen) {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_close);
                    fab_sub1.setClickable(false);
                    isFabOpen = false;
                } else {
                    fab_main.setImageResource(R.drawable.booking);
                    fab_sub1.startAnimation(fab_open);
                    fab_sub1.setClickable(true);
                    isFabOpen = true;
                }
            }
        });

        auto = PreferenceManager.getDefaultSharedPreferences(getActivity());
        auto_editor = auto.edit();
        mAuth = FirebaseAuth.getInstance();
        cuurentUser = mAuth.getCurrentUser();

        TextView logoutTXTBTN = view.findViewById(R.id.logoutTXTBTN);

        logoutTXTBTN.setOnClickListener(v -> {
            Log.i("LogOut", "log------------out");

            mAuth.signOut();
            auto_editor.clear();
            auto_editor.apply();
            startLoginActivity();
        });

        delete = (TextView)view.findViewById(R.id.delete);
        name_student = (EditText) view.findViewById(R.id.name_student);
        my_department = (EditText) view.findViewById(R.id.my_department);
        my_agency = (EditText) view.findViewById(R.id.my_agency);
        my_spot= (EditText) view.findViewById(R.id.my_spot);
        save = (TextView) view.findViewById(R.id.save);
        imageview = (ImageView) view.findViewById(R.id.icon_student);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://hustory-82cc1.appspot.com");
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/0xAmXnqcqaT96ir6tr77ifGTc4D2").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시

                Glide.with(mContext.getApplicationContext())
                        .load(uri)
                        .into(imageview);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                Toast.makeText(mContext.getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        TextView logoutTXTBTN2 = view.findViewById(R.id.logoutTXTBTN);

        logoutTXTBTN2.setOnClickListener(v -> {
            Log.i("LogOut", "log------------out");

            mAuth.signOut();
            auto_editor.clear();
            auto_editor.apply();
            startLoginActivity();
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUsername_student = name_student.getText().toString();
                String getUsermy_department = my_department.getText().toString();
                String getUsermy_agency = my_agency.getText().toString();
                String getUsermy_spot = my_spot.getText().toString();

                uploadFile();

                //hashmap 만들기
                HashMap result = new HashMap<>();
                result.put("name_student", getUsername_student);
                result.put("my_department", getUsermy_department);
                result.put("my_agency", getUsermy_agency);
                result.put("my_spot", getUsermy_spot);

                writeNewUser1(UserInfo.UID, getUsername_student,getUsermy_agency,getUsermy_spot,getUsermy_department);

            }
        });
    }

    private void writeNewUser1(String userId, String name_student, String my_agency, String my_department, String my_spot) {
        professor professor = new professor(name_student,my_agency,my_department,my_spot);

        myRef.child("Member").child(userId).child("management").setValue(professor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed

                    }
                });
    }

    public void startLoginActivity () {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public boolean dispatchTouchEvent (MotionEvent ev){
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

    @Override
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();

            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    //upload the file
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();


            String filename = UserInfo.UID;
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://hustory-82cc1.appspot.com").child("images/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getActivity().getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }


    private void writeNewUser(String userId, String my_school, String my_major, String my_company_1, String my_company_2, String my_company_3,String version_student) {
        User user = new User(my_school, my_major, my_company_1, my_company_2, my_company_3,version_student);

        mDatabase.child("Member").child(userId).child("management").child("1").setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed

                    }
                });
    }
}

