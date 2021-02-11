package com.example.draft_laundry.LaundryMain.dorm202;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.draft_laundry.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Laundry_Information202 extends Fragment {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    Integer ltime = 0;
    // 세탁기, 건조기 수
    String temp;
    Integer num_washer = 2;
    Integer num_dryer = 1;
    Button bt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_landuryinformation202, container, false);
        // 데이터 Write
        // mConditionRef.setValue(20);
        // 데이터 Read
//        DatabaseReference mConditionRef = mDatabase.child("202").child("w1");
//        mConditionRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ltime = snapshot.getValue(Integer.class);
//                Button w1_button;
//                w1_button = (Button) getActivity().findViewById(R.id.w1_202);
//                w1_button.setText(ltime.toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 세탁기 리스트
        ArrayList<Button> washer_bt = new ArrayList<>();
        Button w1_button = (Button) getActivity().findViewById(R.id.w1_202);
        Button w2_button = (Button) getActivity().findViewById(R.id.w2_202);
        washer_bt.add(w1_button);
        washer_bt.add(w2_button);
        for (int i = 1; i <= num_washer; i++) {
            temp = "w" + String.valueOf(i);
            DatabaseReference mConditionRef = mDatabase.child("202").child(temp);
            int finalI = i;
            mConditionRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ltime = snapshot.getValue(Integer.class);
                    temp = ltime.toString();
                    bt = washer_bt.get(finalI -1);
                    bt.setText(ltime.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        
        // 건조기 리스트
        ArrayList<Button> dryer_bt = new ArrayList<>();
        Button d1_button = (Button) getActivity().findViewById(R.id.d1_202);
        dryer_bt.add(d1_button);
        for (int i = 1; i <= num_dryer; i++) {
            temp = "d" + String.valueOf(i);
            DatabaseReference mConditionRef = mDatabase.child("202").child(temp);
            int finalI = i;
            mConditionRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ltime = snapshot.getValue(Integer.class);
                    temp = ltime.toString();
                    bt = dryer_bt.get(finalI -1);
                    bt.setText(ltime.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}