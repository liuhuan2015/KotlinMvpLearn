package com.liuh.kotlinmvp.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import com.liuh.kotlinmvp.R
import com.liuh.kotlinmvp.view.recyclerview.MyViewHolder
import com.liuh.kotlinmvp.view.recyclerview.adapter.CommonAdapter
import io.reactivex.Observable
import mvp.model.bean.HomeBean

/**
 * Date: 2018/8/14 16:05
 * Description:首页精选的Adapter
 */
class HomeAdapter(context: Context, data: ArrayList<HomeBean.Issue.Item>)
    : CommonAdapter<HomeBean.Issue.Item>(context, data, -1) {

    // banner 作为 RecyclerView 的第一项
    var bannerItemSize = 0

    companion object {
        private val ITEM_TYPE_BANNER = 1   //Banner 类型
        private val ITEM_TYPE_TEXT_HEADER = 2  //textHeader
        private val ITEM_TYPE_CONTENT = 3 //item

    }

    //设置 Banner 大小
    fun setBannerSize(count: Int) {
        bannerItemSize = count
    }

    /**
     * 添加更多数据
     */
    fun addItemData(itemList: ArrayList<HomeBean.Issue.Item>) {
        this.mData.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 ->
                ITEM_TYPE_BANNER
            mData[position + bannerItemSize - 1].type == "textHeader" ->
                ITEM_TYPE_TEXT_HEADER
            else ->
                ITEM_TYPE_CONTENT
        }
    }

    override fun getItemCount(): Int {
        return when {
            mData.size > bannerItemSize -> mData.size - bannerItemSize + 1
            mData.isEmpty() -> 0
            else -> 1
        }
    }

    /**
     * 绑定布局
     */
    override fun bindData(holder: MyViewHolder, data: HomeBean.Issue.Item, position: Int) {
        when (getItemViewType(position)) {
        //banner
            ITEM_TYPE_BANNER -> {
                val bannerItemData: ArrayList<HomeBean.Issue.Item> = mData.take(bannerItemSize).toCollection(ArrayList())
                val bannerFeedList = ArrayList<String>()
                val bannerTitleList = ArrayList<String>()

                //取出banner
                Observable.fromIterable(bannerItemData)
                        .subscribe({ list ->
                            bannerFeedList.add(list.data?.cover?.feed ?: "")
                            bannerTitleList.add(list.data?.title ?: "")
                        })

                //设置banner
                with(holder) {
                    getView<BGABanner>(R.id.banner).run {
                        setAutoPlayAble(bannerFeedList.size > 1)
                        setData(bannerFeedList, bannerTitleList)
                        setAdapter(object : BGABanner.Adapter<ImageView, String> {
                            override fun fillBannerItem(p0: BGABanner?, p1: ImageView?, p2: String?, p3: Int) {


                            }
                        })
                    }
                }

                //没有使用到的参数在Kotlin中用 "_"代替
                holder.getView<BGABanner>(R.id.banner).setDelegate {

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return when (viewType) {
            ITEM_TYPE_BANNER ->
                MyViewHolder(inflateView(R.layout.item_home_banner, parent))
            ITEM_TYPE_TEXT_HEADER ->
                MyViewHolder(inflateView(R.layout.item_home_header, parent))
            else ->
                MyViewHolder(inflateView(R.layout.item_home_content, parent))
        }
    }

    /**
     * 加载布局
     */
    private fun inflateView(mLayoutId: Int, parent: ViewGroup): View {
        //创建View
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return view!!
    }


}