package com.example.weatherapp.Objetos;
/**
 * Object to store data about weather conditions
 *
 * @author Marco Seiça Fidalgo
 * @date 6/12/2020
 * @version 1.0
 * @copyright Open-source Project
 *
 */
import java.io.Serializable;

public class WeatherData implements Serializable {

    private String iconCode;       // The API gives a code for each weather icon. It will help differentiate images for different weather conditions
    private String cityName;       // City name
    private String condition;      // Main weather condition
    private String description;    // Description of the weather condition
    private double temperature;    // Temperature in Celcius
    private double feelsLike;      // Feels like, in Celcius
    private double minTemperature; // Min. Temperature in Celcius
    private double maxTemperature; // Max. Temperature in Celcius
    private int atmPressure;       // Atmospheric pressure in hPa (Hectopascal)
    private int humidity;          // Relative humidity in %
    private double windSpeed;      // Wind speed in Kph

    public WeatherData(String iconCode, String cityName, String condition, String description,
                       double temperature, double feelsLike, double minTemperature, double maxTemperature,
                       int atmPressure, int humidity, double windSpeed) {
        this.iconCode = iconCode;
        this.cityName = cityName;
        this.condition = condition;
        this.description = description;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.atmPressure = atmPressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    /** Getters e Setters */

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public int getAtmPressure() {
        return atmPressure;
    }

    public void setAtmPressure(int atmPressure) {
        this.atmPressure = atmPressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
}