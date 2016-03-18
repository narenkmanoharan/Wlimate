package com.manoharan.narendra_kumar.wlimate.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.manoharan.narendra_kumar.R;
import com.manoharan.narendra_kumar.wlimate.adapters.HourAdapter;
import com.manoharan.narendra_kumar.wlimate.weather.HourlyWeather;

/**
 * Created by naren on 16-03-2016.
 */

public class HourlyForecastActivity extends ActionBarActivity {

    private HourlyWeather[] mHourlyWeathers;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHourlyWeathers = Arrays.copyOf(parcelables, parcelables.length, HourlyWeather[].class);

        HourAdapter adapter = new HourAdapter(mHourlyWeathers);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

    }
}
