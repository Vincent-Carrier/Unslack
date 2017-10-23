package vincentcarrier.todo.screens.tasklist

import android.support.constraint.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import kotlinx.android.synthetic.main.task_list_item.view.taskItemTextView
import vincentcarrier.todo.R

@EpoxyModelClass(layout = R.layout.task_list_item)
abstract class TaskItemViewModel : EpoxyModel<ConstraintLayout>(){
  @EpoxyAttribute var name = ""

  override fun bind(view: ConstraintLayout) {
    with(view) {
      taskItemTextView.text = name
    }
  }
}

