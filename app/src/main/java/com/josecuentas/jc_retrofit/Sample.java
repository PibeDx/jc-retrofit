package com.josecuentas.jc_retrofit;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by José Norberto Cuentas Turpo
 * Email: <jcuentast@gmail.com> on 3/03/16.
 */
public class Sample {

    public static void main(String[] args) throws Exception{
        OkHttpClient client = new OkHttpClient();
//        client.interceptors().add(new LoggingInterceptor());

        Retrofit retrofit = new Retrofit.Builder().baseUrl(IPService.END)//.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        retrofit.create(IPService.class)
                .getIPInfo("58.19.239.11")
//                .observeOn(AndroidSchedulers.mainThread())
                .filter(jsonObject -> jsonObject.get("code").getAsInt()==0)
                .map(jsonObject1 -> jsonObject1.get("data"))
                .subscribe(System.out::println);
        return;
    }

    public interface IPService {
        String END = "http://ip.taobao.com";

        @GET("/service/getIpInfo.php")
        Observable<JsonObject> getIPInfo(@Query("ip") String ip);

    }


    public static class LoggingInterceptor implements Interceptor {
        @Override public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            System.out.println(
                    String.format("Sending request %s on %s%n%s", request.url(), chain.connection(),
                            request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            System.out.println(
                    String.format("Received response for %s in %.1fms%n%s", response.request().url(),
                            (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
