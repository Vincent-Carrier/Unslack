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
import vincentcarrier.todo.data.TaskRepository

class TaskListActivity : AppCompatActivity() {

  private val vm by lazy {
    ViewModelProviders.of(
        this, TaskListVmFactory(TaskRepository(intent.extras.getLong("project_id"))))
        .get(TaskListViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_task_list)

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

    taskList.adapter = vm.adapter

    vm.whenTasksLoaded().subscribeBy()
  }


}