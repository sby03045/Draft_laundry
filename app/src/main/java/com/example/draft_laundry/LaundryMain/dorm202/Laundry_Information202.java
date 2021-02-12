package com.example.draft_laundry.LaundryMain.dorm202;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.draft_laundry.LaundryMain.Dryer;
import com.example.draft_laundry.LaundryMain.Washer;
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

    //새로고침을 위함
    private SwipeRefreshLayout refreshLayout = null;

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

        refreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh);

        // 세탁기 리스트
        ArrayList<Washer> washer_bt = new ArrayList<>();
        Washer w1 = new Washer((Button) getActivity().findViewById(R.id.w1_202), false, false);
        Washer w2 = new Washer((Button) getActivity().findViewById(R.id.w2_202), false, false);
        washer_bt.add(w1);
        washer_bt.add(w2);
        setwasher(num_washer, washer_bt);
        
        // 건조기 리스트
        ArrayList<Dryer> dryer_bt = new ArrayList<>();
        Dryer d1 = new Dryer((Button) getActivity().findViewById(R.id.d1_202), false, false);
        dryer_bt.add(d1);
        setdryer(num_dryer, dryer_bt);

        // 새로고침 이벤트 처리
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setwasher(num_washer, washer_bt);
                setdryer(num_dryer, dryer_bt);

                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void setwasher(Integer num_washer, ArrayList<Washer> washer_bt) {
        for (int i = 1; i <= num_washer; i++) {
            temp = "w" + String.valueOf(i);
            int finalI = i;
            // isError 값 받아와서 Washer 클래스에 저장하고, Drawableleft를 이에 따라 맞춰줌.
            // Json 파일 자체를 받아와서 수정하면 간단한 코드를 작성할 수 있을 것으로 예상함. 현재는 따로 따로 함.
            DatabaseReference mConditionRef_isError = mDatabase.child("202").child(temp).child("isError");
            mConditionRef_isError.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    (washer_bt.get(finalI -1)).isError = snapshot.getValue(Boolean.class);
                    if (washer_bt.get(finalI -1).isError == false) {
                        Drawable img = getContext().getResources().getDrawable( R.drawable.washer_64);
                        washer_bt.get(finalI -1).bt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    }
                    else {
                        Drawable img = getContext().getResources().getDrawable( R.drawable.error_64);
                        washer_bt.get(finalI -1).bt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // isTimer 값 받아오기
            DatabaseReference mConditionRef_isTimer = mDatabase.child("202").child(temp).child("isTimer");
            mConditionRef_isTimer.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean bt_isTimer;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // remaining_time 값 가져오기
            DatabaseReference mConditionRef_rt = mDatabase.child("202").child(temp).child("remaining_time");
            mConditionRef_rt.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ltime = snapshot.getValue(Integer.class);
                    temp = ltime.toString();
                    bt = (washer_bt.get(finalI -1)).bt;
                    if (washer_bt.get(finalI -1).isError == false) {
                        bt.setText(ltime.toString());
                    }
                    else {
                        bt.setText("고장");
                    }
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("t", "hi");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("무엇을 하시겠습니까?");

                            // 현재 상태가 고장나지 않았을 때 버튼을 누르면 AlertDialog를 띄움
                            if (washer_bt.get(finalI -1).isError == false) {
                                // AlertDialog 세팅
                                builder.setItems(R.array.NotErrorState, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int pos)
                                    {
                                        String[] items = getResources().getStringArray(R.array.NotErrorState);
                                        switch (pos) {
                                            case 0:
                                                // 알림 예약 설정
                                                Log.d("t", "알림 예약 설정");
                                                break;
                                            case 1:
                                                // 고장남
                                                Log.d("t", "고장 상태로 바꿔주기");
                                                Washer temp_washer = washer_bt.get(finalI -1);
                                                temp_washer.isError = true;
                                                // Button background image, text 변경
                                                Drawable img = getContext().getResources().getDrawable( R.drawable.error_64);
                                                temp_washer.bt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                                                temp_washer.bt.setText("고장");
                                                mConditionRef_isError.setValue(true);

                                                break;
                                        }
                                    }
                                });
                            }

                            else {
                                builder.setItems(R.array.ErrorState, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int pos)
                                    {
                                        String[] items = getResources().getStringArray(R.array.ErrorState);
                                        switch (pos) {
                                            case 0:
                                                // 알림 예약 설정
                                                Log.d("t", "알림 예약 설정");
                                                break;
                                            case 1:
                                                // 수리끝남
                                                Log.d("t", "원래 상태로 바꿔주기");
                                                Washer temp_washer = washer_bt.get(finalI -1);
                                                temp_washer.isError = false;
                                                // Button background image, text 변경
                                                Drawable img = getContext().getResources().getDrawable( R.drawable.washer_64);
                                                temp_washer.bt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                                                ltime = snapshot.getValue(Integer.class);
                                                temp_washer.bt.setText(ltime.toString());
                                                mConditionRef_isError.setValue(false);
                                                break;
                                        }
                                    }
                                });
                            }

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void setdryer(Integer num_dryer, ArrayList<Dryer> dryer_bt) {
        for (int i = 1; i <= num_dryer; i++) {
            temp = "d" + String.valueOf(i);
            int finalI = i;
            // isError 값 받아와서 Washer 클래스에 저장하고, Drawableleft를 이에 따라 맞춰줌.
            // Json 파일 자체를 받아와서 수정하면 간단한 코드를 작성할 수 있을 것으로 예상함. 현재는 따로 따로 함.
            DatabaseReference mConditionRef_isError = mDatabase.child("202").child(temp).child("isError");
            // isTimer 값 받아오기
            
            // remaining_time 값 가져오기
            DatabaseReference mConditionRef_rt = mDatabase.child("202").child(temp).child("remaining_time");
            mConditionRef_rt.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ltime = snapshot.getValue(Integer.class);
                    temp = ltime.toString();
                    bt = (dryer_bt.get(finalI -1)).bt;
                    bt.setText(ltime.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}