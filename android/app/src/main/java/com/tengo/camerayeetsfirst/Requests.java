package com.tengo.camerayeetsfirst;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by johnteng on 10/26/19.
 */

public interface Requests {

    interface GitHubService {
        @GET("users/{user}/repos")
        Call<List<String>> listRepos(@Path("user") String user);
    }

    interface SendPicture {
        @POST("/send")
        void upload(@Body RequestBody bytes, Callback<String> cb);
    }
}
