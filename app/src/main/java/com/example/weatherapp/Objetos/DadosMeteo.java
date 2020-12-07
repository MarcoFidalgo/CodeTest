package com.example.weatherapp.Objetos;

import java.io.Serializable;

public class DadosMeteo implements Serializable {

    private String codigoIcone;     // A API fornece um código para ícones, vai ajudar para diferenciar imagens
    private String nomeCidade;      // Nome da cidade
    private String condicao;        // Condição meteorológica principal
    private String descricao;       // Descrição da condição meteorológica
    private double temperatura;     // Temperatura em graus celcius
    private double sensacaoTermica; // Sensação térmica em graus celcius
    private double temperaturaMin;  // Temperatura mínima em graus celcius
    private double temperaturaMax;  // Temperatura máxima em graus celcius
    private int pressaoAtm;         // Pressão atmosférica em hPa (Hectopascal)
    private int humidade;           // Humidade relativa em %
    private double velocVento;      // Velocidade do vento em Km/h

    public DadosMeteo(String codigoIcone, String nomeCidade, String condicao, String descricao,
                      double temperatura, double sensacaoTermica, double temperaturaMin, double temperaturaMax,
                      int pressaoAtm, int humidade, double velocVento) {
        this.codigoIcone = codigoIcone;
        this.nomeCidade = nomeCidade;
        this.condicao = condicao;
        this.descricao = descricao;
        this.temperatura = temperatura;
        this.sensacaoTermica = sensacaoTermica;
        this.temperaturaMin = temperaturaMin;
        this.temperaturaMax = temperaturaMax;
        this.pressaoAtm = pressaoAtm;
        this.humidade = humidade;
        this.velocVento = velocVento;
    }

    /** Getters e Setters */
    public String getCodigoIcone() {
        return codigoIcone;
    }

    public void setCodigoIcone(String codigoIcone) {
        this.codigoIcone = codigoIcone;
    }

    public String getNomeCidade() {
        return nomeCidade;
    }

    public void setNomeCidade(String nomeCidade) {
        this.nomeCidade = nomeCidade;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getSensacaoTermica() {
        return sensacaoTermica;
    }

    public void setSensacaoTermica(double sensacaoTermica) {
        this.sensacaoTermica = sensacaoTermica;
    }

    public double getTemperaturaMin() {
        return temperaturaMin;
    }

    public void setTemperaturaMin(double temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }

    public double getTemperaturaMax() {
        return temperaturaMax;
    }

    public void setTemperaturaMax(double temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }

    public int getPressaoAtm() {
        return pressaoAtm;
    }

    public void setPressaoAtm(int pressaoAtm) {
        this.pressaoAtm = pressaoAtm;
    }

    public int getHumidade() {
        return humidade;
    }

    public void setHumidade(int humidade) {
        this.humidade = humidade;
    }

    public double getVelocVento() {
        return velocVento;
    }

    public void setVelocVento(double velocVento) {
        this.velocVento = velocVento;
    }
}
