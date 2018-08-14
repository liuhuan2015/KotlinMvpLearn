package com.liuh.kotlinmvp.view.recyclerview

/**
 * Date: 2018/8/14 16:29
 * Description: 多布局条目类型
 */
interface MultipleType<in T> {

    fun getLayoutId(item: T, position: Int): Int

}