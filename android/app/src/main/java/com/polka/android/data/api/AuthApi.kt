package com.polka.android.data.api

import com.polka.android.data.network.dto.auth.LoginRequest
import com.polka.android.data.network.dto.auth.LoginResponse
import com.polka.android.data.network.dto.auth.LogoutRequest
import com.polka.android.data.network.dto.auth.RefreshRequest
import com.polka.android.data.network.dto.auth.RefreshResponse
import com.polka.android.data.network.dto.auth.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): RefreshResponse

    @POST("auth/logout")
    suspend fun logout(@Body request: LogoutRequest)
}