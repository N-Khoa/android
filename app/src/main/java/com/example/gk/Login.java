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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    TextView btnRegisterPage,btnForgotPassword;
    EditText edtPassword,edtEmail;
    Button btnLogin;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setControl();
        setEvent();

    }

    private void setEvent() {
        btnRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtEmail.getText().toString().isEmpty() || edtEmail.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "Vui lòng nhập sthông tin.", Toast.LENGTH_LONG).show();
                    return;
                }else if (!checkEmail(edtEmail.getText().toString())) {
                    Toast.makeText(Login.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = edtEmail.getText().toString();
                String password =edtPassword.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Login.this,Home_page.class);
                                    startActivity(intent);
                                    finish();
                                } else  {
                                    Toast.makeText(Login.this, "Không tin không chính xác. ", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        });
            }

            private boolean checkEmail(String email) {
                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                Pattern pattern = Pattern.compile(emailRegex);
                Matcher matcher = pattern.matcher(email);
                Boolean check = matcher.matches();
                return check;
            }
        });
    }

    private void setControl() {
        btnRegisterPage = findViewById(R.id.btnRegisterPage);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
    }
}