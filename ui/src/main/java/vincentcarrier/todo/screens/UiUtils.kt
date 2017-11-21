package vincentcarrier.todo.screens

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


fun Activity.dismissKeyboard() {
  val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  if(inputMethodManager.isAcceptingText)
    inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken,0)
}