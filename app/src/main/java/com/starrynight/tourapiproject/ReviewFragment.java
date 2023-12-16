package com.starrynight.tourapiproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.starrynight.tourapiproject.postPage.postRetrofit.MainPost;
import com.starrynight.tourapiproject.postPage.postRetrofit.MainPost_adapter;
import com.starrynight.tourapiproject.postPage.postRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.postWritePage.PostWriteActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author : jinhyeok
 * @version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2022-09-02      jinhyeok       주석 수정
 * @className : MainFragment
 * @description : 홈 화면 Fragment 입니다.
 * @modification : 2022-09-02 (jinhyeok) 주석 수정
 * @date : 2022-09-02
 */
public class ReviewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    Long userId;
    SwipeRefreshLayout swipeRefreshLayout;
    NestedScrollView nestedScrollView;
    List<Long> myhashTagIdList;
    int count = 5, end, limit;
    List<MainPost> mainPostList;
    List<MainPost> result;
    Boolean noMorePost;
    RecyclerView recyclerView;
    MainPost_adapter adapter;
    ProgressBar progressBar;
    LinearLayout weatherLocationSearch;
    FloatingActionButton postwritebtn;

    private static final String TAG = "Main Fragment";

    public ReviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_review, container, false);
        swipeRefreshLayout = v.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        myhashTagIdList = new ArrayList<>();
        nestedScrollView = v.findViewById(R.id.scroll_layout);
        recyclerView = v.findViewById(R.id.recyclerView);
        progressBar = v.findViewById(R.id.mainProgressBar);
        weatherLocationSearch = v.findViewById(R.id.weather_location_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        String fileName = "userId"; // 유저 아이디 가져오기
        try {
            FileInputStream fis = getActivity().openFileInput(fileName);
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            userId = Long.parseLong(line);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Call<List<MainPost>> call2 = RetrofitClient.getApiService().getMainPosts();
        call2.enqueue(new Callback<List<MainPost>>() {
            @Override
            public void onResponse(Call<List<MainPost>> call, Response<List<MainPost>> response) {
                if (response.isSuccessful()) {
                    Log.d("mainPostList", "메인 게시물 리스트 성공");
                    result = response.body();
                    end = count;
                    noMorePost = false;
                    limit = result.size();
                    mainPostList = new ArrayList<>();
                    if (limit < end) {
                        noMorePost = true;
                    }
                    adapter = new MainPost_adapter(result.subList(0, Math.min(end, limit)), getContext());
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("mainPostList", "메인 게시물 리스트 실패");
                }
            }

            @Override
            public void onFailure(Call<List<MainPost>> call, Throwable t) {
                Log.e("mainPostList", "인터넷 오류");
            }
        });
        // 밑으로 스크롤시 게시물 목록 업로딩
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (!noMorePost) {
                        progressBar.setVisibility(View.VISIBLE);
                        end += count;
                        if (limit < end) {
                            noMorePost = true;
                        }
                        adapter = new MainPost_adapter(result.subList(0, Math.min(end, limit)), getContext());
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        //플로팅 버튼으로 게시글 작성 페이지로 넘어가기
        postwritebtn = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        postwritebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PostWriteActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            //프래그먼트 새로고침
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        swipeRefreshLayout.setRefreshing(false);
    }
}