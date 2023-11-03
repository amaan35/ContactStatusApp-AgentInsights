package com.example.contactstatusapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    CallReceiver receiver = new CallReceiver();
    SwitchMaterial toggle;
    private boolean isReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText statusEDT = findViewById(R.id.statusEdittext);
        Button saveButton = findViewById(R.id.saveButton);
        toggle = findViewById(R.id.switchBtn);
        SharedPreferences sharedPreferences = getSharedPreferences("STATUS_HANDLER", MODE_PRIVATE);
        String customStatus = sharedPreferences.getString("status", "");
        statusEDT.setText(customStatus);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newStatus = statusEDT.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("status", newStatus);
                editor.apply();
                Toast.makeText(MainActivity.this, "Status saved, now toggle on to apply", Toast.LENGTH_SHORT).show();
            }
        });
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
                } else if (b && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    SharedPreferences sharedPreferences = getSharedPreferences("STATUS_HANDLER", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isOn", true);
                    editor.apply();
                    registerReceiver(receiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));
                    isReceiverRegistered = true;
                } else {
                    if (isReceiverRegistered) {
                        isReceiverRegistered = false;
                        unregisterReceiver(receiver);
                    }
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SharedPreferences sharedPreferences = getSharedPreferences("STATUS_HANDLER", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isOn", true);
                editor.apply();
                registerReceiver(receiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));
                isReceiverRegistered = true;
            } else {
                toggle.setChecked(false);
                if (isReceiverRegistered) {
                    isReceiverRegistered = false;
                    unregisterReceiver(receiver);
                }
            }
        }
    }
}