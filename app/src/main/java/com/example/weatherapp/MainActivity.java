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

 import android.Manifest;
 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.content.pm.PackageManager;
 import android.graphics.drawable.Drawable;
 import android.location.Address;
 import android.location.Geocoder;
 import android.location.LocationManager;
 import android.media.Image;
 import android.net.ConnectivityManager;
 import android.net.NetworkInfo;
 import android.os.Bundle;
 import android.os.Parcelable;
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

 import com.example.weatherapp.AsyncTasks.AsyncTaskTempo;
 import com.google.android.gms.common.images.ImageManager;
 import com.google.android.gms.location.FusedLocationProviderClient;
 import com.google.android.gms.location.LocationServices;

 import org.apache.commons.lang3.StringUtils;

 import java.io.Serializable;
 import java.util.List;
 import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    private static final String[] LISTA_CIDADES = {"Lisboa", "Madrid", "Paris", "Berlim", "Copenhaga", "Roma", "Londres", "Dublin", "Praga", "Viena"};
    private boolean ativadadePrincipal;
    boolean gpsLigado = false;

    private String cidade = "Lisboa";   //Por defeito fica Lisboa


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pedePermissoes();
        verificaSeTemGPSLigado();

        //Lança asynctask para a Cidade Atual
        if(gpsLigado){
            /*NOTA: Como existe a impossibilidade de pesquisar por vários nomes de cidade mas apenas por IDs,
                    foi tomada a decisão de fazer 2 pedidos distintos.
                    1 para a cidade corrente e outro para as restantes cidades.

                    A solução alternativa com apenas um pedido, seria procurar pelo ID da cidade corrente
                    no fich. JSON disponibilizado no site da API e juntar aos IDS das restantes cidades*/

            //Se for a 1ª vez que corre
            if(savedInstanceState == null) {
                buscaCidadeCorr();
                buscaCidadesRestantes();
            }
            else{

            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Caso rode o ecrã, esconde o loading(pq volta a aparecer)
        if(((ListView)findViewById(R.id.main_listView)).getVisibility() == View.VISIBLE){
            ((ProgressBar)findViewById(R.id.barraLoading)).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state
        try {
            View aaa = this.findViewById(android.R.id.content).getRootView();
            outState.putString("nomeCidade", ((TextView) findViewById(R.id.main_cidadeCorrente)).getText().toString());
            outState.putString("tempCidade", ((TextView) findViewById(R.id.main_cidadeCorrente_temp)).getText().toString());
            outState.putSerializable("lista", (Serializable) ((ListView) findViewById(R.id.main_listView)).getAdapter());
            //outState.putSerializable("imagem",(Serializable) ((ImageView)findViewById(R.id.main_imagemCidade)).getDrawable());

            String a = "1";
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Read the state
        ((TextView)findViewById(R.id.main_cidadeCorrente)).setText( savedInstanceState.getString("nomeCidade") );
        ((TextView)findViewById(R.id.main_cidadeCorrente_temp)).setText( savedInstanceState.getString("tempCidade") );
        ((ListView)findViewById(R.id.main_listView)).setAdapter((ListAdapter) savedInstanceState.getSerializable("lista"));
        //((ImageView)findViewById(R.id.main_imagemCidade)).setImageDrawable((Drawable) savedInstanceState.getSerializable("imagem"));
    }

    /** Busca cidade corrente */
    public void buscaCidadeCorr() {
        ((ProgressBar)findViewById(R.id.barraLoading)).setVisibility(View.VISIBLE);
        try {
            FusedLocationProviderClient mLocationProvider = LocationServices.getFusedLocationProviderClient(this);
            mLocationProvider.getLastLocation().addOnSuccessListener(this, location -> {
                /*NOTA: Location pode ficar a null nestas situações:
                        https://droidbyme.medium.com/get-current-location-using-fusedlocationproviderclient-in-android-cb7ebf5ab88e
                        Não vou tratar estas situações agora
                */
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        Log.d("testee", "CIDADE: " + addresses.get(0).getLocality());
                        cidade = addresses.get(0).getLocality();

                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                        TextView tvCidadeCorr = findViewById(R.id.main_cidadeCorrente);
                        TextView tvCidadeCorrTemp = findViewById(R.id.main_cidadeCorrente_temp);
                        ImageView imagemCidade = findViewById(R.id.main_imagemCidade);
                        if (networkInfo != null && networkInfo.isConnected()) {
                            AsyncTaskTempo asyncTaskTempo = new AsyncTaskTempo(tvCidadeCorr,tvCidadeCorrTemp,imagemCidade,cidade);
                            asyncTaskTempo.execute();
                        } else
                            tvCidadeCorr.setText(R.string.sem_internet);
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

    /** Busca restantes cidades */
    public void buscaCidadesRestantes() {
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            ListView lvCidades = findViewById(R.id.main_listView);
            if (networkInfo != null && networkInfo.isConnected()) {
                AsyncTaskTempo asyncTaskTempo = new AsyncTaskTempo(this,lvCidades,LISTA_CIDADES);
                asyncTaskTempo.execute();
            }
            else
                ((TextView)findViewById(R.id.main_cidadeCorrente)).setText(R.string.sem_internet);
        }
        catch(SecurityException e){e.printStackTrace();}
    }

    /**
     * Aux: https://www.youtube.com/watch?v=TnYXQHvuPIw&ab_channel=codestance */
    private void verificaSeTemGPSLigado(){

        boolean estadoInternet = false,
                estadoGPS = false;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            estadoGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);//High accuracy
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {
            estadoInternet = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception e){e.printStackTrace();}

        if(!estadoGPS && !estadoInternet){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Localização desligada")
                    .setCancelable(false)
                    .setPositiveButton("Ligar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //redireciona o user para o painel de localização do dispositivo
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancelar",null)
                    .show();
        }
        else{
            gpsLigado = true;
        }


    }

    /** Pede Permissões de Localização */
    private void pedePermissoes(){
        //Permissões de localização em tempo real
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }

    }

    /** Trata os pedidos de permissões em runtime */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Quando dá permissões de localização pela 1ª vez, busca a localização de seguida
                    buscaCidadeCorr();
                    buscaCidadesRestantes();
                }
                else {
                    Toast.makeText(MainActivity.this, "#############Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            default:
                break;
        }

    }
}