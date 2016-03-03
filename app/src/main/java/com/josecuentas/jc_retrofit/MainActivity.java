package com.josecuentas.jc_retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.gson.JsonElement;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);


        btnBuscar.setOnClickListener(v->{
//            OkHttpClient client = new OkHttpClient();
//            client.interceptors().add(new Sample.LoggingInterceptor());

            Retrofit retrofit = new Retrofit.Builder().baseUrl(Sample.IPService.END)//.client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            retrofit.create(Sample.IPService.class)
                    .getIPInfo("58.19.239.11")
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(jsonObject -> jsonObject.get("code").getAsInt()==0)
                    .map(jsonObject1 -> jsonObject1.get("data"))
                    .subscribe(new Subscriber<JsonElement>() {
                        @Override
                        public void onCompleted() {
                            System.out.println("Complete");
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(JsonElement jsonElement) {
                            System.out.println(jsonElement);
                        }
                    });

//                    .subscribe(System.out::println);
        });


    }


    public void onError(String error){
        System.out.println(error);
    }


}
