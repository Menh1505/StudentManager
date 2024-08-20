package com.example.lab5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab5.R;
import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextInputEditText inpUsername = findViewById(R.id.inpUsername);
        TextInputEditText inpPassword = findViewById(R.id.inpPassword);
        TextInputEditText inpConfirm = findViewById(R.id.inpConfirmPassword);
        Button btnDangKy = findViewById(R.id.btnDangKy);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inpUsername.getText().toString().matches(""))
                {
                    Toast.makeText(Register.this, "Tên người dùng không được trống", Toast.LENGTH_SHORT).show();
                }
                else if (inpPassword.getText().toString().matches(""))
                {
                    Toast.makeText(Register.this, "Mật khẩu không được trống", Toast.LENGTH_SHORT).show();
                }
                else if (inpConfirm.getText().toString().matches(""))
                {
                    Toast.makeText(Register.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(Register.this, Login.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("StatusMessage", "Đăng ký thành công");
                    bundle.putString("Username", inpUsername.getText().toString());
                    bundle.putString("Password", inpPassword.getText().toString());

                    intent.putExtras(bundle);
                    setResult(1,intent);
                    finish();
                }
            }
        });

    }
}