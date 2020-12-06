package com.example.weatherapp.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.weatherapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Asynctask que permite fazer o download de dados json relativos à meteorologia de
 * várias cidades, incluíndo a cidade corrente do utilizador
 *
 * @author Marco Seiça Fidalgo
 * @date 6/12/2020
 * @version 1.0
 * @copyright Projeto open-source
 *
 */
public class AsyncTaskTempo extends AsyncTask<String, Void, String> {

    private TextView texto;
    private String cidade;


    public AsyncTaskTempo(TextView texto) {
        this.texto = texto;
    }


    @Override
    protected String doInBackground(String... params) {
        String resultado = "(erro)";
        try {
            resultado = getDados(params[0]);
            this.cidade = params[1];
        } catch (Exception e) {
            Log.d("testee","[ERRO] doInBackground AsyncTaskTempo - Erro: "+e);
        }
        return resultado;
    }
    @Override
    protected void onPostExecute(String resultado) {
        //texto.setText(resultado);
        try {
            JSONObject obj = new JSONObject(resultado);
           // JSONArray lista = obj.getJSONArray("list");//Usado em Arrays [ ]
            //JSONObject obj1 = lista.getJSONObject(2);
            JSONObject main = obj.getJSONObject("main");
            double temperatura = main.getDouble("temp");

            JSONObject o1 = new JSONObject();
            texto.setText(cidade+"\n"+temperatura+"º");
        }
        catch (JSONException e) {
            texto.setText("Erro a ler informacao JSON");
            Log.d("testee","[ERRO] onPostExecute AsyncTaskTempo - Erro: "+e);
        }


    }
    String getDados(String endereco) {
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
}
