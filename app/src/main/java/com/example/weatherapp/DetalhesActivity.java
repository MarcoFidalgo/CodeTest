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

public class DetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
    }
}