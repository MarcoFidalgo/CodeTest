package com.example.weatherapp;
/**
 * Secondary activity where it's possible to view in more detail,
 * data about weather conditions of a selected city in the main activity
 *
 * @author Marco Seiça Fidalgo
 * @date 6/12/2020
 * @version 1.0
 * @copyright Open-source Project
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.Objetos.WeatherData;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        // To retrieve object in second Activity
        WeatherData weatherData = (WeatherData) getIntent().getSerializableExtra("cityData");
        Log.d("testee","Changed activity");

        ((TextView)findViewById(R.id.det_nome_cidade)).setText(weatherData.getCityName()+"");
        ((TextView)findViewById(R.id.det_descricao)).setText(weatherData.getDescription()+"");
        ((TextView)findViewById(R.id.det_temp)).setText(Math.round(weatherData.getTemperature())+"º");
        ((TextView)findViewById(R.id.det_temp_sentida)).setText(Math.round(weatherData.getFeelsLike())+"º");
        ((TextView)findViewById(R.id.det_pressao_atm)).setText(weatherData.getAtmPressure()+" hPa");
        ((TextView)findViewById(R.id.det_vento)).setText(Math.round(weatherData.getWindSpeed())+" km/h");
        ((TextView)findViewById(R.id.det_humidade)).setText(weatherData.getHumidity()+" %");

        ImageView ivCidade = (ImageView)findViewById(R.id.det_imagemCidade);
        cityBackground(ivCidade, weatherData.getIconCode());
    }


    public void cityBackground(ImageView view, String iconCode){
        switch(iconCode) {
            case "01d":
                view.setImageResource(R.drawable.f01d);
                break;
            case "01n":
                view.setImageResource(R.drawable.f01n);
                break;
            case "02d":
                view.setImageResource(R.drawable.f02d);
                break;
            case "02n":
                view.setImageResource(R.drawable.f02n);
                break;
            case "03d":
                view.setImageResource(R.drawable.f03d);
                break;
            case "03n":
                view.setImageResource(R.drawable.f03n);
                break;
            case "04d":
                view.setImageResource(R.drawable.f04d);
                break;
            case "04n":
                view.setImageResource(R.drawable.f04n);
                break;
            case "09d":
                view.setImageResource(R.drawable.f09d);
                break;
            case "09n":
                view.setImageResource(R.drawable.f09n);
                break;
            case "10d":
                view.setImageResource(R.drawable.f10d);
                break;
            case "10n":
                view.setImageResource(R.drawable.f10n);
                break;
            case "11d":
                view.setImageResource(R.drawable.f11d);
                break;
            case "11n":
                view.setImageResource(R.drawable.f11n);
                break;
            case "13d":
                view.setImageResource(R.drawable.f13d);
                break;
            case "13n":
                view.setImageResource(R.drawable.f13n);
                break;
            case "50d":
                view.setImageResource(R.drawable.f50d);
                break;
            case "50n":
                view.setImageResource(R.drawable.f50n);
                break;
            default:
                break;
        }
    }
}