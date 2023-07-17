package com.starrynight.tourapiproject.myPage.myWish.post;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * className :  MyComment
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-07-16
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-07-16      jinhyeok      최초생성
 */
public class MyComment {
    @SerializedName("commentId")
    private Long commentId;
    @SerializedName("postId")
    private Long postId;
    @SerializedName("yearDate")
    private String yearDate;
    @SerializedName("comment")
    private String comment;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("postTitle")
    private String postTitle;

    public MyComment(Long commentId, Long postId, String yearDate, String comment, String thumbnail, String postTitle) {
        this.commentId = commentId;
        this.postId = postId;
        this.yearDate = yearDate;
        this.comment = comment;
        this.thumbnail = thumbnail;
        this.postTitle = postTitle;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getYearDate() {
        return yearDate;
    }

    public void setYearDate(String yearDate) {
        this.yearDate = yearDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }
}
