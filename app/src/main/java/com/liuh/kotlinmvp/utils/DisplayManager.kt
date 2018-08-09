package com.liuh.kotlinmvp.utils

import android.content.Context
import android.util.DisplayMetrics
import com.orhanobut.logger.Logger

/**
 * Date: 2018/8/9 09:58
 * Description:
 *
 * val v1 =num?.toInt() //不做处理返回 null
 *
 * val v2 =num?.toInt() ?:0 //判断为空时返回0
 *
 * val v3 =num!!.toInt() //抛出空指针异常（用“!!”表示不能为空）
 *
 */
object DisplayManager {

    init {

    }

    private var displayMetrics: DisplayMetrics? = null

    private var screenWidth: Int? = null

    private var screenHeight: Int? = null

    private var screenDpi: Int? = null

    fun init(context: Context) {
        displayMetrics = context.resources.displayMetrics
        screenWidth = displayMetrics?.widthPixels
        screenHeight = displayMetrics?.heightPixels
        screenDpi = displayMetrics?.densityDpi

        Logger.i("screenWidth: " + screenWidth + " screenHeight: " + screenHeight + " screenDpi: " + screenDpi)
    }

    //UI图的大小
    private val STANDARD_WIDTH = 1080
    private val STANDARD_HEIGHT = 1920

    fun getScreenWidth(): Int? {
        return screenWidth
    }

    fun getScreenHeight(): Int? {
        return screenHeight
    }

    /**
     * 输入ui图的尺寸，输出实际的px，第二个参数是父布局
     *
     * @param px ui图中的大小
     * @param parentWidth 父View在ui图中的大小
     */
    fun getRealWidth(px: Int, parentWidth: Float): Int? {
        return (px / parentWidth * getScreenWidth()!!).toInt()
    }

    /**
     * 输入ui图的尺寸，输出实际的px，第二个参数是父布局
     *
     * @param px ui图中的大小
     * @param parentWidth 父View在ui图中的大小
     */
    fun getRealHeight(px: Int, parentHeight: Float): Int? {
        return (px / parentHeight * getScreenHeight()!!).toInt()
    }

    /**
     * 输入ui图中的高度尺寸，输出实际的px值
     *
     * @param px
     */
    fun getRealHeight(px: Int): Int? {
        return getRealHeight(px, STANDARD_HEIGHT.toFloat())
    }

    /**
     * dp转px
     *
     * @param dpValue 输入的dp值
     */
    fun dp2px(dpValue: Float): Int? {
        val scale = displayMetrics?.density
        return (dpValue * scale!! + 0.5f).toInt()
    }

}