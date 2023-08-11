package com.starrynight.tourapiproject.postPage.postRetrofit;

import android.view.View;

import com.starrynight.tourapiproject.postPage.ImageSliderAdapter;

/**
 * className :  MainPostImageSliderItemClickListener
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-07-24
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-07-24      jinhyeok      최초생성
 */
public interface MainPostImageSliderItemClickListener {
        public void onItemClick(MainPostSliderAdapter.MyViewHolder holder, View view, int position);
}
