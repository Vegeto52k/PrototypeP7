package fr.vegeto52.prototypep7.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vegeto52-PC on 16/03/2023.
 */
public class RetrofitService {

    private static Retrofit sRetrofit;
    private static String NEAR_BY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/";

    public static final Retrofit getRetrofitInstance(){

        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        if(sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(NEAR_BY_SEARCH_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return sRetrofit;
    }
}
