package com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit;

public class InterestAreaDetailWeatherInfo {

    public String bestDay; // 오늘 or 내일
    public Integer bestHour; // 최고 관측적합도 시간
    public Integer bestObservationalFit; // 최고 관측적합도
    public String weatherReport; // 날씨 요약 레포트

    public String getBestDay() {
        return bestDay;
    }

    public void setBestDay(String bestDay) {
        this.bestDay = bestDay;
    }

    public Integer getBestHour() {
        return bestHour;
    }

    public void setBestHour(Integer bestHour) {
        this.bestHour = bestHour;
    }

    public Integer getBestObservationalFit() {
        return bestObservationalFit;
    }

    public void setBestObservationalFit(Integer bestObservationalFit) {
        this.bestObservationalFit = bestObservationalFit;
    }

    public String getWeatherReport() {
        return weatherReport;
    }

    public void setWeatherReport(String weatherReport) {
        this.weatherReport = weatherReport;
    }
}
