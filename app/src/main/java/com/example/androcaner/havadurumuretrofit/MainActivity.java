package com.example.androcaner.havadurumuretrofit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView bulunulanyer, derece, acıklama;
    private LocationManager locationManager;
    CircleImageView havadurumuicon;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;


    private final int REQUEST_ACCESS_FINE = 1;
    private static final String OPENWEATHER_API_KEY = "API_KEYIN_BULUNDUGU_YER";
    RetrofitInterface openweatherretrointerface;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bulunulanyer = findViewById(R.id.konum);
        derece = findViewById(R.id.derece);
        acıklama = findViewById(R.id.acıklama);
        havadurumuicon = findViewById(R.id.havaicon);
        recyclerView = findViewById(R.id.forecastrecycler);
        swipeRefreshLayout=findViewById(R.id.swiperefresh);





        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_FINE);

        }

        //NETWORK_PROVIDER, GPS_PROVIDER'DAN DAHA İYİ SONUÇ VERİYOR FAKAT EMULATORDE WİFİ BAĞLANTISI BULUNMADIĞI İÇİN İNTERNET ÜZERİNDEN KONUM BULUNAMUYOR. BU NEDENDEN DOLAYI GPS KULLANMAK ZORUNDA KALDIM...
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                final Double lat, lon;
                lat = location.getLatitude();
                lon = location.getLongitude();

                openweatherretrointerface = OpenWeatherClient.getRetrofitOpenWeatherClient().create(RetrofitInterface.class);
                Call<OpenWeather> openWeatherCurrentWeatherCall = openweatherretrointerface.getOpenWeatherCurrentWeather(lat, lon, "tr", "metric", OPENWEATHER_API_KEY);

                openWeatherCurrentWeatherCall.enqueue(new Callback<OpenWeather>() {
                    @Override
                    public void onResponse(Call<OpenWeather> call, Response<OpenWeather> response) {
                        List<Weather> weatherList = response.body().getWeather();

                        Float dereceintdeger = response.body().getMain().getTemp();

                        bulunulanyer.setText(response.body().getName());
                        derece.setText(dereceintdeger.intValue() + "°C");
                        acıklama.setText(weatherList.get(0).getDescription());
                        Picasso.with(getApplicationContext()).load("https://openweathermap.org/img/w/" + weatherList.get(0).getIcon() + ".png").placeholder(R.drawable.blabla).resize(100, 100).into(havadurumuicon);

                        
                        //Widgete veri gönderme:
                        Paper.init(getApplicationContext());
                        Paper.book().write("YER", response.body().getName());
                        Paper.book().write("DERECE", dereceintdeger);
                        Paper.book().write("ICON", weatherList.get(0).getIcon());


                    }

                    @Override
                    public void onFailure(Call<OpenWeather> call, Throwable t) {

                        System.out.println("BAŞARISIZCURRENT" + t.getLocalizedMessage());

                    }
                });


                openweatherretrointerface = OpenWeatherClient.getRetrofitOpenWeatherClient().create(RetrofitInterface.class);
                Call<OpenWeatherForecastJson> openWeatherCall = openweatherretrointerface.getOpenWeather(lat, lon, "tr", "metric", OPENWEATHER_API_KEY);


                openWeatherCall.enqueue(new Callback<OpenWeatherForecastJson>() {
                    @Override
                    public void onResponse(Call<OpenWeatherForecastJson> call, Response<OpenWeatherForecastJson> response) {
                        List<com.example.androcaner.havadurumuretrofit.List> list = response.body().getList();


                        HavaDurumuAdapter havaDurumuAdapter = new HavaDurumuAdapter(list, getApplicationContext());
                        recyclerView.setAdapter(havaDurumuAdapter);


                    }

                    @Override
                    public void onFailure(Call<OpenWeatherForecastJson> call, Throwable t) {

                        System.out.println("BAŞARISIZ:" + t.getLocalizedMessage());

                    }
                });


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

        //Emulator üzerinde uygulama açıldığında ayarlardan gps konumu göndermemiz gerekiyor, swipetorefresh yapılınca da aynı şekilde ayarlardan telefona konum göndermemiz gerek yoksa veriler yüklenemiyor...(Emulatorden kaynaklı sorun...)
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent=getIntent();
                finish();
                startActivity(intent);
            }
        });



    }



}
