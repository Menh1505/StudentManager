package com.example.lab5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddStudent extends AppCompatActivity {
    int chosedMajorId;
    List<Student> listStudent = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnBack = findViewById(R.id.btnBack);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        TextInputEditText inpName = findViewById(R.id.inpName);
        TextInputEditText inpAddress = findViewById(R.id.inpAddress);
        Spinner spnKhoa = findViewById(R.id.spnKhoa);

        ArrayList<Major> listMajor = new ArrayList<>();
        listMajor.add(new Major(0, "Không có"));
        listMajor.add(new Major(1, "Công nghệ thông tin"));
        listMajor.add(new Major(2, "Quản trị kinh doanh"));
        listMajor.add(new Major(3, "Sửa chữa oto"));
        listMajor.add(new Major(4, "Khoa học vật liệu"));
        listMajor.add(new Major(5, "Thiết kế thời trang"));


        MajorAdapter spinnerAdapter = new MajorAdapter(this, listMajor);
        spnKhoa.setAdapter(spinnerAdapter);

        spnKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosedMajorId = listMajor.get(i).Id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spnKhoa.setSelection(0);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean n = inpName.getText().toString().trim().isEmpty();
                boolean a = inpAddress.getText().toString().trim().isEmpty();
                if(n || a)
                {
                    Toast.makeText(AddStudent.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent sendIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("Id", getIntent().getIntExtra("Id", 1));
                bundle.putInt("MajorId", chosedMajorId);
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

    public class Student implements Serializable
    {
        public int Id;
        public Major Major;
        public String Name;
        public String Address;
        Student(int Id, Major Major, String Name, String Address)
        {
            this.Id = Id;
            this.Major = Major;
            this.Name = Name;
            this.Address = Address;
        }
    }

}

