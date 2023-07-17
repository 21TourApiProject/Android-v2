package com.starrynight.tourapiproject.postPage.postRetrofit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.myPage.myPageRetrofit.User;
import com.starrynight.tourapiproject.postPage.PostActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * className :  PostCommentAdapter
 * description : 게시글 댓글 adapter
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-02-27
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-02-27      jinhyeok      최초생성
 */
public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.ViewHolder> {
    ArrayList<PostComment> items = new ArrayList<>();
    private Context context;
    OnPostCommentItemClickListener listener;
    private static Long userId;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.custom_post_comment_item, viewGroup, false);
        //앱 내부저장소에서 저장된 유저 아이디 가져오기
        String fileName = "userId";
        try {
            FileInputStream fis = itemView.getContext().openFileInput(fileName);
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            userId = Long.parseLong(line);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PostComment item = items.get(position);
        viewHolder.setItem(item);
//        viewHolder.love.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(userId.equals(item.getUserId())){
//                    Call<Void> removeCall = com.starrynight.tourapiproject.postPage.postRetrofit.RetrofitClient.getApiService().removeLove(userId,item.getCommentId());
//                    removeCall.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//                            if(response.isSuccessful()){
//                                Toast.makeText(v.getContext(), "댓글 좋아요 삭제 성공", Toast.LENGTH_SHORT).show();
//                                viewHolder.love.setEnabled(false);
//                                Handler handle = new Handler();
//                                handle.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        viewHolder.love.setEnabled(true);
//                                    }
//                                },1500);
//                            }
//                            else{
//                                Log.d("commentLove", "댓글 좋아요 삭제 실패");
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            Log.d("commentLove", "댓글 좋아요 인터넷 오류");
//                        }
//                    });
//                }else{
//                    Call<Void> addCall = com.starrynight.tourapiproject.postPage.postRetrofit.RetrofitClient.getApiService().addLove(userId,item.getCommentId());
//                    addCall.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//                            if(response.isSuccessful()){
//                                Toast.makeText(v.getContext(), "댓글 좋아요 추가 성공", Toast.LENGTH_SHORT).show();
//                                viewHolder.love.setEnabled(false);
//                                Handler handle = new Handler();
//                                handle.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        viewHolder.love.setEnabled(true);
//                                    }
//                                },1500);
//                            }
//                            else{
//                                Log.d("commentLove", "댓글 좋아요 삭제 실패");
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            Log.d("commentLove", "댓글 좋아요 인터넷 오류");
//                        }
//                    });
//                }
//            }
//        });
        if(item.getUserId().equals(userId)){
            viewHolder.option.setVisibility(View.VISIBLE);
        }else{viewHolder.option.setVisibility(View.INVISIBLE);}
        viewHolder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                ad.setMessage("정말로 댓글을 삭제하시겠습니까?");
                ad.setTitle("알림");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> deleteCall = com.starrynight.tourapiproject.postPage.postRetrofit.RetrofitClient.getApiService().deletePostComment(item.getCommentId());
                        deleteCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(v.getContext(), "내가 쓴 게시글 댓글을 삭제했어요.", Toast.LENGTH_SHORT).show();
                                    Intent intent =  ((Activity)v.getContext()).getIntent();
                                    ((Activity)v.getContext()).finish();
                                    ((Activity)v.getContext()).overridePendingTransition(0,0);
                                    ((Activity)v.getContext()).startActivity(intent);
                                    ((Activity)v.getContext()).overridePendingTransition(0,0);
                                }else{
                                    Log.d("deletePostComment", "게시물 댓글 삭제 실패");
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.d("deletePostComment", "게시물 댓글 삭제 인터넷 오류");
                            }
                        });
                        dialog.dismiss();
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(PostComment item) {
        items.add(item);
    }

    public void setItems(ArrayList<PostComment> items) {
        this.items = items;
    }

    public PostComment getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, PostComment item) {
        items.set(position, item);
    }



    public void setOnPostCommentItemClickListener(OnPostCommentItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView postComment;
        TextView time;
        TextView user;
        TextView love;
        TextView loveCount;
        LinearLayout option;


        public ViewHolder(View itemView, final OnPostCommentItemClickListener listener) {
            super(itemView);

            postComment = itemView.findViewById(R.id.postComment);
            time = itemView.findViewById(R.id.comment_time);
            user = itemView.findViewById(R.id.post_nickname);
            //love = itemView.findViewById(R.id.comment_love);
            //loveCount = itemView.findViewById(R.id.comment_love_count);
            option = itemView.findViewById(R.id.comment_option);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(PostComment item) {
            postComment.setText(item.getComment());
            time.setText(item.getYearDate()+" "+item.getTime());


            Call<User> call3 = RetrofitClient.getApiService().getUser(item.getUserId());
            call3.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Log.d("user", "게시물 유저정보 업로드");
                        User user1 = response.body();
                        user.setText(user1.getNickName());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("user", "게시물 유저정보 인터넷 오류");
                }
            });
        }
    }
}
