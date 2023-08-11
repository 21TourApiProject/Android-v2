package com.starrynight.tourapiproject.myPage.myWish.post;

import android.view.View;

/**
 * className :  OnMyCommentItemClickListener
 * description : 내가 쓴 댓글 클릭 리스너
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-07-16
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-07-16      jinhyeok      최초생성
 */
public interface OnMyCommentItemClickListener {
    public void onItemClick(MyCommentAdapter.ViewHolder holder, View view, int position);
}

