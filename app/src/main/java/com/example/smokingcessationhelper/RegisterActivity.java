package com.example.smokingcessationhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    private ImageView imageView;
    private FirebaseStorage storage;
    private static final int PICK_IMAGE = 0;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        imageView = findViewById(R.id.RegisterActivity_ivSelectImage);
        imageView.setOnClickListener(onClickListener);
        findViewById(R.id.RegisterActivity_btRegister).setOnClickListener(onClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.RegisterActivity_btRegister:
                    signUp();
                    break;
                case R.id.RegisterActivity_ivSelectImage:
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_IMAGE);
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
           if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                }
            }
            else if(resultCode == RESULT_CANCELED){
                startToast("선택 취소");
            }
        }
    }

    private void signUp() {
        String email = ((EditText)findViewById(R.id.RegisterActivity_etEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.RegisterActivity_etPwd)).getText().toString();
        String name = ((EditText)findViewById(R.id.RegisterActivity_etId)).getText().toString();

        if(email.length() > 0 && password.length() > 0 && name.length() >0) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startToast("회원가입에 성공했습니다.");

                                    String email = ((EditText)findViewById(R.id.RegisterActivity_etEmail)).getText().toString();
                                    String password = ((EditText)findViewById(R.id.RegisterActivity_etPwd)).getText().toString();
                                    String name = ((EditText)findViewById(R.id.RegisterActivity_etId)).getText().toString();

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    UserInfo userInfo = new UserInfo(email, name, password);
                                    
                                    db.collection(email)
                                            .document("UserInfo")
                                            .set(userInfo)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "setData:Success ");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error setting document", e);
                                                }
                                            });


                                    if(path != null) {
                                        StorageReference storageRef = storage.getReference();
                                        Uri file = Uri.fromFile(new File(path));
                                        StorageReference profile = storageRef.child("images/" + email);
                                        UploadTask uploadTask = profile.putFile(file);
                                        uploadTask
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        startToast("에러");
                                                        //int errorCode = ((StorageException) exception).getErrorCode();
                                                        //String errorMessage = exception.getMessage();
                                                    }
                                                })
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    startToast("성공");
                                                    }
                                                });
                                    }
                                }
                                else {startToast("회원가입에 실패했습니다."); }
                            }
                        });
        }else {
            startToast("이름과 이메일, 비밀번호를 입력해주세요.");
        }
    }


    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
