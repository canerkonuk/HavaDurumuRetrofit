package com.example.androcaner.havadurumuretrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("forecast")
    Call<OpenWeatherForecastJson> getOpenWeather(@Query("lat")Double lat,@Query("lon")Double lon,@Query("lang")String lang,@Query("units")String units,@Query("appid")String appid);

    @GET("weather")
    Call<OpenWeather> getOpenWeatherCurrentWeather(@Query("lat")Double lat,@Query("lon")Double lon,@Query("lang")String lang,@Query("units")String units,@Query("appid")String appid);


}
