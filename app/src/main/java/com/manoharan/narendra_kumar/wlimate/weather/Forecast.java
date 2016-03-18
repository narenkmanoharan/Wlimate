package com.manoharan.narendra_kumar.wlimate.weather;

import com.manoharan.narendra_kumar.R;

/**
 * Created by naren on 16-03-2016.
 */
public class Forecast {
    private Current mCurrent;
    private HourlyWeather[] mHourlyForecast;
    private DailyWeather[] mDailyForecast;

    private DailyWeather mDailyWeather;

    public DailyWeather getDailyWeather() {
        return mDailyWeather;
    }

    public void setDailyWeather(DailyWeather dailyWeather) {
        mDailyWeather = dailyWeather;
    }


    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public HourlyWeather[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(HourlyWeather[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    public DailyWeather[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(DailyWeather[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }

    public static int getIconId(String iconString) {

        {
            //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.

            int iconId = R.drawable.clear_day_ezcar;

            if (iconString.equals("clear-day")) {
                iconId = R.drawable.clear_day_ezcar;
            } else if (iconString.equals("clear-night")) {
                iconId = R.drawable.clear_night;
            } else if (iconString.equals("rain")) {
                iconId = R.drawable.rain;
            } else if (iconString.equals("snow")) {
                iconId = R.drawable.snow;
            } else if (iconString.equals("sleet")) {
                iconId = R.drawable.sleet;
            } else if (iconString.equals("wind")) {
                iconId = R.drawable.wind;
            } else if (iconString.equals("fog")) {
                iconId = R.drawable.fog;
            } else if (iconString.equals("cloudy")) {
                iconId = R.drawable.cloudy;
            } else if (iconString.equals("partly-cloudy-day")) {
                iconId = R.drawable.partly_cloudy;
            } else if (iconString.equals("partly-cloudy-night")) {
                iconId = R.drawable.cloudy_night;
            }

            return iconId;

        }


    }


    public static int getBackground(String iconString) {

        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.

        int backgroundId = R.drawable.clear_day_bg;

        if (iconString.equals("clear-day")) {
            backgroundId = R.drawable.clear_day_bg;
        } else if (iconString.equals("clear-night")) {
            backgroundId = R.drawable.clear_night_bg;
        } else if (iconString.equals("rain")) {
            backgroundId = R.drawable.weather_rain_bg;
        } else if (iconString.equals("snow")) {
            backgroundId = R.drawable.snow_bg;
        } else if (iconString.equals("sleet")) {
            backgroundId = R.drawable.snow_bg;
        } else if (iconString.equals("wind")) {
            backgroundId = R.drawable.wind_bg;
        } else if (iconString.equals("fog")) {
            backgroundId = R.drawable.snow_bg;
        } else if (iconString.equals("cloudy")) {
            backgroundId = R.drawable.cloudy_bg;
        } else if (iconString.equals("partly-cloudy-day")) {
            backgroundId = R.drawable.partly_cloudy_day_bg;
        } else if (iconString.equals("partly-cloudy-night")) {
            backgroundId = R.drawable.cloudy_night_bg;
        }

        return backgroundId;


    }

}
