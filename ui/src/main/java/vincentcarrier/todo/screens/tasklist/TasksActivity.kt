package vincentcarrier.todo.screens.tasklist

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable.Factory
import android.view.KeyEvent
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import com.github.salomonbrys.kodein.factory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_task_list.addTaskEditText
import kotlinx.android.synthetic.main.activity_task_list.taskList
import org.jetbrains.anko.toast
import vincentcarrier.todo.R.layout
import vincentcarrier.todo.screens.PROJECT_ID
import vincentcarrier.todo.screens.dismissKeyboard


class TasksActivity : KodeinAppCompatActivity() {

  // Needed in order to retrieve the intent during onCreate() and not before (causing a NPE)
  private val vmFactory: (Long) -> TasksViewModel by factory()
  private val vm by lazy { vmFactory(intent.getLongExtra(PROJECT_ID, 0)) }

  private val disposables = CompositeDisposable()

  @SuppressLint("MissingSuperCall") // Weird bug with KodeinAppCompatActivity, probably due to Android Studio
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_task_list)

    taskList.adapter = vm.adapter

    with(addTaskEditText) {
      setOnEditorActionListener { _, _, event ->
        if ((event.keyCode == KeyEvent.KEYCODE_ENTER) and !text.isNullOrBlank()) {
          vm.addTask(text.toString())
          text = Factory.getInstance().newEditable("") // clearComposingText() doesn't work for some reason
          dismissKeyboard()
          return@setOnEditorActionListener true
        } else return@setOnEditorActionListener false
      }
    }
  }

  override fun onResume() {
    super.onResume()
    vm.whenTasksLoaded()
        .subscribeBy(
            onError = { toast(it.localizedMessage) }
        ).addTo(disposables)
  }

  override fun onPause() {
    super.onPause()
    disposables.clear()
  }
}