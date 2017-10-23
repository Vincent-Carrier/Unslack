package vincentcarrier.todo.screens.projectlist

import android.support.constraint.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import kotlinx.android.synthetic.main.project_list_item.view.projectItemTextView
import vincentcarrier.todo.R

@EpoxyModelClass(layout = R.layout.project_list_item)
abstract class ProjectItemViewModel : EpoxyModel<ConstraintLayout>(){
  @EpoxyAttribute  var name = ""
  @EpoxyAttribute(DoNotHash) var openProject = {}

  override fun bind(view: ConstraintLayout) {
    with(view) {
      projectItemTextView.text = name
      setOnClickListener { openProject.invoke() }
    }
  }
}