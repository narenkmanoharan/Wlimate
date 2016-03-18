package com.manoharan.narendra_kumar.wlimate.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Arrays;

import com.manoharan.narendra_kumar.R;

import com.manoharan.narendra_kumar.wlimate.adapters.DayAdapter;
import com.manoharan.narendra_kumar.wlimate.weather.DailyWeather;

/**
 * Created by naren on 16-03-2016.
 */

public class DailyForecastActivity extends ListActivity {

    private DailyWeather[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);

        String weekSummary = intent.getStringExtra(MainActivity.WEEK_SUMMARY);
        TextView textView = (TextView) findViewById(R.id.weekSummary);
        textView.setText(weekSummary);


        mDays = Arrays.copyOf(parcelables, parcelables.length, DailyWeather[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);
        setListAdapter(adapter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

    }


}
