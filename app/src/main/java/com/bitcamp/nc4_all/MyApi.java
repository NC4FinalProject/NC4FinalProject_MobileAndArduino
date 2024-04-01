package com.bitcamp.nc4_all;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyApi {
    @POST("/member/login")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);
}
