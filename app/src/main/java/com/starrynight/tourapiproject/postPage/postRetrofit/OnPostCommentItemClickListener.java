package com.starrynight.tourapiproject.postPage.postRetrofit;

import android.view.View;

import com.starrynight.tourapiproject.postPage.RelatePostAdapter;

/**
 * className :  PostCommentItemClickListener
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-02-27
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-02-27      jinhyeok      최초생성
 */
public interface OnPostCommentItemClickListener {
    public void onItemClick(PostCommentAdapter.ViewHolder holder, View view, int position);
}
