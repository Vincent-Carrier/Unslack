package vincentcarrier.todo.data.models

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationRequest.Builder
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import org.joda.time.LocalDateTime

object User {
  var accessToken = ACCESS_TOKEN

  fun isLoggedIn() = accessToken.isNotEmpty()

  var lastSyncTime = LocalDateTime().minusMillis(Int.MAX_VALUE)
    private set

  fun syncCompleted() { lastSyncTime = LocalDateTime() }

  fun needsSyncing(seconds: Int = 10) = isLoggedIn() and lastSyncTime.isBefore(LocalDateTime().minusSeconds(seconds))
}

fun Context.startLogin(redirectActivity: Class<Activity>) {
  val config = AuthorizationServiceConfiguration(
      Uri.parse("https://todoist.com/oauth/authorize"),
      Uri.parse("https://todoist.com/oauth/access_token")
  )
  val clientId = "dfd9ed95d48943768c91ad9de6202262"
  val scope = "data:read_write"
  val state = "Beep beep boop"
  val redirectUri = Uri.parse("https://vincentcarrier.todo/oauthcallback")
  val request = Builder(config, clientId, AuthorizationRequest.RESPONSE_TYPE_CODE, redirectUri)
      .setScope(scope)
      .setState(state)
      .build()

  val postAuthIntent = Intent(this, redirectActivity)
  val pendingIntent = PendingIntent.getActivity(this, request.hashCode(),
      postAuthIntent, 0)
  AuthorizationService(this).performAuthorizationRequest(request, pendingIntent)
}