package com.example.luv;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.luv.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingSer";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // 메시지에 데이터 페이로드가 있는 경우
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            // 페이로드에서 필요한 데이터 추출
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");

            // 알림 표시 및 공지사항 클래스로 이동
            sendNotification(title, message);
        }
    }


    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        // TODO: 토큰을 서버로 전송하거나 필요한 처리를 수행합니다.
        sendTokenToServer(token);
    }

    private void sendTokenToServer(String token) {
        // TODO: 토큰을 서버로 전송하는 로직을 구현합니다.
        // 서버로 토큰을 전달하는 방법은 서버의 구현에 따라 다를 수 있습니다.
        // 토큰을 서버 API를 호출하거나 데이터베이스에 저장하는 등의 방식을 사용할 수 있습니다.
        // 토큰을 서버로 전송하여 사용자를 식별하거나 푸시 알림을 보내는 등의 작업을 수행할 수 있습니다.
    }


    /**
     * 알림을 표시하고 공지사항 클래스로 이동합니다.
     *
     * @param title   알림 제목
     * @param message 알림 내용
     */
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity3.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.icon1)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = getString(R.string.default_notification_channel_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            notificationBuilder.setChannelId(channelId);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}