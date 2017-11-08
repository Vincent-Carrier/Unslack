package vincentcarrier.todo.data.remote

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi.Builder
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import vincentcarrier.todo.BuildConfig
import vincentcarrier.todo.models.User

internal val todoistService: TodoistApi = TodoistService().create()

private class TodoistService {

  fun create(retrofit: Retrofit = retrofit()): TodoistApi = retrofit.create<TodoistApi>(TodoistApi::class.java)

  fun retrofit(
      client: OkHttpClient = okHttpClient(),
      baseUrl: String = "https://todoist.com/api/v7/",
      converterFactory: Converter.Factory = converterFactory()
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

  fun converterFactory(): Converter.Factory {
    return MoshiConverterFactory.create(
        Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    )
  }

  fun okHttpClient(token: String = User.accessToken): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
          val original = chain.request()
          val url = original.url().newBuilder().addQueryParameter("token", token).build()
          val request = original.newBuilder().url(url).build()
          chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
          level = if (BuildConfig.DEBUG) BASIC else NONE
        })
        .build()
  }
}