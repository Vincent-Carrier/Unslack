package vincentcarrier.todo.screens.tasklist

import android.support.constraint.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import kotlinx.android.synthetic.main.task_list_item.view.taskCompletedButton
import kotlinx.android.synthetic.main.task_list_item.view.taskItemTextView
import vincentcarrier.todo.R

@EpoxyModelClass
abstract class TaskItemViewModel : EpoxyModel<ConstraintLayout>(){
  @EpoxyAttribute var name = ""
  @EpoxyAttribute(DoNotHash) var completeTask = {}

  override fun bind(view: ConstraintLayout) {
    with(view) {
      taskItemTextView.text = name
      taskCompletedButton.setOnClickListener { completeTask() }
    }
  }

  override fun getDefaultLayout() = R.layout.task_list_item
}

