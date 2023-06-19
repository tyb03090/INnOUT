package com.example.luv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "MyFirebaseMessagingReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // FirebaseMessagingService에서 전달한 메시지 데이터 추출
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            // 메시지 데이터를 RemoteMessage로 변환
            RemoteMessage remoteMessage = new RemoteMessage(bundle);
            // 메시지 데이터를 읽고 MainActivity3로 전달
            sendNotificationDataToActivity(remoteMessage.getData(), context);
        }
    }

    private void sendNotificationDataToActivity(Map<String, String> data, Context context) {
        // 메시지 데이터를 인텐트에 담아 MainActivity3로 전달
        Intent intent = new Intent(context, MainActivity3.class);
        intent.putExtra("notification_data", data.get("message"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
