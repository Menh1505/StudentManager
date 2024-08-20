package com.example.lab5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentManager extends AppCompatActivity {
    int chosedMajorId = -1;
    int chosedStudentId = -1;
    int studentId = 0;
    ArrayList<Student> listStudent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnDelete = findViewById(R.id.btnDelete);
        Spinner spnMajor = findViewById(R.id.spnMajor);
        ListView lstvStudent = findViewById(R.id.lstvStudent);

        ArrayList<Major> listMajor = new ArrayList<>();
        listMajor.add(new Major(-1, "Tất cả"));
        listMajor.add(new Major(0, "Không có"));
        listMajor.add(new Major(1, "Công nghệ thông tin"));
        listMajor.add(new Major(2, "Quản trị kinh doanh"));
        listMajor.add(new Major(3, "Sửa chữa oto"));
        listMajor.add(new Major(4, "Khoa học vật liệu"));
        listMajor.add(new Major(5, "Thiết kế thời trang"));

        StudentAdapter studentAdapter = new StudentAdapter(this, listStudent);
        lstvStudent.setAdapter(studentAdapter);
        lstvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Student temp = (Student)adapterView.getItemAtPosition(i);
                chosedStudentId = temp.Id;
                Toast.makeText(StudentManager.this, "Student id: " + chosedStudentId, Toast.LENGTH_SHORT).show();
            }
        });

        MajorAdapter spinnerAdapter = new MajorAdapter(this, listMajor);
        spnMajor.setAdapter(spinnerAdapter);
        spnMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosedMajorId = i - 1;
                Log.i("StdManager", "chosed major id");
                if(chosedMajorId != -1)
                {
                    Log.i("StdManager", "major id: " + chosedMajorId);
                    ArrayList<Student> chosedStudent = new ArrayList<>();
                    for (Student student : listStudent) {
                        Log.i("StdManager", "student id: " + student.Major.Id);
                        if (student.Major.Id == chosedMajorId) {
                            chosedStudent.add(student);
                        }
                    }
                    StudentAdapter chosedStudentAdapter = new StudentAdapter(StudentManager.this, chosedStudent);
                    lstvStudent.setAdapter(chosedStudentAdapter);
                }
                else
                {
                    lstvStudent.setAdapter(studentAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spnMajor.setSelection(0);
            }
        });



        ActivityResultLauncher<Intent> getStudent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == 1)
                        {
                            Intent intent = result.getData();
                            if(intent != null)
                            {
                                Bundle bundle = intent.getExtras();
                                Optional<Major> optionalMajor = listMajor.stream()
                                        .filter(item -> item.Id == bundle.getInt("MajorId"))
                                        .findFirst();
                                Major major = optionalMajor.orElse(null);
                                String Name = bundle.getString("Name");
                                String Address = bundle.getString("Address");
                                studentId = bundle.getInt("Id");
                                for(Student student: listStudent)
                                {
                                    if(student.Id == studentId)
                                    {
                                        student.Name = Name;
                                        student.Address = Address;
                                        student.Major = major;
                                        studentAdapter.notifyDataSetChanged();
                                        Toast.makeText(StudentManager.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                Student student = new Student(studentId, major, Name, Address);
                                studentId++;
                                listStudent.add(student);
                                Toast.makeText(StudentManager.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                studentAdapter.notifyDataSetChanged(); // cập nhật list
                            }
                        }
                    }
                });


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(StudentManager.this, AddStudent.class);
                sendIntent.putExtra("Id", studentId);
                getStudent.launch(sendIntent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(StudentManager.this, UpdateStudent.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Id", listStudent.get(chosedStudentId).Id);
                bundle.putString("Name", listStudent.get(chosedStudentId).Name);
                bundle.putString("Address", listStudent.get(chosedStudentId).Address);
                bundle.putInt("MajorId", listStudent.get(chosedStudentId).Major.Id);
                sendIntent.putExtras(bundle);
                getStudent.launch(sendIntent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chosedStudentId != -1)
                {
                    new AlertDialog.Builder(studentAdapter.context)
                            .setTitle("Xóa học sinh")
                            .setMessage("Bạn có chắc chắn muốn xóa học sinh " + listStudent.get(chosedStudentId).Name + "?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                listStudent.remove(chosedStudentId);
                                studentAdapter.notifyDataSetChanged(); // Update the list view
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
            }
        });
    }
    public class Major
    {
        public int Id;
        public String Name;
        Major(AddStudent.Major major)
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
    public class Student
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
    public class StudentAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<Student> list;

        public StudentAdapter(Context context, ArrayList<Student> list)
        {
            this.context = context;
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            }

            TextView txt1 = view.findViewById(android.R.id.text1);
            txt1.setText(list.get(i).Name);

            return view;
        }
    }
}