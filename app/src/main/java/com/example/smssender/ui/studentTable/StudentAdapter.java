package com.example.smssender.ui.studentTable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smssender.R;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    private Context context;
    private List<Student> studentList;

    public StudentAdapter(@NonNull Context context, @NonNull List<Student> objects) {
        super(context, 0, objects);
        this.context = context;
        this.studentList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sudent_item_layout, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewPhone = convertView.findViewById(R.id.textViewPhone);

        Student student = studentList.get(position);
        textViewName.setText(student.getName());
        textViewPhone.setText(student.getPhone());

        return convertView;
    }
}
