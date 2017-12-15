package vincentcarrier.todo.screens.projectlist

import android.annotation.SuppressLint
import android.os.Bundle
import com.github.salomonbrys.kodein.android.KodeinAppCompatActivity
import com.github.salomonbrys.kodein.instance
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_project_list.toolbar
import kotlinx.android.synthetic.main.content_project_list.projectList
import org.jetbrains.anko.toast
import vincentcarrier.todo.R

class ProjectsActivity : KodeinAppCompatActivity() {

  private val vm: ProjectsViewModel by instance()
  private val disposables = CompositeDisposable()

  @SuppressLint("MissingSuperCall")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_project_list)
    setSupportActionBar(toolbar)

    projectList.adapter = vm.adapter
  }

  override fun onResume() {
    super.onResume()
    vm.whenProjectsLoaded()
        .subscribeBy(
            onError = { toast(it.localizedMessage) }
        ).addTo(disposables)
  }

  override fun onPause() {
    super.onPause()
    disposables.clear()
  }
}
