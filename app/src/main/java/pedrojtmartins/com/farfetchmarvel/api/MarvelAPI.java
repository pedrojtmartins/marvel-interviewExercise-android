package pedrojtmartins.com.farfetchmarvel.api;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Pedro Martins
 * 20/06/2017
 */

public class MarvelAPI {
    private static final String apiPublicKey = "00a78323e41c1ffab9ebd8aad420260a";
    private static final String apiPrivateKey = "810b38ecc02f016062217b1ade9f8f2826018970";
    private static final String baseUrl = "http://gateway.marvel.com/";

    private static IMarvelAPI api;
    private MarvelAPI() {
    }

    public static IMarvelAPI getInstance() {
        if (api == null) {
            api = initApi();
        }

        return api;
    }

    private static IMarvelAPI initApi() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                HttpUrl originalHttpUrl = originalRequest.url();

                String timestamp = String.valueOf(System.currentTimeMillis());
                String toHash = timestamp + apiPrivateKey + apiPublicKey;
                String hash = md5(toHash);

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("ts", timestamp)
                        .addQueryParameter("apikey", apiPublicKey)
                        .addQueryParameter("hash", hash)
                        .build();

                Request newRequest = originalRequest.newBuilder().url(url).build();
                return chain.proceed(newRequest);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(IMarvelAPI.class);
    }

    private static String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
