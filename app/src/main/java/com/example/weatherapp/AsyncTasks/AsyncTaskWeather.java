package com.example.weatherapp.AsyncTasks;
/**
 * AsyncTask that allows to download JSON data relative to the weather of various cities,
 * including the user's current one
 *
 * @author Marco Seiça Fidalgo
 * @date 6/12/2020
 * @version 1.0
 * @copyright Open-source Project
 *
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weatherapp.CustomAdapters.CustomAdapterLVMain;
import com.example.weatherapp.Objetos.WeatherData;
import com.example.weatherapp.R;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;



public class AsyncTaskWeather extends AsyncTask<String, Void, Object> {
    private static final String API_KEY = "6b09297fd3edfcc07d3df5c3fb286350";
    private static final String URL_OPENWEATHER = "https://api.openweathermap.org/data/2.5/weather?q=<CIDADE>&appid=<API_KEY>&units=metric";

    private TextView tvCity = null;
    private TextView tvCityTemp = null;
    private ImageView cityImage = null;
    private ListView lvCities = null;
    private String city;
    private String[] cityList;
    private Context context;


    public AsyncTaskWeather(TextView tvCity, TextView tvCityTemp, ImageView cityImage, String city) {
        this.tvCity = tvCity;
        this.tvCityTemp = tvCityTemp;
        this.cityImage = cityImage;
        this.city = city;
    }

    public AsyncTaskWeather(Context context, ListView lvCities, String[] cityList) {
        this.context = context;
        this.lvCities = lvCities;
        this.cityList = cityList;
    }


    @Override
    protected Object doInBackground(String... params) {
        String result = "(error)";

        try {
            if(this.tvCity != null) {
                result = getCityData( modifyURL(URL_OPENWEATHER,city) );
                return convertsRequestResult(result);
            }
            else if(this.lvCities != null){
                WeatherData[] dadosCidades = new WeatherData[cityList.length];
                for (int i = 0; i < cityList.length; i++) {
                    result = getCityData( modifyURL(URL_OPENWEATHER,cityList[i]));
                    dadosCidades[i] = convertsRequestResult(result);
                }
                return dadosCidades;
            }
        } catch (Exception e) {
            Log.d("testee","[ERROR] doInBackground AsyncTaskWeather - Error: "+e);
        }
        return result;
    }
    @Override
    protected void onPostExecute(Object objData) {

        //Current city
        if (objData instanceof WeatherData) {
            WeatherData weatherDataObj = (WeatherData)objData;
            tvCity.setText(city);
            tvCityTemp.setText(Math.round(weatherDataObj.getTemperature())+"º");
            cityBackground(cityImage, weatherDataObj.getIconCode());
        }

        //List of cities
        else if(objData instanceof WeatherData[]){
            WeatherData[] weatherDataObj = (WeatherData[])objData;
            //Iterate through all elements, the ones that are null, don't go into the adapter
            if(weatherDataObj.length > 0) {
                final CustomAdapterLVMain customAdapterDocsAntigos = new CustomAdapterLVMain(context, weatherDataObj);
                lvCities.setAdapter(customAdapterDocsAntigos);
            }
            else {
                //Didn't find any of the needed cities
                tvCity.setText(R.string.citylist_notfound);
            }
        }
        else{
            //In case of getting a null as reply
            tvCity.setText(R.string.current_city_notfound);
        }
    }


    /** Converts the request result into an object WeatherData */
    public WeatherData convertsRequestResult(String result){
        try {
            JSONObject obj = new JSONObject((String) result);

            JSONArray  weather = obj.getJSONArray("weather");
            JSONObject  main = obj.getJSONObject("main"),
                        wind = obj.getJSONObject("wind");

            JSONObject o1 = new JSONObject();
            String a = (weather.getJSONObject(0)).getString("main");

            return new WeatherData(
                    (weather.getJSONObject(0)).getString("icon"),       // Icon representing the weather condition
                    obj.getString("name"),                                     // City name
                    (weather.getJSONObject(0)).getString("main"),       // Weather condition
                    (weather.getJSONObject(0)).getString("description"),// Description of the condition
                    main.getDouble("temp"),                                   // Temperature
                    main.getDouble("feels_like"),                             // Feels like
                    main.getDouble("temp_min"),                               // Min. Temperature
                    main.getDouble("temp_max"),                               // Max. Temperature
                    main.getInt("pressure"),                                  // Atmospheric pressure.
                    main.getInt("humidity"),                                  // Relative humidity
                    wind.getDouble("speed"));                                 // Wind Speed
        }
        catch(JSONException je){
            je.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /** Sends an URL to the API requesting weather data  */
    String getCityData(String link) {
        StringBuilder resp = new StringBuilder();
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* miliseconds */);
            conn.setConnectTimeout(15000 /* miliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK /*200*/) {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null)
                    resp.append(line + "\n");
            }
            else
                resp.append(R.string.erro_aceder_pagina + code);
        }
        catch (Exception e) {
            Log.d("testee","[ERROR] AsyncTaskWeather-getData() - Error: "+e);
        }
        return resp.toString();
    }

    /** Modifies the URL to access the API depending on the city */
    public String modifyURL(String urlBase, String city){
        String url;
        url = StringUtils.replace(urlBase,"<CIDADE>",city);
        url = StringUtils.replace(url,"<API_KEY>",API_KEY);

        //Suporte para lingua PT
        if( Locale.getDefault().getDisplayLanguage().contains("português"))
            url += "&lang=pt";
        return url;
    }

    public void cityBackground(ImageView view, String iconCode){
        switch(iconCode) {
            case "01d":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f01d);
                break;
            case "01n":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f01n);
                break;
            case "02d":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f02d);
                break;
            case "02n":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f02n);
                break;
            case "03d":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f03d);
                break;
            case "03n":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f03n);
                break;
            case "04d":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f04d);
                break;
            case "04n":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f04n);
                break;
            case "09d":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f09d);
                break;
            case "09n":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f09n);
                break;
            case "10d":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f10d);
                break;
            case "10n":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f10n);
                break;
            case "11d":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f11d);
                break;
            case "11n":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f11n);
                break;
            case "13d":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f13d);
                break;
            case "13n":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f13n);
                break;
            case "50d":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f50d);
                break;
            case "50n":
                ((ImageView) view.findViewById(R.id.main_imagemCidade)).setImageResource(R.drawable.f50n);
                break;
            default:
                break;
        }
    }
}
