package com.wilson.sepsisapp;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import me.itangqi.waveloadingview.WaveLoadingView;

public class SepsisActivity extends AppCompatActivity {

    int progress=72;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sepsis);

        final WaveLoadingView mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        mWaveLoadingView.setTopTitle("SEPSIS");
        mWaveLoadingView.setCenterTitle(progress+"%");
        mWaveLoadingView.setCenterTitleColor(Color.WHITE);
        mWaveLoadingView.setBottomTitleSize(30);
        mWaveLoadingView.setProgressValue(progress);
        mWaveLoadingView.setBorderWidth(10);
        mWaveLoadingView.setAmplitudeRatio(60);
        mWaveLoadingView.setWaveColor(Color.RED);
        mWaveLoadingView.setBorderColor(Color.RED);
        mWaveLoadingView.setTopTitleStrokeColor(Color.WHITE);
        mWaveLoadingView.setTopTitleStrokeWidth(3);
        mWaveLoadingView.pauseAnimation();
        mWaveLoadingView.resumeAnimation();
        mWaveLoadingView.cancelAnimation();
        mWaveLoadingView.startAnimation();
    }
}
