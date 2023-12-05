package com.starrynight.tourapiproject.myPage.myPageRetrofit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.ktx.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.starrynight.tourapiproject.MainActivity;
import com.starrynight.tourapiproject.R;

/**

 * className :  FcmService
 * description : TODO 예시 클래스 입니다.  
 * modification : 2022.08.01(박진혁) methodA수정 
 * author : jinhyeok
 * date : 2023-09-02 
 * version : 1.0 
    ====개정이력(Modification Information)====
        수정일        수정자        수정내용
    -----------------------------------------
        2023-09-02      jinhyeok      최초생성
 */
public class FcmService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    private String msg, title;


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("MyFcmService", "New token :: " + token);

        Log.d("MyFcmService", "Notification Title :: ${remoteMessage.notification?.title}");
        Log.d("MyFcmService", "Notification Body :: ${remoteMessage.notification?.body}");
        Log.d("MyFcmService", "Notification Data :: ${remoteMessage.data}");
        //token을 서버로 전송

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //수신한 메시지를 처리
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel("tour-api-project") == null) {
                NotificationChannel channel = new NotificationChannel("tour-api-project", "com.starrynight.tourapiproject", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), "tour-api-project");
        }else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.main_icon2_foreground);
        PendingIntent intent = PendingIntent.getActivity(this,0, new Intent (getApplicationContext(), MainActivity.class),PendingIntent.FLAG_IMMUTABLE);

        Notification notification = builder.build();
        SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
        boolean isDenied = pref.getBoolean("isDenied",true);
        Log.d("isDenied","수신여부: "+isDenied);
        if(isDenied){
            notificationManager.notify(1, notification);

        }

    }
}
