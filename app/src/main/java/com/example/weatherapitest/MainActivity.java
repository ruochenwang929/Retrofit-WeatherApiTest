package com.example.weatherapitest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.example.weatherapitest.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HTTP;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        int brightness = -35; //RGB偏移量，变暗为负数
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(matrix);


        //The Retrofit class generates an implementation of the weatherApi interface.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create()) //将取出来的数据转换成Gson
                .build();

        //create retrofit
        WeatherApiInterface weatherApiInterface = retrofit.create(WeatherApiInterface.class);

        Call<Root> call = weatherApiInterface.getWeather();

        //调用call方法时，拿到数据则用response，拿不到数据则用failure
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) { //response拿到的就是json文件
                Root root = response.body();
                double a = root.getMain().getTemp();
                int b = (int) (a - 273.15); //Change Kelvin to Celsius
                String temp = String.valueOf(b);
                binding.tempTextView.setText(temp + "℃");

                String str = root.getWeather().get(0).getMain();
                //str.equals("Snow") || str.equals("Extreme")
                if (str.equals("Rain"))
                {
                    binding.weatherImage.setImageDrawable(getResources().getDrawable(R.drawable.rain));
                    binding.mainTextView.setText(str + ", recommended indoor exercise");
                }
                else if (str.equals("Extreme"))
                {
                    binding.weatherImage.setImageDrawable(getResources().getDrawable(R.drawable.extreme));
                    binding.mainTextView.setText(str + ", recommended indoor exercise");
                }
                else if (str.equals("Snow"))
                {
                    binding.weatherImage.setImageDrawable(getResources().getDrawable(R.drawable.snow));
                    binding.weatherImage.setColorFilter(cmcf); //imageView为显示图片的View。
                    binding.mainTextView.setText(str + ", recommended indoor exercise");
                }
                else
                {
                    //clear
                    binding.weatherImage.setImageDrawable(getResources().getDrawable(R.drawable.clear));
                    binding.weatherImage.setColorFilter(cmcf); //imageView为显示图片的View。
                    binding.mainTextView.setText(str + ", recommended outdoor exercise");
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                System.out.println("Error," + t.getMessage());
            }

        });
    }
}