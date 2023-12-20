package com.starrynight.tourapiproject.alarmPage;

import com.google.gson.annotations.SerializedName;
/**
* @className : Alarm
* @description : 알림 클래스입니다.
* @modification : 2022-09-02 (jinhyeok) 주석 수정
* @author : jinhyeok
* @date : 2022-09-02
* @version : 1.0
   ====개정이력(Modification Information)====
  수정일        수정자        수정내용
   -----------------------------------------
   2022-09-02      jinhyeok       주석 수정

 */
public class Alarm {
    @SerializedName("alarmTitle")
    private String alarmTitle;
    @SerializedName("alarmDate")
    private String alarmDate;
    @SerializedName("alarmContent")
    private String alarmContent;
    @SerializedName("isNotice")
    private String isNotice;
    @SerializedName("itemId")
    private Long itemId;

    public Alarm() {
    }

    public String getAlarmTitle() {
        return alarmTitle;
    }

    public void setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
    }

    public String getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(String alarmDate) {
        this.alarmDate = alarmDate;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public Alarm(String alarmTitle, String alarmDate, String alarmContent) {
        this.alarmTitle = alarmTitle;
        this.alarmDate = alarmDate;
        this.alarmContent = alarmContent;
    }

    public String getIsNotice() {
        return isNotice;
    }

    public void setIsNotice(String isNotice) {
        this.isNotice = isNotice;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
