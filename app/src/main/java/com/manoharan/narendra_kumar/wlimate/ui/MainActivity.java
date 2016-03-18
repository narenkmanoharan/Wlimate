package com.manoharan.narendra_kumar.wlimate.ui;


import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.ImageView;


import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.manoharan.narendra_kumar.R;

import com.manoharan.narendra_kumar.wlimate.weather.Current;
import com.manoharan.narendra_kumar.wlimate.weather.DailyWeather;
import com.manoharan.narendra_kumar.wlimate.weather.Forecast;
import com.manoharan.narendra_kumar.wlimate.weather.HourlyWeather;

/**
 * Created by naren on 16-03-2016.
 */

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    protected Location mLastLocation;
    protected TextView mLocationAddressTextView;
    public static final String TAG = MainActivity.class.getSimpleName();
    public Forecast mforecast;
    public DailyWeather mDailyWeather;
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    public static final String WEEK_SUMMARY = "";
    public String weekSummary;
    public String locality;
    public String mIcon;


    @Bind(R.id.timeLabel)
    TextView mTimeLabel;
    @Bind(R.id.temperatureLabel)
    TextView mTemperatureLabel;
    @Bind(R.id.humidityValue)
    TextView mHumidityValue;
    @Bind(R.id.precipValue)
    TextView mPrecipValue;
    @Bind(R.id.summaryLabel)
    TextView mSummaryLabel;
    @Bind(R.id.iconImageView)
    ImageView mIconImageView;
    @Bind(R.id.refreshImageView)
    ImageView mRefreshImageView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.mainBackground)
    RelativeLayout mMainBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mProgressBar.setVisibility(View.INVISIBLE);
        Log.d(TAG, "Main UI code is running!");
        mLocationAddressTextView = (TextView) findViewById(R.id.weekSummary);
        buildGoogleApiClient();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(60000); // 1 second, in milliseconds

        Typeface timeTypeface = Typeface.createFromAsset(
                this.getAssets(),
                "font/Roboto-Medium.ttf");
        mTimeLabel.setTypeface(timeTypeface);

        Typeface tempTypeface = Typeface.createFromAsset(
                this.getAssets(),
                "font/Roboto-Thin.ttf");
        mTemperatureLabel.setTypeface(tempTypeface);

        Typeface humTypeface = Typeface.createFromAsset(
                this.getAssets(),
                "font/Roboto-Medium.ttf");
        mHumidityValue.setTypeface(humTypeface);

        Typeface percipTypeface = Typeface.createFromAsset(
                this.getAssets(),
                "font/Roboto-Medium.ttf");
        mPrecipValue.setTypeface(percipTypeface);

        Typeface sumTypeface = Typeface.createFromAsset(
                this.getAssets(),
                "font/Roboto-Medium.ttf");
        mSummaryLabel.setTypeface(sumTypeface);

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "108a908510d2109f2a82d98b6a4d49f9";

        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude + "?units=si";

        if (isNetworkAvailable()) {

            toogleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toogleRefresh();
                        }
                    });
                    alertUserAboutError(getString(R.string.error_title), getString(R.string.error_message));
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toogleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mforecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUserAboutError(getString(R.string.error_title), getString(R.string.error_message));
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Exeception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exeception caught: ", e);
                    }
                }
            });
        } else {
            alertUserAboutError(getString(R.string.error_title), getString(R.string.network_unavailable_message));
        }
    }

    public DailyWeather[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");
        weekSummary = daily.getString("summary");

        DailyWeather[] days = new DailyWeather[data.length()];

        for (int i = 0; i < data.length(); i++) {

            JSONObject jsonDay = data.getJSONObject(i);
            DailyWeather dailyWeather = new DailyWeather();

            dailyWeather.setSummary(weekSummary);
            dailyWeather.setIcon(jsonDay.getString("icon"));
            dailyWeather.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            dailyWeather.setTime(jsonDay.getLong("time"));
            dailyWeather.setTimezone(timezone);

            days[i] = dailyWeather;
        }

        Log.i(TAG, weekSummary);

        return days;

    }

    private HourlyWeather[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        HourlyWeather[] hourlyWeathers = new HourlyWeather[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonHour = data.getJSONObject(i);
            HourlyWeather hourlyWeather = new HourlyWeather();

            hourlyWeather.setSummary(jsonHour.getString("summary"));
            hourlyWeather.setTemperature(jsonHour.getDouble("temperature"));
            hourlyWeather.setIcon(jsonHour.getString("icon"));
            hourlyWeather.setTime(jsonHour.getLong("time"));
            hourlyWeather.setTimezone(timezone);

            hourlyWeathers[i] = hourlyWeather;

        }
        return hourlyWeathers;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timezone);

        Log.d(TAG, current.getFormattedTime());

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;

        }
        return isAvailable;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (lastLocation == null) {

            alertUserAboutError(getString(R.string.error_title), getString(R.string.NoLocationServices));
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {

            final double lastLatitude = lastLocation.getLatitude();
            final double lastLongitude = lastLocation.getLongitude();

            mRefreshImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getForecast(lastLatitude, lastLongitude);
                }
            });

            getForecast(lastLatitude, lastLongitude);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(lastLatitude, lastLongitude, 1);

                if (addresses == null || addresses.size() == 0) {

                    String geocoderAPIKey = "AIzaSyCTIaJJ8hNy4kE5RFcpeoS7TEplA66pZX8";

                    String revGeocoderUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lastLatitude + "," + lastLongitude
                            + "&key=" + geocoderAPIKey;

                    OkHttpClient client2 = new OkHttpClient();
                    Request request2 = new Request.Builder().url(revGeocoderUrl).build();

                    Call call = client2.newCall(request2);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request2, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toogleRefresh();
                                }
                            });
                            alertUserAboutError(getString(R.string.error_title), getString(R.string.error_message));
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toogleRefresh();
                                }
                            });
                            try {
                                String jsonDataGeocoder = response.body().string();
                                Log.v(TAG, jsonDataGeocoder);
                                if (response.isSuccessful()) {

                                    JSONObject geocoderAPI = new JSONObject(jsonDataGeocoder);
                                    JSONArray addressComp = geocoderAPI.getJSONArray("results").getJSONObject(1).getJSONArray("address_components");
                                    locality = addressComp.getJSONObject(0).getString("long_name");

                                    Log.i(TAG, "Geocoder API connected:" + locality);

                                } else {
                                    alertUserAboutError(getString(R.string.error_title), getString(R.string.error_message));
                                }

                            } catch (IOException | JSONException e) {
                                Log.e(TAG, "Exeception caught: ", e);
                            }
                        }
                    });

                    mLocationAddressTextView.setText(locality);

                } else {

                    String cityName = addresses.get(0).getAddressLine(0);
                    String stateName = addresses.get(0).getAddressLine(1);
                    String countryName = addresses.get(0).getAddressLine(2);
                    String nName = addresses.get(0).getAddressLine(3);

                    Log.i(TAG, "Location services connected." + cityName.replaceAll("\\d", ""));
                    Log.i(TAG, "Location services connected." + stateName.replaceAll("\\d", ""));
                    Log.i(TAG, "Location services connected." + countryName);
                    Log.i(TAG, "Location services connected." + nName);

                    mLocationAddressTextView.setText(stateName.replaceAll("\\d", ""));
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        final double currentLatitude = location.getLatitude();
        final double currentLongitude = location.getLongitude();
        Log.i(TAG, "current Location" + currentLatitude + currentLongitude);

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(currentLatitude, currentLongitude);
            }
        });

        getForecast(currentLatitude, currentLongitude);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);

            if (addresses == null || addresses.size() == 0) {
                alertUserAboutError(getString(R.string.error_title), getString(R.string.no_address));
            } else {
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

                Log.i(TAG, "Location services connected." + cityName.replaceAll("\\d", ""));
                Log.i(TAG, "Location services connected." + stateName.replaceAll("\\d", ""));
                Log.i(TAG, "Location services connected." + countryName);

                mLocationAddressTextView.setText(cityName.replaceAll("\\d", "") + ", " + stateName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }


    private void toogleRefresh() {

        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mforecast.getCurrent();

        mTemperatureLabel.setText(current.getTemperature() + "");
        mTimeLabel.setText(current.getFormattedTime() + "");
        mHumidityValue.setText(Math.round(current.getHumidity() * 100) + "%");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());

        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);

        Drawable drawableBg = getResources().getDrawable(current.getBackgroundId());
        mMainBackground.setBackground(drawableBg);

    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));

        return forecast;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private void alertUserAboutError(String alertTitle, String alertBody) {
        Bundle messageArgs = new Bundle();
        messageArgs.putString(AlertDialogFragment.TITLE_ID, alertTitle);
        messageArgs.putString(AlertDialogFragment.MESSAGE_ID, alertBody);

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setArguments(messageArgs);
        dialog.show(getFragmentManager(), "error_dialog");

    }


    @OnClick(R.id.dailyButton)
    public void startDailyActivity(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mforecast.getDailyForecast());
        intent.putExtra(WEEK_SUMMARY, weekSummary);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view) {
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mforecast.getHourlyForecast());
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }
}
