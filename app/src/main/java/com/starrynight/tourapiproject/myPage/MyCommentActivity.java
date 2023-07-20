package com.starrynight.tourapiproject.myPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.myPage.myWish.post.MyComment;
import com.starrynight.tourapiproject.myPage.myWish.post.MyCommentAdapter;
import com.starrynight.tourapiproject.myPage.myWish.post.OnMyCommentItemClickListener;
import com.starrynight.tourapiproject.postPage.PostActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCommentActivity extends AppCompatActivity {
    private static final int POST = 103;
    Long userId;
    List<MyComment> myComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);


        Intent intent = getIntent();
        userId = (Long) intent.getSerializableExtra("userId"); //전 페이지에서 받아온 사용자 id

        //뒤로 가기
        LinearLayout myCommentBack = findViewById(R.id.myCommentBack);
        myCommentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView myCommentRecyclerView = findViewById(R.id.myCommentRecyclerView);
        LinearLayoutManager myCommentLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myCommentRecyclerView.setLayoutManager(myCommentLayoutManager);
        myComments = new ArrayList<>();

        //내가 쓴 댓글 가져오는 이벤트
        Call<List<MyComment>> call = RetrofitClient.getApiService().getMyComments(userId);
        call.enqueue(new Callback<List<MyComment>>() {
            @Override
            public void onResponse(Call<List<MyComment>> call, Response<List<MyComment>> response) {
                if(response.isSuccessful()){
                    myComments = response.body();
                    MyCommentAdapter adapter = new MyCommentAdapter(myComments,MyCommentActivity.this);
                    myCommentRecyclerView.setAdapter(adapter);
                    adapter.setOnMyCommentItemClickListener(new OnMyCommentItemClickListener() {
                        @Override
                        public void onItemClick(MyCommentAdapter.ViewHolder holder, View view, int position) {
                            MyComment item = adapter.getItem(position);
                            Intent intent = new Intent(MyCommentActivity.this, PostActivity.class);
                            intent.putExtra("postId", item.getPostId());
                            startActivityForResult(intent, POST);
                        }
                    });
                }else{
                    Log.d("myComment", "내가 쓴 댓글 가져오기 실패");
                }
            }

            @Override
            public void onFailure(Call<List<MyComment>> call, Throwable t) {
                Log.e("myComment", "내가 쓴 댓글 가져오기 인터넷 연결 실패");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == POST) {
            //액티비티 새로고침
            Intent intent = getIntent();
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}