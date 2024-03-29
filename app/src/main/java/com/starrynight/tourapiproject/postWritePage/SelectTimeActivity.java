package com.starrynight.tourapiproject.postWritePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.starrynight.tourapiproject.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SelectTimeActivity extends AppCompatActivity {
    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    CheckBox timeCheckbox;
    String yearDate = "", time = "";
    boolean day=false;

    private TextView datePicker;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private TextView timePicker;
    private TimePickerDialog.OnTimeSetListener callbackMethod2;
    private LinearLayout timeCheckBoxLayout;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatHour = new SimpleDateFormat("HH");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatMin = new SimpleDateFormat("mm");
    private String todaydate;
    private String todaytime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        //날짜 클릭 이벤트
        datePicker = (TextView) findViewById(R.id.datePicker);
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;
                String month = Integer.toString(monthOfYear);
                String day = Integer.toString(dayOfMonth);

                if (monthOfYear < 10) {
                    month = "0" + Integer.toString(monthOfYear);
                }

                if (dayOfMonth < 10) {
                    day = "0" + Integer.toString(dayOfMonth);
                }
                datePicker.setText(year + ". " + month + ". " + day);
                String realDate = year+"-"+month+"-"+day;
                yearDate = realDate;

            }
        };

        //시간 클릭 이벤트
        timePicker = (TextView) findViewById(R.id.timePicker);
        callbackMethod2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hour = Integer.toString(hourOfDay);
                String min = Integer.toString(minute);
                if (hourOfDay < 10) {
                    hour = "0" + hourOfDay;
                }
                if (minute < 10) {
                    min = "0" + minute;
                }
                String realtime = hour + ":" + min;
                time = realtime;
                timeCheckbox.setChecked(false);
                if (hourOfDay==0){
                    day=true;
                    int plus=Integer.valueOf(hour).intValue()+12;
                    timePicker.setText("오전 "+plus + ":" + min);
                }
                else if(hourOfDay>0&&hourOfDay<12){
                    day=true;
                    timePicker.setText("오전 "+hour + ":" + min);
                }else if (hourOfDay==12){
                    day=false;
                    timePicker.setText("오후 " +hour + ":" + min);
                } else{
                    day=false;
                    int minus=Integer.valueOf(hour).intValue()-12;
                    hour=Integer.toString(minus);
                    timePicker.setText("오후 " +hour + ":" + min);}
            }
        };
        //시간 모르겠음 체크박스 클릭
        timeCheckBoxLayout=findViewById(R.id.timeCheckBoxLayout);
        timeCheckbox = findViewById(R.id.timeCheckBox);
        timeCheckBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeCheckbox.isChecked()){
                    timePicker.setText("모르겠음");
                    time = "00:00";
                }else{
                    timePicker.setText("관측 시간대");
                }
            }
        });

        //뒤로 가기 버튼
        ImageView back = findViewById(R.id.select_time_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //시간 등록 버튼 클릭 이벤트
        TextView selectTime= findViewById(R.id.finish_select_time);
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(yearDate.isEmpty()){
                    Toast.makeText(getApplicationContext(), "날짜를 입력 해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    if(time.isEmpty()){
                        time="00:00";
                    }
                    intent.putExtra("date",yearDate);
                    intent.putExtra("time",time);
                    intent.putExtra("day",day);
                    setResult(4,intent);
                    finish();
                }
            }
        });

    }
    public void onClickDatePicker(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, callbackMethod, mYear, mMonth, mDay);
        datePicker.setBackground(ContextCompat.getDrawable(this, R.drawable.postwrite_time));
        timePicker.setBackground(ContextCompat.getDrawable(this, R.drawable.postwrite_time_non));
        //datePickerDialog.getDatePicker().setCalendarViewShown(false);
        //datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();

    }

    public void onClickTimePicker(View view) {
        todaydate = formatHour.format(c.getTime());
        todaytime = formatMin.format(c.getTime());
        datePicker.setBackground(ContextCompat.getDrawable(this, R.drawable.postwrite_time_non));
        timePicker.setBackground(ContextCompat.getDrawable(this, R.drawable.postwrite_time));
        int todayHour = Integer.parseInt(todaydate);
        int todayTime = Integer.parseInt(todaytime);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Dialog_NoActionBar, callbackMethod2, todayHour, todayTime, false);
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }
}