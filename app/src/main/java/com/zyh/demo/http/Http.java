package com.zyh.demo.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/8
 */

public class Http {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final int CONNECTION_TIMEOUT_SECODE = 20;
    private static final int READ_TIMEOUT_SECODE = 20;
    private static final int WRITE_TIMEOUT_SECODE = 20;
    private static final OkHttpClient INSTANCE = new OkHttpClient.Builder()
            .addInterceptor(new LogInterceptor())
            .addInterceptor(new HeadInterceptor())
            .connectTimeout(CONNECTION_TIMEOUT_SECODE, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECODE, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECODE, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();

    public static HttpApi get(String url) {
        final OkHttpClient client = getClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return HttpApi.of(client.newCall(request));
    }

    public static OkHttpClient getClient() {
        return INSTANCE;
    }


}
