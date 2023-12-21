package com.starrynight.tourapiproject.observationPage;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
* @className : RecyclerDecoration.java
* @description : 해쉬태그 오른쪽 마진 지정용
* @modification : gyul chyoung (2022-08-30) 주석추가
* @author : 2022-08-30
* @date : gyul chyoung
* @version : 1.0
     ====개정이력(Modification Information)====
  수정일        수정자        수정내용    -----------------------------------------
   gyul chyoung       2022-08-30       주석추가
 */

public class RecyclerDecoration extends RecyclerView.ItemDecoration {

    private final int divHeight;


    public RecyclerDecoration(int divHeight) {
        this.divHeight = divHeight;
    }
    public RecyclerDecoration(int divHeight, Context context) {
        this.divHeight = dpToPx(context, divHeight);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = divHeight;

    }

    private int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
