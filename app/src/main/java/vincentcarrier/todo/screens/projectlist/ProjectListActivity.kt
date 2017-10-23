package vincentcarrier.todo.screens.projectlist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_project_list.fab
import kotlinx.android.synthetic.main.activity_project_list.toolbar
import kotlinx.android.synthetic.main.content_project_list.projectList
import vincentcarrier.todo.R

class ProjectListActivity : AppCompatActivity() {

  private val vm by lazy {
    ViewModelProviders.of(this, ProjectListVmFactory(application)).get(ProjectListViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_project_list)
    setSupportActionBar(toolbar)

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show()
    }
    projectList.adapter = vm.adapter
  }

  override fun onStart() {
    super.onStart()
    vm.whenProjectsLoaded().subscribeBy()
  }

}
