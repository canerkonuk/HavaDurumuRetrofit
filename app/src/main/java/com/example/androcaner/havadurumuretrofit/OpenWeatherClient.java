package com.example.androcaner.havadurumuretrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherClient {

    public static Retrofit retrofitOpenWeather;
    private static final String OpenWeatherBaseUrl="https://api.openweathermap.org/data/2.5/";

    public static Retrofit getRetrofitOpenWeatherClient(){
        if (retrofitOpenWeather==null){
            retrofitOpenWeather=new Retrofit.Builder()
                    .baseUrl(OpenWeatherBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient())
                    .build();
            return retrofitOpenWeather;
        }

        return retrofitOpenWeather;
    }
}
