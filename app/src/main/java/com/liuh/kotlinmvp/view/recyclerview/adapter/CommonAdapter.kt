package com.liuh.kotlinmvp.view.recyclerview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.liuh.kotlinmvp.view.recyclerview.MultipleType
import com.liuh.kotlinmvp.view.recyclerview.MyViewHolder

/**
 * Date: 2018/8/14 16:21
 * Description: 通用的Adapter
 */
abstract class CommonAdapter<T>(var mContext: Context, var mData: ArrayList<T>,//条目布局
                                private var mLayoutId: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var mInflater: LayoutInflater? = null
    private var mTypeSupport: MultipleType<T>? = null

    //使用接口回调点击事件
    private var mItemClickListener: MyOnItemClickListener? = null

    //使用接口回调长按点击事件
    private var mItemLongClickListener: MyOnItemLongClickListener? = null

    init {
        mInflater = LayoutInflater.from(mContext)
    }

    //需要多布局
    constructor(context: Context, data: ArrayList<T>, typeSupport: MultipleType<T>) : this(context, data, -1) {
        this.mTypeSupport = typeSupport
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (mTypeSupport != null) {
            //需要多布局
            mLayoutId = viewType
        }
        //创建View
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return MyViewHolder(view!!)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    fun setOnItemClickListener(itemClickListener: MyOnItemClickListener) {
        this.mItemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(itemLongClickListener: MyOnItemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener
    }

}