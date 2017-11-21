package vincentcarrier.todo.screens.projectlist

import android.annotation.SuppressLint
import android.os.Bundle
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import com.github.salomonbrys.kodein.instance
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposeWith
import kotlinx.android.synthetic.main.activity_project_list.fab
import kotlinx.android.synthetic.main.activity_project_list.toolbar
import kotlinx.android.synthetic.main.content_project_list.projectList
import org.jetbrains.anko.toast
import vincentcarrier.todo.R

class ProjectsActivity : KodeinAppCompatActivity() {

  private val vm: ProjectsViewModel by instance()
  private val scopeProvider: AndroidLifecycleScopeProvider by instance()

  @SuppressLint("MissingSuperCall")
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
