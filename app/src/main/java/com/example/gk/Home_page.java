package com.example.gk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_page extends AppCompatActivity {
    Button btnBack;
    TextView txtIntro,txtUser,txtEmail;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setControl();
        setEvent();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user==null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }else{
            reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Kiểm tra xem dữ liệu có tồn tại không
                    if (dataSnapshot.exists()) {
                        // Lấy giá trị của username và email từ dataSnapshot
                        String username = "User: " + dataSnapshot.child("username").getValue(String.class);
                        String email = "Email: "+ dataSnapshot.child("email").getValue(String.class);
                        // Hiển thị dữ liệu trên giao diện
                        txtUser.setText(username);
                        txtEmail.setText(email);
                    } else {
                        Toast.makeText(Home_page.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home_page.this, "Đã xảy ra lỗi khi truy xuất dữ liệu", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home_page.this, Login.class);
                startActivity(intent);
                Toast.makeText(Home_page.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setControl() {
        btnBack = findViewById(R.id.btnBack);
        txtIntro = findViewById(R.id.txt);
        txtUser = findViewById(R.id.txtIUser);
        txtEmail = findViewById(R.id.txtEmail);
    }
}