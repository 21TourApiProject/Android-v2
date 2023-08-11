package com.starrynight.tourapiproject.postPage.postRetrofit;

import com.google.gson.annotations.SerializedName;
import com.starrynight.tourapiproject.myPage.myPageRetrofit.User;

import java.util.List;

/**
 * className :  postComment
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
public class PostComment {
    @SerializedName("commentId")
    private Long commentId;
    @SerializedName("post")
    private Post post;
    @SerializedName("postId")
    private Long postId;
    @SerializedName("user")
    private User user;
    @SerializedName("userId")
    private Long userId;
    @SerializedName("comment")
    private String comment;
    @SerializedName("time")
    private String time;
    @SerializedName("yearDate")
    private String yearDate;

    public PostComment(Long commentId, Post post, Long postId, User user, Long userId, String comment, String time, String yearDate) {
        this.commentId = commentId;
        this.post = post;
        this.postId = postId;
        this.user = user;
        this.userId = userId;
        this.comment = comment;
        this.time = time;
        this.yearDate = yearDate;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
