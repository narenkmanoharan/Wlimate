package com.manoharan.narendra_kumar.wlimate.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.manoharan.narendra_kumar.R;

import com.manoharan.narendra_kumar.wlimate.weather.HourlyWeather;

/**
 * Created by naren on 16-03-2016.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private HourlyWeather[] mHourlyWeathers;

    public HourAdapter(HourlyWeather[] hourlyWeathers) {
        mHourlyWeathers = hourlyWeathers;
    }


    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);

        HourViewHolder viewHolder = new HourViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHourlyWeathers[position]);
    }

    @Override
    public int getItemCount() {
        return mHourlyWeathers.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {

        public TextView mTimeLabel;
        public TextView mSummaryLabel;
        public TextView mTemperatureLabel;
        public ImageView mIconImageView;

        public HourViewHolder(View itemView) {
            super(itemView);

            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
        }

        public void bindHour(HourlyWeather hourlyWeather) {
            mTimeLabel.setText(hourlyWeather.getHour());
            mSummaryLabel.setText(hourlyWeather.getSummary());
            mTemperatureLabel.setText(hourlyWeather.getTemperature() + "º");
            mIconImageView.setImageResource(hourlyWeather.getIconId());

        }

    }

}
