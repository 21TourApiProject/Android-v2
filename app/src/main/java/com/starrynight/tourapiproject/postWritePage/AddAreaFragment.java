package com.starrynight.tourapiproject.postWritePage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.observationPage.RecyclerDecoration;
import com.starrynight.tourapiproject.postWritePage.postWriteRetrofit.PostHashTagAdapter;
import com.starrynight.tourapiproject.postWritePage.postWriteRetrofit.PostHashTagParams;
import com.starrynight.tourapiproject.searchPage.filter.HashTagItem;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.RetrofitClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.starrynight.tourapiproject.searchPage.filter.HashTagItem.VIEWTYPE_ACTIVE;

public class AddAreaFragment extends BottomSheetDialogFragment {

    public AddAreaFragment(){}
    List<PostHashTagParams> postHashTagParams = new ArrayList<>();
    RecyclerView areaRecyclerView;
    TextView complete,areaName;
    ImageView close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FilterBottomSheetDialogTheme);
        setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().setCanceledOnTouchOutside(true);
        return inflater.inflate(R.layout.fragment_add_area, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        areaName =view.findViewById(R.id.areaName);
        String optionOb= ((SearchObservingPointActivity)getActivity()).observePoint;

        areaName.setText("'"+optionOb);

        areaRecyclerView = view.findViewById(R.id.localPostHashTag);
        areaRecyclerView.addItemDecoration(new RecyclerDecoration(10));

        PostHashTagAdapter localAdapter =new PostHashTagAdapter();
        FlexboxLayoutManager flexboxLayoutManager4;
        flexboxLayoutManager4 = new FlexboxLayoutManager(view.getContext());
        flexboxLayoutManager4.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager4.setJustifyContent(JustifyContent.FLEX_START);

        Call<List<HashTagItem>> areaCall = RetrofitClient.getApiService().getAreaFilter();
        areaCall.enqueue(new Callback<List<HashTagItem>>() {
            @Override
            public void onResponse(Call<List<HashTagItem>> call, Response<List<HashTagItem>> response) {
                if(response.isSuccessful()){
                    List<HashTagItem> areaList = response.body();
                    for(HashTagItem item: areaList){
                        Log.d("postHashTag", "지역 해쉬태그 호출 성공");
                        localAdapter.addItem(item);
                    }
                    areaRecyclerView.setLayoutManager(flexboxLayoutManager4);
                    areaRecyclerView.setAdapter(localAdapter);

                }else{
                    Log.e("postHashTag", "지역 해쉬태그 호출 실패");
                }
            }

            @Override
            public void onFailure(Call<List<HashTagItem>> call, Throwable t) {
                Log.e("postHashTag", "지역 해쉬태그 호출 인터넷 실패");
            }
        });

        final List<String> result = new ArrayList<>(); //메인 해시태그 리스트
        complete = view.findViewById(R.id.addComplete); //완료 버튼 클릭
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count =0;
                for(int i=0;i<localAdapter.getItemCount();i++){ // 지역 해시태그는 1개만 고를 수 있도록 제한
                    if(localAdapter.getItem(i).getIsActive()==VIEWTYPE_ACTIVE){
                        count++;
                    }
                }
                if(count!=1){
                    Toast.makeText(view.getContext(), "지역 해시태그는 한개만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    for(int i=0;i<localAdapter.getItemCount();i++){
                        if(localAdapter.getItem(i).getIsActive()==VIEWTYPE_ACTIVE){
                            PostHashTagParams postHashTagParam = new PostHashTagParams();
                            postHashTagParam.setAreaName(localAdapter.getItem(i).getName());
                            postHashTagParam.setAreaId(localAdapter.getItem(i).getId());
                            postHashTagParams.add(postHashTagParam);
                            result.add(localAdapter.getItem(i).getName());
                        }
                    }
                    Intent intent =new Intent();
                    intent.putExtra("postAreaParams", (Serializable) postHashTagParams);
                    intent.putExtra("areaList", (Serializable) result);
                    ((SearchObservingPointActivity)getActivity()).onActivityResult(206,6,intent);
                    dismiss();
                }
            }
        });

        //x 버튼
        close = view.findViewById(R.id.areaCloseBtn);
        close.setOnClickListener(v -> dismiss());
    }
}
