package com.starrynight.tourapiproject.weatherPage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.weatherPage.wtAreaRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.weatherPage.wtAreaRetrofit.WtAreaParams;
import com.starrynight.tourapiproject.weatherPage.wtFineDustModel.WtFineDustModel;
import com.starrynight.tourapiproject.weatherPage.wtFineDustModel.WtFineDustRetrofit;
import com.starrynight.tourapiproject.weatherPage.wtMetModel.WtMetModel;
import com.starrynight.tourapiproject.weatherPage.wtMetModel.WtMetRetrofit;
import com.starrynight.tourapiproject.weatherPage.wtObFit.ObFitItem;
import com.starrynight.tourapiproject.weatherPage.wtObFit.ObFitViewAdapter;
import com.starrynight.tourapiproject.weatherPage.wtTodayRetrofit.WtTodayParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @className : WeatherActivity
 * @description : ?????? ??????????????????.
 * @modification : 2022-09-03 (hyeonz) ????????????
 * @author : hyeonz
 * @date : 2022-09-03
 * @version : 1.0
====????????????(Modification Information)====
?????????        ?????????        ????????????
-----------------------------------------
2022-09-03      hyeonz    ????????????
 */
public class WeatherActivity extends AppCompatActivity {
    WeatherLoadingDialog dialog;
    WeatherActivity.LoadingAsyncTask task;

    //Picker ??????
    Calendar c = Calendar.getInstance();
    Calendar cal = Calendar.getInstance();
    Calendar today = Calendar.getInstance();

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy.MM.dd");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDateDash = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDate2 = new SimpleDateFormat("yyyyMMdd");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatHourMin = new SimpleDateFormat("HH:mm");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatHour = new SimpleDateFormat("HH");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyyMMddHH");

    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);

    LinearLayout timePickerLinear;
    ImageView wtHelp;
    ImageView wtWeather;

    int cntClick = 0;

    //???????????????
    RecyclerView obFitRecycler;
    ObFitViewAdapter obFitViewAdapter;
    LinearLayout obFit;
    List<Double> obFitList = new ArrayList<>();
    Double obFitMax = 0.0;
    int obFitMaxId = 0;
    List<Integer> obFitImageList = new ArrayList<>();
    Double obFitValue;
    Double obFitValueSelect;
    int obFitApiId;
    int obFitApiPlusId;
    String obFitPercent;

    private String obFitHour[] = {"00", "01", "02", "03", "04", "05"
            , "06", "07", "08", "09", "10", "11", "12", "13", "14", "15"
            , "16", "17", "18", "19", "20", "21", "22", "23", "?????? 00", "?????? 01", "?????? 02", "?????? 03", "?????? 04", "?????? 05"
            , "?????? 06", "?????? 07", "?????? 08", "?????? 09", "?????? 10", "?????? 11", "?????? 12", "?????? 13", "??????1 4", "?????? 15"
            , "?????? 16", "?????? 17", "?????? 18", "?????? 19", "?????? 20", "?????? 21", "?????? 22", "?????? 23"};

    List<String> obFitHourList = new ArrayList<>();

    double observationalFitDegree;
    double cloudVolume;
    double cloudVolumeValue;
    double feel_like;
    double feel_likeValue;
    double moonAge;
    double moonAgeValue;
    String fineDust="??????";
    String fineDustSt="??????";
    double fineDustValue;
    double precipitationProbability;
    double precipitationProbabilityValue;
    double lightPollution;
    double lightPollutionValue;
    double biggestValue;
    double averageValue;
    double obFitFinal;

    private TextView datePicker;
    private DatePickerDialog.OnDateSetListener dateListener;
    private TextView timePicker;
    private TimePickerDialog.OnTimeSetListener timeListener;


    TextView wtTimePickerHour;

    String WT_MET_API_KEY;
    String WT_FINE_DUST_API_KEY;

    String strDate;

    String timePickerTxt;

    ArrayAdapter<CharSequence> cityAdSpin, provAdSpin;
    String choice_se = "";

    // ????????? ?????? + ??????
    String selectDateTime;
    String todayDateTime;

    String selectDate;
    String todayDateDash;
    String nextDateDash;
    String selectTime;
    String selectDateDash;

    String todayDate;
    String todayTime;

    String plusDay;
    String plusTwoDay;


    String timeZero = "00";
    String timeNoon = "12";

    private String hour[] = {"00", "01", "02", "03", "04", "05"
            , "06", "07", "08", "09", "10", "11", "12", "13", "14", "15"
            , "16", "17", "18", "19", "20", "21", "22", "23"};

    ArrayList<String> list1 = new ArrayList<>(Arrays.asList(hour));
    ArrayList<String> list2 = new ArrayList<>(Arrays.asList(hour));

    private String hourChange[];

    public NumberPicker numberPicker;


    private Button btnConfirm;
    private Button btnCancel;

    // unix ?????? ??????
    String unixTime;
    String unixSunMoon;
    String unixSunrise;
    String unixSunset;
    String unixToDate;
    String unixToDay;
    String unixToHourMin;
    String unixToHour;

    String sunriseHr;
    String sunsetHr;
    String selectHr;
    Integer selectHrInt;

    Date unixDate;
    Date unixDay;
    Date unixHourMin;
    Date unixHour;

    //?????????
    double doublePrecip;

    // ?????? TextView
    TextView commentTv;
    TextView todayWeatherTv;
    TextView starObFitTv;
    TextView bestObTimeTv;
    TextView sunriseTv;
    TextView sunsetTv;
    TextView moonriseTv;
    TextView moonsetTv;

    TextView cloudTv;
    TextView tempTv;
    TextView tempTextTv;
    TextView moonPhaseTv;
    TextView fineDustTv;
    TextView windTv;
    TextView humidityTv;
    TextView precipitationTv;
    TextView lightPolTv;
    TextView minLightPolTv;
    TextView maxLightPolTv;
    TextView lightSlashTv;

    TextView detailMent;

    String cloudValue;
    String feelsLikeValue;
    String precipValue;
    String tempValue;
    Double tempValueDouble;
    String humidityValue;
    String windValue;
    String tempMaxValue;
    String tempMinValue;

    String cloudText;
    String tempText;
    String tempMaxText;
    String tempMinText;
    String moonPhaseText;
    String windText;
    String fineDustText;
    String fineDustText2;
    String humidityText;
    String precipText;

    TextView minTempTv;
    TextView maxTempTv;
    TextView tempSlashTv;

    //?????? ?????? ??????
    String cityName;
    String provName;
    Double latitude;
    Double longitude;

    //?????????
    Double minLightPol;
    Double maxLightPol;
    String minLightPolText;
    String maxLightPolText;

    //????????????
    int dustTotalCnt;
    int dustNoInfo = 0;
    int dustCheck = 0;
    int dustNoCheck = 0;

    Date changeDate;
    Date dustNextDate;
    Date changeSelectDate;
    String addDate;

    String listDust;
    String listDustNext;
    String[] arrayComma;
    String[] arrayCommaNext;

    String[] dustStateArray = new String[19];
    String[] dustStateArray2 = new String[19];
    int index;
    String state;

    //?????? ??????
    LinearLayout detailWeather;
    String cloudState;
    String tempState;
    String moonPhaseState;
    String fineDustState;
    String windState;
    String humidityState;
    String precipState;
    String lightPolState;

    //????????? ??????
    String todayWtId;
    int todayWtIdInt;
    String todayWtName1;
    String todayWtName2;
    String todayWtName;

    //?????????
    private GpsTracker gpsTracker;

    String nowLocation;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    Double nowLatitude;
    Double nowLongitude;

    String nowCity = "??????";
    String nowProvince = "?????????";

    {
        try {
            WT_MET_API_KEY = URLDecoder.decode("7c7ba4d9df15258ce566f6592d875413", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    {
        try {
            WT_FINE_DUST_API_KEY = URLDecoder.decode("%2BbGNCh8qjhDibGZBmk6VZpWQNDaE9ePej4RbIqtZWnGBScQJshf4ELZgbQj5pqfAtnJPGU7ggOsyK0RmLDJlTQ%3D%3D", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        dialog = new WeatherLoadingDialog(WeatherActivity.this);
        task = new LoadingAsyncTask(WeatherActivity.this, 5000);
        task.execute();

        if (!checkLocationServicesStatus()) {
            //gps ??????
            showDialogForLocationServiceSetting();
            Log.d("checkLocationCancel", "0");
        } else {
            Log.d("checkLocationCancel", "1");
            checkRunTimePermission();
        }

        gpsTracker = new GpsTracker(WeatherActivity.this);

        nowLatitude = gpsTracker.getLatitude();
        nowLongitude = gpsTracker.getLongitude();

        nowLocation = getCurrentAddress(nowLatitude, nowLongitude);
        Log.d("nowLocationResult", nowLocation);
        Log.d("nowCityProv1", nowCity + " " + nowProvince);

        setCityName();

        setTextView();
        onClickBackBtn();
        onClickCloudInfo();
        onClickHelpBtn();

        onSetDatePicker();
        onSetTimePicker();

        //?????? ?????? Spinner
        onSetAreaSpinner();

        selectDateTime = selectDate + selectTime;
        Log.d("selectDateTime", selectDateTime);

    }

    // ??? ?????? ????????? recycler ?????????
    @SuppressLint("SetTextI18n")
    public void setObFitRecycler() {
        obFitRecycler = findViewById(R.id.ob_fit_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        obFitRecycler.setLayoutManager(linearLayoutManager);

        obFitViewAdapter = new ObFitViewAdapter();
        obFitRecycler.setAdapter(obFitViewAdapter);

        Log.d("obFitListOut", obFitList.toString());

        for (int i = 0; i < obFitList.size(); i++) {
            if (obFitList.get(i) > obFitMax) {
                obFitMax = obFitList.get(i);
                obFitMaxId = i;
                Log.d("obFitMaxCheck", String.valueOf(obFitMax));
                Log.d("obFitMaxCheck", String.valueOf(obFitMaxId));
            }

            if (obFitList.get(i) < 40) {
                obFitImageList.add(R.drawable.wt__hour_very_bad);
            } else if (obFitList.get(i) < 60) {
                obFitImageList.add(R.drawable.wt__hour_very_bad);
            } else if (obFitList.get(i) < 70) {
                obFitImageList.add(R.drawable.wt__hour_average);
            } else if (obFitList.get(i) < 85) {
                obFitImageList.add(R.drawable.wt__hour_good);
            } else {
                obFitImageList.add(R.drawable.wt__hour_very_good);
            }
        }
        bestObTimeTv.setText(obFitHourList.get(obFitMaxId) + "???");
        obFitMax = 0.0;

        for (int i = 0; i < obFitList.size(); i++) {
            obFitPercent = String.format("%.0f", obFitList.get(i)) + "%";
            obFitViewAdapter.addItem(new ObFitItem(obFitImageList.get(i), obFitHourList.get(i) + "???", obFitPercent));
        }
        obFitImageList.clear();
        obFitRecycler.setAdapter(obFitViewAdapter);
    }

    // textView??? ???????????? ?????????
    public void setTextView() {
        commentTv = findViewById(R.id.wt_comment);
        todayWeatherTv = findViewById(R.id.wt_today_weather_info);
        starObFitTv = findViewById(R.id.wt_star_ob_fit_info);
        bestObTimeTv = findViewById(R.id.wt_best_ob_time_info);
        sunriseTv = findViewById(R.id.sunrise_info);
        sunsetTv = findViewById(R.id.sunset_info);
        moonriseTv = findViewById(R.id.moonrise_info);
        moonsetTv = findViewById(R.id.moonset_info);

        cloudTv = findViewById(R.id.wt_cloud);
        tempTv = findViewById(R.id.wt_temp);
        tempTextTv = findViewById(R.id.wt_temp_text);
        moonPhaseTv = findViewById(R.id.wt_moon_phase);
        fineDustTv = findViewById(R.id.wt_find_dust);
        windTv = findViewById(R.id.wt_wind);
        humidityTv = findViewById(R.id.wt_humidity);
        precipitationTv = findViewById(R.id.wt_precipitation);

        lightPolTv = findViewById(R.id.wt_light_pol);
        minLightPolTv = findViewById(R.id.wt_min_light_pol);
        maxLightPolTv = findViewById(R.id.wt_max_light_pol);
        lightSlashTv = findViewById(R.id.wt_light_slash);

        minTempTv = findViewById(R.id.wt_min_temp);
        maxTempTv = findViewById(R.id.wt_max_temp);
        tempSlashTv = findViewById(R.id.wt_temp_slash);

        detailMent = findViewById(R.id.wt_detail_ment);

        wtTimePickerHour = findViewById(R.id.wt_timePicker_hour);
        timePickerLinear = findViewById(R.id.wt_timePicker_linear);
        wtHelp = findViewById(R.id.wt_help);
        wtWeather = findViewById(R.id.wt_weather);
    }

    // ???????????? ?????? ????????? ?????????
    public void onClickBackBtn() {
        ImageView button = findViewById(R.id.wt_back_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // ????????? ?????? ????????? ?????????
    public void onClickHelpBtn() {
        wtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WtHelpActivity.class);
                startActivity(intent);
            }
        });
    }

    // ????????? ?????? ?????? ???????????? ?????? ????????? ?????????
    public void onClickCloudInfo() {
        TextView button1 = findViewById(R.id.wt_today_cloud);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.weather.go.kr/wgis-nuri/html/map.html"));
                startActivity(intent);
            }
        });
    }

    // ?????? Picker ?????? ?????????
    public void onSetDatePicker() {
//        cal.add(Calendar.DATE, 2);
//        plusDay = formatDate2.format(cal.getTime());
//        plusDay += selectTime;
//        Log.d("plusTwoDay", plusDay);

        selectDate = formatDate2.format(cal.getTime());
        todayDate = formatDate2.format(cal.getTime());
        Log.d("todayDate", todayDate);

        datePicker = (TextView) findViewById(R.id.wt_datePicker);
        timePicker = findViewById(R.id.wt_timePicker);

        strDate = formatDate.format(cal.getTime());
        Log.d("selectDate", selectDate);
        datePicker.setText(strDate);

        todayTime = formatHour.format(cal.getTime());
        timePickerLinear.setVisibility(View.VISIBLE);
        timePicker.setText(todayTime);
        wtTimePickerHour.setVisibility(View.VISIBLE);

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //monthOfYear += 1;
                cal.set(year, monthOfYear, dayOfMonth);
                strDate = formatDate.format(cal.getTime());
                Log.d("strDate", strDate);
                datePicker.setText(strDate);
                //20210901
                selectDate = year + String.format("%02d", (monthOfYear + 1)) + String.format("%02d", (dayOfMonth));
                Log.d("selectDate", selectDate);

                if (todayTime.equals("00")) {
                    if (selectDate.equals(plusDay)) {
                        timePickerLinear.setVisibility(View.VISIBLE);
                        timePicker.setText(timeZero);
                        wtTimePickerHour.setVisibility(View.VISIBLE);
                        selectTime = timeZero;
                        Log.d("selectTime1", selectTime);
                    } else if (selectDate.equals(todayDate)) {
                        Log.d("todayTime3", todayTime);
                        timePickerLinear.setVisibility(View.VISIBLE);
                        timePicker.setText(todayTime);
                        wtTimePickerHour.setVisibility(View.VISIBLE);
                    } else {
                        timePickerLinear.setVisibility(View.GONE);
                        wtTimePickerHour.setVisibility(View.GONE);
                        selectTime = timeNoon;
                        Log.d("selectTime4", selectTime);
                    }
                } else {
                    if (selectDate.equals(plusDay)) {
                        timePickerLinear.setVisibility(View.VISIBLE);
                        timePicker.setText(timeZero);
                        wtTimePickerHour.setVisibility(View.VISIBLE);
                        selectTime = timeZero;
                        Log.d("selectTime1", selectTime);
                    } else if (selectDate.equals(plusTwoDay)) {
                        timePickerLinear.setVisibility(View.VISIBLE);
                        timePicker.setText(timeZero);
                        wtTimePickerHour.setVisibility(View.VISIBLE);
                        selectTime = timeZero;
                        Log.d("selectTime2", selectTime);
                    } else if (selectDate.equals(todayDate)) {
                        Log.d("todayTime3", todayTime);
                        timePickerLinear.setVisibility(View.VISIBLE);
                        timePicker.setText(todayTime);
                        wtTimePickerHour.setVisibility(View.VISIBLE);
                    } else {
                        timePickerLinear.setVisibility(View.GONE);
                        wtTimePickerHour.setVisibility(View.GONE);
                        selectTime = timeNoon;
                        Log.d("selectTime4", selectTime);
                    }
                }

                selectDateTime = selectDate + selectTime;
                Log.d("selectDateTime", selectDateTime);

                connectMetApi();
                Log.d("connectMetApiCheck", "0");
                connectFineDustApi();
                Log.d("obCheck", "0");
            }
        };
    }

    // ?????? ?????? ????????? ?????????
    public void wtClickDatePicker(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateListener, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
        today.add(Calendar.DAY_OF_MONTH, 7);
        datePickerDialog.getDatePicker().setMaxDate(today.getTimeInMillis());
        datePickerDialog.show();
        today.add(Calendar.DAY_OF_MONTH, -7);
    }

    // ?????? ?????? ?????????
    public void onSetTimePicker() {
        timePicker = findViewById(R.id.wt_timePicker);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyyMMddHH");
        todayDateTime = formatDateTime.format(cal.getTime());
        Log.d("todayDateTime", todayDateTime);

        selectTime = todayTime;
        Log.d("selectTime5", selectTime);

        c.add(Calendar.DATE, 1);
        plusDay = formatDate2.format(c.getTime());
        c.add(Calendar.DATE, 1);
        plusTwoDay = formatDate2.format(c.getTime());
        Log.d("plusDay", plusDay);
        Log.d("plusTwoDay", plusTwoDay);
        c.add(Calendar.DATE, -2);
    }

    //?????? ?????? ????????? ?????????
    public void wtClickTimePicker(View view) {
        timePicker = findViewById(R.id.wt_timePicker);

        View dialogView = getLayoutInflater().inflate(R.layout.wt_dialog_timepicker, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        numberPicker = (NumberPicker) alertDialog.findViewById(R.id.hourPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(0);

        try {
            cal.setTime(Objects.requireNonNull(formatDate2.parse(selectDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // ?????? ???????????? ?????? ?????? ????????? ?????? ??????
        if (todayDate.equals(selectDate)) {
            switch (todayTime) {
                case "00":
                    setMinHour(-1);
                    break;
                case "01":
                    setMinHour(0);
                    break;
                case "02":
                    setMinHour(1);
                    break;
                case "03":
                    setMinHour(2);
                    break;
                case "04":
                    setMinHour(3);
                    break;
                case "05":
                    setMinHour(4);
                    break;
                case "06":
                    setMinHour(5);
                    break;
                case "07":
                    setMinHour(6);
                    break;
                case "08":
                    setMinHour(7);
                    break;
                case "09":
                    setMinHour(8);
                    break;
                case "10":
                    setMinHour(9);
                    break;
                case "11":
                    setMinHour(10);
                    break;
                case "12":
                    setMinHour(11);
                    break;
                case "13":
                    setMinHour(12);
                    break;
                case "14":
                    setMinHour(13);
                    break;
                case "15":
                    setMinHour(14);
                    break;
                case "16":
                    setMinHour(15);
                    break;
                case "17":
                    setMinHour(16);
                    break;
                case "18":
                    setMinHour(17);
                    break;
                case "19":
                    setMinHour(18);
                    break;
                case "20":
                    setMinHour(19);
                    break;
                case "21":
                    setMinHour(20);
                    break;
                case "22":
                    setMinHour(21);
                    break;
                case "23":
                    setMinHour(22);
                    break;
            }
            // 48?????? ????????? ?????? ?????? ??????
        } else {
            if (selectDate.equals(plusDay)) {
                setLimitHour(1);
            } else {
                if (selectDate.equals(plusTwoDay)) {
                    switch (todayTime) {
                        case "00":
                            numberPicker.setEnabled(false);
                            break;
                        case "01":
                            setLimitHour(24);
                            break;
                        case "02":
                            setLimitHour(23);
                            break;
                        case "03":
                            setLimitHour(22);
                            break;
                        case "04":
                            setLimitHour(21);
                            break;
                        case "05":
                            setLimitHour(20);
                            break;
                        case "06":
                            setLimitHour(19);
                            break;
                        case "07":
                            setLimitHour(18);
                            break;
                        case "08":
                            setLimitHour(17);
                            break;
                        case "09":
                            setLimitHour(16);
                            break;
                        case "10":
                            setLimitHour(15);
                            break;
                        case "11":
                            setLimitHour(14);
                            break;
                        case "12":
                            setLimitHour(13);
                            break;
                        case "13":
                            setLimitHour(12);
                            break;
                        case "14":
                            setLimitHour(11);
                            break;
                        case "15":
                            setLimitHour(10);
                            break;
                        case "16":
                            setLimitHour(9);
                            break;
                        case "17":
                            setLimitHour(8);
                            break;
                        case "18":
                            setLimitHour(7);
                            break;
                        case "19":
                            setLimitHour(6);
                            break;
                        case "20":
                            setLimitHour(5);
                            break;
                        case "21":
                            setLimitHour(4);
                            break;
                        case "22":
                            setLimitHour(3);
                            break;
                        case "23":
                            setLimitHour(2);
                            break;
                    }
                } else {
                    numberPicker.setEnabled(false);
                }
            }
        }

        btnConfirm = dialogView.findViewById(R.id.wt_btn_confirm);
        btnCancel = dialogView.findViewById(R.id.wt_btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("checkSelectTime", selectTime);
                if (todayTime.equals("00")) {
                    if ((selectDate.equals(todayDate)) || (selectDate.equals(plusDay))) {
                        timePickerTxt = hourChange[numberPicker.getValue()];
                        timePickerLinear.setVisibility(View.VISIBLE);
                        timePicker.setText(timePickerTxt);
                        wtTimePickerHour.setVisibility(View.VISIBLE);
                        selectTime = String.valueOf(hourChange[numberPicker.getValue()]);
                    } else {
                        timePickerLinear.setVisibility(View.VISIBLE);
                        wtTimePickerHour.setVisibility(View.GONE);
                        selectTime = timeNoon;
                    }
                } else {
                    if ((selectDate.equals(todayDate)) || (selectDate.equals(plusDay)) || (selectDate.equals(plusTwoDay))) {
                        timePickerTxt = hourChange[numberPicker.getValue()];
                        timePickerLinear.setVisibility(View.VISIBLE);
                        timePicker.setText(timePickerTxt);
                        wtTimePickerHour.setVisibility(View.VISIBLE);
                        selectTime = String.valueOf(hourChange[numberPicker.getValue()]);
                    } else {
                        timePickerLinear.setVisibility(View.GONE);
                        wtTimePickerHour.setVisibility(View.GONE);
                        selectTime = timeNoon;
                    }
                }

                Log.d("selectTime6", selectTime);

                selectDateTime = selectDate + selectTime;
                Log.d("selectDateTime", selectDateTime);

                connectMetApi();
                Log.d("connectMetApiCheck", "1");
                alertDialog.dismiss();
            }
        });
    }

    // ????????? ?????? ?????? ?????????
    public void connectMetApi() {
        Log.d("latitude1`", String.valueOf(latitude));
        Log.d("longitude1", String.valueOf(longitude));

        obFit = findViewById(R.id.ob_fit);

        Call<WtMetModel> getWeatherInstance = WtMetRetrofit.wtMetInterface()
                .getMetData(latitude, longitude, "minutely,current", WT_MET_API_KEY, "metric");

        getWeatherInstance.enqueue(new Callback<WtMetModel>() {
            @SuppressLint({"DefaultLocale", "SetTextI18n", "LongLogTag"})
            @Override
            public void onResponse(Call<WtMetModel> call, Response<WtMetModel> response) {

                if (response.isSuccessful()) {
                    WtMetModel data = response.body();

                    for (int i = 0; i < 8; i++) {
                        unixTime = data.getDaily().get(i).getDt();
                        unixChange(unixTime);
                        Log.d("unixToDay", unixToDay);

                        if (selectDate.equals(unixToDay)) {
                            //?????? ??????
                            moonPhaseText = data.getDaily().get(i).getMoonPhase();
                            moonAge = Double.parseDouble(moonPhaseText);
                            moonPhaseTv.setText(moonPhaseText);

                            //??????
                            unixSunMoon = data.getDaily().get(i).getSunrise();
                            unixSunrise = unixSunMoon;
                            unixChange(unixSunMoon);
                            sunriseTv.setText(unixToHourMin);

                            sunriseHr = unixToHour;
                            Log.d("sunriseHr", unixToHour);

                            //??????
                            unixSunMoon = data.getDaily().get(i).getSunset();
                            unixSunset = unixSunMoon;
                            unixChange(unixSunMoon);
                            sunsetTv.setText(unixToHourMin);

                            sunsetHr = unixToHour;
                            Log.d("sunsetHr", unixToHour);

                            //??????
                            unixSunMoon = data.getDaily().get(i).getMoonrise();
                            unixChange(unixSunMoon);
                            moonriseTv.setText(unixToHourMin);

                            //??????
                            unixSunMoon = data.getDaily().get(i).getMoonset();
                            unixChange(unixSunMoon);
                            moonsetTv.setText(unixToHourMin);
                        }
                    }

                    detailWeather = findViewById(R.id.detail_weather);

                    if (todayTime.equals("00")) {
                        if (selectDate.equals(todayDate) || selectDate.equals(plusDay)) {
                            for (int i = 0; i < 48; i++) {
                                unixTime = data.getHourly().get(i).getDt();
                                unixChange(unixTime);
                                Log.d("unixToDate", unixToDate);

                                if (selectDateTime.equals(unixToDate)) {
                                    Log.d("selectDateTime", selectDateTime);
                                    //tempTv??? ?????????
                                    setTempVisibility(0);

                                    cloudValue = data.getHourly().get(i).getClouds();
                                    feelsLikeValue = data.getHourly().get(i).getFeelsLike();
                                    precipValue = data.getHourly().get(i).getPop();
                                    tempValue = data.getHourly().get(i).getTemp();
                                    humidityValue = data.getHourly().get(i).getHumidity();
                                    windValue = data.getHourly().get(i).getWindSpeed();

                                    cloudText = cloudValue + "%";
                                    tempText = String.format("%.1f", Double.parseDouble(tempValue)) + "??C";
                                    windText = windValue + "m/s";
                                    humidityText = humidityValue + "%";
                                    todayWtId = data.getHourly().get(i).getWeather().get(0).getId();
                                    todayWtIdInt = Integer.parseInt(todayWtId);
                                    doublePrecip = Double.parseDouble(precipValue) * 100;
                                    precipText = doublePrecip + "%";

                                    //???????????????
                                    cloudVolume = Double.parseDouble(cloudValue);
                                    feel_like = Double.parseDouble(feelsLikeValue);
                                    precipitationProbability = Double.parseDouble(precipValue);
                                    obFitValueSelect = setObservationalFitDegree();
                                    Log.d("cloudVolume", String.valueOf(cloudVolume));
                                    Log.d("feel_like", String.valueOf(feel_like));
                                    Log.d("precipitationProbability", String.valueOf(precipitationProbability));
                                    Log.d("obFitValueSelect", String.valueOf(obFitValueSelect));

                                    setDetailState(cloudVolume, Double.parseDouble(tempValue),
                                            Double.parseDouble(humidityValue), moonAge,
                                            Double.parseDouble(windValue), doublePrecip, lightPollution, fineDustSt);

                                    cloudTv.setText(cloudText);
                                    tempTv.setText(tempText);
                                    tempTextTv.setText("??????");
                                    windTv.setText(windText);
                                    humidityTv.setText(humidityText);
                                    precipitationTv.setText(precipText);

                                    selectHr = selectTime;
                                    Log.d("selectHr", selectHr);
                                    selectHrInt = Integer.parseInt(selectHr);
                                    if (selectHrInt >= 7 && selectHrInt < 18) {
                                        starObFitTv.setText("0%");
                                        setObFitComment(0.0);
                                    } else {
                                        starObFitTv.setText(String.format("%.0f", obFitValueSelect) + "%");
                                        setObFitComment(obFitValueSelect);
                                    }
                                    connectTodayWeather();
                                    Log.d("getI", String.valueOf(i));
                                }
                            }
                        } else {
                            for (int i = 0; i < 8; i++) {
                                unixTime = data.getDaily().get(i).getDt();
                                unixChange(unixTime);
                                Log.d("unixToDateDaily", unixToDate);

                                if (selectDateTime.equals(unixToDate)) {
                                    setTempVisibility(1);

                                    cloudValue = data.getDaily().get(i).getClouds();
                                    feelsLikeValue = data.getDaily().get(i).getFeelsLike().getDay();
                                    precipValue = data.getDaily().get(i).getPop();
                                    tempMinValue = data.getDaily().get(i).getTemp().getMin();
                                    tempMaxValue = data.getDaily().get(i).getTemp().getMax();
                                    humidityValue = data.getDaily().get(i).getHumidity();
                                    windValue = data.getDaily().get(i).getWindSpeed();

                                    cloudText = cloudValue + "%";
                                    tempMinText = String.format("%.1f", Double.parseDouble(tempMinValue)) + "??C";
                                    tempMaxText = String.format("%.1f", Double.parseDouble(tempMaxValue)) + "??C";

                                    tempValueDouble = (Double.parseDouble(tempMinValue) + Double.parseDouble(tempMaxValue)) / 2;

                                    windText = windValue + "m/s";
                                    humidityText = humidityValue + "%";
                                    todayWtId = data.getDaily().get(i).getWeather().get(0).getId();

                                    doublePrecip = Double.parseDouble(precipValue) * 100;
                                    precipText = doublePrecip + "%";

                                    //???????????????
                                    cloudVolume = Double.parseDouble(cloudValue);
                                    feel_like = Double.parseDouble(feelsLikeValue);
                                    precipitationProbability = Double.parseDouble(precipValue);
                                    obFitValueSelect = setObservationalFitDegree();
                                    Log.d("cloudVolume", String.valueOf(cloudVolume));
                                    Log.d("feel_like", String.valueOf(feel_like));
                                    Log.d("precipitationProbability", String.valueOf(precipitationProbability));
                                    Log.d("obFitValueSelect", String.valueOf(obFitValueSelect));

                                    setDetailState(cloudVolume, tempValueDouble,
                                            Double.parseDouble(humidityValue), moonAge,
                                            Double.parseDouble(windValue), doublePrecip, lightPollution, fineDustSt);

                                    cloudTv.setText(cloudText);
                                    minTempTv.setText(tempMinText);
                                    maxTempTv.setText(tempMaxText);
                                    tempTextTv.setText("??????\n(??????/??????)");
                                    windTv.setText(windText);
                                    humidityTv.setText(humidityText);
                                    precipitationTv.setText(precipText);

                                    starObFitTv.setText(String.format("%.0f", obFitValueSelect) + "%");
                                    setObFitComment(obFitValueSelect);

                                    connectTodayWeather();
                                    Log.d("getIDaily", String.valueOf(i));
                                }
                            }
                        }
                    } else {
                        if (selectDate.equals(todayDate) || selectDate.equals(plusDay) || selectDate.equals(plusTwoDay)) {
                            for (int i = 0; i < 48; i++) {
                                unixTime = data.getHourly().get(i).getDt();
                                unixChange(unixTime);
                                Log.d("unixToDate", unixToDate);

                                if (selectDateTime.equals(unixToDate)) {
                                    //tempTv??? ?????????
                                    setTempVisibility(0);

                                    cloudValue = data.getHourly().get(i).getClouds();
                                    feelsLikeValue = data.getHourly().get(i).getFeelsLike();
                                    precipValue = data.getHourly().get(i).getPop();
                                    tempValue = data.getHourly().get(i).getTemp();
                                    humidityValue = data.getHourly().get(i).getHumidity();
                                    windValue = data.getHourly().get(i).getWindSpeed();

                                    cloudText = cloudValue + "%";
                                    tempText = String.format("%.1f", Double.parseDouble(tempValue)) + "??C";
                                    windText = windValue + "m/s";
                                    humidityText = humidityValue + "%";
                                    todayWtId = data.getHourly().get(i).getWeather().get(0).getId();
                                    todayWtIdInt = Integer.parseInt(todayWtId);
                                    doublePrecip = Double.parseDouble(precipValue) * 100;
                                    precipText = doublePrecip + "%";

                                    //???????????????
                                    cloudVolume = Double.parseDouble(cloudValue);
                                    feel_like = Double.parseDouble(feelsLikeValue);
                                    precipitationProbability = Double.parseDouble(precipValue);
                                    obFitValueSelect = setObservationalFitDegree();
                                    Log.d("cloudVolume", String.valueOf(cloudVolume));
                                    Log.d("feel_like", String.valueOf(feel_like));
                                    Log.d("precipitationProbability", String.valueOf(precipitationProbability));
                                    Log.d("obFitValueSelect", String.valueOf(obFitValueSelect));

                                    setDetailState(cloudVolume, Double.parseDouble(tempValue),
                                            Double.parseDouble(humidityValue), moonAge,
                                            Double.parseDouble(windValue), doublePrecip, lightPollution, fineDustSt);

                                    cloudTv.setText(cloudText);
                                    tempTv.setText(tempText);
                                    tempTextTv.setText("??????");
                                    windTv.setText(windText);
                                    humidityTv.setText(humidityText);
                                    precipitationTv.setText(precipText);

                                    selectHr = selectTime;
                                    Log.d("selectHr", selectHr);
                                    selectHrInt = Integer.parseInt(selectHr);
                                    if (selectHrInt >= 7 && selectHrInt < 18) {
                                        starObFitTv.setText("0%");
                                        setObFitComment(0.0);
                                    } else {
                                        starObFitTv.setText(String.format("%.0f", obFitValueSelect) + "%");
                                        setObFitComment(obFitValueSelect);
                                    }
                                    connectTodayWeather();
                                    Log.d("getI", String.valueOf(i));
                                }
                            }
                        } else {
                            for (int i = 0; i < 8; i++) {
                                unixTime = data.getDaily().get(i).getDt();
                                unixChange(unixTime);
                                Log.d("unixToDateDaily", unixToDate);

                                if (selectDateTime.equals(unixToDate)) {
                                    setTempVisibility(1);

                                    cloudValue = data.getDaily().get(i).getClouds();
                                    feelsLikeValue = data.getDaily().get(i).getFeelsLike().getDay();
                                    precipValue = data.getDaily().get(i).getPop();
                                    tempMinValue = data.getDaily().get(i).getTemp().getMin();
                                    tempMaxValue = data.getDaily().get(i).getTemp().getMax();
                                    humidityValue = data.getDaily().get(i).getHumidity();
                                    windValue = data.getDaily().get(i).getWindSpeed();

                                    cloudText = cloudValue + "%";
                                    tempMinText = String.format("%.1f", Double.parseDouble(tempMinValue)) + "??C";
                                    tempMaxText = String.format("%.1f", Double.parseDouble(tempMaxValue)) + "??C";

                                    tempValueDouble = (Double.parseDouble(tempMinValue) + Double.parseDouble(tempMaxValue)) / 2;

                                    windText = windValue + "m/s";
                                    humidityText = humidityValue + "%";
                                    todayWtId = data.getDaily().get(i).getWeather().get(0).getId();

                                    doublePrecip = Double.parseDouble(precipValue) * 100;
                                    precipText = doublePrecip + "%";

                                    //???????????????
                                    cloudVolume = Double.parseDouble(cloudValue);
                                    feel_like = Double.parseDouble(feelsLikeValue);
                                    precipitationProbability = Double.parseDouble(precipValue);
                                    obFitValueSelect = setObservationalFitDegree();
                                    Log.d("cloudVolume", String.valueOf(cloudVolume));
                                    Log.d("feel_like", String.valueOf(feel_like));
                                    Log.d("precipitationProbability", String.valueOf(precipitationProbability));
                                    Log.d("obFitValueSelect", String.valueOf(obFitValueSelect));

                                    setDetailState(cloudVolume, tempValueDouble,
                                            Double.parseDouble(humidityValue), moonAge,
                                            Double.parseDouble(windValue), doublePrecip, lightPollution, fineDustSt);

                                    cloudTv.setText(cloudText);
                                    minTempTv.setText(tempMinText);
                                    maxTempTv.setText(tempMaxText);
                                    tempTextTv.setText("??????\n(??????/??????)");
                                    windTv.setText(windText);
                                    humidityTv.setText(humidityText);
                                    precipitationTv.setText(precipText);

                                    starObFitTv.setText(String.format("%.0f", obFitValueSelect) + "%");
                                    setObFitComment(obFitValueSelect);
                                    connectTodayWeather();
                                    Log.d("getIDaily", String.valueOf(i));
                                }
                            }
                        }
                    }

                    // ????????? ???????????????

                    // ??????????????? ????????? ?????????
                    obFitList.clear();
                    obFitHourList.clear();
                    if (selectDate.equals(todayDate)) {
                        obFit.setVisibility(View.VISIBLE);

                        //?????? 0???? ~ ?????? 06???
                        if (todayDateTime.equals(todayDate + "00")) {
                            obFitApiId = 0;
                        } else if (todayDateTime.equals(todayDate + "01")) {
                            obFitApiId = 1;
                        } else if (todayDateTime.equals(todayDate + "02")) {
                            obFitApiId = 2;
                        } else if (todayDateTime.equals(todayDate + "03")) {
                            obFitApiId = 3;
                        } else if (todayDateTime.equals(todayDate + "04")) {
                            obFitApiId = 4;
                        } else if (todayDateTime.equals(todayDate + "05")) {
                            obFitApiId = 5;
                        } else if (todayDateTime.equals(todayDate + "06")) {
                            obFitApiId = 6;
                        } else if (todayDateTime.equals(todayDate + "07")) {
                            obFitApiId = 7;
                        } else if (todayDateTime.equals(todayDate + "08")) {
                            obFitApiId = 8;
                        } else if (todayDateTime.equals(todayDate + "09")) {
                            obFitApiId = 9;
                        } else if (todayDateTime.equals(todayDate + "10")) {
                            obFitApiId = 10;
                        } else if (todayDateTime.equals(todayDate + "11")) {
                            obFitApiId = 11;
                        } else if (todayDateTime.equals(todayDate + "12")) {
                            obFitApiId = 12;
                        } else if (todayDateTime.equals(todayDate + "13")) {
                            obFitApiId = 13;
                        } else if (todayDateTime.equals(todayDate + "14")) {
                            obFitApiId = 14;
                        } else if (todayDateTime.equals(todayDate + "15")) {
                            obFitApiId = 15;
                        } else if (todayDateTime.equals(todayDate + "16")) {
                            obFitApiId = 16;
                        } else if (todayDateTime.equals(todayDate + "17")) {
                            obFitApiId = 17;
                        } else if (todayDateTime.equals(todayDate + "18")) {
                            obFitApiId = 18;
                        } else if (todayDateTime.equals(todayDate + "19")) {
                            obFitApiId = 19;
                        } else if (todayDateTime.equals(todayDate + "20")) {
                            obFitApiId = 20;
                        } else if (todayDateTime.equals(todayDate + "21")) {
                            obFitApiId = 21;
                        } else if (todayDateTime.equals(todayDate + "22")) {
                            obFitApiId = 22;
                        } else if (todayDateTime.equals(todayDate + "23")) {
                            obFitApiId = 23;
                        }
                        Log.d("obFitApiId", String.valueOf(obFitApiId));

                        if (obFitApiId < 7) {
                            for (int j = 0; j < 7 - obFitApiId; j++) {
                                cloudVolume = Double.parseDouble(data.getHourly().get(j).getClouds());
                                feel_like = Double.parseDouble(data.getHourly().get(j).getFeelsLike());
                                precipitationProbability = Double.parseDouble(data.getHourly().get(j).getPop());
                                obFitValue = setObservationalFitDegree();
                                obFitList.add(obFitValue);
                                obFitHourList.add(obFitHour[obFitApiId + j]);
                                Log.d("obFitCloud", String.valueOf(cloudVolume));
                                Log.d("obFitTemp", String.valueOf(feel_like));
                                Log.d("obFitMoon", String.valueOf(moonAge));
                                Log.d("obFitFineDust", fineDust);
                                Log.d("obFitPrecip", String.valueOf(precipitationProbability));
                                Log.d("obFitLightPol", String.valueOf(lightPollution));
                                Log.d("obValue", String.valueOf(setObservationalFitDegree()));
                            }
                        }

                        if (obFitApiId >= 18 && obFitApiId < 24) {
                            // ?????? 1???? ~ ?????? 23???
                            for (int j = 0; j < 24 - obFitApiId; j++) {
                                cloudVolume = Double.parseDouble(data.getHourly().get(j).getClouds());
                                feel_like = Double.parseDouble(data.getHourly().get(j).getFeelsLike());
                                precipitationProbability = Double.parseDouble(data.getHourly().get(j).getPop());
                                obFitValue = setObservationalFitDegree();
                                obFitList.add(obFitValue);
                                obFitHourList.add(obFitHour[obFitApiId + j]);
                                Log.d("obFitCloud", String.valueOf(cloudVolume));
                                Log.d("obFitTemp", String.valueOf(feel_like));
                                Log.d("obFitMoon", String.valueOf(moonAge));
                                Log.d("obFitFineDust", fineDust);
                                Log.d("obFitPrecip", String.valueOf(precipitationProbability));
                                Log.d("obFitLightPol", String.valueOf(lightPollution));
                                Log.d("obValue", String.valueOf(setObservationalFitDegree()));
                            }
                        } else {
                            // ?????? ?????? 6??? ~ ?????? 23???
                            for (int j = 18 - obFitApiId; j < 24 - obFitApiId; j++) {
                                cloudVolume = Double.parseDouble(data.getHourly().get(j).getClouds());
                                feel_like = Double.parseDouble(data.getHourly().get(j).getFeelsLike());
                                precipitationProbability = Double.parseDouble(data.getHourly().get(j).getPop());
                                obFitValue = setObservationalFitDegree();
                                obFitList.add(obFitValue);
                                Log.d("obFitCloud", String.valueOf(cloudVolume));
                                Log.d("obFitTemp", String.valueOf(feel_like));
                                Log.d("obFitMoon", String.valueOf(moonAge));
                                Log.d("obFitFineDust", fineDust);
                                Log.d("obFitPrecip", String.valueOf(precipitationProbability));
                                Log.d("obFitLightPol", String.valueOf(lightPollution));
                                Log.d("obValue", String.valueOf(setObservationalFitDegree()));
                            }
                            obFitHourList.addAll(Arrays.asList(obFitHour).subList(18, 24));
                        }
                        //?????? 00??? ~ ?????? 06???
                        moonPhaseText = data.getDaily().get(1).getMoonPhase();
                        moonAge = Double.parseDouble(moonPhaseText);
                        Log.d("obFitSelect", String.valueOf(moonAge));

                        connectNextFineDustApi();
                        for (int j = 24 - obFitApiId; j < 31 - obFitApiId; j++) {
                            cloudVolume = Double.parseDouble(data.getHourly().get(j).getClouds());
                            feel_like = Double.parseDouble(data.getHourly().get(j).getFeelsLike());
                            precipitationProbability = Double.parseDouble(data.getHourly().get(j).getPop());
                            obFitValue = setObservationalFitDegree();
                            obFitList.add(obFitValue);
                            Log.d("obFitCloud", String.valueOf(cloudVolume));
                            Log.d("obFitTemp", String.valueOf(feel_like));
                            Log.d("obFitMoon", String.valueOf(moonAge));
                            Log.d("obFitFineDust", fineDust);
                            Log.d("obFitPrecip", String.valueOf(precipitationProbability));
                            Log.d("obFitLightPol", String.valueOf(lightPollution));
                            Log.d("obValue", String.valueOf(setObservationalFitDegree()));
                        }
                        obFitHourList.addAll(Arrays.asList(obFitHour).subList(24, 31));
                        setObFitRecycler();
                    } else if (selectDate.equals(plusDay)) {
                        obFit.setVisibility(View.VISIBLE);
                        obFitList.clear();

                        for (int i = 0; i < 48; i++) {
                            unixTime = data.getHourly().get(i).getDt();
                            unixChange(unixTime);
                            Log.d("unixToDate", unixToDate);
                            if (unixToDate.equals(plusDay + "00")) {
                                obFitApiPlusId = i;
                            }
                        }
                        // ?????? 00??? ~ ?????? 06???
                        for (int i = obFitApiPlusId; i < obFitApiPlusId + 7; i++) {
                            cloudVolume = Double.parseDouble(data.getHourly().get(i).getClouds());
                            moonAge = Double.parseDouble(moonPhaseText);
                            feel_like = Double.parseDouble(data.getHourly().get(i).getFeelsLike());
                            precipitationProbability = Double.parseDouble(data.getHourly().get(i).getPop());
                            obFitValue = setObservationalFitDegree();
                            obFitList.add(obFitValue);
                            Log.d("errorcheck", "0");
                        }
                        obFitHourList.addAll(Arrays.asList(obFitHour).subList(0, 7));

                        // ?????? 18??? ~ ?????? 23???
                        for (int i = obFitApiPlusId + 18; i < obFitApiPlusId + 24; i++) {
                            cloudVolume = Double.parseDouble(data.getHourly().get(i).getClouds());
                            moonAge = Double.parseDouble(moonPhaseText);
                            feel_like = Double.parseDouble(data.getHourly().get(i).getFeelsLike());
                            precipitationProbability = Double.parseDouble(data.getHourly().get(i).getPop());
                            obFitValue = setObservationalFitDegree();
                            obFitList.add(obFitValue);
                            Log.d("errorcheck", "1");
                        }
                        obFitHourList.addAll(Arrays.asList(obFitHour).subList(18, 24));
                        Log.d("obFitApiid", "obFitApiId");
                        if (obFitApiId >= 1 && obFitApiId < 7) {
                            for (int i = obFitApiPlusId + 24; i < obFitApiPlusId + obFitApiId + 24; i++) {
                                cloudVolume = Double.parseDouble(data.getHourly().get(i).getClouds());
                                moonAge = Double.parseDouble(moonPhaseText);
                                feel_like = Double.parseDouble(data.getHourly().get(i).getFeelsLike());
                                precipitationProbability = Double.parseDouble(data.getHourly().get(i).getPop());
                                obFitValue = setObservationalFitDegree();
                                obFitList.add(obFitValue);
                                Log.d("errorcheck", "2");
                            }
                            obFitHourList.addAll(Arrays.asList(obFitHour).subList(24, 24 + obFitApiId));
                        } else if (obFitApiId >= 7) {
                            for (int i = obFitApiPlusId + 24; i < obFitApiPlusId + 31; i++) {
                                cloudVolume = Double.parseDouble(data.getHourly().get(i).getClouds());
                                moonAge = Double.parseDouble(moonPhaseText);
                                feel_like = Double.parseDouble(data.getHourly().get(i).getFeelsLike());
                                precipitationProbability = Double.parseDouble(data.getHourly().get(i).getPop());
                                obFitValue = setObservationalFitDegree();
                                obFitList.add(obFitValue);
                                Log.d("errorcheck", "3");
                            }
                            obFitHourList.addAll(Arrays.asList(obFitHour).subList(24, 31));
                        }
                        setObFitRecycler();
                    } else {
                        obFit.setVisibility(View.GONE);
                        bestObTimeTv.setText("????????????");
                    }

                    detailWeather.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public void onClick(View v) {
                            if (cntClick == 0) {
                                if (maxTempTv.getVisibility() == View.VISIBLE) {
                                    setTempVisibility(0);
                                }

                                if (maxLightPolTv.getVisibility() == View.VISIBLE) {
                                    setLightPolVisibility(0);
                                }

                                cloudTv.setText(cloudState);
                                tempTv.setText(tempState);
                                moonPhaseTv.setText(moonPhaseState);
                                Log.d("dustNoInfo1", String.valueOf(dustNoInfo));

                                if (dustNoCheck == 1) {
                                    fineDustTv.setText("????????????");
                                } else {
                                    fineDustTv.setText(fineDustState);
                                }

                                windTv.setText(windState);
                                humidityTv.setText(humidityState);
                                precipitationTv.setText(precipState);
                                lightPolTv.setText(lightPolState);
                                detailMent.setText("???????????? ?????? ????????? ??????????????????!");

                                cntClick = 1;
                            } else {
                                if (todayTime.equals("00")) {
                                    if (selectDate.equals(todayDate) || selectDate.equals(plusDay)) {
                                        tempTv.setText(tempText);
                                        tempTextTv.setText("??????");
                                    } else {
                                        setTempVisibility(1);

                                        minTempTv.setText(tempMinText);
                                        maxTempTv.setText(tempMaxText);
                                        tempTextTv.setText("??????\n(??????/??????)");
                                    }
                                } else {
                                    if (selectDate.equals(todayDate) || selectDate.equals(plusDay) || selectDate.equals(plusTwoDay)) {
                                        tempTv.setText(tempText);
                                    } else {
                                        setTempVisibility(1);

                                        minTempTv.setText(tempMinText);
                                        maxTempTv.setText(tempMaxText);
                                    }
                                }

                                cloudTv.setText(cloudText);
                                moonPhaseTv.setText(moonPhaseText);
                                Log.d("dustNoInfo2", String.valueOf(dustNoInfo));

                                if (dustNoCheck == 1) {
                                    fineDustTv.setText("????????????");
                                } else {
                                    fineDustTv.setText(fineDustSt);
                                }

                                windTv.setText(windText);
                                humidityTv.setText(humidityText);
                                precipitationTv.setText(precipText);

                                setLightPolVisibility(1);

                                maxLightPolTv.setText(maxLightPolText);
                                minLightPolTv.setText(minLightPolText);
                                detailMent.setText("???????????? ?????? ????????? ??????????????????!");


                                cntClick = 0;
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<WtMetModel> call, Throwable t) {
                t.printStackTrace();
                Log.v("My Tag", "responseError= " + call.request().url());
            }
        });
        Log.d("openWeatherApi", "????????????");
    }

    /**
     * TODO unix ?????? ?????? ?????????
     * @param  unixTime - unix ??????
     */
    public void unixChange(String unixTime) {
        //yyyyMMddHH
        unixDate = new java.util.Date(Long.parseLong(unixTime) * 1000L);
        formatDateTime.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        unixToDate = formatDateTime.format(unixDate);

        //yyyyMMdd
        unixDay = new java.util.Date(Long.parseLong(unixTime) * 1000L);
        formatDate2.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        unixToDay = formatDate2.format(unixDay);

        //HH:mm
        unixHourMin = new java.util.Date(Long.parseLong(unixTime) * 1000L);
        formatHourMin.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        unixToHourMin = formatHourMin.format(unixHourMin);

        //HH
        unixHour = new java.util.Date(Long.parseLong(unixTime) * 1000L);
        formatHour.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        unixToHour = formatHour.format(unixHour);
    }

    // ???????????? API ??????
    public void connectFineDustApi() {
        if (dustCheck == 0) {
            if ((todayTime.equals("00") || todayTime.equals("01") || todayTime.equals("02") || todayTime.equals("03") || todayTime.equals("04"))) {
                try {
                    addDate = todayDate;
                    cal.add(Calendar.DATE, -1);

                    addDate = formatDate2.format(cal.getTime());

                    cal.add(Calendar.DATE, 1);

                    changeDate = formatDate2.parse(addDate);
                    todayDateDash = formatDateDash.format(changeDate);
                    Log.d("todayDateDash", todayDateDash);
                    Log.d("dustApi", "?????? ????????? api ??????");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dustCheck = 1;
            } else {
                try {
                    addDate = todayDate;
                    changeDate = formatDate2.parse(addDate);
                    todayDateDash = formatDateDash.format(changeDate);
                    Log.d("todayDateDash", todayDateDash);
                    Log.d("dustApi", "?????? ????????? api ??????");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dustCheck = 1;
        }

        try {
            changeSelectDate = formatDate2.parse(selectDate);
            selectDateDash = formatDateDash.format(changeSelectDate);
            Log.d("selectDateDash", selectDateDash);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("dustCheck", String.valueOf(dustCheck));
        Call<WtFineDustModel> getFineDustInstance = WtFineDustRetrofit.wtFineDustInterface()
                .getFineDustData(WT_FINE_DUST_API_KEY, "JSON", todayDateDash, "PM10");
        getFineDustInstance.enqueue(new Callback<WtFineDustModel>() {
            @Override
            public void onResponse(Call<WtFineDustModel> call, Response<WtFineDustModel> response) {
                if (response.isSuccessful()) {
                    WtFineDustModel data = response.body();
                    Log.d("FineDust", "response= " + response.raw().request().url().url());

                    dustTotalCnt = data.getResponse().getBody().getTotalCount();
                    for (int i = 0; i < dustTotalCnt; i++) {
                        if (data.getResponse().getBody().getItems().get(i).getInformCode().equals("PM10") &&
                                selectDateDash.equals(data.getResponse().getBody().getItems().get(i).getInformData())) {
                            listDust = data.getResponse().getBody().getItems().get(i).getInformGrade();
                            Log.d("dustDateTime", data.getResponse().getBody().getItems().get(i).getDataTime());
                            Log.d("dustInformData", data.getResponse().getBody().getItems().get(i).getInformData());
                            dustNoInfo = 1;
                            dustNoCheck = 0;
                            Log.d("dustNoInfo3", String.valueOf(dustNoInfo));
                            break;
                        }
                    }

                }

                arrayComma = listDust.split(",");

                for (String s : arrayComma) {
                    Log.d("test", s);
                }

                for (int i = 0; i < 19; i++) {
                    index = arrayComma[i].indexOf(":");
                    state = arrayComma[i].substring(index + 2);
                    dustStateArray[i] = state;
                }

                if (cityName.equals("??????")) {
                    fineDustText = dustStateArray[0];
                    Log.d("dust", "0");
                } else if (cityName.equals("??????")) {
                    fineDustText = dustStateArray[1];
                    Log.d("dust", "1");
                } else if (cityName.equals("??????")) {
                    fineDustText = dustStateArray[2];
                    Log.d("dust", "2");
                } else if (cityName.equals("??????????????")) {
                    //??????
                    if (provName.equals("?????????") || provName.equals("??????") || provName.equals("??????") || provName.equals("??????") || provName.equals("??????")) {
                        fineDustText = dustStateArray[4];
                        Log.d("dust", "4");
                    }
                    //??????
                    else {
                        fineDustText = dustStateArray[3];
                        Log.d("dust", "3");
                    }
                } else if (cityName.equals("??????")) {
                    fineDustText = dustStateArray[5];
                    Log.d("dust", "5");
                } else if (cityName.equals("??????????????")) {
                    //??????
                    if (provName.equals("??????") || provName.equals("??????") || provName.equals("??????") || provName.equals("??????") || provName.equals("??????") || provName.equals("?????????") || provName.equals("?????????") || provName.equals("?????????")) {
                        fineDustText = dustStateArray[8];
                        Log.d("dust", "8");
                    }
                    //??????
                    else {
                        fineDustText = dustStateArray[6];
                        Log.d("dust", "6");
                    }
                } else if (cityName.equals("??????????????")) {
                    //??????
                    if (provName.equals("??????") || provName.equals("??????") || provName.equals("??????") || provName.equals("?????????") || provName.equals("??????")) {
                        fineDustText = dustStateArray[7];
                        Log.d("dust", "7");
                    }
                    //??????
                    else {
                        fineDustText = dustStateArray[9];
                        Log.d("dust", "9");
                    }
                } else if (cityName.equals("??????")) {
                    fineDustText = dustStateArray[11];
                    Log.d("dust", "11");
                } else if (cityName.equals("??????")) {
                    fineDustText = dustStateArray[10];
                    Log.d("dust", "10");

                } else if (cityName.equals("??????????????")) {
                    //??????
                    if (provName.equals("?????????") || provName.equals("??????") || provName.equals("??????") || provName.equals("?????????") || provName.equals("??????")) {
                        fineDustText = dustStateArray[13];
                        Log.d("dust", "13");
                    }
                    //??????
                    else {
                        fineDustText = dustStateArray[12];
                        Log.d("dust", "12");
                    }
                } else if (cityName.equals("??????")) {
                    fineDustText = dustStateArray[18];
                    Log.d("dust", "18");
                } else if (cityName.equals("??????")) {
                    //?????? ??????
                    if (provName.equals("?????????") || provName.equals("?????????") || provName.equals("?????????") || provName.equals("????????????") || provName.equals("????????????")
                            || provName.equals("?????????") || provName.equals("?????????") || provName.equals("????????????") || provName.equals("?????????") || provName.equals("?????????")) {
                        fineDustText = dustStateArray[17];
                        Log.d("dust", "17");
                    }
                    //?????? ??????
                    else {
                        fineDustText = dustStateArray[16];
                        Log.d("dust", "16");
                    }
                } else if (cityName.equals("??????")) {
                    //??????
                    if (provName.equals("?????????") || provName.equals("?????????") || provName.equals("?????????") || provName.equals("?????????") || provName.equals("?????????")
                            || provName.equals("?????????") || provName.equals("?????????")) {
                        fineDustText = dustStateArray[14];
                        Log.d("dust", "14");
                    }
                    //??????
                    else {
                        fineDustText = dustStateArray[15];
                        Log.d("dust", "15");
                    }
                } else {
                    Log.d("dustError", "else??? ??????");
                }

                fineDustSt = setFineDustValue(fineDustText);
                fineDustTv.setText(fineDustSt);
                fineDust = fineDustText;
                if (dustNoInfo == 0) {
                    fineDust = "??????";
                    dustNoCheck = 1;
                    fineDustTv.setText("????????????");
                    Log.d("dustNoInfo4", String.valueOf(dustNoInfo));
                    Log.d("obFitCheckWhenDustNo", fineDust);
                }
                dustNoInfo = 0;
                Log.d("dustNoInfo7", String.valueOf(dustNoInfo));
            }

            @Override
            public void onFailure(Call<WtFineDustModel> call, Throwable t) {
                t.printStackTrace();
                Log.v("FineDust", "responseError= " + call.request().url());
            }
        });
    }

    //?????? ??? ???????????? API ??????
    public void connectNextFineDustApi() {
        if (dustCheck == 0) {
            if ((todayTime.equals("00") || todayTime.equals("01") || todayTime.equals("02") || todayTime.equals("03") || todayTime.equals("04"))) {
                try {
                    addDate = todayDate;
                    cal.add(Calendar.DATE, -1);

                    addDate = formatDate2.format(cal.getTime());

                    cal.add(Calendar.DATE, 1);

                    changeDate = formatDate2.parse(addDate);
                    todayDateDash = formatDateDash.format(changeDate);
                    Log.d("todayDateDash", todayDateDash);
                    Log.d("dustApi", "?????? ????????? api ??????");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dustCheck = 1;
            } else {
                try {
                    addDate = todayDate;
                    changeDate = formatDate2.parse(addDate);
                    todayDateDash = formatDateDash.format(changeDate);
                    Log.d("todayDateDash", todayDateDash);
                    Log.d("dustApi", "?????? ????????? api ??????");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dustCheck = 1;
        }

        try {
            dustNextDate = formatDate2.parse(plusDay);
            nextDateDash = formatDateDash.format(dustNextDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("dustCheck", String.valueOf(dustCheck));
        Call<WtFineDustModel> getFineDustInstance = WtFineDustRetrofit.wtFineDustInterface()
                .getFineDustData(WT_FINE_DUST_API_KEY, "JSON", todayDateDash, "PM10");
        getFineDustInstance.enqueue(new Callback<WtFineDustModel>() {
            @Override
            public void onResponse(Call<WtFineDustModel> call, Response<WtFineDustModel> response) {
                if (response.isSuccessful()) {
                    WtFineDustModel data = response.body();
                    Log.d("FineDust", "response= " + response.raw().request().url().url());

                    dustTotalCnt = data.getResponse().getBody().getTotalCount();
                    for (int i = 0; i < dustTotalCnt; i++) {
                        if (data.getResponse().getBody().getItems().get(i).getInformCode().equals("PM10") &&
                                nextDateDash.equals(data.getResponse().getBody().getItems().get(i).getInformData())) {
                            listDustNext = data.getResponse().getBody().getItems().get(i).getInformGrade();
                            Log.d("dustDateTime", data.getResponse().getBody().getItems().get(i).getDataTime());
                            Log.d("dustInformData", data.getResponse().getBody().getItems().get(i).getInformData());
                            break;
                        }
                    }
                }

                arrayCommaNext = listDustNext.split(",");

                for (String s : arrayCommaNext) {
                    Log.d("test", s);
                }

                for (int i = 0; i < 19; i++) {
                    index = arrayCommaNext[i].indexOf(":");
                    state = arrayCommaNext[i].substring(index + 2);
                    dustStateArray2[i] = state;
                }

                if (cityName.equals("??????")) {
                    fineDustText2 = dustStateArray2[0];
                    Log.d("dust", "0");
                } else if (cityName.equals("??????")) {
                    fineDustText2 = dustStateArray2[1];
                    Log.d("dust", "1");
                } else if (cityName.equals("??????")) {
                    fineDustText2 = dustStateArray2[2];
                    Log.d("dust", "2");
                } else if (cityName.equals("??????????????")) {
                    //??????
                    if (provName.equals("?????????") || provName.equals("??????") || provName.equals("??????") || provName.equals("??????") || provName.equals("??????")) {
                        fineDustText2 = dustStateArray2[4];
                        Log.d("dust", "4");
                    }
                    //??????
                    else {
                        fineDustText2 = dustStateArray2[3];
                        Log.d("dust", "3");
                    }
                } else if (cityName.equals("??????")) {
                    fineDustText2 = dustStateArray2[5];
                    Log.d("dust", "5");
                } else if (cityName.equals("??????????????")) {
                    //??????
                    if (provName.equals("??????") || provName.equals("??????") || provName.equals("??????") || provName.equals("??????") || provName.equals("??????") || provName.equals("?????????") || provName.equals("?????????") || provName.equals("?????????")) {
                        fineDustText2 = dustStateArray2[8];
                        Log.d("dust", "8");
                    }
                    //??????
                    else {
                        fineDustText2 = dustStateArray2[6];
                        Log.d("dust", "6");
                    }
                } else if (cityName.equals("??????????????")) {
                    //??????
                    if (provName.equals("??????") || provName.equals("??????") || provName.equals("??????") || provName.equals("?????????") || provName.equals("??????")) {
                        fineDustText2 = dustStateArray2[7];
                        Log.d("dust", "7");
                    }
                    //??????
                    else {
                        fineDustText2 = dustStateArray2[9];
                        Log.d("dust", "9");
                    }
                } else if (cityName.equals("??????")) {
                    fineDustText2 = dustStateArray2[11];
                    Log.d("dust", "11");
                } else if (cityName.equals("??????")) {
                    //??????
                    fineDustText2 = dustStateArray2[10];
                    Log.d("dust", "10");
                } else if (cityName.equals("??????????????")) {
                    //??????
                    if (provName.equals("?????????") || provName.equals("??????") || provName.equals("??????") || provName.equals("?????????") || provName.equals("??????")) {
                        fineDustText2 = dustStateArray2[13];
                        Log.d("dust", "13");
                    }
                    //??????
                    else {
                        fineDustText2 = dustStateArray2[12];
                        Log.d("dust", "12");
                    }
                } else if (cityName.equals("??????")) {
                    fineDustText2 = dustStateArray2[18];
                    Log.d("dust", "18");
                } else if (cityName.equals("??????")) {
                    //?????? ??????
                    if (provName.equals("?????????") || provName.equals("?????????") || provName.equals("?????????") || provName.equals("????????????") || provName.equals("????????????")
                            || provName.equals("?????????") || provName.equals("?????????") || provName.equals("????????????") || provName.equals("?????????") || provName.equals("?????????")) {
                        fineDustText2 = dustStateArray2[17];
                        Log.d("dust", "17");
                    }
                    //?????? ??????
                    else {
                        fineDustText2 = dustStateArray2[16];
                        Log.d("dust", "16");
                    }
                } else if (cityName.equals("??????")) {
                    //??????
                    if (provName.equals("?????????") || provName.equals("?????????") || provName.equals("?????????") || provName.equals("?????????") || provName.equals("?????????")
                            || provName.equals("?????????") || provName.equals("?????????")) {
                        fineDustText2 = dustStateArray2[14];
                        Log.d("dust", "14");
                    }
                    //??????
                    else {
                        fineDustText2 = dustStateArray2[15];
                        Log.d("dust", "15");
                    }
                } else {
                    Log.d("dustError", "else??? ??????");
                }

                fineDustSt = setFineDustValue(fineDustText2);
                fineDust = fineDustText2;
                Log.d("obFitCheckWhenDustNo", fineDust);
            }

            @Override
            public void onFailure(Call<WtFineDustModel> call, Throwable t) {
                t.printStackTrace();
                Log.v("FineDust", "responseError= " + call.request().url());
            }
        });
    }

    /**
     * TODO ??????????????? ?????? ?????????
     * @return ??????????????? ?????????
     * @throws
     */
    public double setObservationalFitDegree() {
        cloudVolumeValue = Math.round(100 * (-(1 / (-(0.25) * (cloudVolume / 100 - 2.7)) - 1.48148)) * 100) / 100.0;

        if (moonAge <= 0.5) {
            moonAgeValue = -Math.round(((8 * Math.pow(moonAge, 3.46)) / 0.727 * 100) * 100) / 100.0;
        } else if (moonAge > 0.5 && moonAge <= 0.5609) {
            moonAgeValue = -Math.round(((-75 * Math.pow(moonAge - 0.5, 2) + 0.727) / 0.727 * 100) * 100) / 100.0;
        } else if (moonAge > 0.5609) {
            moonAgeValue = -Math.round(((1 / (5.6 * Math.pow(moonAge + 0.3493, 10)) - 0.0089) / 0.727 * 100) * 100) / 100.0;
        }
        if (feel_like < 18) {
            feel_likeValue = Math.round(-0.008 * Math.pow((feel_like - 18), 2) * 100) / 100.0;
        } else {
            feel_likeValue = Math.round(-0.09 * Math.pow((feel_like - 18), 2) * 100) / 100.0;
        }

        switch (fineDust) {
            case "??????":
                fineDustValue = 0;
                break;
            case "??????":
                fineDustValue = -5;
                break;
            case "??????":
                fineDustValue = -15;
                break;
            case "????????????":
                fineDustValue = -30;
                break;
            case"??????":
                fineDustValue = 0;
                Log.d("fineDust", "???????????? ?????? ??????");
                break;
            default:
                fineDustValue = 0;
                break;
        }
        precipitationProbabilityValue = Math.round(100 * (-(1 / (-(1.2) * (precipitationProbability / 100 - 1.5)) - 0.55556)) * 100) / 100.0;

        if (lightPollution < 28.928) {
            lightPollutionValue = Math.round(-(1 / (-(0.001) * (lightPollution - 48)) - 20.833) * 100) / 100.0;
        } else if (lightPollution >= 28.928 && lightPollution < 77.53) {
            lightPollutionValue = Math.round(-(1 / (-(0.0001) * (lightPollution + 52)) + 155) * 100) / 100.0;
        } else if (lightPollution >= 77.53 && lightPollution < 88.674) {
            lightPollutionValue = Math.round(-(1 / (-(0.001) * (lightPollution - 110)) + 47) * 100) / 100.0;
        } else {
            lightPollutionValue = Math.round(-(1 / (-(0.01) * (lightPollution - 71)) + 100) * 100) / 100.0;
        }
        if (cloudVolumeValue < feel_likeValue && cloudVolumeValue < moonAgeValue && cloudVolumeValue < fineDustValue && cloudVolumeValue < precipitationProbabilityValue && cloudVolumeValue < lightPollutionValue) {
            biggestValue = cloudVolumeValue;
        } else if (feel_likeValue < cloudVolumeValue && feel_likeValue < moonAgeValue && feel_likeValue < fineDustValue && feel_likeValue < precipitationProbabilityValue && feel_likeValue < lightPollutionValue) {
            biggestValue = feel_likeValue;
        } else if (moonAgeValue < cloudVolumeValue && moonAgeValue < feel_likeValue && moonAgeValue < fineDustValue && moonAgeValue < precipitationProbabilityValue && moonAgeValue < lightPollutionValue) {
            biggestValue = moonAgeValue;
        } else if (fineDustValue < cloudVolumeValue && fineDustValue < feel_likeValue && fineDustValue < moonAgeValue && fineDustValue < precipitationProbabilityValue && fineDustValue < lightPollutionValue) {
            biggestValue = fineDustValue;
        } else if (precipitationProbabilityValue < cloudVolumeValue && precipitationProbabilityValue < feel_likeValue && precipitationProbabilityValue < moonAgeValue && precipitationProbabilityValue < fineDustValue && precipitationProbabilityValue < lightPollutionValue) {
            biggestValue = precipitationProbabilityValue;
        } else {
            biggestValue = lightPollutionValue;
        }
        averageValue = (cloudVolumeValue + feel_likeValue + moonAgeValue + fineDustValue + precipitationProbabilityValue + lightPollutionValue - biggestValue) / 6;
        if (100 + (biggestValue + averageValue * 0.3) > 0) {
            observationalFitDegree = 100 + (biggestValue + averageValue * 0.3);
        } else {
            observationalFitDegree = 0;
        }

        obFitFinal = Math.round(observationalFitDegree * 100) / 100.0;

        if (obFitFinal < 0) {
            return 0;
        } else {
            return obFitFinal;
        }
    }

    // ???,??? Spinner ?????? ?????????
    public void onSetAreaSpinner() {
        final Spinner citySpinner = (Spinner) findViewById(R.id.wt_citySpinner);
        final Spinner provSpinner = (Spinner) findViewById(R.id.wt_provinceSpinner);

        cityAdSpin = ArrayAdapter.createFromResource(this, R.array.wt_cityList, android.R.layout.simple_spinner_dropdown_item);
        cityAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdSpin);

        //default
//        choice_do = "??????";
//        choice_se = "?????????";
//
//        cityName = choice_do;
//        provName = choice_se;

//        latitude = 37.5006;
//        longitude = 127.0508;

        citySpinner.setSelection(getIndex(citySpinner, cityName));
        Log.d("nowCityName", cityName);

        if (nowCity == null || provName == null) {
            nowCity = "??????";
            provName = "?????????";
            Toast.makeText(WeatherActivity.this, "?????? ????????? ???????????? ????????????.", Toast.LENGTH_LONG).show();

        }

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cityAdSpin.getItem(position).equals("??????")) {
                    cityName = "??????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_Seoul, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "0");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (cityAdSpin.getItem(position).equals("??????")) {
                    cityName = "??????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_Gyeonggi, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "1");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (cityAdSpin.getItem(position).equals("??????")) {
                    cityName = "??????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_Incheon, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "2");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (cityAdSpin.getItem(position).equals("??????")) {
                    cityName = "??????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_Gangwon, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "3");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }

                    });
                    Log.d("cityName", cityName);
                    Log.d("provName", provName);
                } else if (cityAdSpin.getItem(position).equals("??????")) {
                    cityName = "??????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_Chungbuk, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "4");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (cityAdSpin.getItem(position).equals("??????")) {
                    cityName = "??????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_Chungnam, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "5");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (cityAdSpin.getItem(position).equals("??????????????")) {
                    cityName = "??????????????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_DaejeonSejong, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "6");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (cityAdSpin.getItem(position).equals("??????????????")) {
                    cityName = "??????????????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_GwangjuJeonbuk, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "7");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (cityAdSpin.getItem(position).equals("??????")) {
                    cityName = "??????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_Jeonnam, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "8");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (cityAdSpin.getItem(position).equals("??????????????")) {
                    cityName = "??????????????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_DaeguGyeongbuk, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "9");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (cityAdSpin.getItem(position).equals("??????")) {
                    cityName = "??????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_Gyeongnam, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "10");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (cityAdSpin.getItem(position).equals("??????????????")) {
                    cityName = "??????????????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_BusanUlsan, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "11");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (cityAdSpin.getItem(position).equals("??????")) {
                    cityName = "??????";

                    provAdSpin = ArrayAdapter.createFromResource(WeatherActivity.this, R.array.wt_Jeju, android.R.layout.simple_spinner_dropdown_item);
                    provAdSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provSpinner.setAdapter(provAdSpin);
                    provSpinner.setSelection(getIndex(provSpinner, provName));
                    Log.d("nowProvName", provName);

                    provSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = provAdSpin.getItem(position).toString();
                            provName = choice_se;

                            connectWtArea();
                            Log.d("connectWtAreaCheck", "12");
                            connectFineDustApi();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // ??????????????? ??????, ??????, ????????? ????????????
    public void connectWtArea() {
        Log.d("cityName1", cityName);
        Log.d("provName1", provName);

        Call<WtAreaParams> areaInfoCall = RetrofitClient.getApiService().getAreaInfo(cityName, provName);
        areaInfoCall.enqueue(new Callback<WtAreaParams>() {
            @Override
            public void onResponse(Call<WtAreaParams> call, Response<WtAreaParams> response) {
                if (response.isSuccessful()) {
                    WtAreaParams result = response.body();
                    latitude = result.getLatitude();
                    longitude = result.getLongitude();

                    minLightPol = result.getMinLightPol();
                    maxLightPol = result.getMaxLightPol();

                    lightPollution = (minLightPol + maxLightPol) / 2;
                    Log.d("lightPollution", String.valueOf(lightPollution));

                    minLightPolText = minLightPol.toString();
                    maxLightPolText = maxLightPol.toString();

                    minLightPolTv.setText(minLightPolText);
                    maxLightPolTv.setText(maxLightPolText);

                    connectMetApi();
                    Log.d("connectMetApiCheck", "2");
                    Log.d("latitude", String.valueOf(latitude));
                    Log.d("longitude", String.valueOf(longitude));
                } else {
                    Log.d("connectWtArea", "??????????????? ??????, ?????? ???????????? ??????");
                }
            }

            @Override
            public void onFailure(Call<WtAreaParams> call, Throwable t) {
                Log.e("????????????", t.getMessage());
            }
        });
    }

    /**
     * TODO ????????? ???????????? ?????? ??? ?????? ?????????
     * @param  num - ????????? ?????? ???
     */
    public void setMinHour(int num) {
        for (int i = 0; i < num + 1; i++) {
            String remove = list2.remove(0);
            Log.d("remove", remove);
            Log.d("number", String.valueOf(num));
        }
        hourChange = list2.toArray(new String[0]);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(hourChange.length - 1);
        numberPicker.setDisplayedValues(hourChange);

        for (int i = 0; i < num + 1; i++) {
            list2.add(i, hour[i]);
        }
        // hourChange = list2.toArray(new String[0]);
    }

    /**
     * TODO ????????? ???????????? ?????? ??? ?????? ?????????
     * @param  num - ????????? ?????? ???
     */
    public void setLimitHour(int num) {
        hourChange = list2.toArray(new String[0]);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(hourChange.length - num);
        numberPicker.setDisplayedValues(hourChange);
    }

    /**
     * TODO ?????? ?????? ???????????? ???????????? ??????/?????? ?????? ?????? ?????????
     * @param  state - ??????/?????? ????????? ????????? ??????
     */
    public void setTempVisibility(int state) {
        if (state == 0) {
            tempTv.setVisibility(View.VISIBLE);
            maxTempTv.setVisibility(View.GONE);
            minTempTv.setVisibility(View.GONE);
            tempSlashTv.setVisibility(View.GONE);
        } else {
            tempTv.setVisibility(View.GONE);
            maxTempTv.setVisibility(View.VISIBLE);
            minTempTv.setVisibility(View.VISIBLE);
            tempSlashTv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * TODO ?????? ?????? ???????????? ???????????? ????????? ?????? ?????????
     * @param  state - ???????????? ????????? ??????
     */
    public void setLightPolVisibility(int state) {
        if (state == 0) {
            lightPolTv.setVisibility(View.VISIBLE);
            maxLightPolTv.setVisibility(View.GONE);
            minLightPolTv.setVisibility(View.GONE);
            lightSlashTv.setVisibility(View.GONE);
        } else {
            lightPolTv.setVisibility(View.GONE);
            maxLightPolTv.setVisibility(View.VISIBLE);
            minLightPolTv.setVisibility(View.VISIBLE);
            lightSlashTv.setVisibility(View.VISIBLE);
        }
    }

    //?????? id??? ????????? ?????? text ????????????
    public void connectTodayWeather() {
        Call<WtTodayParams> todayInfoCall = com.starrynight.tourapiproject.weatherPage.wtTodayRetrofit.RetrofitClient.getApiService().getTodayWeatherInfo(todayWtIdInt);
        todayInfoCall.enqueue(new Callback<WtTodayParams>() {
            @Override
            public void onResponse(Call<WtTodayParams> call, Response<WtTodayParams> response) {
                if (response.isSuccessful()) {

                    WtTodayParams result = response.body();
                    todayWtName1 = result.getTodayWtName1();
                    todayWtName2 = result.getTodayWtName2();

                    if (todayWtName2 == null) {
                        todayWtName = todayWtName1;
                    } else {
                        todayWtName = todayWtName1 + "\n" + todayWtName2;
                    }

                    todayWeatherTv.setText(todayWtName);
                } else {
                    Log.d("connectWtToday", "id??? ????????? ?????? ???????????? ??????");
                }
            }

            @Override
            public void onFailure(Call<WtTodayParams> call, Throwable t) {
                Log.e("????????????", t.getMessage());
            }
        });
    }

    /**
     * TODO ??? ?????? ????????? ?????? ?????? ?????????
     * @param  obFitValueSelect - ????????? ??? ?????? ????????? ???
     */
    public void setObFitComment(Double obFitValueSelect) {
        if (obFitValueSelect < 40) {
            commentTv.setText("?????? ?????? ????????????");
            wtWeather.setImageResource(R.drawable.wt__very_bad);
        } else if (obFitValueSelect < 60) {
            commentTv.setText("??? ?????? ?????? ????????????");
            wtWeather.setImageResource(R.drawable.wt__bad);
        } else if (obFitValueSelect < 70) {
            commentTv.setText("??? ?????? ????????? ????????????");
            wtWeather.setImageResource(R.drawable.wt__average);
        } else if (obFitValueSelect < 85) {
            commentTv.setText("??? ?????? ?????? ????????????!");
            wtWeather.setImageResource(R.drawable.wt__good);
        } else {
            commentTv.setText("??? ?????? ????????? ????????????!");
            wtWeather.setImageResource(R.drawable.wt__very_good);
        }
    }

    /**
     * TODO ?????? ?????? ?????? ?????? ?????? ?????????
     * @param  cloud - ?????????
     * @param  temp - ??????
     * @param  humidity - ??????
     * @param  moonAge - ??????
     * @param  wind - ??????
     * @param  precip - ?????????
     * @param  lightPol - ?????????
     * @param  Dust - ????????????
     */
    public void setDetailState(Double cloud, Double temp, Double humidity, Double moonAge, Double wind, Double precip, Double lightPol, String Dust) {
        if (cloud < 11) {
            cloudState = "????????????";
        } else if (cloud < 21) {
            cloudState = "??????";
        } else if (cloud < 41) {
            cloudState = "??????";
        } else if (cloud < 61) {
            cloudState = "??????";
        } else {
            cloudState = "????????????";
        }

        if ((temp <= -1) || (temp >= 31)) {
            tempState = "????????????";
        } else if ((temp >= 0 && temp <= 9) || (temp >= 29 && temp <= 30)) {
            tempState = "??????";
        } else if ((temp >= 10) && (temp <= 13) || (temp >= 26 && temp <= 28)) {
            tempState = "??????";
        } else if ((temp >= 14 && temp <= 17) || (temp >= 24 && temp <= 25)) {
            tempState = "??????";
        } else {
            tempState = "????????????";
        }

        if ((humidity >= 0 && humidity <= 14) || (humidity >= 85 && humidity <= 100)) {
            humidityState = "????????????";
        } else if ((humidity >= 20 && humidity <= 29) || (humidity >= 72 && humidity <= 84)) {
            humidityState = "??????";
        } else if ((humidity >= 30) && (humidity <= 38) || (humidity >= 63 && humidity <= 71)) {
            humidityState = "??????";
        } else if ((humidity >= 37 && humidity <= 42) || (humidity >= 58 && humidity <= 62)) {
            humidityState = "??????";
        } else {
            humidityState = "????????????";
        }

        if ((moonAge >= 0.36 && moonAge <= 0.62)) {
            moonPhaseState = "????????????";
        } else if ((moonAge >= 0.22 && moonAge <= 0.35) || (moonAge >= 0.63 && moonAge <= 0.74)) {
            moonPhaseState = "??????";
        } else if ((moonAge >= 0.14) && (moonAge <= 0.21) || (moonAge >= 0.75 && moonAge <= 0.84)) {
            moonPhaseState = "??????";
        } else if ((moonAge >= 0.07 && moonAge <= 0.13) || (moonAge >= 0.85 && moonAge <= 0.93)) {
            moonPhaseState = "??????";
        } else {
            moonPhaseState = "????????????";
        }

        if (wind <= 2.5) {
            windState = "????????????";
        } else if (wind <= 4.5) {
            windState = "??????";
        } else if (wind <= 6.5) {
            windState = "??????";
        } else if (wind <= 10) {
            windState = "??????";
        } else {
            windState = "????????????";
        }

        if (precip < 16) {
            precipState = "????????????";
        } else if (precip < 26) {
            precipState = "??????";
        } else if (precip < 41) {
            precipState = "??????";
        } else if (precip < 61) {
            precipState = "??????";
        } else {
            precipState = "????????????";
        }

        if (lightPol <= 1) {
            lightPolState = "????????????";
        } else if (lightPol <= 15) {
            lightPolState = "??????";
        } else if (lightPol <= 45) {
            lightPolState = "??????";
        } else if (lightPol <= 80) {
            lightPolState = "??????";
        } else {
            lightPolState = "????????????";
        }

        if (Dust.equals("151???/??? ??????")) {
            fineDustState = "????????????";
        } else if (Dust.equals("31~80???/???")) {
            fineDustState = "??????";
        } else if (Dust.equals("81~150???/???")) {
            fineDustState = "??????";
        } else if(Dust.equals("??????")){
            fineDustState = "??????";
            fineDustSt="?????? ??????";
            Log.d("fineDustState","???????????? ?????? ?????? ??????");
        }
        else {
            fineDustState = "??????";
        }
    }

    /**
     * TODO ???????????? ????????? ???????????? ????????? ?????? ?????????
     * @param  fineDustApiValue - ???????????? ??????
     * @return fineDust ?????? ???
     */
    public String setFineDustValue(String fineDustApiValue) {
        String dust;
        if (fineDustApiValue.equals("??????")) {
            dust = "0~30???/???";
        } else if (fineDustApiValue.equals("??????")) {
            dust= "31~80???/???";
        } else if (fineDustApiValue.equals("??????")) {
            dust = "81~150???/???";
        } else {
            dust = "151???/??? ??????";
        }
        return dust;
    }

    /*
     * ActivityCompat.requestPermissions??? ????????? ????????? ????????? ????????? ???????????? ??????????????????.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // ?????? ????????? PERMISSIONS_REQUEST_CODE ??????, ????????? ????????? ???????????? ??????????????????

            boolean check_result = true;

            Log.d("nowCityProv7", nowCity + " " + nowProvince);
            // ?????? ???????????? ??????????????? ???????????????.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    Log.d("nowCityProv8 ", nowCity + " " + nowProvince);
                    break;
                }
            }

            if (check_result) {
                //?????? ?????? ??????
                gpsTracker = new GpsTracker(WeatherActivity.this);

                nowLatitude = gpsTracker.getLatitude();
                nowLongitude = gpsTracker.getLongitude();

                nowLocation = getCurrentAddress(nowLatitude, nowLongitude);

                Log.d("nowLocationResult", nowLocation);
                Log.d("nowCityProv6", nowCity + " " + nowProvince);

                setCityName();

                setTextView();
                onClickBackBtn();
                onClickCloudInfo();
                onClickHelpBtn();

                //setMyLocation();

                onSetDatePicker();
                onSetTimePicker();

                //?????? ?????? Spinner
                onSetAreaSpinner();

                selectDateTime = selectDate + selectTime;
            } else {
                // ????????? ???????????? ????????? ?????? ????????? ??? ?????? ????????? ??????????????? ?????? ???????????????.2 ?????? ????????? ????????????.
                // ?????? ?????? ??????
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    nowCity = "??????";
                    nowProvince = "?????????";

                    Log.d("nowCityProv2", nowCity + " " + nowProvince);

                } else {
                    Toast.makeText(WeatherActivity.this, "???????????? ?????????????????????. ??????(??? ??????)?????? ???????????? ???????????? ?????????. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    //????????? ????????? ??????
    void checkRunTimePermission() {
        // 1. ?????? ???????????? ????????? ????????? ???????????????.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(WeatherActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(WeatherActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        Log.d("LocationAllow", "1");
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. ?????? ???????????? ????????? ?????????
            // ( ??????????????? 6.0 ?????? ????????? ????????? ???????????? ???????????? ????????? ?????? ????????? ?????? ???????????????.)
            Log.d("LocationAllow", "0");

            // 3.  ?????? ?????? ????????? ??? ??????


        } else {  //2. ????????? ????????? ????????? ?????? ????????? ????????? ????????? ???????????????. 2?????? ??????(3-1, 4-1)??? ????????????.

            // 3-1. ???????????? ????????? ????????? ??? ?????? ?????? ????????????
            if (ActivityCompat.shouldShowRequestPermissionRationale(WeatherActivity.this, REQUIRED_PERMISSIONS[0])) {
                Log.d("LocationAllow", "3");
                // 3-2. ????????? ???????????? ?????? ?????????????????? ???????????? ????????? ????????? ???????????? ????????? ????????????.
                Toast.makeText(WeatherActivity.this, "??? ?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                // 3-3. ??????????????? ????????? ????????? ?????????. ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(WeatherActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                Log.d("LocationAllow", "4");
                // 4-1. ???????????? ????????? ????????? ??? ?????? ?????? ???????????? ????????? ????????? ?????? ?????????.
                // ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(WeatherActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    /**
     * TODO ?????? ??????, ????????? ?????? ?????? ?????????
     * @param  latitude - ??????
     * @param  longitude - ??????
     * @return ?????? ?????????
     */
    public String getCurrentAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
            );
        } catch (IOException ioException) {
            Toast.makeText(this, "???????????? ????????? ????????????", Toast.LENGTH_LONG).show();
            return "???????????? ????????? ????????????";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "????????? GPS ??????", Toast.LENGTH_LONG).show();
            return "????????? GPS ??????";
        }

        if (addresses == null || addresses.size() == 00) {
            return "";
        }

        Address address = addresses.get(0);
        nowCity = address.getAdminArea();
        nowProvince = address.getLocality();

        if (nowProvince == null) {
            nowProvince = address.getSubLocality();
        }
        Log.d("nowCityProv3", nowCity + " " + nowProvince);

        setCityName();

        return address.getAddressLine(0).toString() + "\n";
    }

    //??????????????? GPS ???????????? ?????? ????????????
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
        builder.setTitle("?????? ????????? ????????????");
        builder.setMessage("?????? ???????????? ???????????? ?????? ???????????? ???????????????.\n"
                + "?????? ????????? ?????????????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);

                gpsTracker = new GpsTracker(WeatherActivity.this);

                nowLatitude = gpsTracker.getLatitude();
                nowLongitude = gpsTracker.getLongitude();

                nowLocation = getCurrentAddress(nowLatitude, nowLongitude);
                Log.d("nowLocationResult", nowLocation);
                Log.d("nowCityProv4", nowCity + " " + nowProvince);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                nowCity = "??????";
                nowProvince = "?????????";
                Log.d("nowCityProv5", nowCity + " " + nowProvince);
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //???????????? GPS ?????? ???????????? ??????
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        gpsTracker = new GpsTracker(WeatherActivity.this);

                        nowLatitude = gpsTracker.getLatitude();
                        nowLongitude = gpsTracker.getLongitude();

                        nowLocation = getCurrentAddress(nowLatitude, nowLongitude);

                        Log.d("nowLocationResult", nowLocation);
                        Log.d("nowCityProv6", nowCity + " " + nowProvince);

                        setCityName();

                        setTextView();
                        onClickBackBtn();
                        onClickCloudInfo();
                        onClickHelpBtn();


                        onSetDatePicker();
                        onSetTimePicker();

                        //?????? ?????? Spinner
                        onSetAreaSpinner();

                        selectDateTime = selectDate + selectTime;
                        Log.d("@@@", "onActivityResult : GPS ????????? ?????????");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // ??? ?????? ?????? ?????????
    public void setCityName() {
        if (nowCity.equals("???????????????")) {
            nowCity = "??????";
        } else if (nowCity.equals("?????????")) {
            nowCity = "??????";
        } else if (nowCity.equals("???????????????")) {
            nowCity = "??????";
        } else if (nowCity.equals("?????????")) {
            nowCity = "??????";
        } else if (nowCity.equals("????????????")) {
            nowCity = "??????";
        } else if (nowCity.equals("????????????")) {
            nowCity = "??????";
        } else if (nowCity.equals("???????????????") || nowCity.equals("?????????????????????")) {
            nowCity = "??????????????";
        } else if (nowCity.equals("???????????????") || nowCity.equals("????????????")) {
            nowCity = "??????????????";
        } else if (nowCity.equals("????????????")) {
            nowCity = "??????";
        } else if (nowCity.equals("???????????????") || nowCity.equals("????????????")) {
            nowCity = "??????????????";
        } else if (nowCity.equals("????????????")) {
            nowCity = "??????";
        } else if (nowCity.equals("???????????????") || nowCity.equals("???????????????")) {
            nowCity = "??????????????";
        } else if (nowCity.equals("?????????????????????")) {
            nowCity = "??????";
        }

        cityName = nowCity;
        provName = nowProvince;

        Log.d("nowCityProv8", cityName + provName);
    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    private class LoadingAsyncTask extends AsyncTask<String, Long, Boolean> {
        private Context mContext = null;
        private Long mtime;

        public LoadingAsyncTask(Context context, long time) {
            mContext = WeatherActivity.this;
            mtime = time;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                Thread.sleep(mtime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return (true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
        }
    }
}