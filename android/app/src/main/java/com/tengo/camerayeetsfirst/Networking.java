package com.tengo.camerayeetsfirst;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by johnteng on 10/26/19.
 */

public class Networking {

    interface NetworkDelegate {
        void onSuccess(final Response response);
        void onFailure();
    }

    static void postRequest(final String postUrl, final RequestBody postBody, final NetworkDelegate delegate) {
        final OkHttpClient client = new OkHttpClient.Builder()
                .build();

        final Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        delegate.onFailure();
                        call.cancel();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        call.cancel();
                        delegate.onSuccess(response);
                    }
                });
    }
}
