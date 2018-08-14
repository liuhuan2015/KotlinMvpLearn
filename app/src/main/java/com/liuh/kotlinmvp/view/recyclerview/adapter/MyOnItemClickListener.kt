package com.liuh.kotlinmvp.view.recyclerview.adapter

/**
 * Date: 2018/8/14 16:39
 * Description: Adapter的条目点击事件
 */
interface MyOnItemClickListener {

    fun onItemClick(obj: Any?, position: Int)
}