package vincentcarrier.todo.screens.redirect

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import vincentcarrier.todo.R
import vincentcarrier.todo.models.User

class RedirectActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_redirect)

    User.accessToken = Uri.parse(intent.dataString).getQueryParameter("access_token")
  }
}
