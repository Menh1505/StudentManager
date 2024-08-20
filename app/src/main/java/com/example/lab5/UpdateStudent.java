package com.example.lab5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class UpdateStudent extends AppCompatActivity {
    int MajorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnBack = findViewById(R.id.btnBack);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        TextInputEditText inpName = findViewById(R.id.inpName);
        TextInputEditText inpAddress = findViewById(R.id.inpAddress);
        Spinner spnMajor = findViewById(R.id.spnMajor);

        //lấy dữ liệu bên kia đổ vào
        Intent receivedIntent = getIntent();
        Bundle receivedBundle = receivedIntent.getExtras();
        int receivedMajorId = receivedBundle.getInt("MajorId");
        inpName.setText(receivedBundle.getString("Name"));
        inpAddress.setText(receivedBundle.getString("Address"));

        ArrayList<Major> listMajor = new ArrayList<>();
        listMajor.add(new Major(0, "Không có"));
        listMajor.add(new Major(1, "Công nghệ thông tin"));
        listMajor.add(new Major(2, "Quản trị kinh doanh"));
        listMajor.add(new Major(3, "Sửa chữa oto"));
        listMajor.add(new Major(4, "Khoa học vật liệu"));
        listMajor.add(new Major(5, "Thiết kế thời trang"));
        MajorAdapter spinnerAdapter = new MajorAdapter(this, listMajor);
        spnMajor.setAdapter(spinnerAdapter);
        spnMajor.setSelection(receivedMajorId);

        spnMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MajorId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spnMajor.setSelection(0);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean n = inpName.getText().toString().trim().isEmpty();
                boolean a = inpAddress.getText().toString().trim().isEmpty();
                if(n || a)
                {
                    Toast.makeText(UpdateStudent.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent sendIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("Id", getIntent().getIntExtra("Id", 1));
                bundle.putInt("MajorId", MajorId);
                bundle.putString("Name", inpName.getText().toString());
                bundle.putString("Address", inpAddress.getText().toString());
                sendIntent.putExtras(bundle);
                setResult(1, sendIntent);
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public class Major
    {
        public int Id;
        public String Name;
        Major(Major major)
        {
            this.Id = major.Id;
            this.Name = major.Name;
        }
        Major(int Id, String Name)
        {
            this.Id = Id;
            this.Name = Name;
        }
    }
    public class MajorAdapter extends ArrayAdapter<Major> {

        public MajorAdapter(@NonNull Context context, @NonNull List<Major> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            Major major = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(android.R.id.text1);
            if (major != null) {
                tvName.setText(major.Name);
            }
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getView(position, convertView, parent);
        }
    }
}