package com.example.hatio.insidepositioning;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Random;


public class OdometerCamera extends Activity {

//    static {
//        System.loadLibrary("insidepositioning");
//    }
//
////    public native String getJNIString();

    private GaugeView mGaugeView;
    private TextView tvSpeed;
    private TextView tvAverageSpeed;
    private final Random RAND = new Random();
    private int averageSpeed = 0;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_odometer);

        GaugeView gv = new GaugeView(OdometerCamera.this);

//        setContentView(gv);

        mGaugeView = (GaugeView) findViewById(R.id.gauge_view1);
        tvSpeed = (TextView) findViewById(R.id.tv_speed);
        tvAverageSpeed = (TextView) findViewById(R.id.tv_average_speed);
        mTimer.start();

    }


    private final CountDownTimer mTimer = new CountDownTimer(300000, 1000) {

        @Override
        public void onTick(final long millisUntilFinished) {
            int rndSpeed = RAND.nextInt(61);
            averageSpeed += rndSpeed;
            count += 1;

            mGaugeView.setTargetValue(rndSpeed);
            tvSpeed.setText(rndSpeed + "km");
            tvAverageSpeed.setText((averageSpeed / count) + "km");
        }

        @Override
        public void onFinish() {}
    };
}
