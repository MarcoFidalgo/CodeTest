package com.example.weatherapp;
/**
 * Atividade secundária onde mostra detalhadamente os dados relativos à meteorologia de uma cidade escolhida
 * no ecrã principal
 *
 * @author Marco Seiça Fidalgo
 * @date 6/12/2020
 * @version 1.0
 * @copyright Projeto open-source
 *
 */
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.weatherapp.Objetos.DadosMeteo;

public class DetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        // To retrieve object in second Activity
        DadosMeteo dados = (DadosMeteo) getIntent().getSerializableExtra("dadosCidade");
        Log.d("testee","troquei ecra");


        ((TextView)findViewById(R.id.det_nome_cidade)).setText(dados.getNomeCidade()+"");
        ((TextView)findViewById(R.id.det_descricao)).setText(dados.getDescricao()+"");
        ((TextView)findViewById(R.id.det_temp)).setText(dados.getTemperatura()+"º");
        ((TextView)findViewById(R.id.det_temp_sentida)).setText(dados.getSensacaoTermica()+"º");
        ((TextView)findViewById(R.id.det_pressao_atm)).setText(dados.getPressaoAtm()+" hPa");
        ((TextView)findViewById(R.id.det_vento)).setText(dados.getVelocVento()+" km/h");
        ((TextView)findViewById(R.id.det_humidade)).setText(dados.getHumidade()+" %");


    }
}