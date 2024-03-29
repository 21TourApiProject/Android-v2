package com.starrynight.tourapiproject.postWritePage.postWriteRetrofit;

import java.io.Serializable;
/**
 * className :   PostHashTagParams
 * description : 게시물 해시태그 param 입니다.
 * modification : 2022.08.01(박진혁) 주석 수정
 * author : jinhyeok
 * date : 2022-08-01
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2022-08-01      jinhyeok      주석 수정
 */
public class PostHashTagParams implements Serializable {

    private String hashTagName;

    private String areaName;

    private Long areaId;

    public PostHashTagParams() {
    }


    public void setHashTagName(String hashTagName) {
        this.hashTagName = hashTagName;
    }

    public String getHashTagName() {
        return hashTagName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public PostHashTagParams(String hashTagName, String areaName,Long areaId) {
        this.hashTagName = hashTagName;
        this.areaName = areaName;
        this.areaId=areaId;
    }
}
