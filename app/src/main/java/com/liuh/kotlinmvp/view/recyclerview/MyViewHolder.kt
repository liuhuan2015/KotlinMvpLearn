package com.liuh.kotlinmvp.view.recyclerview

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

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
        //使用缓存的方式减少findViewById的次数
        if (view == null) {
            view = itemView.findViewById(viewId)
            mView?.put(viewId, view)
        }
        return view as T
    }

    fun <T : ViewGroup> getViewGroup(viewId: Int): T {
        //对已有的view做缓存
        var view: View? = mView?.get(viewId)
        //使用缓存的方式减少findViewById的次数
        if (view == null) {
            view = itemView.findViewById(viewId)
            mView?.put(viewId, view)
        }
        return view as T
    }

    //通用的功能进行封装：设置文本、设置条目点击事件、设置图片
    fun setText(viewId: Int, text: CharSequence): MyViewHolder {
        val view = getView<TextView>(viewId)
        view.text = "" + text
        return this
    }

    fun setHintText(viewId: Int, text: CharSequence): MyViewHolder {
        val view = getView<TextView>(viewId)
        view.hint = "" + text
        return this
    }

    /**
     * 设置本地图片
     */
    fun setImageResource(viewId: Int, resId: Int): MyViewHolder {
        val iv = getView<ImageView>(viewId)
        iv.setImageResource(resId)
        return this
    }

    /**
     * 加载图片资源路径
     */
    fun setImagePath(viewId: Int, imageLoader: HolderImageLoader): MyViewHolder {
        val iv = getView<ImageView>(viewId)
        imageLoader.loadImage(iv, imageLoader.path)
        return this
    }

    abstract class HolderImageLoader(val path: String) {
        /**
         * 需要去复写这个方法加载图片
         */
        abstract fun loadImage(iv: ImageView, path: String)
    }

    /**
     * 设置View的Visibility
     */
    fun setViewVisibility(viewId: Int, visibility: Int): MyViewHolder {
        getView<View>(viewId).visibility = visibility
        return this
    }

    /**
     * 设置条目点击事件
     */
    fun setOnItemClickListener(listener: View.OnClickListener) {
        itemView.setOnClickListener(listener)
    }

    /**
     * 设置条目长按事件
     */
    fun setOnItemLongClickListener(listener: View.OnLongClickListener) {
        itemView.setOnLongClickListener(listener)
    }

}