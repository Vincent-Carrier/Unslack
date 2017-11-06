package vincentcarrier.todo.screens.projectlist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.activity_project_list.fab
import kotlinx.android.synthetic.main.activity_project_list.toolbar
import kotlinx.android.synthetic.main.content_project_list.projectList
import org.jetbrains.anko.toast
import vincentcarrier.todo.R

class ProjectListActivity : AppCompatActivity() {

  private val vm by lazy {
    ViewModelProviders.of(this, ProjectListVmFactory(application)).get(ProjectListViewModel::class.java)
  }

  private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_project_list)
    setSupportActionBar(toolbar)

    fab.setOnClickListener { TODO() }

    projectList.adapter = vm.adapter
  }

  override fun onStart() {
    super.onStart()
    vm.whenProjectsLoaded()
        .autoDisposeWith(scopeProvider)
        .subscribe({}, { toast(it.localizedMessage) })
  }
}
