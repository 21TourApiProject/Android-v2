package com.starrynight.tourapiproject.observationPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.Observation;
import com.starrynight.tourapiproject.postPage.PostActivity;
import com.starrynight.tourapiproject.postPage.postRetrofit.PostImage;
import com.starrynight.tourapiproject.postWritePage.PostWriteActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ObserveReviewFragment extends Fragment {

    private String[] relatefilename = new String[5];
    ImageView relateImage1;
    ImageView relateImage2;
    ImageView relateImage3;
    private FrameLayout relateImageFrame;

    Long observationId;

    public ObserveReviewFragment() {
        // Required empty public constructor
    }

    public ObserveReviewFragment(Observation observation) {
        this.observationId = observation.getObservationId();
    }

    public static ObserveReviewFragment newInstance(String param1, String param2) {
        ObserveReviewFragment fragment = new ObserveReviewFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_observe_review, container, false);
        // Inflate the layout for this fragment

        relateImage1 = v.findViewById(R.id.relateImage);
        relateImage2 = v.findViewById(R.id.relateImage2);
        relateImage3 = v.findViewById(R.id.relateImage3);
        relateImageFrame = v.findViewById(R.id.relateImageFrame);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView postwrite_btn = view.findViewById(R.id.writePost_btn);
        postwrite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), PostWriteActivity.class);
                startActivity(intent1);
            }
        });

        //게시물 이미지 가져오기
        Call<List<PostImage>> call = com.starrynight.tourapiproject.postPage.postRetrofit.RetrofitClient.getApiService().getRelatePostImageList(observationId);
        call.enqueue(new Callback<List<PostImage>>() {
            @Override
            public void onResponse(Call<List<PostImage>> call, Response<List<PostImage>> response) {
                if (response.isSuccessful()) {
                    Log.d("relatePostImage", "관련 게시물 이미지 업로드");
                    List<PostImage> relateImageList = response.body();
                    for (int i = 0; i < relateImageList.size(); i++) {
                        relatefilename[i] = relateImageList.get(i).getImageName();
                        if (relateImageList.size() > 4) {
                            break;
                        }
                    }
                    if (relatefilename[0] != null) {
                        Glide.with(getActivity())
                                .load("https://starry-night.s3.ap-northeast-2.amazonaws.com/postImage/" + relatefilename[0])
                                .into(relateImage1);
                    }
                    if (relatefilename[1] != null) {
                        relateImage2.setVisibility(View.VISIBLE);
                        Glide.with(getActivity())
                                .load("https://starry-night.s3.ap-northeast-2.amazonaws.com/postImage/" + relatefilename[1])
                                .into(relateImage2);
                    }
                    if (relatefilename[2] != null) {
                        relateImageFrame.setVisibility(View.VISIBLE);
                        Glide.with(getActivity())
                                .load("https://starry-night.s3.ap-northeast-2.amazonaws.com/postImage/" + relatefilename[2])
                                .into(relateImage3);
                    }
                    relateImage1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!relateImageList.isEmpty()) {
                                Intent intent01 = new Intent(getActivity(), PostActivity.class);
                                intent01.putExtra("postId", relateImageList.get(0).getPostId());
                                startActivity(intent01);
                            }
                        }
                    });
                    relateImage2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2 = new Intent(getActivity(), PostActivity.class);
                            intent2.putExtra("postId", relateImageList.get(1).getPostId());
                            startActivity(intent2);
                        }
                    });
                    relateImage3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2 = new Intent(getActivity(), MoreObservationActivity.class);
                            intent2.putExtra("observationId", observationId);
                            startActivity(intent2);
                        }
                    });
                } else {
                    Log.d("relatePostImage", "관련 게시물 이미지 업로드 실패");
                }
            }

            @Override
            public void onFailure(Call<List<PostImage>> call, Throwable t) {
                Log.d("relatePostImage", "관련 게시물 이미지 업로드 인터넷 오류");
            }
        });

    }
}