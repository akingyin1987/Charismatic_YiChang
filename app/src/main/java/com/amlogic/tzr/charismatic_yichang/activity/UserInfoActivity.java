package com.amlogic.tzr.charismatic_yichang.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.BaseActivity;
import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.BitmapUtil;
import com.amlogic.tzr.charismatic_yichang.bean.User;
import com.amlogic.tzr.charismatic_yichang.event.LoginEvent;
import com.amlogic.tzr.charismatic_yichang.view.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.greenrobot.event.EventBus;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "UserInfoActivity";

    private static final int PICK_FROM_CAMERA = 0x000000;
    private static final int PICK_FROM_FILE = 0x000001;
    private static final int CROP_FROM_CAMERA = 0x000002;
    private static final int CROP_FROM_FILE = 0x000003;

    public static final String ARG_TAKEN_PHOTO_URI = "image";

    private Context mContext;

    private Uri photoUri;

    private int photoSize;

    private Toolbar mToolbar;

    private ImageView headImg;

    private LinearLayout layout_phone;

    private TextView tv_head, tv_nick, tv_sex, tv_address, tv_introduce, tv_phone, tv_creatTime;

    private Uri imgUri;


    private User mUser;

    private boolean isMe=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mContext = UserInfoActivity.this;
        mUser= (User) getIntent().getSerializableExtra(UserProfileActivity.CURRENT_USER);
        if (mUser.getObjectId().equals(BmobUser.getCurrentUser(mContext,User.class).getObjectId())){
            isMe=true;
        }
        initView();
        initData();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.tl_aui_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("详细资料");

        layout_phone= (LinearLayout) findViewById(R.id.ll_cui_phone);
        headImg = (ImageView) findViewById(R.id.iv_cui_img);
        tv_head = (TextView) findViewById(R.id.tv_cui_editImg);
        tv_nick = (TextView) findViewById(R.id.tv_cui_nick);
        tv_sex = (TextView) findViewById(R.id.tv_cui_sex);
        tv_address = (TextView) findViewById(R.id.tv_cui_address);
        tv_introduce = (TextView) findViewById(R.id.tv_cui_introduce);
        tv_phone = (TextView) findViewById(R.id.tv_cui_phone);
        tv_creatTime = (TextView) findViewById(R.id.tv_cui_createTime);

        tv_head.setOnClickListener(this);
        tv_nick.setOnClickListener(this);
        tv_sex.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        tv_introduce.setOnClickListener(this);
        tv_phone.setOnClickListener(this);
        tv_creatTime.setOnClickListener(this);


    }


    private void initData(){
        if (mUser!=null){
            if (mUser.getHead_thumb()!=null){
                String icon_url=mUser.getHead_thumb().getFileUrl(mContext);
                Picasso.with(mContext).load(icon_url).transform(new CircleTransformation()).into(headImg);
            }else{
                Picasso.with(mContext).load(R.mipmap.ic_user).transform(new CircleTransformation()).into(headImg);

            }
            if (mUser.getNick()!=null){

            }else {

            }
            if (mUser.getSex()==false){
                tv_sex.setText("男");
            }else{
                tv_sex.setText("女");
            }
            if (mUser.getAddress()!=null){

            }else {

            }
            if (mUser.getIntroduce()!=null){

            }else {

            }
            if (mUser!=BmobUser.getCurrentUser(mContext,User.class)){
                layout_phone.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                finish();
                break;
            case R.id.action_publish:

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (isMe) {
            switch (v.getId()) {
                case R.id.tv_cui_editImg:
                    showDialog();
                    break;
                case R.id.tv_cui_nick:

                    break;
                case R.id.tv_cui_sex:

                    break;
                case R.id.tv_cui_address:

                    break;
                case R.id.tv_cui_introduce:

                    break;
                case R.id.tv_cui_phone:

                    break;
                case R.id.tv_cui_createTime:

                    break;

            }
        }
    }

    private void showDialog(){
        User mUser= BmobUser.getCurrentUser(mContext,User.class);
        if (mUser!=null) {
            View dialog_publish = LayoutInflater.from(mContext).inflate(
                    R.layout.dialog_publish_photo, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(dialog_publish);
            builder.create();
            final AlertDialog dialog = builder.show();
            ImageView choiceCamera = (ImageView) dialog_publish
                    .findViewById(R.id.img_choice_from_camera);
            ImageView choicePhoto = (ImageView) dialog_publish
                    .findViewById(R.id.img_choice_from_photo);
            ImageView choiceCancle = (ImageView) dialog_publish.findViewById(R.id.img_choice_cancale);
            choiceCamera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    getPicFromCapture();

                }
            });

            choicePhoto.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    getPicFromContent();
                }
            });

            choiceCancle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
        }else{
            startActivity(new Intent(mContext, LoginActivity.class));
        }
    }

    private void getPicFromContent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_FROM_FILE);
    }

    private void getPicFromCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imgUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "avatar_" + String.valueOf(System.currentTimeMillis()) + ".png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                cropImageUri(imgUri, 250, 250, CROP_FROM_CAMERA);
                break;
            case PICK_FROM_FILE:
                imgUri = data.getData();
                doCrop();
                break;
            case CROP_FROM_CAMERA:
                if (imgUri != null) {
                    setCropImg(data,CROP_FROM_CAMERA);
                }
                break;
            case CROP_FROM_FILE:
                if (null != data) {
                    setCropImg(data,CROP_FROM_FILE);
                }
                break;
        }

    }

    private void doCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imgUri, "image/*");
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_FROM_FILE);
    }

    private void setCropImg(Intent picdata,int type) {
        Bundle bundle = picdata.getExtras();
        Bitmap mBitmap=null;
        if (null != bundle) {
            if (type==CROP_FROM_FILE)
            {
                 mBitmap =bundle.getParcelable("data");
            }else if(type==CROP_FROM_CAMERA)
            {
//                mBitmap = BitmapUtil.decodeUriAsBitmap(mContext,imgUri);
                try {
                    mBitmap = BitmapUtil.decodeSampledBitmapFromResource(mContext,photoUri,150,150);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            String BitmapPath = BitmapUtil.saveBitmap(mBitmap, mContext);
            final BmobFile file = new BmobFile(new File(BitmapPath));
            file.upload(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    Log.e(TAG, "file.upload is ok");

                    mUser.setHead_thumb(file);
                    updateHead_thumb(file.getFileUrl(mContext));
                    mUser.update(mContext, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            EventBus.getDefault().post(new LoginEvent(true, mUser));
                            Log.e(TAG, " currentUser.update is ok");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.e(TAG, " currentUser.update is onFailure---" + i + s);
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.e(TAG, "file.upload is onFailure---" + i + s);
                }
            });


        }

    }

    private void updateHead_thumb(String url) {
        Picasso.with(mContext).load(url).transform(new CircleTransformation()).into(headImg);
    }




}
