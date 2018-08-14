package com.liuh.kotlinmvp.view.recyclerview

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View

@Suppress("UNCHECKED_CAST")
/**
 * Date: 2018/8/14 16:57
 * Description:
 */
class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    //用于缓存已存在的界面
    private var mView: SparseArray<View>? = null

    init {
        mView = SparseArray()
    }

    fun <T : View> getView(viewId: Int): T {
        //对已有的view做缓存
        var view: View? = mView?.get(viewId)

        return view as T
    }


}