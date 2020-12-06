package com.example.weatherapp;
/**
 * Atividade principal de introdução à aplicação. Mostra Um resumo da meteorologia
 * da cidade corrente do utilizador.
 * Adicionalmente mostra uma lista com o resumo de 10 cidades diferentes.
 *
 * @author Marco Seiça Fidalgo
 * @date 6/12/2020
 * @version 1.0
 * @copyright Projeto open-source
 *
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.example.weatherapp.AsyncTasks.AsyncTaskTempo;

import org.apache.commons.lang3.StringUtils;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "6b09297fd3edfcc07d3df5c3fb286350";
    private static final String URL_OPENWEATHER = "https://api.openweathermap.org/data/2.5/weather?q=<CIDADE>&appid=<API_KEY>&units=metric";


    private String cidade = "Lisboa";   //Por defeito fica Lisboa


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvCidadeCorr = findViewById(R.id.main_cidadeCorrente);


    //Lança asynctask para a Cidade Atual
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            AsyncTaskTempo asyncTaskTempo = new AsyncTaskTempo(tvCidadeCorr);
            asyncTaskTempo.execute(trataURL(cidade), cidade);
        } else {
            tvCidadeCorr.setText(R.string.sem_internet);
        }
    }

    /** Busca cidade corrente */
    public void buscaCidadeCorr(){

    //Localização
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10,
                1, mLocationListener);

    }

    /** Modifica a URL para aceder à API consoante a cidade/API_KEY*/
    public String trataURL(String cidade){
        String url;
        url = StringUtils.replace(URL_OPENWEATHER,"<CIDADE>",cidade);
        url = StringUtils.replace(url,"<API_KEY>",API_KEY);
        return url;
    }

    /** TODO: Está certo? Sempre que é recebido um update de posição, corre esta função*/
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Log.d("testee","Loca NOVA");

        }
    };
}