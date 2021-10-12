package com.example.draft_laundry.LaundryMain.dorm201;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.draft_laundry.LaundryMain.Dryer;
import com.example.draft_laundry.LaundryMain.LaundryMainActivity;
import com.example.draft_laundry.LaundryMain.Washer;
import com.example.draft_laundry.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Laundry_Information201 extends Fragment {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    Integer ltime = 0;
    // 세탁기, 건조기 수
    String temp;
    Integer num_washer = 1;
    Integer num_dryer = 1;
    Button bt;

    //새로고침을 위함
    private SwipeRefreshLayout refreshLayout = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_landuryinformation201, container, false);
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
        Washer w1 = new Washer((Button) getActivity().findViewById(R.id.w1_201), false, false);
        washer_bt.add(w1);
        setwasher(num_washer, washer_bt);

        // 건조기 리스트
        ArrayList<Dryer> dryer_bt = new ArrayList<>();
        Dryer d1 = new Dryer((Button) getActivity().findViewById(R.id.d1_201), false, false);
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
            DatabaseReference mConditionRef_isError = mDatabase.child("201").child(temp).child("isError");
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
            DatabaseReference mConditionRef_isTimer = mDatabase.child("201").child(temp).child("isTimer");
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
            DatabaseReference mConditionRef_rt = mDatabase.child("201").child(temp).child("remaining_time");
            mConditionRef_rt.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ltime = snapshot.getValue(Integer.class);
                    temp = ltime.toString();
                    bt = (washer_bt.get(finalI -1)).bt;
                    if (washer_bt.get(finalI -1).isError == false) {
                        bt.setText(String.valueOf(finalI)+"번: "+ltime.toString());
                    }
                    else {
                        bt.setText("고장");
                    }
                    final TimerTask[] tt = new TimerTask[1];
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("t", "hi");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("무엇을 하시겠습니까?");

                            // 현재 상태가 고장나지 않았을 때 버튼을 누르면 AlertDialog를 띄움
                            if (washer_bt.get(finalI -1).isError == false && washer_bt.get(finalI -1).isTimer == false) {
                                // AlertDialog 세팅
                                builder.setItems(R.array.NotErrorStateandisTimerFalse, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int pos)
                                    {
                                        String[] items = getResources().getStringArray(R.array.NotErrorStateandisTimerFalse);
                                        switch (pos) {
                                            case 0:

                                                // 알림 예약 설정
                                                Log.d("t", "알림 예약 설정");
                                                washer_bt.get(finalI -1).isTimer = true;

                                                // 파이어베이스에서 시간 받아와서 counter에 저장, counter 시간 후에 알림 보내주기
                                                tt[0] = new TimerTask() {
                                                    int counter = snapshot.getValue(Integer.class);

                                                    @Override
                                                    public void run() {
                                                        counter--;
                                                        Log.d("washer_countertag"+String.valueOf(finalI), String.valueOf(counter));
                                                        if (counter == 0) {
                                                            // 푸쉬 알림
                                                            Log.d("tag", "push");
                                                            // 푸쉬알림할때 나오는 아이콘 세팅
                                                            /*
                                                                Bitmap mLargeIconForNoti =
                                                                        BitmapFactory.decodeResource(getResources(), R.drawable.bell_64);
                                                                // 푸쉬 알림 누르면 액티비티 실행해주는 코드
                                                                PendingIntent mPendingIntent = PendingIntent.getActivity(getContext(), 0,
                                                                        new Intent(getActivity().getApplicationContext(), Laundry_Information201.class),
                                                                        PendingIntent.FLAG_UPDATE_CURRENT
                                                                );
                                                                NotificationCompat.Builder mBuilder =
                                                                        new NotificationCompat.Builder(getContext())
                                                                                .setContentTitle("title")
                                                                                .setContentText("text")
                                                                                .setSmallIcon(R.drawable.bell_64)
                                                                                .setDefaults(Notification.DEFAULT_VIBRATE)
                                                                                .setLargeIcon(mLargeIconForNoti)
                                                                                .setPriority(Notification.PRIORITY_DEFAULT)
                                                                                .setAutoCancel(true)
                                                                                .setContentIntent(mPendingIntent);
                                                                NotificationManager mNotificationManager =
                                                                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                                                                mNotificationManager.notify(0, mBuilder.build());

                                                             */
                                                            // https://lcw126.tistory.com/73 요기 참고한 푸쉬 알람 세팅
                                                            NotificationManager notificationManager=(NotificationManager) (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);;

                                                            //Notification 객체를 생성해주는 건축가객체 생성(AlertDialog 와 비슷)
                                                            NotificationCompat.Builder builder= null;

                                                            //Oreo 버전(API26 버전)이상에서는 알림시에 NotificationChannel 이라는 개념이 필수 구성요소가 됨.
                                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                                                                String channelID="washer_"+String.valueOf(finalI); //알림채널 식별자
                                                                String channelName="MyWasher"+String.valueOf(finalI); //알림채널의 이름(별명)

                                                                //알림채널 객체 만들기
                                                                NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);

                                                                //알림매니저에게 채널 객체의 생성을 요청
                                                                notificationManager.createNotificationChannel(channel);

                                                                //알림건축가 객체 생성
                                                                builder=new NotificationCompat.Builder(getContext(), channelID);


                                                            }else{
                                                                //알림 건축가 객체 생성
                                                                builder= new NotificationCompat.Builder(getContext(), null);
                                                            }

                                                            //건축가에게 원하는 알림의 설정작업
                                                            builder.setSmallIcon(android.R.drawable.ic_menu_view);

                                                            //상태바를 드래그하여 아래로 내리면 보이는
                                                            //알림창(확장 상태바)의 설정
                                                            builder.setContentTitle(String.valueOf(finalI)+"번 세탁기 알림");//알림창 제목
                                                            builder.setContentText("세탁 끝!");//알림창 내용
                                                            //알림창의 큰 이미지
                                                            Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.washer_64);
                                                            builder.setLargeIcon(bm);//매개변수가 Bitmap을 줘야한다.

                                                            Intent intent= new Intent(getActivity().getApplicationContext(), LaundryMainActivity.class);
                                                            intent.putExtra("dorm_num", "201");
                                                            //지금 실행하는 것이 아니라 잠시 보류시키는 Intent 객체 필요
                                                            PendingIntent pendingIntent= PendingIntent.getActivity(getContext(), (int)(System.currentTimeMillis()/1000),intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                                            builder.setContentIntent(pendingIntent);

                                                            //알림창 클릭시에 자동으로 알림제거
                                                            builder.setAutoCancel(true);
                                                            // 진동
                                                            builder.setVibrate(new long[]{0, 1000});// 0초 대기, 1초 진동

                                                            //건축가에게 알림 객체 생성하도록
                                                            Notification notification=builder.build();

                                                            //알림매니저에게 알림(Notify) 요청
                                                            notificationManager.notify((int)(System.currentTimeMillis()/1000), notification);

                                                            //알림 요청시에 사용한 번호를 알림제거 할 수 있음.
                                                            //notificationManager.cancel(1);
                                                            Log.d("tag", "Finish");
                                                            tt[0].cancel();
                                                            washer_bt.get(finalI - 1).isTimer = false;
                                                        }
                                                    }
                                                };
                                                Timer timer = new Timer();
                                                // ltime 분마다 counter가 --됨
                                                timer.schedule(tt[0], 0, 1000);
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
                            else if (washer_bt.get(finalI -1).isError == false && washer_bt.get(finalI -1).isTimer == true) {
                                // AlertDialog 세팅
                                builder.setItems(R.array.NotErrorStateandisTimerTrue, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int pos)
                                    {
                                        String[] items = getResources().getStringArray(R.array.NotErrorStateandisTimerTrue);
                                        switch (pos) {
                                            case 0:
                                                // 알림 예약 설정
                                                Log.d("t", "알림 예약 취소");
                                                washer_bt.get(finalI -1).isTimer = false;
                                                tt[0].cancel();
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
//                                            case 0:
//                                                // 알림 예약 설정
//                                                Log.d("t", "알림 예약 설정");
//                                                break;
                                            case 0:
                                                // 수리끝남
                                                Log.d("t", "원래 상태로 바꿔주기");
                                                Washer temp_washer = washer_bt.get(finalI -1);
                                                temp_washer.isError = false;
                                                // Button background image, text 변경
                                                Drawable img = getContext().getResources().getDrawable( R.drawable.washer_64);
                                                temp_washer.bt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                                                ltime = snapshot.getValue(Integer.class);
                                                temp_washer.bt.setText(String.valueOf(finalI)+"번: "+ltime.toString());
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
            DatabaseReference mConditionRef_isError = mDatabase.child("201").child(temp).child("isError");
            mConditionRef_isError.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    (dryer_bt.get(finalI -1)).isError = snapshot.getValue(Boolean.class);
                    if (dryer_bt.get(finalI -1).isError == false) {
                        Drawable img = getContext().getResources().getDrawable( R.drawable.dryer_64);
                        dryer_bt.get(finalI -1).bt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    }
                    else {
                        Drawable img = getContext().getResources().getDrawable( R.drawable.error_64);
                        dryer_bt.get(finalI -1).bt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // isTimer 값 받아오기
            DatabaseReference mConditionRef_isTimer = mDatabase.child("201").child(temp).child("isTimer");
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
            DatabaseReference mConditionRef_rt = mDatabase.child("201").child(temp).child("remaining_time");
            mConditionRef_rt.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ltime = snapshot.getValue(Integer.class);
                    temp = ltime.toString();
                    bt = (dryer_bt.get(finalI -1)).bt;
                    if (dryer_bt.get(finalI -1).isError == false) {
                        bt.setText(String.valueOf(finalI)+"번: "+ltime.toString());
                    }
                    else {
                        bt.setText("고장");
                    }
                    final TimerTask[] tt = new TimerTask[1];
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("t", "hi");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("무엇을 하시겠습니까?");

                            // 현재 상태가 고장나지 않았을 때 버튼을 누르면 AlertDialog를 띄움
                            if (dryer_bt.get(finalI -1).isError == false && dryer_bt.get(finalI -1).isTimer == false) {
                                // AlertDialog 세팅
                                builder.setItems(R.array.NotErrorStateandisTimerFalse, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int pos)
                                    {
                                        String[] items = getResources().getStringArray(R.array.NotErrorStateandisTimerFalse);
                                        switch (pos) {
                                            case 0:

                                                // 알림 예약 설정
                                                Log.d("t", "알림 예약 설정");
                                                dryer_bt.get(finalI -1).isTimer = true;

                                                // 파이어베이스에서 시간 받아와서 counter에 저장, counter 시간 후에 알림 보내주기
                                                tt[0] = new TimerTask() {
                                                    int counter = snapshot.getValue(Integer.class);

                                                    @Override
                                                    public void run() {
                                                        counter--;
                                                        Log.d("dryer_countertag"+String.valueOf(finalI), String.valueOf(counter));
                                                        if (counter == 0) {
                                                            // 푸쉬 알림
                                                            Log.d("tag", "push");
                                                            // 푸쉬알림할때 나오는 아이콘 세팅
                                                            /*
                                                                Bitmap mLargeIconForNoti =
                                                                        BitmapFactory.decodeResource(getResources(), R.drawable.bell_64);
                                                                // 푸쉬 알림 누르면 액티비티 실행해주는 코드
                                                                PendingIntent mPendingIntent = PendingIntent.getActivity(getContext(), 0,
                                                                        new Intent(getActivity().getApplicationContext(), Laundry_Information201.class),
                                                                        PendingIntent.FLAG_UPDATE_CURRENT
                                                                );
                                                                NotificationCompat.Builder mBuilder =
                                                                        new NotificationCompat.Builder(getContext())
                                                                                .setContentTitle("title")
                                                                                .setContentText("text")
                                                                                .setSmallIcon(R.drawable.bell_64)
                                                                                .setDefaults(Notification.DEFAULT_VIBRATE)
                                                                                .setLargeIcon(mLargeIconForNoti)
                                                                                .setPriority(Notification.PRIORITY_DEFAULT)
                                                                                .setAutoCancel(true)
                                                                                .setContentIntent(mPendingIntent);
                                                                NotificationManager mNotificationManager =
                                                                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                                                                mNotificationManager.notify(0, mBuilder.build());

                                                             */
                                                            // https://lcw126.tistory.com/73 요기 참고한 푸쉬 알람 세팅
                                                            NotificationManager notificationManager=(NotificationManager) (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);;

                                                            //Notification 객체를 생성해주는 건축가객체 생성(AlertDialog 와 비슷)
                                                            NotificationCompat.Builder builder= null;

                                                            //Oreo 버전(API26 버전)이상에서는 알림시에 NotificationChannel 이라는 개념이 필수 구성요소가 됨.
                                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                                                                String channelID="dryer_"+String.valueOf(finalI); //알림채널 식별자
                                                                String channelName="MyDryer"+String.valueOf(finalI); //알림채널의 이름(별명)

                                                                //알림채널 객체 만들기
                                                                NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);

                                                                //알림매니저에게 채널 객체의 생성을 요청
                                                                notificationManager.createNotificationChannel(channel);

                                                                //알림건축가 객체 생성
                                                                builder=new NotificationCompat.Builder(getContext(), channelID);


                                                            }else{
                                                                //알림 건축가 객체 생성
                                                                builder= new NotificationCompat.Builder(getContext(), null);
                                                            }

                                                            //건축가에게 원하는 알림의 설정작업
                                                            builder.setSmallIcon(android.R.drawable.ic_menu_view);

                                                            //상태바를 드래그하여 아래로 내리면 보이는
                                                            //알림창(확장 상태바)의 설정
                                                            builder.setContentTitle(String.valueOf(finalI)+"번 건조기 알림");//알림창 제목
                                                            builder.setContentText("건조 끝!");//알림창 내용
                                                            //알림창의 큰 이미지
                                                            Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.dryer_64);
                                                            builder.setLargeIcon(bm);//매개변수가 Bitmap을 줘야한다.

                                                            Intent intent= new Intent(getActivity().getApplicationContext(), LaundryMainActivity.class);
                                                            intent.putExtra("dorm_num", "201");
                                                            //지금 실행하는 것이 아니라 잠시 보류시키는 Intent 객체 필요
                                                            PendingIntent pendingIntent= PendingIntent.getActivity(getContext(), 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                                            builder.setContentIntent(pendingIntent);

                                                            //알림창 클릭시에 자동으로 알림제거
                                                            builder.setAutoCancel(true);
                                                            // 진동
                                                            builder.setVibrate(new long[]{0, 1000});// 0초 대기, 1초 진동

                                                            //건축가에게 알림 객체 생성하도록
                                                            Notification notification=builder.build();

                                                            //알림매니저에게 알림(Notify) 요청
                                                            notificationManager.notify(1, notification);

                                                            //알림 요청시에 사용한 번호를 알림제거 할 수 있음.
                                                            //notificationManager.cancel(1);
                                                            Log.d("tag", "Finish");
                                                            tt[0].cancel();
                                                            dryer_bt.get(finalI - 1).isTimer = false;
                                                        }
                                                    }
                                                };
                                                Timer timer = new Timer();
                                                // ltime 분마다 counter가 --됨
                                                timer.schedule(tt[0], 0, 1000);
                                                break;
                                            case 1:
                                                // 고장남
                                                Log.d("t", "고장 상태로 바꿔주기");
                                                Dryer temp_dryer = dryer_bt.get(finalI -1);
                                                temp_dryer.isError = true;
                                                // Button background image, text 변경
                                                Drawable img = getContext().getResources().getDrawable( R.drawable.error_64);
                                                temp_dryer.bt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                                                temp_dryer.bt.setText("고장");
                                                mConditionRef_isError.setValue(true);

                                                break;
                                        }
                                    }
                                });
                            }
                            else if (dryer_bt.get(finalI -1).isError == false && dryer_bt.get(finalI -1).isTimer == true) {
                                // AlertDialog 세팅
                                builder.setItems(R.array.NotErrorStateandisTimerTrue, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int pos)
                                    {
                                        String[] items = getResources().getStringArray(R.array.NotErrorStateandisTimerTrue);
                                        switch (pos) {
                                            case 0:
                                                // 알림 예약 설정
                                                Log.d("t", "알림 예약 취소");
                                                dryer_bt.get(finalI -1).isTimer = false;
                                                tt[0].cancel();
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
//                                            case 0:
//                                                // 알림 예약 설정
//                                                Log.d("t", "알림 예약 설정");
//                                                break;
                                            case 0:
                                                // 수리끝남
                                                Log.d("t", "원래 상태로 바꿔주기");
                                                Dryer temp_dryer = dryer_bt.get(finalI -1);
                                                temp_dryer.isError = false;
                                                // Button background image, text 변경
                                                Drawable img = getContext().getResources().getDrawable( R.drawable.dryer_64);
                                                temp_dryer.bt.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                                                ltime = snapshot.getValue(Integer.class);
                                                temp_dryer.bt.setText(String.valueOf(finalI)+"번: "+ltime.toString());
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

}
