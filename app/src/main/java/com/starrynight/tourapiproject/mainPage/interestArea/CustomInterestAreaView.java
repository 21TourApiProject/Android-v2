package com.starrynight.tourapiproject.mainPage.interestArea;

import static androidx.core.content.ContextCompat.getColor;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomInterestAreaView extends LinearLayout {

    TextView interestAreaName;
    CircleImageView interestAreaImage;
    TextView interestAreaObservationalFit;
    ImageView interestAreaDelete;
    Context context;

    public CustomInterestAreaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
        this.context = context;

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomInterestAreaView,
                0, 0);

        String name = typedArray.getString(R.styleable.CustomInterestAreaView_name);
        String image = typedArray.getString(R.styleable.CustomInterestAreaView_image);
        String observationalFit = typedArray.getString(R.styleable.CustomInterestAreaView_observationalFit);
    }

    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick();
    }

    public void setInterestAreaDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.custom_view_interest_area, this, false);
        addView(v);

        interestAreaName = v.findViewById(R.id.interestAreaName);
        interestAreaImage = v.findViewById(R.id.interestAreaImage);
        interestAreaObservationalFit = v.findViewById(R.id.interestAreaObservationalFit);
        interestAreaDelete = v.findViewById(R.id.interestAreaDelete);

        interestAreaDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick();
                }
            }
        });
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomInterestAreaView);
//        setTypeArray(typedArray);
    }

    public void setInterestAreaName(String name) {
        interestAreaName.setText(name);
//        invalidate();
//        requestLayout();
    }

    public void setInterestAreaImage(String image) {
        if (Objects.nonNull(image)) {
            Glide.with(getContext()).load(image).into(interestAreaImage);
        }
    }

    public void setInterestAreaObservationalFit(String observationalFit) {
        if (observationalFit != null) { // db 에 값 있을 때만 표시
            if (observationalFit.equals("-1")) { // api 콜 횟수 초과 시
                interestAreaObservationalFit.setText("로딩중...");
                interestAreaObservationalFit.setTextAppearance(R.style.medium_single_12_gray_300);
            } else {
                interestAreaObservationalFit.setText("~" + observationalFit + "%");
                if (Integer.parseInt(observationalFit) < 60) {
                    interestAreaObservationalFit.setTextColor(getColor(context, R.color.point_red));
                }
            }
        }
    }

    // 삭제 버튼 setVisibility
    public void showInterestAreaDelete(boolean show) {
        if (show) interestAreaDelete.setVisibility(View.VISIBLE);
        else interestAreaDelete.setVisibility(View.GONE);
    }

}
