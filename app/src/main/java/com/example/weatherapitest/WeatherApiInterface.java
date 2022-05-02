package com.example.weatherapitest;


//https://api.openweathermap.org/data/2.5/weather?lat=31.230391&lon=121.473701&appid=3bdb45244c9b2d4e6d4fbb5a5f176963

import retrofit2.Call;
import retrofit2.http.GET;

//turns HTTP API into a Java interface.
public interface WeatherApiInterface {
    //@GET表示是取数据
    @GET("weather?lat=31.230391&lon=121.473701&appid=3bdb45244c9b2d4e6d4fbb5a5f176963")
    Call<Root> getWeather();
}
