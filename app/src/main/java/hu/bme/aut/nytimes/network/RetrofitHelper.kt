package hu.bme.aut.nytimes.network

import hu.bme.aut.nytimes.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    private const val baseUrl = "https://api.nytimes.com/"

    fun getInstance(): Retrofit {

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .addInterceptor {
                val original = it.request()
                val httpUrl = original.url()

                val newHttpUrl = httpUrl.newBuilder().addQueryParameter("api-key", BuildConfig.API_KEY).build()

                val request = original.newBuilder().url(newHttpUrl).build()
                it.proceed(request)
            }
            .build()

        return Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}