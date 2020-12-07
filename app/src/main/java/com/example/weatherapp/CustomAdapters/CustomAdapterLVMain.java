package com.example.weatherapp.CustomAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weatherapp.DetalhesActivity;
import com.example.weatherapp.Objetos.DadosMeteo;
import com.example.weatherapp.R;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomAdapterLVMain extends BaseAdapter implements Serializable {

    private Context context;
    private DadosMeteo[] dadosMeteo;
    private LayoutInflater inflater;

    public CustomAdapterLVMain(Context applicationContext, DadosMeteo[] dadosMeteo) {
        this.context = applicationContext;
        this.dadosMeteo = dadosMeteo;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.lv_row_mainactivity,parent, false); // inflate the layout

        // get current item to be displayed
        DadosMeteo dados = (DadosMeteo) getItem(position);

        //Altera os dados da vista
        ((TextView)view.findViewById(R.id.main_lv_nome)).setText(dados.getNomeCidade());
        ((TextView)view.findViewById(R.id.main_lv_condicao)).setText(dados.getCondicao());
        ((TextView)view.findViewById(R.id.main_lv_temp)).setText(Math.round(dados.getTemperaturaMin()) + "º  " + Math.round(dados.getTemperaturaMax())+"º");


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mudar activity

                Intent intent = new Intent(context, DetalhesActivity.class);
                Bundle b = new Bundle();
                intent.putExtra("dadosCidade", dados);
                intent.putExtras(b); //Put your id to your next Intent
                context.startActivity(intent);

            }
        });


        return view;
    }

    @Override
    public int getCount() {
        return dadosMeteo.length;
    }

    @Override
    public Object getItem(int i) {
        return dadosMeteo[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


}
