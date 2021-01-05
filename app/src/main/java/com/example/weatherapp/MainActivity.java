 package com.example.weatherapp;
/**
 * Main Activity that introduces the application. Shows an overview of the weather
 * in the user's current city.
 * Additionally it shows a list with an overview of 10 different cities
 *
 * @author Marco Seiça Fidalgo
 * @date 6/12/2020
 * @version 1.0
 * @copyright Open-source Project
 *
 */

 import android.Manifest;
 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.content.pm.PackageManager;
 import android.location.Address;
 import android.location.Geocoder;
 import android.location.LocationManager;
 import android.net.ConnectivityManager;
 import android.net.NetworkInfo;
 import android.os.Bundle;
 import android.provider.Settings;
 import android.util.Log;
 import android.view.View;
 import android.widget.ImageView;
 import android.widget.ListAdapter;
 import android.widget.ListView;
 import android.widget.ProgressBar;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.appcompat.app.AlertDialog;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.core.app.ActivityCompat;

 import com.example.weatherapp.AsyncTasks.AsyncTaskWeather;
 import com.google.android.gms.location.FusedLocationProviderClient;
 import com.google.android.gms.location.LocationServices;

 import java.io.Serializable;
 import java.util.List;
 import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Serializable{
    private static final String[] CITY_LIST = {"Lisboa", "Madrid", "Paris", "Berlim", "Copenhaga", "Roma", "Londres", "Dublin", "Praga", "Viena"};
    private boolean mainActivity;
    boolean gpsOn = false;

    private String city = "Lisboa";   //Default current city: Lisboa


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();
        //getGPSInternetState();
        Log.d("testee","Requested Permissions");
        //Launches AsyncTask for the current city
        if(gpsOn){
            Log.d("testee","GPS ON");
            //First time it runs
            if(savedInstanceState == null) {
                searchCurrCity();
                searchRemainingCities();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //When changing screen orientation, hides the loading circle
        if(((ListView)findViewById(R.id.main_listView)).getVisibility() == View.VISIBLE){
            ((ProgressBar)findViewById(R.id.barraLoading)).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state
        try {
            outState.putString("cityName", ((TextView) findViewById(R.id.main_cidadeCorrente)).getText().toString());
            outState.putString("cityTemp", ((TextView) findViewById(R.id.main_cidadeCorrente_temp)).getText().toString());
            outState.putSerializable("list", (Serializable) ((ListView) findViewById(R.id.main_listView)).getAdapter());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Read the state
        ((TextView)findViewById(R.id.main_cidadeCorrente)).setText( savedInstanceState.getString("cityName") );
        ((TextView)findViewById(R.id.main_cidadeCorrente_temp)).setText( savedInstanceState.getString("cityTemp") );
        ((ListView)findViewById(R.id.main_listView)).setAdapter((ListAdapter) savedInstanceState.getSerializable("list"));
    }

    /** Gets current city */
    public void searchCurrCity() {
        ((ProgressBar)findViewById(R.id.barraLoading)).setVisibility(View.VISIBLE);
        try {
            FusedLocationProviderClient mLocationProvider = LocationServices.getFusedLocationProviderClient(this);
            mLocationProvider.getLastLocation().addOnSuccessListener(this, location -> {
                /*NOTE: Location can happen to be null in these situations:
                        https://droidbyme.medium.com/get-current-location-using-fusedlocationproviderclient-in-android-cb7ebf5ab88e
                        I'm not going to protect against these situations for now
                */
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        Log.d("testee", "CITY: " + addresses.get(0).getLocality());
                        city = addresses.get(0).getLocality();

                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                        TextView tvCurrCity = findViewById(R.id.main_cidadeCorrente);
                        TextView tvCurrCityTemp = findViewById(R.id.main_cidadeCorrente_temp);
                        ImageView cityImage = findViewById(R.id.main_imagemCidade);
                        if (networkInfo != null && networkInfo.isConnected()) {
                            AsyncTaskWeather asyncTaskWeather = new AsyncTaskWeather(tvCurrCity,tvCurrCityTemp,cityImage,city);
                            asyncTaskWeather.execute();
                        } else
                            tvCurrCity.setText(R.string.sem_internet);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(e -> {
                Log.d("testee","addOnFailureListener");
            });
        }
        catch(SecurityException e){e.printStackTrace();}
    }

    /** Gets remaining cities from CITY_LIST */
    public void searchRemainingCities() {
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            ListView lvCities = findViewById(R.id.main_listView);
            if (networkInfo != null && networkInfo.isConnected()) {
                AsyncTaskWeather asyncTaskWeather = new AsyncTaskWeather(this,lvCities,CITY_LIST);
                asyncTaskWeather.execute();
            }
            else
                ((TextView)findViewById(R.id.main_cidadeCorrente)).setText(R.string.sem_internet);
        }
        catch(SecurityException e){e.printStackTrace();}
    }

    /**
     * LIXO LIXO //TODO APAGAR DEPOIS
     * Aux: https://www.youtube.com/watch?v=TnYXQHvuPIw&ab_channel=codestance */
    private void getGPSInternetState(){

        boolean internetState = false,
                gpsState = false;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            gpsState = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);//High accuracy
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {
            internetState = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception e){e.printStackTrace();}

        if(!gpsState && !internetState){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.gps_off)
                    .setCancelable(false)
                    .setPositiveButton(R.string.turn_on, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Redirects the user to the GPS configuration phone screen
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.cancel,null)
                    .show();
        }
        else
            gpsOn = true;
    }

    /** Requests Permissions of Location in real time*/
    private void requestPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
        else{
            gpsOn = true;
            Log.d("testee","permissions already granted");
        }
    }

    /** Handles permission requests in runtime */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //When the user gives GPS permissions for the first time, searches for the current city
                    //getGPSInternetState();
                    searchCurrCity();
                    searchRemainingCities();
                }
                else {
                    Toast.makeText(MainActivity.this, R.string.permission_denied_extstorage, Toast.LENGTH_SHORT).show();
                }
                return;
            default:
                break;
        }
    }
}