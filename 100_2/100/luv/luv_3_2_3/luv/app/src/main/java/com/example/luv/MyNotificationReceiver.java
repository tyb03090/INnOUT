package com.example.luv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 알림을 수신하고 처리하는 로직을 구현합니다.
        // 알림에서 필요한 데이터를 추출하여 메인 액티비티에 전달할 수 있습니다.
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String title = extras.getString("title");
            String message = extras.getString("message");
            // 메인 액티비티로 데이터 전달
            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            mainActivityIntent.putExtra("title", title);
            mainActivityIntent.putExtra("message", message);
            context.startActivity(mainActivityIntent);
        }
    }
}
