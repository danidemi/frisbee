package org.gdg.frisbee.android.api;

import org.gdg.frisbee.android.utils.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class GroupDirectoryFactory {

    private static final String BASE_URL = "https://developers.google.com";

    private GroupDirectoryFactory() {
    }

    private static Retrofit provideRestAdapter(OkHttpClient okHttpClient) {
        OkHttpClient.Builder client = okHttpClient.newBuilder();
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request compressedRequest = chain.request().newBuilder()
                    .header("User-Agent", "GDG-Frisbee/0.1 (Android)")
                    .header("Referrer", "https://developers.google.com/groups/directory/")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("Cache-Control", "no-cache")
                    .header("DNT", "1")
                    .build();
                return chain.proceed(compressedRequest);
            }
        });

        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create(Utils.getGson()))
            .build();
    }

    public static GroupDirectory provideGroupDirectoryApi(OkHttpClient okHttpClient) {
        return provideRestAdapter(okHttpClient).create(GroupDirectory.class);
    }
}
