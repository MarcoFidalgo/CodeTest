package com.example.weatherapp.CustomAdapters;
/**
 * Adapter that allows to link customized data to a view
 *
 * @author Marco Seiça Fidalgo
 * @date 6/12/2020
 * @version 1.0
 * @copyright Open-source Project
 *
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.weatherapp.DetalhesActivity;
import com.example.weatherapp.Objetos.DadosMeteo;
import com.example.weatherapp.R;

import java.io.Serializable;

public class CustomAdapterLVMain extends BaseAdapter implements Serializable, Parcelable {

    private Context context;
    private DadosMeteo[] dadosMeteo;
    private LayoutInflater inflater;

    public CustomAdapterLVMain(Context applicationContext, DadosMeteo[] dadosMeteo) {
        this.context = applicationContext;
        this.dadosMeteo = dadosMeteo;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    protected CustomAdapterLVMain(Parcel in) {
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Activity act = (Activity)context;
        view = inflater.inflate(R.layout.lv_row_mainactivity,parent, false); // inflate the layout

        // get current item to be displayed
        DadosMeteo dados = (DadosMeteo) getItem(position);

        //Altera os dados da vista
        ((TextView)view.findViewById(R.id.main_lv_nome)).setText(dados.getNomeCidade());
        ((TextView)view.findViewById(R.id.main_lv_temp)).setText(Math.round(dados.getTemperaturaMin()) + "º  " + Math.round(dados.getTemperaturaMax())+"º");
        trocaIcons(view, dados.getCodigoIcone());


        //Visibilidades
        ((ProgressBar)act.findViewById(R.id.barraLoading)).setVisibility(View.INVISIBLE);
        parent.setVisibility(View.VISIBLE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mudar activity
                Intent intent = new Intent(context, DetalhesActivity.class);
                Bundle b = new Bundle();
                intent.putExtra("cityData", dados);
                intent.putExtras(b); //Put your id to your next Intent
                context.startActivity(intent);
            }
        });

        return view;
    }

    /** Troca a imagem dos icon consoante a condição meteo. da cidade da view*/
    public void trocaIcons(View view, String codigoIcone){
        switch(codigoIcone) {
            case "01d":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x01d);
                break;
            case "01n":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x01n);
                break;
            case "02d":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x02d);
                break;
            case "02n":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x02n);
                break;
            case "03d":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x03d);
                break;
            case "03n":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x03n);
                break;
            case "04d":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x04d);
                break;
            case "04n":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x04n);
                break;
            case "09d":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x09d);
                break;
            case "09n":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x09n);
                break;
            case "10d":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x10d);
                break;
            case "10n":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x10n);
                break;
            case "11d":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x11d);
                break;
            case "11n":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x11n);
                break;
            case "13d":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x13d);
                break;
            case "13n":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x13n);
                break;
            case "50d":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x50d);
                break;
            case "50n":
                ((ImageView) view.findViewById(R.id.main_lv_condicao)).setImageResource(R.drawable.x50n);
                break;
            default:
                break;
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public static final Creator<CustomAdapterLVMain> CREATOR = new Creator<CustomAdapterLVMain>() {
        @Override
        public CustomAdapterLVMain createFromParcel(Parcel in) {
            return new CustomAdapterLVMain(in);
        }

        @Override
        public CustomAdapterLVMain[] newArray(int size) {
            return new CustomAdapterLVMain[size];
        }
    };
}
