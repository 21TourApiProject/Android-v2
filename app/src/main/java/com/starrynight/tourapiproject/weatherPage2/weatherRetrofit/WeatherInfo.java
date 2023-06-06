package com.starrynight.tourapiproject.weatherPage2.weatherRetrofit;

import java.util.List;

public class WeatherInfo {

    public DetailWeather detailWeather;

    public List<HourObservationalFit> hourObservationalFitList;

    public List<DayObservationalFit> dayObservationalFitList;

    public DetailWeather getDetailWeather() {
        return detailWeather;
    }

    public List<HourObservationalFit> getHourObservationalFitList() {
        return hourObservationalFitList;
    }

    public List<DayObservationalFit> getDayObservationalFitList() {
        return dayObservationalFitList;
    }

    public static class DetailWeather {
        public String weatherText;
        public String tempHighest;
        public String tempLowest;
        public String rainfallProbability;
        public String humidity;
        public String cloud;
        public String fineDust;
        public String windSpeed;
        public String moonAge;
        public String sunrise;
        public String sunset;
        public String moonrise;
        public String moonset;

        public String getWeatherText() {
            return weatherText;
        }

        public String getTempHighest() {
            return tempHighest;
        }

        public String getTempLowest() {
            return tempLowest;
        }

        public String getRainfallProbability() {
            return rainfallProbability;
        }

        public String getHumidity() {
            return humidity;
        }

        public String getCloud() {
            return cloud;
        }

        public String getFineDust() {
            return fineDust;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public String getMoonAge() {
            return moonAge;
        }

        public String getSunrise() {
            return sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public String getMoonrise() {
            return moonrise;
        }

        public String getMoonset() {
            return moonset;
        }
    }
}
