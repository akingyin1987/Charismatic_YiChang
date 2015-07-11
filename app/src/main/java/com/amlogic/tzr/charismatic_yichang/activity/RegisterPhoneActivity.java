package com.amlogic.tzr.charismatic_yichang.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.ConfigUtil;
import com.amlogic.tzr.charismatic_yichang.view.TextURLView;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterPhoneActivity extends AppCompatActivity {
    private Context mContext;

    private Toolbar mToolbar;

    private EditText et_phoneNumber;

    private Button btn_next;

    private TextInputLayout til_phoneNumber;

    private TextURLView textURLView;

    private String phString;

    private StringBuilder allBuilder ;

    private EventHandler eh;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);


            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("RegisterActivity_event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                    Intent sendPhoneIntent = new Intent(mContext, RegisterCodeActivity.class);
                    sendPhoneIntent.putExtra("user_phone", phString);
                    startActivity(sendPhoneIntent);
                }
            } else {

                try {
                    Log.e("data", data.toString());
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;
                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(mContext, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 如果木有找到资源，默认提示

                Toast.makeText(mContext, "smssdk_network_error", Toast.LENGTH_SHORT).show();

            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        mContext=RegisterPhoneActivity.this;
        SMSSDK.initSDK(this, ConfigUtil.APPKEY, ConfigUtil.APPSECRET);
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = Message.obtain();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.tl_rap_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.action_register));
        et_phoneNumber= (EditText) findViewById(R.id.et_arp_phone);
        til_phoneNumber= (TextInputLayout) findViewById(R.id.til_arp_phone);
        til_phoneNumber.setHint(getResources().getString(R.string.input_phoneNumber));
        et_phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() < 10 || s.length() > 11) {
                    til_phoneNumber.setError(getResources().getString(R.string.error_invalid_phoneNumber));
                    til_phoneNumber.setErrorEnabled(true);
                } else {
                    til_phoneNumber.setErrorEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textURLView = (TextURLView) findViewById(R.id.tv_arp_url);
        textURLView.setText(R.string.agree_agreement);
        btn_next= (Button) findViewById(R.id.btn_arp_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_phoneNumber.getText().toString())) {
                    phString = et_phoneNumber.getText().toString();
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    allBuilder = new StringBuilder();
                    allBuilder.append("我们将发送验证码短信到这个号码:").append(phString);
                    Log.e("Register", allBuilder.toString());
                    builder.setMessage(allBuilder.toString());
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SMSSDK.getVerificationCode("86", phString);
                        }
                    });
                    builder.show();

                } else {
                    til_phoneNumber.setError(getResources().getString(R.string.error_invalid_phoneNumber));
                    til_phoneNumber.setErrorEnabled(true);
                }

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
