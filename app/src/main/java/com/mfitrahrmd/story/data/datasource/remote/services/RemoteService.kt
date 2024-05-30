package com.mfitrahrmd.story.data.datasource.remote.services

import com.mfitrahrmd.story.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteService private constructor() {
    private var retrofit: Retrofit
    private var _authenticationService: AuthenticationService? = null
    val authenticationService: AuthenticationService
        get() {
            if (_authenticationService == null) {
                _authenticationService = retrofit.create(AuthenticationService::class.java)
            }

            return _authenticationService!!
        }
    private var _storyService: StoryService? = null
    val storyService: StoryService
        get() {
            if (_storyService == null) {
                _storyService = retrofit.create(StoryService::class.java)
            }

            return _storyService!!
        }

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor {
                // add 'Bearer' prefix if there is token in request header
                val token = it.request().header("Authorization")
                val requestBuilder = it.request().newBuilder()
                if (!token.isNullOrEmpty()) {
                    val bearerToken = "Bearer $token"
                    requestBuilder.addHeader("Authorization", bearerToken)
                }

                it.proceed(
                    requestBuilder.build()
                )
            }
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(NetworkResponseCallAdapterFactory())
            .build()
    }

    companion object {
        @Volatile
        private var INSTANCE: RemoteService? = null

        fun getInstance(): RemoteService {
            return INSTANCE ?: synchronized(this) {
                val instance = RemoteService()
                INSTANCE = instance

                instance
            }
        }
    }
}