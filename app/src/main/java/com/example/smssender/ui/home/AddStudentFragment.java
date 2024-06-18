package com.example.smssender.ui.home;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smssender.R;
import com.example.smssender.databinding.FragmentAddstudentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



public class AddStudentFragment extends Fragment {

    private FragmentAddstudentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddstudentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText stdName = root.findViewById(R.id.nameEditText);
        EditText phoneNumber = root.findViewById(R.id.phoneEditText);

        Button add = root.findViewById(R.id.addButton);


        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String name = stdName.getText().toString().trim();
                String phone = phoneNumber.getText().toString().trim();

                // Add student name and phone number to our FireStore database if valid
                if (checkPhoneNumber(phone))
                {
                    addStudent(name, phone);
                    Toast.makeText(v.getContext(), "מספר טלפון נוסף בהצלחה", Toast.LENGTH_SHORT).show();

                    // Reset text fields for faster addition
                    stdName.setText("");
                    phoneNumber.setText("");
                }
                else
                {
                    // Show pop message for invalid phone number
                    Toast.makeText(v.getContext(), "מספר טלפון שגוי", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addStudent(String name, String phoneNumber)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("phoneNumber", phoneNumber);
        user.put("studentName", name);

        // Add a new document with a generated ID
        db.collection("students")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public boolean checkPhoneNumber(String phoneNumber)
    {
        String regex = "^05\\d{7,8}$";
        return phoneNumber.matches(regex);
    }
}