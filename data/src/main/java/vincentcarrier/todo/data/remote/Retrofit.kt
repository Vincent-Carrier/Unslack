package vincentcarrier.todo.data.remote

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi.Builder
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal fun service(retrofit: Retrofit): TodoistApi = retrofit.create(TodoistApi::class.java)

internal fun retrofit(
    client: OkHttpClient,
    baseUrl: String,
    converterFactory: Converter.Factory
): Retrofit {
  return Retrofit.Builder()
      .client(client)
      .baseUrl(baseUrl)
      .addConverterFactory(converterFactory)
      .addCallAdapterFactory(
          RxJava2CallAdapterFactory
              .createWithScheduler(Schedulers.io()))
      .build()
}

internal fun converterFactory(): Converter.Factory {
  return MoshiConverterFactory.create(
      Builder()
          .add(KotlinJsonAdapterFactory())
          .build()
  )
}

internal fun okHttpClient(token: String): OkHttpClient {
  return OkHttpClient.Builder()
      .addInterceptor { chain ->
        val original = chain.request()
        val url = original.url().newBuilder().addQueryParameter("token", token).build()
        val request = original.newBuilder().url(url).build()
        chain.proceed(request)
      }
//      .addInterceptor(HttpLoggingInterceptor().apply {
//        level = if (BuildConfig.DEBUG) BASIC else NONE
//      })
      .build()
}