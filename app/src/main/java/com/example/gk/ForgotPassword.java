package com.example.gk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {
    EditText edtEmail,edtOtp;
    Button btnOtp,btnConfirm;
    TextView btnBack;
    String otp;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail.getText().toString().isEmpty()){
                    Toast.makeText(ForgotPassword.this,"Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!checkEmail(edtEmail.getText().toString())) {
                    Toast.makeText(ForgotPassword.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    otp = sendOTP(edtEmail.getText().toString());
                }
            }
            @NonNull
            private String sendOTP(String email) {
                Random random = new Random();
                int otp = 100000 + random.nextInt(900000);
                JavaMailAPI javaMailAPI = new JavaMailAPI(ForgotPassword.this,email,"OTP CODE","OTP: " + otp);
                javaMailAPI.execute();
                return String.valueOf(otp);
            }
            private boolean checkEmail(String email) {
                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                Pattern pattern = Pattern.compile(emailRegex);
                Matcher matcher = pattern.matcher(email);
                Boolean check = matcher.matches();
                return check;
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtOtp.getText().toString().isEmpty()){
                    Toast.makeText(ForgotPassword.this, "Vui lòng nhập OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!otp.equals(edtOtp.getText().toString())){
                    Toast.makeText(ForgotPassword.this, "OTP không khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    resetPassword(edtEmail.getText().toString());
                    Intent intent = new Intent(ForgotPassword.this,Login.class);
                    startActivity(intent);
                }
            }
            private void resetPassword(String email) {
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "Đã gửi link đổi mật khẩu đến mail của bạn!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void setControl() {
        edtEmail = findViewById(R.id.edtEmail);
        edtOtp = findViewById(R.id.edtOtp);
        btnOtp = findViewById(R.id.btnOtp);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);
        auth = FirebaseAuth.getInstance();
    }
}