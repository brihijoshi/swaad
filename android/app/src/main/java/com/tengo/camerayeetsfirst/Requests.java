package com.tengo.camerayeetsfirst;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by johnteng on 10/26/19.
 */

public interface Requests {

    interface PictureService {
        @POST("/api/test")
        @Multipart
        Call<ResponseBody> upload(@Part MultipartBody.Part image);
    }
}
