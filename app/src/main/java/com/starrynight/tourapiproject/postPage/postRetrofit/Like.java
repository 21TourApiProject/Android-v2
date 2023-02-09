package com.starrynight.tourapiproject.postPage.postRetrofit;

import com.google.gson.annotations.SerializedName;

/**
 * className :  Like
 * description : LikeParam
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-02-08
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-02-08      jinhyeok      최초생성
 */
public class Like {
    @SerializedName("itemId")
    private Long itemid;
    @SerializedName("likeCount")
    private int likeCount;

    public Like(Long itemid, int likeCount) {
        this.itemid = itemid;
        this.likeCount = likeCount;
    }

    public Long getItemid() {
        return itemid;
    }

    public void setItemid(Long itemid) {
        this.itemid = itemid;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
