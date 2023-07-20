package com.starrynight.tourapiproject.searchPage.searchPageRetrofit;

import com.starrynight.tourapiproject.myPage.myWish.post.MyPost;
import com.starrynight.tourapiproject.searchPage.filter.HashTagItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SearchPageRetrofitService {

    @POST("search/touristPointForMap")
    Call<List<SearchParams1>> getTouristPointWithFilterforMap(@Body SearchKey searchKey);

    @POST("search/touristPoint")
    Call<List<SearchParams1>> getTouristPointWithFilter(@Body SearchKey searchKey);

    @POST("search/observation/{pageNo}")
    Call<List<SearchParams1>> getObservationWithFilter(@Body SearchKey searchKey, @Path("pageNo") Integer pageNo);

    @POST("search/post")
    Call<List<SearchParams1>> getPostWithFilter(@Body SearchKey searchKey);

    @GET("searchFirst/{typeName}")
    Call<List<SearchFirst>> getSearchFirst(@Path("typeName") String typeName);

    @GET("hashTags/filter")
    Call<List<HashTagItem>> getHashTag();

    @GET("area/filter")
    Call<List<HashTagItem>> getAreaFilter();

    @POST("search/observation/count")
    Call<Long> getSearchCountWithFilter(@Body SearchKey searchKey);

}
