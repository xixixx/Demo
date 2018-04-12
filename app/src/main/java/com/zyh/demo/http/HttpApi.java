package com.zyh.demo.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author 占迎辉 (zhanyinghui@parkingwang.com)
 * @version 2018/4/8
 */

public class HttpApi {
    private final Call mCall;
    private CompleteHandler mCompleteTask;
    private ErrorHandler mErrorHandler;
    private SuccessHandler mSuccessHandler;
    private FailHandler mFailHandler;
    private HttpHandler httpHandler;
    private StartHandler startHandler;

    public static HttpApi of(Call call) {
        return new HttpApi(call);
    }

    public HttpApi(Call call) {
        mCall = call;
    }

    public HttpApi errorHandle(ErrorHandler errorHandler) {
        this.mErrorHandler = errorHandler;
        return this;
    }

    public HttpApi successHandle(SuccessHandler successHandler) {
        this.mSuccessHandler = successHandler;
        return this;
    }

    public HttpApi onFailHandle(FailHandler failHandler) {
        this.mFailHandler = failHandler;
        return this;
    }

    public HttpApi onHttpHandle(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
        return this;
    }

    public HttpApi completeHandler(CompleteHandler runnable) {
        this.mCompleteTask = runnable;
        return this;
    }

    public HttpApi startHandler(StartHandler startHandler) {
        this.startHandler = startHandler;
        return this;
    }

    private void complete() {
        if (mCompleteTask != null) {
            mCompleteTask.onComplete();
        }
    }


    public void call() {
        if (startHandler != null)
            startHandler.onStart();
        mCall.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    mErrorHandler.onErrors(e.toString());
                } finally {
                    complete();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        mFailHandler.onFile(response.code(), "json解析异常");
                    } else {
                        parseJson(response);
                        httpHandler.onSuccess(response.body().string());
                    }

                } catch (Exception e) {
                    mErrorHandler.onErrors(e.toString());
                } finally {
                    complete();
                }
            }
        });
    }


    private void parseJson(Response response) {
        JSONObject json = new JSONObject();
        final String text = readText(response);
        try {
            json = (JSONObject) JSON.parse(text);
        } catch (Exception e) {
            mErrorHandler.onErrors("json 解析异常");
        }
        if (mSuccessHandler != null) {
            mSuccessHandler.onSuccess(json);
        }

    }

    private String readText(Response response) {
        final ResponseBody body = response.body();
        try {
            if (body != null) {
                return body.string();
            } else {
                return "{}";
            }
        } catch (Exception e) {
            return "{}";
        }
    }
}
