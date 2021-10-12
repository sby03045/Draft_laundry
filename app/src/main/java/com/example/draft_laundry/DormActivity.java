package com.example.draft_laundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.draft_laundry.LaundryMain.LaundryMainActivity;

public class DormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button dorm201 = (Button)findViewById(R.id.dorm201);
        Button dorm202 = (Button)findViewById(R.id.dorm202);
        Button dorm203 = (Button)findViewById(R.id.dorm203);
        Button dorm204 = (Button)findViewById(R.id.dorm204);

        dorm201.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "201동", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LaundryMainActivity.class);
                intent.putExtra("dorm_num", "201");
                startActivity(intent);
            }
        });
        dorm202.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "202동", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LaundryMainActivity.class);
                intent.putExtra("dorm_num", "202");
                startActivity(intent);
            }
        });
        dorm203.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "203동", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LaundryMainActivity.class);
                intent.putExtra("dorm_num", "203");
                startActivity(intent);
            }
        });
        dorm204.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "204동", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LaundryMainActivity.class);
                intent.putExtra("dorm_num", "204");
                startActivity(intent);
            }
        });
    }
}