package com.example.weatherapp.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weatherapp.Objetos.DadosMeteo;
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

/**
 * Asynctask que permite fazer o download de dados JSON relativos à meteorologia de
 * várias cidades, incluíndo a cidade corrente do utilizador
 *
 * @author Marco Seiça Fidalgo
 * @date 6/12/2020
 * @version 1.0
 * @copyright Projeto open-source
 *
 */
public class AsyncTaskTempo extends AsyncTask<String, Void, Object> {
    private static final String API_KEY = "6b09297fd3edfcc07d3df5c3fb286350";
    private static final String URL_OPENWEATHER = "https://api.openweathermap.org/data/2.5/weather?q=<CIDADE>&appid=<API_KEY>&units=metric";

    //private static final String URL_OPENWEATHER_IDS = "https://api.openweathermap.org/data/2.5/group?id=<CIDADE>&appid=<API_KEY>&units=metric";
    //private static final String LISTA_CIDADES = "{2267057,3117735,2968815,2009543,2618425,3169070,2643743,2964574,3067696,2761369}";

    private TextView tvCidade = null;
    private ListView lvCidades = null;
    private String cidade;
    private String[] listaCidades;


    public AsyncTaskTempo(TextView tvCidade, String cidade) {
        this.tvCidade = tvCidade;
        this.cidade = cidade;
    }

    public AsyncTaskTempo(ListView lvCidades, String[] listaCidades) {
        this.lvCidades = lvCidades;
        this.listaCidades = listaCidades;
    }


    @Override
    protected Object doInBackground(String... params) {
        String resultado = "(erro)";

        try {
            if(this.tvCidade != null) {
                resultado = getDadosCidade( trataURL(URL_OPENWEATHER,cidade) );
                return trataResultado(resultado);
            }
            else if(this.lvCidades != null){
                DadosMeteo[] dadosCidades = new DadosMeteo[listaCidades.length];
                for (int i = 0; i < listaCidades.length; i++) {
                    resultado = getDadosCidade( trataURL(URL_OPENWEATHER,listaCidades[i]));
                    dadosCidades[i] = trataResultado(resultado);
                }
                return dadosCidades;
            }
        } catch (Exception e) {
            Log.d("testee","[ERRO] doInBackground AsyncTaskTempo - Erro: "+e);
        }
        return resultado;
    }
    @Override
    protected void onPostExecute(Object objDados) {
        //SE RECEBER um DADOSMETEO é singular, se receber um array de dadosmeteo é a lista de cidades

        //Cidade Corrente
        if (objDados instanceof DadosMeteo) {
            DadosMeteo dados = (DadosMeteo)objDados;
            tvCidade.setText(cidade + "\n" + dados.getTemperatura() + "º");

        }
        //Lista de cidades
        else if(objDados instanceof DadosMeteo[]){
            DadosMeteo[] dados = (DadosMeteo[])objDados;
            //Corre todos os elementos, os que estiverem a null não vão para o adapter
            if(dados.length > 0) {
                //TODO: enviar os dados para a ListView
            }
            else {
                //nao encontrou nenhuma das cidades pretendidas
                tvCidade.setText("Não foi possível encontrar as cidades pretendidas");
            }
        }
        else{
            //Caso receba um null
            tvCidade.setText("Não foi possível encontrar a cidade corrente");
        }


    }

    //## Funções auxiliares

    /** Trata resultado do pedido devolvendo um objeto 'DadosMeteo' */
    public DadosMeteo trataResultado(String resultado){
        try {
            JSONObject obj = new JSONObject((String) resultado);
            // JSONArray lista = obj.getJSONArray("list");//Usado em Arrays [ ]
            //JSONObject obj1 = lista.getJSONObject(2);

            JSONArray  weather = obj.getJSONArray("weather");
            JSONObject  main = obj.getJSONObject("main"),
                        wind = obj.getJSONObject("wind");
            double temperatura = main.getDouble("temp");

            JSONObject o1 = new JSONObject();

            String a = (weather.getJSONObject(0)).getString("main");

            return new DadosMeteo(
                    (weather.getJSONObject(0)).getString("icon"),       // Ícone da condi. atmosférica
                    obj.getString("name"),                                     // Nome cidade
                    (weather.getJSONObject(0)).getString("main"),       // Condição
                    (weather.getJSONObject(0)).getString("description"),// Descrição da condição
                    main.getDouble("temp"),                                   // Temperatura atual
                    main.getDouble("feels_like"),                             // Sensação térmica
                    main.getDouble("temp_min"),                               // Temperatura mínima
                    main.getDouble("temp_max"),                               // Temepratura máxima
                    main.getInt("pressure"),                                  // Pressão atm.
                    main.getInt("humidity"),                                  // Humidade relativa
                    wind.getDouble("speed"));                                 // Velocidade vento
        }
        catch(JSONException je){
            je.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /** Faz o pedido ao API enviando o URL tratado e recebendo os dados JSON como String */
    String getDadosCidade(String endereco) {
        StringBuilder resp = new StringBuilder();
        try {
            URL url = new URL(endereco);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milisegundos */);
            conn.setConnectTimeout(15000 /* milisegundos */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int codigo = conn.getResponseCode();
            if (codigo == HttpURLConnection.HTTP_OK /*200*/) {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null)
                    resp.append(line + "\n");
            }
            else {
                resp.append(R.string.erro_aceder_pagina + codigo);
            }
        } catch (Exception e) {
            Log.d("testee","[ERRO] AsyncTaskTempo-getData() - Erro: "+e);
        }
        return resp.toString();
    }

    /** Modifica a URL para aceder à API consoante a cidade/API_KEY*/
    public String trataURL(String urlBase, String cidades){
        String url;
        url = StringUtils.replace(urlBase,"<CIDADE>",cidades);
        url = StringUtils.replace(url,"<API_KEY>",API_KEY);
        return url;
    }

}
