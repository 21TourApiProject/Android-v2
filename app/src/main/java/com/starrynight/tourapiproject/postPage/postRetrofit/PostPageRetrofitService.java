package com.starrynight.tourapiproject.postPage.postRetrofit;

import com.starrynight.tourapiproject.myPage.myPageRetrofit.User;
import com.starrynight.tourapiproject.myPage.myWish.obtp.MyWishObTp;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.Observation;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.Filter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostPageRetrofitService {
    @GET("post/{postId}")
    Call<Post> getPost(@Path("postId") Long postId);

    @GET("post/{postId}/postImage")
    Call<List<String>> getPostImage(@Path("postId") Long postId);

    @GET("post/{observationId}/observation")
    Call<Observation> getObservation(@Path("observationId") Long observationId);

    @GET("postHashTagName/{postId}")
    Call<List<String>> getPostHashTagName(@Path("postId") Long postId);

    @GET("postImage/{postObservePointId}")
    Call<List<PostImage>> getRelatePostImageList(@Path("postObservePointId") Long postObservePointId);

    @DELETE("post/{postId}")
    Call<Void> deletePost(@Path("postId") Long postId);

    @POST("post/main")
    Call<List<MainPost>> getMainPosts();

    @GET("user/{userId}")
    Call<User> getUser(@Path("userId") Long userId);

    @GET("user/{userId}/myHashTagId")
    Call<List<Long>> getMyHashTagIdList(@Path("userId") Long userId);

    @GET("postHashTag/{postId}")
    Call<List<PostHashTag>> getPostHashTags(@Path("postId") Long postId);

    //좋아요 관련 Service
    @GET("like/{userId}/{itemId}/{likeType}")
    Call<Boolean> isThereLike(@Path("userId") Long userId, @Path("itemId") Long itemId, @Path("likeType") Integer likeType);

    @POST("like/{userId}/{itemId}/{likeType}")
    Call<Void> createLike(@Path("userId") Long userId, @Path("itemId") Long itemId, @Path("likeType") Integer likeType);

    @DELETE("like/{userId}/{itemId}/{likeType}")
    Call<Void> deleteLike(@Path("userId") Long userId, @Path("itemId") Long itemId, @Path("likeType") Integer likeType);

    @GET("like/{itemId}/{likeType}") // 좋아요 수 가져오기
    Call<Long> getLikeCount(@Path("itemId") Long itemId, @Path("likeType") Integer likeType);

    @POST("postComment /{postId}")
    Call<Void> createPostComment(@Path("postId")Long postId, @Body PostCommentParams postCommentParams);

    @GET("postComment /{postCommentId}")
    Call<PostComment> getPostComment(@Path("postCommentId")Long postCommentId);

    @GET("postCommentList /{postId}")
    Call<List<PostComment>> getPostCommentById(@Path("postId")Long postId);

    @GET("postComment/{userId}/{postCommentId}")
    Call<Void> addLove(@Path("userId") Long userId, @Path("postCommentId")Long postCommentId);

    @GET("postComment/{userId}/{postCommentId}")
    Call<Void> removeLove(@Path("userId") Long userId, @Path("postCommentId")Long postCommentId);

    @DELETE("postComment/{postCommentId}")
    Call<Void> deletePostComment(@Path("postCommentId")Long postCommentId);

}
