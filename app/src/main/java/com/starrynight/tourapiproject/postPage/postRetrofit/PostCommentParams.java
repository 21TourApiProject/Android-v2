package com.starrynight.tourapiproject.postPage.postRetrofit;

import com.google.gson.annotations.SerializedName;
import com.starrynight.tourapiproject.myPage.myPageRetrofit.User;

import java.util.List;

/**
 * className :  PostCommentParams
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-03-03
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-03-03      jinhyeok      최초생성
 */
public class PostCommentParams {
    @SerializedName("comment")
    private String comment;
    @SerializedName("userId")
    private Long userId;
    @SerializedName("time")
    private String time;
    @SerializedName("yearDate")
    private String yearDate;

    public PostCommentParams(String comment, Long userId, String time, String yearDate) {
        this.comment = comment;
        this.userId = userId;
        this.time = time;
        this.yearDate = yearDate;
    }

    public PostCommentParams() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getYearDate() {
        return yearDate;
    }

    public void setYearDate(String yearDate) {
        this.yearDate = yearDate;
    }

}
