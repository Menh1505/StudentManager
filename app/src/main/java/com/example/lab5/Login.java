package com.example.lab5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab5.StudentManager;
import com.google.android.material.textfield.TextInputEditText;


public class Login extends AppCompatActivity {
    private String _Username = "";
    private String _Password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnDangKy = findViewById(R.id.btnDangKy);
        Button btnDangNhap = findViewById(R.id.btnDangNhap);

        ActivityResultLauncher<Intent> getUser = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            Intent intent = result.getData();
                            if (intent != null) {
                                Bundle bundle = intent.getExtras();
                                _Username = bundle.getString("Username");
                                _Password = bundle.getString("Password");
                                Toast.makeText(Login.this, bundle.getString("StatusMessage"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                getUser.launch(intent);
            }
        });

        TextInputEditText inpUsename = findViewById(R.id.inpUsername);
        TextInputEditText inpPassword = findViewById(R.id.inpPassword);

        String loginSuccess = "Đăng nhập thành công";
        String loginFail = "Tên người dùng hoặc mật khẩu không chính xác";
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_Username.matches("") || _Password.matches(""))
                {
                    Toast.makeText(Login.this, "Bạn chưa có tài khoảng, hãy đăng ký", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean u = _Username.equals(inpUsename.getText().toString());
                boolean p = _Password.equals(inpPassword.getText().toString());

                if(u && p)
                {
                    Toast.makeText(Login.this, loginSuccess, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, StudentManager.class));
                }
                else Toast.makeText(Login.this, loginFail, Toast.LENGTH_SHORT).show();
            }
        });
    }
}