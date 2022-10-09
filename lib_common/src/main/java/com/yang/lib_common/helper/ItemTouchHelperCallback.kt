package com.yang.lib_common.helper

import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


/**
 * @ClassName ItemTouchHelperCallback
 *
 * @Description
 *
 * @Author 1
 *
 * @Date 2020/12/30 11:56
 */
class ItemTouchHelperCallback constructor(private val moveAndSwipedListener:MoveAndSwipedListener): ItemTouchHelper.Callback() {


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {


        return makeMovementFlags(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT
        )

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        moveAndSwipedListener.onMoved(viewHolder.absoluteAdapterPosition,target.absoluteAdapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        moveAndSwipedListener.onDelete(viewHolder.absoluteAdapterPosition)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT)
        viewHolder.itemView.alpha = 1f
        viewHolder.itemView.scaleX = 1f
        viewHolder.itemView.scaleY = 1f
    }
}