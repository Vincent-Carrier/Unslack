package vincentcarrier.todo.data.remote

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.github.salomonbrys.kodein.with
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi.Builder
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import vincentcarrier.todo.data.models.User

private fun service(retrofit: Retrofit): TodoistApi = retrofit.create(TodoistApi::class.java)

private fun retrofit(
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

private fun converterFactory(): Converter.Factory {
  return MoshiConverterFactory.create(
      Builder()
          .add(KotlinJsonAdapterFactory())
          .build()
  )
}

private fun okHttpClient(token: String): OkHttpClient {
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

val webServiceModule = Kodein.Module {
  bind<TodoistApi>() with singleton { service(instance()) }
  bind() from singleton { retrofit(instance(), instance("base url"), instance()) }
  bind() from singleton { okHttpClient(User.accessToken) }
  bind() from singleton { converterFactory() }
  constant("base url") with "https://todoist.com/api/v7/"
}