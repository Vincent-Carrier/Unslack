package vincentcarrier.todo.screens.tasklist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable.Factory
import android.view.KeyEvent
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_task_list.addTaskEditText
import kotlinx.android.synthetic.main.activity_task_list.taskList
import vincentcarrier.todo.R.layout

class TaskListActivity : AppCompatActivity() {

  private val vm by lazy {
    ViewModelProviders.of(this, TaskListVmFactory()).get(TaskListViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_task_list)

    taskList.adapter = vm.adapter
    setUpEditText()

    vm.projectId = intent.extras.getLong("project_id")
  }

  override fun onStart() {
    super.onStart()
    vm.whenTasksLoaded().subscribeBy()
  }

  private fun setUpEditText() {
    with(addTaskEditText) {
      setOnEditorActionListener { _, _, event ->
        if ((event.keyCode == KeyEvent.KEYCODE_ENTER) and !text.isNullOrBlank()) {
          vm.addTask(text.toString())
          // clearComposingText() doesn't work for some reason
          text = Factory.getInstance().newEditable("")
          return@setOnEditorActionListener true
        } else return@setOnEditorActionListener false
      }
    }
  }
}