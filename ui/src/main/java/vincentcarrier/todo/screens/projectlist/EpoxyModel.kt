package vincentcarrier.todo.screens.projectlist

import android.support.constraint.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import kotlinx.android.synthetic.main.project_list_item.view.projectItemTextView
import org.jetbrains.anko.startActivity
import vincentcarrier.todo.R
import vincentcarrier.todo.screens.PROJECT_ID
import vincentcarrier.todo.screens.tasklist.TasksActivity

@EpoxyModelClass
abstract class ProjectItemViewModel : EpoxyModel<ConstraintLayout>() {
  @EpoxyAttribute var name = ""
  @EpoxyAttribute var projectId: Long? = null

  override fun bind(view: ConstraintLayout) {
    with(view) {
      projectItemTextView.text = name
      setOnClickListener {
        context.startActivity<TasksActivity>(PROJECT_ID to projectId!!)
      }
    }
  }

  override fun getDefaultLayout() = R.layout.project_list_item
}