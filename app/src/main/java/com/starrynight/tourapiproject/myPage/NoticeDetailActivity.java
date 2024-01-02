package com.starrynight.tourapiproject.myPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.myPage.notice.Notice;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeDetailActivity extends AppCompatActivity {
    Long noticeId;
    TextView noticeTitle,noticeContent,noticeDate;
    LinearLayout noticeBack;
    private static final String TAG = "noticeDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        noticeTitle=findViewById(R.id.noticeDetailTitle);
        noticeContent=findViewById(R.id.noticeDetailContent);
        noticeDate=findViewById(R.id.noticeDetailDate);
        noticeBack=findViewById(R.id.noticeDetailBack);

        Intent intent = getIntent();
        noticeId = (Long) intent.getSerializableExtra("noticeId");
        Call<Notice> call = RetrofitClient.getApiService().getNotice(noticeId);
        call.enqueue(new Callback<Notice>() {
            @Override
            public void onResponse(Call<Notice> call, Response<Notice> response) {
                if(response.isSuccessful()){
                    Notice notice = response.body();
                    noticeTitle.setText(notice.getNoticeTitle());
                    noticeContent.setText(Html.fromHtml(notice.getNoticeContent(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                    noticeDate.setText(notice.getNoticeDate());
                }
            }

            @Override
            public void onFailure(Call<Notice> call, Throwable t) {
                Log.e(TAG, "공지 인터넷 연결 오류");
            }
        });

        noticeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}