package com.starrynight.tourapiproject.postWritePage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.postItemPage.PostWriteHashTagItem2;
import com.starrynight.tourapiproject.postItemPage.PostWriteHashTagItemAdapter;
import com.starrynight.tourapiproject.postPage.postRetrofit.PostHashTag;
import com.starrynight.tourapiproject.postWritePage.postWriteRetrofit.PostHashTagParams;
import com.starrynight.tourapiproject.postWritePage.postWriteRetrofit.PostImageParams;
import com.starrynight.tourapiproject.postWritePage.postWriteRetrofit.PostParams;
import com.starrynight.tourapiproject.postWritePage.postWriteRetrofit.PostWriteLoadingDialog;
import com.starrynight.tourapiproject.postWritePage.postWriteRetrofit.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * className :  PostWriteActivity
 * description : 게시물 작성 페이지 class
 * modification : 2022.08.01(박진혁) 주석 수정
 * author : jinhyeok
 * date : 2022-08-01
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2022-08-01      jinhyeok      주석 수정
 */
public class PostWriteActivity extends AppCompatActivity {

    final int PICK_IMAGE_SAMSUNG = 200;
    final int PICK_IMAGE_MULTIPLE = 201;
    int numOfPicture = 0;
    SelectImageAdapter adapter;
    RecyclerView recyclerView,hashTagRecyclerView,areaRecyclerView;
    String postContent = "",yearDate = "", time = "",postTitle, observationName, optionobservationName;
    List<PostHashTagParams> postHashTagParams = new ArrayList<>();
    List<PostHashTagParams> postAreaParams = new ArrayList<>();
    List<PostImageParams> postImageParams = new ArrayList<>();
    String postObservePointName = "";
    List<String> hashTagList = new ArrayList<>();
    List<String> areaList = new ArrayList<>();
    Long userId;
    PostWriteLoadingDialog dialog;
    File file;
    LinearLayout ob_linear;
    ArrayList<File> files = new ArrayList<>();
    BottomNavigationView photoBNV;
    private TextView postObservePointItem;
    String[] WRITE_PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] READ_PERMISSION = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    String[] READ_MEDIA_IMAGES = new String[]{Manifest.permission.READ_MEDIA_IMAGES};
    String[] INTERNET_PERMISSION = new String[]{Manifest.permission.INTERNET};
    EditText addContext;
    TextView dateText,timeText;
    ImageView hashTagPin;
    boolean dayOrNight;
    TextView exampleHashTagText,registBtn;

    int PERMISSIONS_REQUEST_CODE = 100;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);
        postObservePointItem = (TextView) findViewById(R.id.postObservationItem);
        ob_linear = findViewById(R.id.postwrite_ob_linear);
        dialog = new PostWriteLoadingDialog(PostWriteActivity.this);
        addContext = findViewById(R.id.postContentText);
        exampleHashTagText = findViewById(R.id.exampleHashTagTextView);
        hashTagPin=(ImageView) findViewById(R.id.postwrite_hashTagpin);
        registBtn = findViewById(R.id.save);

//      앱 내부저장소에서 userId 가져오기
        String fileName = "userId";
        try {
            FileInputStream fis = openFileInput(fileName);
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            userId = Long.parseLong(line);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        addContext.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(@SuppressLint("ClickableViewAccessibility") View v, MotionEvent event) {
                if (addContext.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        // 사진 추가 버튼 클릭 이벤트
        photoBNV= findViewById(R.id.bottom_nav_photo);
        photoBNV.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_photo:
                        recyclerView.setVisibility(View.VISIBLE);
                        if (numOfPicture > 10) {
                            Toast.makeText(PostWriteActivity.this, "사진은 최대 10장까지 선택할수있습니다.", Toast.LENGTH_LONG).show();
                        }
                        //권한 설정
                        int permission;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO);
                        } else {
                            permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                        int permission2;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES);
                        } else {
                            permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                        int permission3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);//denied면 -1

                        Log.d("test", "onClick: location clicked");
                        if (permission2 == PackageManager.PERMISSION_GRANTED && permission3 == PackageManager.PERMISSION_GRANTED) {
                            Log.d("MyTag", "읽기,쓰기,인터넷 권한이 있습니다.");
                            Intent intent = new Intent("android.intent.action.MULTIPLE_PICK");
                            intent.setType("image/*");
                            PackageManager manager = getApplicationContext().getPackageManager();
                            List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);


                            if (infos.size() > 0) { //테스트 하고 삼성,일반 차이없으면 삭제 예정
                                Log.e("FAT=", "삼성폰");
                                startActivityForResult(intent, PICK_IMAGE_SAMSUNG);
                            } else {
                                Log.e("FAT=", "일반폰");
                                Intent pickerIntent = new Intent(Intent.ACTION_PICK);
                                pickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                pickerIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                                pickerIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickerIntent, PICK_IMAGE_MULTIPLE);

//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 일반폰 - 반드시 있어야 다중선택 가능
//                    intent.setAction(Intent.ACTION_PICK); // ACTION_GET_CONTENT 사용불가 - 엘지 G2 테스트
//                    startActivityForResult(intent, PICK_IMAGE_MULTIPLE);

//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                    //intent.setType("image/*");
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    startActivityForResult(Intent.createChooser(intent, "사진 최대 9장 선택가능"), PICK_IMAGE_MULTIPLE);
                            }
                        } else {
                            Log.d("test", "permission denied");
                            Toast.makeText(getApplicationContext(), "읽기 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                ActivityCompat.requestPermissions(PostWriteActivity.this, READ_MEDIA_IMAGES, PERMISSIONS_REQUEST_CODE);
                            } else {
                                ActivityCompat.requestPermissions(PostWriteActivity.this, READ_PERMISSION, PERMISSIONS_REQUEST_CODE);
                            }
                            ActivityCompat.requestPermissions(PostWriteActivity.this, INTERNET_PERMISSION, PERMISSIONS_REQUEST_CODE);
                        }
                }
                return false;
            }
        });

        //선택한 사진 추가 어댑터
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SelectImageAdapter();
        recyclerView.setAdapter(adapter);

        //사진 삭제 버튼 클릭 이벤트
        adapter.setOnSelectImageItemClickListener(new OnSelectImageItemClickListener() {
            @Override
            public void onItemClick(SelectImageAdapter.ViewHolder holder, View view, int position) {
                adapter.removeItem(position);
                adapter.notifyDataSetChanged();
                numOfPicture--;
                postImageParams.remove(position);
                if (numOfPicture == 0) {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        //관측지점검색 버튼 클릭 이벤트
        ConstraintLayout observationlayout = findViewById(R.id.layout_observation);
        observationlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostWriteActivity.this, SearchObservingPointActivity.class);
                startActivityForResult(intent, 202);
            }
        });
        areaRecyclerView = findViewById(R.id.postAreaRecyclerView);

        //해시태그추가 버튼 클릭 이벤트
        LinearLayout hashTaglayout = findViewById(R.id.layout_hashtag);
        hashTaglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostWriteActivity.this, AddHashTagActivity.class);
                if (postHashTagParams != null) {
                    intent.putExtra("hashTagParams", (Serializable) postHashTagParams);
                }
                startActivityForResult(intent, 203);
            }
        });
        hashTagRecyclerView = findViewById(R.id.postHashTagrecyclerView);
        hashTagRecyclerView.addItemDecoration(new RecyclerViewDecoration(20, 20));

        //시간 추가 버튼 클릭 이벤트
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        ConstraintLayout timeLayout = findViewById(R.id.layout_time);
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostWriteActivity.this, SelectTimeActivity.class);
                startActivityForResult(intent, 204);
            }
        });

        //뒤로가기 버튼
        TextView back = findViewById(R.id.postWrite_back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView save_btn = findViewById(R.id.save);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(PostWriteActivity.this, R.style.MyDialogTheme);
                ad.setMessage("게시물을 작성하시겠습니까?");
                ad.setTitle("알림");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadfiles(files);
                        postContent = ((EditText) (findViewById(R.id.postContentText))).getText().toString();
                        if (postContent.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "게시물 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (numOfPicture == 0) {
                            Toast.makeText(getApplicationContext(), "사진을 추가해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        postTitle = ((EditText) (findViewById(R.id.postWrite_titleText))).getText().toString();
                        if (postTitle.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "게시물 제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (yearDate.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "관측 날짜을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (hashTagList.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "해시태그를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (postObservePointName.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "관측지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        long now = System.currentTimeMillis();//게시물을 쓴 현재시간 가져오기
                        Date date = new Date(now);
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                        String writeDate= dateFormat1.format(date);
                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
                        String writeTime= dateFormat2.format(date);

                        PostParams postParams = new PostParams();
                        postParams.setPostContent(postContent);
                        postParams.setYearDate(yearDate);
                        postParams.setTime(time);
                        postParams.setWriteDate(writeDate);
                        postParams.setWriteTime(writeTime);
                        postParams.setUserId(userId);
                        postParams.setPostTitle(postTitle);
                        postParams.setOptionObservation(optionobservationName);
                        Long areaId =0L;
                        List<PostHashTagParams> finalParams= new ArrayList<PostHashTagParams>();
                        if(postAreaParams!=null){
                            finalParams.addAll(postAreaParams);
                            finalParams.addAll(postHashTagParams);
                            areaId=postAreaParams.get(0).getAreaId();
                        }else{
                            finalParams.addAll(postHashTagParams);
                        }
                        Call<Long> call = RetrofitClient.getApiService().postup(postObservePointName, postParams,areaId);
                        call.enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(Call<Long> call, Response<Long> response) {
                                if (response.isSuccessful()) {
                                    Log.d("post", "게시물 작성 성공");
                                    Long result = response.body();
                                    Call<Void> call1 = RetrofitClient.getApiService().createPostImage(result, postImageParams);
                                    call1.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.isSuccessful()) {
                                                Log.d("postImage", "이미지 업로드 성공");
                                            } else {
                                                Log.d("postImage", "이미지 업로드 실패");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Log.d("postImage", "이미지 업로드 인터넷 오류");
                                        }
                                    });

                                    Call<Void> call2 = RetrofitClient.getApiService().createPostHashTag(result, finalParams);
                                    call2.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.isSuccessful()) {
                                                Log.d("posthashTag", "해시태그 업로드 성공");
                                            } else {
                                                Log.d("posthashTag", "해시태그 업로드 실패");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Log.d("posthashTag", "해시태그 업로드 인터넷 오류");
                                        }
                                    });
                                } else {
                                    Log.d("post", "게시물 작성 실패");
                                }
                            }

                            @Override
                            public void onFailure(Call<Long> call, Throwable t) {
                                Log.d("post", "게시물 작성 인터넷 오류");
                            }
                        });
                        LoadingAsyncTask task = new LoadingAsyncTask(PostWriteActivity.this, 3000);
                        task.execute();
                    }
                });
                ad.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });
    }


    //    private void requestPermission(){
//        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
//            requestPermissions(new String[]{
//                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
//                    ,REQUEST_WRITE_PERMISSION);
//        }else{
//            openFilePicker();
//        }
//    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 202) {
            if (resultCode == 2) {
                Log.d("postObservation", "검색 관측지 데이터 로드");
                canRegist();
                observationName = (String) data.getSerializableExtra("observationName");
                optionobservationName = (String) data.getSerializableExtra("optionObservationName");
                areaList = (List<String>) data.getSerializableExtra("areaList");
                postAreaParams = (List<PostHashTagParams>) data.getSerializableExtra("postAreaParams");
                if (observationName != null) {
                    ob_linear.setVisibility(View.VISIBLE);
                    postObservePointItem.setText(observationName);
                    postObservePointName = observationName;
                    postObservePointItem.setTextColor(getColor(R.color.point_blue));
                    hashTagPin.setVisibility(View.VISIBLE);
                    LinearLayoutManager layoutManager =new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    areaRecyclerView.setLayoutManager(layoutManager);
                    PostWriteHashTagItemAdapter adapter = new PostWriteHashTagItemAdapter();
                    areaRecyclerView.setAdapter(adapter);
                } else {
                    postObservePointItem.setText(optionobservationName);
                    postObservePointItem.setTextColor(getColor(R.color.point_blue));
                    ob_linear.setVisibility(View.VISIBLE);
                    postObservePointName = "나만의 관측지";
                    areaRecyclerView.setVisibility(View.VISIBLE);
                    exampleHashTagText.setVisibility(View.GONE);
                    LinearLayoutManager layoutManager =new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    areaRecyclerView.setLayoutManager(layoutManager);
                    PostWriteHashTagItemAdapter adapter = new PostWriteHashTagItemAdapter();
                    if (areaList.size() != 0) {
                        for (int i = 0; i < areaList.size(); i++) {
                            adapter.addItem(new PostWriteHashTagItem2(areaList.get(i)));
                            if (i==1){
                                break;
                            }
                        }
                    }
                    areaRecyclerView.setAdapter(adapter);
                }

            } else {
                Log.d("postObservation", "검색 관측지 데이터 로드 실패");
            }
        }
        if (requestCode == 203) {
            if (resultCode == 3) {
                Log.d("postHashTag", "게시물 해시태그 넘어옴");
                int allsize = 0;
                canRegist();
                postHashTagParams = (List<PostHashTagParams>) data.getSerializableExtra("postHashTagParams");
                hashTagList = (List<String>) data.getSerializableExtra("hashTagList");
                hashTagRecyclerView.setVisibility(View.VISIBLE);
                exampleHashTagText.setVisibility(View.GONE);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
                hashTagRecyclerView.setLayoutManager(layoutManager);
                PostWriteHashTagItemAdapter adapter = new PostWriteHashTagItemAdapter();
                if (hashTagList.size() != 0) {
                    for (int i = 0; i < hashTagList.size(); i++) {
                        adapter.addItem(new PostWriteHashTagItem2(hashTagList.get(i)));
                    }
                }
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    allsize += adapter.getItem(i).getHashTagname().length();
                }
                if (allsize > 15 && allsize < 30) {
                    layoutManager.setSpanCount(2);
                } else if (allsize > 31 && allsize < 60) {
                    layoutManager.setSpanCount(3);
                } else if (allsize > 61) {
                    layoutManager.setSpanCount(4);
                }
                hashTagRecyclerView.setAdapter(adapter);

            } else {
                Log.d("postHashTag", "게시물 검색 해시태그 로드 실패");
            }
        }
        if(requestCode==204){
            if(resultCode==4){
                canRegist();
                Log.d("postTime", "게시물 관측 시간 넘어옴");
                yearDate = (String)data.getSerializableExtra("date");
                time = (String)data.getSerializableExtra("time");
                dayOrNight = (boolean)data.getSerializableExtra("day");
                dateText.setText(yearDate.replace("-","."));
                dateText.setTextColor(getColor(R.color.point_blue));
                if(time.equals("00:00")){
                    timeText.setVisibility(View.GONE);
                }else{
                    int firstTime= Integer.valueOf(time.substring(0,2)).intValue(); // 오전 오후 구분
                    if(dayOrNight){
                        if(firstTime==0){
                            timeText.setText("오전 "+ Integer.toString(firstTime + 12)+time.substring(2));
                        }else{
                            timeText.setText("오전 "+time);
                        }
                    }else{
                        String opt= time.substring(0,2);
                        if(firstTime==12){
                            timeText.setText("오후 "+time);
                        }else{
                            int minus= Integer.valueOf(opt).intValue()-12;
                            timeText.setText("오후 "+Integer.toString(minus)+time.substring(2));
                            }
                        }
                    timeText.setTextColor(getColor(R.color.point_blue));
                    timeText.setVisibility(View.VISIBLE);
                }
            }
        }
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == PICK_IMAGE_SAMSUNG) { //삼성폰 일때
            final Bundle extras = data.getExtras();
            int count = extras.getInt("selectedCount");
            Object items = extras.getStringArrayList("selectedItems");

            Log.e("FAT=", "삼성폰 : " + items.toString());
        } else { //일반폰/단일 일때
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                Log.e("FAT=", "일반폰/단일 : " + uri.toString());
                try {
                    Bitmap img = resize(this, uri, 100);
                    addImage(img);
                    file = new File(getRealPathFromURI(uri));
                    files.add(file);
                    PostImageParams postImageParam = new PostImageParams();
                    postImageParam.setImageName(userId + "_" + file.getName());
                    postImageParams.add(postImageParam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else { //일반폰/다중 일때
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        if (numOfPicture + clipData.getItemCount() > 10) {
                            Toast.makeText(PostWriteActivity.this, "사진은 최대 10장까지 선택할수있습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ArrayList<Uri> uris = new ArrayList<>();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();
                            file = new File(getRealPathFromURI(uri));
                            files.add(file);
                            PostImageParams postImageParam = new PostImageParams();
                            postImageParam.setImageName(userId + "_" + file.getName());
                            postImageParams.add(postImageParam);
                            Log.e("FAT=", "일반폰/다중 : " + uri.toString());
                            uris.add(uri);
                        }
                        addImages(uris);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //등록가능한 경우 등록 버튼 글씨 활성화
    public void canRegist(){
        if (!postContent.isEmpty()&&numOfPicture != 0&&!postTitle.isEmpty()&&!yearDate.isEmpty()&&!hashTagList.isEmpty()&&!postObservePointName.isEmpty()) {
            registBtn.setTextColor(getColor(R.color.white));
        }

    }

    // 이미지 uri 경로 함수
    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //recyclerview 간격
    public static class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

        private final int divRight;
        private final int divHeight;

        public RecyclerViewDecoration(int divRight, int divHeight) {
            this.divRight = divRight;
            this.divHeight = divHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = divRight;
            outRect.top = divHeight;
        }
    }

    public void uploadWithTransferUtilty(String fileName, File file) {

        String realFileName = userId + "_" + fileName;

        AWSCredentials awsCredentials = new BasicAWSCredentials(readAccessKey(), readSecretKey());    // IAM 생성하며 받은 것 입력
        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2));

        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(getApplicationContext()).build();
        TransferNetworkLossHandler.getInstance(getApplicationContext());

        TransferObserver uploadObserver = transferUtility.upload("starry-night/postImage", realFileName, file);    // (bucket api, file이름, file객체)
        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.COMPLETED) {
                    Log.d("TAG", "onStateChanged: " + id + ", " + state.toString());
                }
            }

            @Override
            public void onProgressChanged(int id, long current, long total) {
                int done = (int) (((double) current / total) * 100.0);
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX:" + ex.toString());
            }
        });
    }

    private void addImage(Bitmap img) {
        numOfPicture++;

        adapter.addItem(new SelectImage(img, numOfPicture));
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addImages(ArrayList<Uri> uris) {
        for (Uri uri : uris) {
            numOfPicture++;
            try {
//                InputStream in = getContentResolver().openInputStream(uri);
//                Bitmap img = BitmapFactory.decodeStream(in);
//                in.close();
                Bitmap img = resize(this, uri, 100); //해상도 최대로 하고싶으면 100으로
                Bitmap roateImage1 = rotateImage(uri, img);
                adapter.addItem(new SelectImage(roateImage1, numOfPicture));
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadfiles(ArrayList<File> files) {
        for (File file : files) {
            uploadWithTransferUtilty(file.getName(), file);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private Bitmap rotateImage(Uri uri, Bitmap bitmap) throws IOException {
        InputStream in = getContentResolver().openInputStream(uri);
        ExifInterface exif = new ExifInterface(in);
        in.close();
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            matrix.postRotate(90);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            matrix.postRotate(180);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            matrix.postRotate(270);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private Bitmap resize(Context context, Uri uri, int resize) {
        Bitmap resizeBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);

            int width = options.outWidth;
            int height = options.outHeight;
            int samplesize = 1;

            while (true) {
                if (width / 2 < resize || height / 2 < resize)
                    break;
                width /= 2;
                height /= 2;
                samplesize *= 2;
            }
            options.inSampleSize = samplesize;
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
            resizeBitmap = bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resizeBitmap;
    }

    private String readAccessKey() {
        String data = null;
        InputStream inputStream = getResources().openRawResource(R.raw.access);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            data = new String(byteArrayOutputStream.toByteArray(), "MS949");

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private String readSecretKey() {
        String data = null;
        InputStream inputStream = getResources().openRawResource(R.raw.secret);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            data = new String(byteArrayOutputStream.toByteArray(), "MS949");

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private class LoadingAsyncTask extends AsyncTask<String, Long, Boolean> {
        private Context mContext = null;
        private Long mtime;

        public LoadingAsyncTask(Context context, long time) {
            mContext = PostWriteActivity.this;
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
            finish();
        }
    }

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
}