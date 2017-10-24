package vincentcarrier.todo.models

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationRequest.Builder
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import vincentcarrier.todo.screens.projectlist.ProjectListActivity


object User {
  var accessToken = "8a41076f7f20434368b2873dfe4d691313acface"

  fun isLoggedIn() = accessToken.isNotEmpty()
}

fun startLogin(context: Context) {
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

  val postAuthIntent = Intent(context, ProjectListActivity::class.java)
  val pendingIntent = PendingIntent.getActivity(context, request.hashCode(),
      postAuthIntent, 0)
  AuthorizationService(context).performAuthorizationRequest(request, pendingIntent)
}