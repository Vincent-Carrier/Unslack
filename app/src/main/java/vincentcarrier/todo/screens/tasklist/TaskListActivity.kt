package vincentcarrier.todo.screens.tasklist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable.Factory
import android.view.KeyEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_task_list.addTaskEditText
import kotlinx.android.synthetic.main.activity_task_list.taskList
import org.jetbrains.anko.toast
import vincentcarrier.todo.R.layout
import vincentcarrier.todo.data.TaskRepo
import vincentcarrier.todo.screens.PROJECT_ID
import vincentcarrier.todo.screens.dismissKeyboard

class TaskListActivity : AppCompatActivity() {

  private val vm by lazy {
    ViewModelProviders.of(this, TaskListVmFactory(TaskRepo(intent.extras.getLong(PROJECT_ID))))
        .get(TaskListViewModel::class.java)
  }

  private val disposables = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_task_list)

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

    taskList.adapter = vm.adapter
  }

  override fun onStart() {
    super.onStart()
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