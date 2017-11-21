package vincentcarrier.todo.screens.tasklist

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable.Factory
import android.view.KeyEvent
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.activity_task_list.addTaskEditText
import kotlinx.android.synthetic.main.activity_task_list.taskList
import org.jetbrains.anko.toast
import vincentcarrier.todo.R.layout
import vincentcarrier.todo.screens.PROJECT_ID
import vincentcarrier.todo.screens.dismissKeyboard

class TasksActivity : KodeinAppCompatActivity() {

  private val vm: TasksViewModel by with(intent.extras.getLong(PROJECT_ID)).instance()
  private val scopeProvider: AndroidLifecycleScopeProvider by instance()

  @SuppressLint("MissingSuperCall") // Weird bug
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

  override fun onStart() {
    super.onStart()
    vm.whenTasksLoaded()
        .autoDisposeWith(scopeProvider)
        .subscribe({}, { toast(it.localizedMessage) })
  }
}