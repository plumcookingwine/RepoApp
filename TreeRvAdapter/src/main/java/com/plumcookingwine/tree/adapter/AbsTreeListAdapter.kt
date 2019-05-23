package com.plumcookingwine.tree.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.plumcookingwine.tree.ItemStatus
import com.plumcookingwine.tree.dao.TreeListDao

import java.util.ArrayList

/**
 * Created by Kevin on 2019/1/22.
 */
abstract class AbsTreeListAdapter<K, V>(private val mTreeList: MutableList<TreeListDao<K, V>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var mGroupItemStatus: MutableList<Boolean>? = null

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(
        cGroup: (groupIndex: Int, position: Int) -> Unit,
        cSub: (subIndex: Int, groupIndex: Int, position: Int) -> Unit
    ) {

        mOnItemClickListener = object : OnItemClickListener {
            override fun onClickGroupItem(groupIndex: Int, position: Int) {
                cGroup(groupIndex, position)
            }

            override fun onClickSubItem(subIndex: Int, groupIndex: Int, position: Int) {
                cSub(subIndex, groupIndex, position)
            }
        }
    }

    interface OnItemClickListener {

        fun onClickGroupItem(groupIndex: Int, position: Int)

        fun onClickSubItem(subIndex: Int, groupIndex: Int, position: Int)
    }

    abstract fun groupLayoutId(): Int

    abstract fun subLayoutId(): Int

    abstract fun onBindGroupHolder(holder: GroupItemViewHolder, k: K?, groupIndex: Int, position: Int)

    abstract fun onBindSubHolder(holder: SubItemViewHolder, v: V, subIndex: Int, groupIndex: Int, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View
        val viewHolder: RecyclerView.ViewHolder

        if (viewType == ItemStatus.VIEW_TYPE_GROUP_ITEM) {
            view = inflater.inflate(groupLayoutId(), parent, false)
            viewHolder = GroupItemViewHolder(view)
        } else {
            view = inflater.inflate(subLayoutId(), parent, false)
            viewHolder = SubItemViewHolder(view)
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val itemStatus = getItemStatusByPosition(position)

        val data = mTreeList[itemStatus.groupItemIndex]

        val groupIndex = itemStatus.groupItemIndex
        val subIndex = itemStatus.subItemIndex

        if (itemStatus.viewType == ItemStatus.VIEW_TYPE_GROUP_ITEM) {
            @Suppress("UNCHECKED_CAST") val holder = viewHolder as AbsTreeListAdapter<K, V>.GroupItemViewHolder
            onBindGroupHolder(holder, data.groupDao, groupIndex, position)
            holder.itemView.setOnClickListener {
                mOnItemClickListener?.onClickGroupItem(groupIndex, position)
            }

        } else if (itemStatus.viewType == ItemStatus.VIEW_TYPE_SUB_ITEM) {
            @Suppress("UNCHECKED_CAST") val holder = viewHolder as AbsTreeListAdapter<K, V>.SubItemViewHolder
            onBindSubHolder(holder, data.subList[subIndex], subIndex, groupIndex, position)
            mOnItemClickListener?.onClickSubItem(subIndex, groupIndex, position)
        }

    }

    override fun getItemCount(): Int {
        var itemCount = 0
        if (null == mGroupItemStatus || 0 == mGroupItemStatus!!.size) {
            return itemCount
        }
        for (i in mTreeList.indices) {
            itemCount++
            val subSize = mTreeList[i].subList.size
            itemCount += if (mGroupItemStatus!![i]) {
                subSize
            } else {
                val minCount = mTreeList[i].minCount
                if (subSize > minCount) minCount else subSize
            }
        }
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return getItemStatusByPosition(position).viewType
    }

    fun setData(list: List<TreeListDao<K, V>>) {
        this.mTreeList.clear()
        this.mTreeList.addAll(list)
        initGroupItemStatus()
        notifyDataSetChanged()
    }

    fun toggleMoreHide(groupIndex: Int) {
        if (mTreeList[groupIndex].isEnableExpand) {
            mGroupItemStatus!![groupIndex] = !mGroupItemStatus!![groupIndex]
            notifyDataSetChanged()
        }
    }

    private fun initGroupItemStatus() {
        if (mGroupItemStatus == null) {
            mGroupItemStatus = ArrayList()
        }
        mGroupItemStatus!!.clear()
        for (item in mTreeList) {
            mGroupItemStatus!!.add(!item.isEnableExpand || item.isEnableExpand && item.isExpand)
        }
    }

    private fun getItemStatusByPosition(position: Int): ItemStatus {

        val itemStatus = ItemStatus()
        var itemCount = 0
        var i = 0

        while (i < mGroupItemStatus!!.size) {

            if (itemCount == position) {
                itemStatus.viewType = ItemStatus.VIEW_TYPE_GROUP_ITEM
                itemStatus.groupItemIndex = i
                break
            } else if (itemCount > position) {
                itemStatus.viewType = ItemStatus.VIEW_TYPE_SUB_ITEM
                itemStatus.groupItemIndex = i - 1
                val dao = mTreeList[i - 1]
                val subSize = dao.subList.size
                val temp = itemCount - subSize
                // Log.i("TAG", "\ntemp === " + temp + ";itemcount === " + itemCount + ";subsize === " + subSize + ";pos === " + position);
                if (!mGroupItemStatus!![i - 1]) {
                    val ind: Int = if (subSize > dao.minCount) {
                        position - temp - subSize + dao.minCount
                    } else {
                        position - temp
                    }
                    itemStatus.subItemIndex = ind
                } else {
                    itemStatus.subItemIndex = position - temp
                }
                break
            }
            val dao = mTreeList[i]
            val subSize = dao.subList.size
            val minCount = dao.minCount
            val realCount = if (subSize > minCount) minCount else subSize
            itemCount += realCount + 1
            if (mGroupItemStatus!![i]) {
                if (subSize > minCount) {
                    itemCount += subSize - minCount
                }
            }
            i++
        }

        if (i >= mGroupItemStatus!!.size) {
            itemStatus.viewType = ItemStatus.VIEW_TYPE_SUB_ITEM
            itemStatus.groupItemIndex = i - 1
            val subSize = mTreeList[i - 1].subList.size
            val minCount = mTreeList[i - 1].minCount
            val temp = itemCount - subSize
            //            Log.i("TAG", "\ntemp2 === " + temp + ";itemcount === " + itemCount + ";subsize === " + subSize + ";pos === " + position);
            if (!mGroupItemStatus!![i - 1]) {
                val ind: Int = if (subSize > minCount) {
                    position - temp - subSize + minCount
                } else {
                    position - temp
                }
                itemStatus.subItemIndex = ind
            } else {
                itemStatus.subItemIndex = position - temp
            }
        }

        return itemStatus
    }


    inner class GroupItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class SubItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

}
