package com.starrynight.tourapiproject.myPage.myPageRetrofit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

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

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        CharSequence htmlBody = Html.fromHtml(body,HtmlCompat.FROM_HTML_MODE_LEGACY);
        String isNotice = remoteMessage.getData().get("isNotice");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("type","alarm");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10 , intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle(title)
                .setContentText(htmlBody)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(getApplicationContext(),R.color.point_blue))
                .setSmallIcon(R.drawable.push_notification_icon);

        Notification notification = builder.build();
        SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
        boolean isCommentDenied = pref.getBoolean("isCommentDenied",true);
        boolean isNoticeDenied = pref.getBoolean("isNoticeDenied",true);
        if(isNoticeDenied &&isNotice.equals("notification")){
            notificationManager.notify(1, notification);
        }
        if(isCommentDenied &&isNotice.equals("comment")){
            notificationManager.notify(1, notification);
        }
    }
}
