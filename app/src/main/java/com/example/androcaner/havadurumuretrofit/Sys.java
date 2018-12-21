package com.example.androcaner.havadurumuretrofit;

public class Sys {
    private String country;
    private int sunrise;
    private int sunset;
    private String pod;

    public Sys(String country, int sunrise, int sunset, String pod) {
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.pod = pod;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }
}
