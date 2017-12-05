package com.yzy.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yzy.library.HourglassView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.hv_01)
    HourglassView mHv01;
    @Bind(R.id.hv_02)
    HourglassView mHv02;
    @Bind(R.id.hv_03)
    HourglassView mHv03;
    @Bind(R.id.hv_04)
    HourglassView mHv04;

    @Bind(R.id.btn_start)
    Button mBtnStart;
    @Bind(R.id.btn_end)
    Button mBtnEnd;
    @Bind(R.id.ll_root)
    LinearLayout mLlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



    }

    @OnClick({R.id.btn_start, R.id.btn_end})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                mHv01.start();
                mHv02.start();
                mHv03.start();
                mHv04.start();
                break;
            case R.id.btn_end:
                mHv01.end();
                mHv02.end();
                mHv03.end();
                mHv04.end();
                break;
        }
    }
}
