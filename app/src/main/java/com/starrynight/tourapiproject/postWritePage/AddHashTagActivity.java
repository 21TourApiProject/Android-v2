package com.starrynight.tourapiproject.postWritePage;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.postWritePage.postWriteRetrofit.PostHashTagParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
/**
* @className : AddHashTagActivity
* @description : 게시물 작성 페이지의 해시태그 추가 페이지 입니다.
* @modification : 2022-09-02 (jinhyeok) 주석 수정
* @author : jinhyeok
* @date : 2022-09-02
* @version : 1.0
   ====개정이력(Modification Information)====
  수정일        수정자        수정내용
   -----------------------------------------
   2022-09-02      jinhyeok       주석 수정

 */
public class AddHashTagActivity extends AppCompatActivity {
    List<PostHashTagParams> postHashTagParams = new ArrayList<>();
    List<PostHashTagParams> postHashTagParams2 = new ArrayList<>();
    String[] hashTaglist = new String[26];
    String[] clicked = new String[26];
    ArrayList<String> hashtag = new ArrayList<>();
    HashMap<String, Integer> hashTagMap = new HashMap<String, Integer>();
    String[] hashTagName = {"서울","강원","경기","전남","전북","경남","경북",
            "뚜벅이", "자전거","드라이브", "연인","가족", "친구", "혼자", "반려동물",
            "공기 좋은", "분위기 있는", "깔끔한", "감성적인", "이색적인", "인생샷","전문적인","캠핑","차박","대한민국 구석구석"};
    Button[] buttons = new Button[26];

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hash_tag);


        Intent intent = getIntent();
        postHashTagParams2 = (List<PostHashTagParams>) intent.getSerializableExtra("hashTagParams");
        for (int i = 0; i < 26; i++) {
            clicked[i] = "";
        }
        if (postHashTagParams2 != null) {
            for (int i = 0; i < postHashTagParams2.size(); i++) {
                if (postHashTagParams2.get(i).getHashTagName() != null) {
                    hashtag.add(postHashTagParams2.get(i).getHashTagName());
                }
            }
        }

        if (hashtag != null) {
            int i = 0;
            for (String name : hashTagName) {
                hashTagMap.put(name, i);
                i++;
            }

            for (String h : hashtag) {
                clicked[hashTagMap.get(h)] = h;
            }
        }
        Button ht1 = findViewById(R.id.ht1);
        Button ht2 = findViewById(R.id.ht2);
        Button ht3 = findViewById(R.id.ht3);
        Button ht4 = findViewById(R.id.ht4);
        Button ht5 = findViewById(R.id.ht5);
        Button ht6 = findViewById(R.id.ht6);
        Button ht7 = findViewById(R.id.ht7);
        Button ht8 = findViewById(R.id.ht8);
        Button ht9 = findViewById(R.id.ht9);
        Button ht10 = findViewById(R.id.ht10);
        Button ht11 = findViewById(R.id.ht11);
        Button ht12 = findViewById(R.id.ht12);
        Button ht13 = findViewById(R.id.ht13);
        Button ht14 = findViewById(R.id.ht14);
        Button ht15 = findViewById(R.id.ht15);
        Button ht16 = findViewById(R.id.ht16);
        Button ht17 = findViewById(R.id.ht17);
        Button ht18 = findViewById(R.id.ht18);
        Button ht19 = findViewById(R.id.ht19);
        Button ht20 = findViewById(R.id.ht20);
        Button ht21 = findViewById(R.id.ht21);
        Button ht22 = findViewById(R.id.ht22);
        Button ht23 = findViewById(R.id.ht23);
        Button ht24 = findViewById(R.id.ht24);
        Button ht25 = findViewById(R.id.ht25);
        buttons[0] = ht1;
        buttons[1] = ht2;
        buttons[2] = ht3;
        buttons[3] = ht4;
        buttons[4] = ht5;
        buttons[5] = ht6;
        buttons[6] = ht7;
        buttons[7] = ht8;
        buttons[8] = ht9;
        buttons[9] = ht10;
        buttons[10] = ht11;
        buttons[11] = ht12;
        buttons[12] = ht13;
        buttons[13] = ht14;
        buttons[14] = ht15;
        buttons[15] = ht16;
        buttons[16] = ht17;
        buttons[17] = ht18;
        buttons[18] = ht19;
        buttons[19] = ht20;
        buttons[20] = ht21;
        buttons[21] = ht22;
        buttons[22] = ht23;
        buttons[23] = ht24;
        buttons[24] = ht25;

        //버튼 클릭시 색 변화
        for (int i = 0; i < 26; i++) {
            if (!clicked[i].equals("")) {
                buttons[i].setTag("isClicked");
                buttons[i].setBackground(ContextCompat.getDrawable(this, R.drawable.selectmyhashtag_hashtag));
                buttons[i].setTextColor(ContextCompat.getColor(this, R.color.gray_700));
            }
        }

        Arrays.fill(hashTaglist, "");
        final List<String> finallist = new ArrayList<>(); //메인 해시태그 리스트

        //완료 버튼 클릭 이벤트
        TextView plusHashTag = findViewById(R.id.finish_add_hashTag);
        plusHashTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 26; i++) {
                    if (!clicked[i].isEmpty()) {
                        PostHashTagParams postHashTagParam = new PostHashTagParams();
                        postHashTagParam.setHashTagName(clicked[i]);
                        postHashTagParams.add(postHashTagParam);
                    }
                }
                for (int i = 0; i < hashTaglist.length; i++) {
                    if (!clicked[i].isEmpty()) {
                        if (hashTaglist[i] == "") {
                            hashTaglist[i] = clicked[i];
                        }
                    }
                }
                Collections.addAll(finallist, hashTaglist);
                for (int i = 25; i >= 0; i--) {
                    if (finallist.get(i) == "") {
                        finallist.remove(i);
                    }
                }
                intent.putExtra("postHashTagParams", (Serializable) postHashTagParams);
                intent.putExtra("hashTagList", (Serializable) finallist);
                setResult(3, intent);
                finish();
            }
        });

        //뒤로 가기 버튼
        ImageView back = findViewById(R.id.addHashTag_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //recyclerview 간격
    public static class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

        private final int divRight;

        public RecyclerViewDecoration(int divRight) {

            this.divRight = divRight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = divRight;
        }
    }

    public void ClickEvent(View view) {
        Button button = (Button) view;

        if (button.getTag() == "isClicked") {
            button.setTag("");
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.selectmyhashtag_hashtag_non));
            button.setTextColor(ContextCompat.getColor(this, R.color.gray_300));

            String viewId = view.getResources().getResourceEntryName(view.getId());
            int id = Integer.parseInt(viewId.substring(2));
            clicked[id - 1] = "";
        } else {
            button.setTag("isClicked");
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.selectmyhashtag_hashtag));
            button.setTextColor(ContextCompat.getColor(this, R.color.gray_700));

            String viewId = view.getResources().getResourceEntryName(view.getId());
            int id = Integer.parseInt(viewId.substring(2));
            clicked[id - 1] = button.getText().toString();
        }
    }
}