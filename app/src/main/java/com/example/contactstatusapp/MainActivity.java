package com.example.contactstatusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText statusEDT = findViewById(R.id.statusEdittext);
        Button saveButton = findViewById(R.id.saveButton);
        SwitchMaterial toggle = findViewById(R.id.switchBtn);
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
    }
}