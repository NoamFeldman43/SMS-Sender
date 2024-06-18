package com.example.smssender.ui.sender;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;

import com.example.smssender.R;
import com.example.smssender.databinding.FragmentSenderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



public class SenderFragment extends Fragment {

    private FragmentSenderBinding binding;
    private String sms_message;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSenderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText sms_message = root.findViewById(R.id.nameEditText);
        this.sms_message = sms_message.getText().toString();

        Button add = root.findViewById(R.id.sendButton);

        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(SenderFragment.super.requireActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(SenderFragment.super.requireActivity(), new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
                else
                {
                    // You already have permission
                    sendSMSSignal(sms_message.getText().toString());
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

    protected void sendSMSSignal(String msg)
    {

        SmsManager smsManager = SmsManager.getDefault();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        String phoneNo = (String)document.get("phoneNumber");
                        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                    }
                }
                else
                {
                    Log.w("MainActivity", "Error getting documents.", task.getException());
                }
            }
        });

        Toast.makeText(binding.getRoot().getContext(), "ההודעה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // Permission was granted, send the SMS
                sendSMSSignal(sms_message);
            }
            else
            {
                // Permission denied, show a message to the user
                Toast.makeText(binding.getRoot().getContext(), "אין גישה לשליחת סמס", Toast.LENGTH_SHORT).show();
            }
        }
    }

}