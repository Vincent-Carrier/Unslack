package vincentcarrier.todo.data.remote

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vincentcarrier.todo.BuildConfig

internal class Retrofit {

  internal fun createTodoistApi(token: String) = todoistApi(retrofit(okHttpClient(token)))

  private fun todoistApi(retrofit: Retrofit): TodoistApi {
    return retrofit.create<TodoistApi>(
        TodoistApi::class.java)
  }

  private fun retrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .baseUrl("https://todoist.com/api/v7/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
  }

  private fun okHttpClient(token: String): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
          val original = chain.request()
          val url = original.url().newBuilder().addQueryParameter("token", token).build()
          val request = original.newBuilder().url(url).build()
          chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
          level = if (BuildConfig.DEBUG) BODY else NONE
        })
        .build()
  }
}