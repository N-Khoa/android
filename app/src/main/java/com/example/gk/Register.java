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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    Button btnRegister;
    TextView btnLoginPage;
    EditText edtEmail, edtPassword,edtConfirmPassword;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtEmail.getText().toString().isEmpty() || edtEmail.getText().toString().isEmpty() || edtConfirmPassword.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Vui lòng nhập thông tin.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!checkEmail(edtEmail.getText().toString())) {
                    Toast.makeText(Register.this, "Email không hợp lệ.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (edtPassword.getText().toString().length()<6) {
                    Toast.makeText(Register.this, "Mật khẩu phải dài hơn 6 ký tự.", Toast.LENGTH_SHORT).show();
                } else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                    Toast.makeText(Register.this, "Mật khẩu không khớp với nhau.", Toast.LENGTH_SHORT).show();
                    return;
                }else {

                    String email = edtEmail.getText().toString();
                    String password =edtPassword.getText().toString();
                    String name = email.substring(0, email.indexOf('@'));

                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        writeDataToDatabase(firebaseAuth.getCurrentUser().getUid().toString(),name ,email);
                                        Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Register.this,Login.class);
                                        startActivity(intent);
                                    } else  {
                                        Toast.makeText(Register.this, "Đăng ký thất bại. ", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            });
                }
            }

            private void writeDataToDatabase(String uri ,String name, String email) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = firebaseDatabase.getReference("Users").child(uri);

                User user = new User(name, email);

                usersRef.setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Data added successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        btnLoginPage = findViewById(R.id.btnLoginPage);
        btnRegister = findViewById(R.id.btnRegister);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
    }
}