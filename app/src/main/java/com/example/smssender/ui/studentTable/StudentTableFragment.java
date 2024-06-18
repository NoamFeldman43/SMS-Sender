package com.example.smssender.ui.studentTable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smssender.R;
import com.example.smssender.databinding.FragmentStudenttableBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentTableFragment extends Fragment {

    private FragmentStudenttableBinding binding;

    private FirebaseFirestore db;
    private List<Student> studentList;
    private StudentAdapter adapter;
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStudenttableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseFirestore.getInstance();
        listView = root.findViewById(R.id.listViewStudents);
        studentList = new ArrayList<>();
        adapter = new StudentAdapter(root.getContext(), studentList);

        listView.setAdapter(adapter);

        createListView();




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void deleteStudent(int position)
    {
        Student selectedStudent = studentList.get(position);
        String documentId = selectedStudent.getDocumentID();

        // Proceed to delete the document using the document ID
        db.collection("students").document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DeleteStudent", "DocumentSnapshot successfully deleted!");
                        studentList.remove(position); // Remove the student from the list
                        adapter.notifyDataSetChanged(); // Notify the adapter to refresh the list
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DeleteStudent", "Error deleting document", e);
                    }
                });
    }

    private void createListView()
    {
        db.collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        String name = (String)document.get("studentName");
                        String phoneNumber = (String)document.get("phoneNumber");
                        String document_id = document.getId();
                        Student student = new Student(name, phoneNumber, document_id);
                        studentList.add(student);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.w("MainActivity", "Error getting documents.", task.getException());
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                new AlertDialog.Builder(binding.getRoot().getContext())
                        .setTitle("מחיקת תלמיד")
                        .setMessage("האם אתה בטוח במחיקה?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                deleteStudent(position);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });
    }
}