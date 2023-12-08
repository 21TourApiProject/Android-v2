package com.starrynight.tourapiproject.mainPage.interestArea;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.starrynight.tourapiproject.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomInterestAreaView extends LinearLayout {

    String name;
    TextView interestAreaName;
    CircleImageView interestAreaImage;
    TextView interestAreaObservationalFit;

    public CustomInterestAreaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomInterestAreaView,
                0, 0);

        name = typedArray.getString(R.styleable.CustomInterestAreaView_name);
        String image = typedArray.getString(R.styleable.CustomInterestAreaView_image);
        String observationalFit = typedArray.getString(R.styleable.CustomInterestAreaView_observationalFit);


    }

    private void initView(){
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.custom_view_interest_area, this, false);
        addView(v);

        interestAreaName = v.findViewById(R.id.interestAreaName);
        interestAreaImage = v.findViewById(R.id.interestAreaImage);
        interestAreaObservationalFit = v.findViewById(R.id.interestAreaObservationalFit);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomInterestAreaView);
//        setTypeArray(typedArray);
    }

//    private void setTypeArray(TypedArray typedArray) {
//        int bg_resID = typedArray.getResourceId(R.styleable.LoginButton_bg, R.drawable.login_naver_bg);
//        bg.setBackgroundResource(bg_resID);
//        int symbol_resID = typedArray.getResourceId(R.styleable.LoginButton_symbol, R.drawable.login_naver_symbol);
//        symbol.setImageResource(symbol_resID);
//        int textColor = typedArray.getColor(R.styleable.LoginButton_textColor, 0);
//        text.setTextColor(textColor);
//        String text_string = typedArray.getString(R.styleable.LoginButton_text);
//        text.setText(text_string);
//        typedArray.recycle();
//    }

    public void setInterestAreaName(String name) {
        interestAreaName.setText(name);

//        invalidate();
//        requestLayout();
    }

    public void setInterestAreaImage(String interestAreaImage) {
//        this.interestAreaImage.setText(interestAreaImage);
    }

    public void setInterestAreaObservationalFit(String observationalFit) {
        interestAreaObservationalFit.setText(observationalFit);
    }

}
