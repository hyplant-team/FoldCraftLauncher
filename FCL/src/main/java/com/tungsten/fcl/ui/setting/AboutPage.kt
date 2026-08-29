package com.tungsten.fcl.ui.setting

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.ui.adapter.SpacingItemDecoration
import com.mio.util.openLink
import com.tungsten.fcl.R
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fcl.databinding.ItemAboutBinding
import com.tungsten.fcl.databinding.ItemAboutDescBinding
import com.tungsten.fcl.databinding.PageSettingAboutBinding
import com.tungsten.fclcore.task.Task
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fcllibrary.component.ui.FCLPage

/**
 * 关于页：说明置顶，下方链接行合成一组（组内绘制分割线），
 * 行背景与分割线样式与版本设置页一致。
 */
class AboutPage(context: Context?, id: Int) : FCLPage(context, id, R.layout.page_setting_about) {

    private lateinit var binding: PageSettingAboutBinding

    init {
        create()
    }

    private fun create() {
        binding = PageSettingAboutBinding.bind(contentView)
        binding.aboutList.layoutManager = LinearLayoutManager(context)
        // 说明行与链接组之间留 8dp 间距，组内行间留 1dp 缝并绘制分割线
        val rowSpacing = (8 * context.resources.displayMetrics.density).toInt()
        val groupDivider = (1 * context.resources.displayMetrics.density).toInt()
        binding.aboutList.addItemDecoration(
            SpacingItemDecoration(
                rowSpacing,
                { parent, position ->
                    val adapter = parent.adapter as? AboutAdapter
                    if (adapter?.isNextInSameGroup(position) == true) groupDivider else rowSpacing
                },
                { ThemeEngine.getInstance().getTheme().getColor() }
            )
        )
        // 主题切换时重绘分割线颜色
        ThemeEngine.getInstance().registerEvent(binding.aboutList) {
            binding.aboutList.invalidate()
        }
        binding.aboutList.adapter = AboutAdapter { openLinkAt(it) }
    }

    override fun refresh(vararg param: Any?): Task<*>? {
        return null
    }

    private fun openLinkAt(linkIndex: Int) {
        val link = LINKS[linkIndex].link
        if (link != null) {
            openLink(context, link)
        }
    }

    /** 链接行：标题资源与跳转链接，条目顺序即显示顺序 */
    private data class LinkItem(val titleRes: Int, val link: String?)

    private class AboutAdapter(
        private val onLinkClick: (Int) -> Unit
    ) : RecyclerView.Adapter<AboutAdapter.Holder>() {

        override fun getItemCount(): Int = LINKS.size + 1

        override fun getItemViewType(position: Int): Int = if (position == 0) TYPE_DESC else TYPE_LINK

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val inflater = LayoutInflater.from(parent.context)
            val item = if (viewType == TYPE_DESC) {
                ItemAboutDescBinding.inflate(inflater, parent, false).root
            } else {
                ItemAboutBinding.inflate(inflater, parent, false).root
            }
            return Holder(item)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            // 行背景为圆角形状：链接行合成一组（组首上圆角/组尾下圆角/中间无圆角），
            // 颜色 tint 取主题浅色；组内行间缝隙绘制分割线
            holder.itemView.setBackgroundResource(
                when (position) {
                    0 -> R.drawable.bg_item_rounded
                    1 -> R.drawable.bg_item_rounded_top
                    itemCount - 1 -> R.drawable.bg_item_rounded_bottom
                    else -> R.drawable.bg_item_rounded_middle
                }
            )
            ThemeEngine.getInstance().unregisterEvent(holder.itemView)
            ThemeEngine.getInstance().registerEvent(holder.itemView) {
                holder.itemView.backgroundTintList =
                    ColorStateList.valueOf(ThemeEngine.getInstance().getTheme().ltColor)
            }
            if (position == 0) {
                holder.itemView.setOnClickListener(null)
                ItemAboutDescBinding.bind(holder.itemView).title.setText(R.string.about_desc)
            } else {
                holder.itemView.setOnClickListener {
                    onLinkClick(holder.bindingAdapterPosition - 1)
                }
                ItemAboutBinding.bind(holder.itemView).title.setText(LINKS[position - 1].titleRes)
            }
        }

        /** 供间距装饰器判断：链接行内相邻时行间留 1dp 缝（绘制分割线） */
        fun isNextInSameGroup(position: Int): Boolean =
            position > 0 && position + 1 < itemCount

        class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    companion object {
        private const val TYPE_DESC = 0
        private const val TYPE_LINK = 1
        private const val QQ_GROUP_KEY = "9_Mnxe5x1l6L7giLuRYQyBh0iWBgCUbw"

        private val LINKS = listOf(
            LinkItem(R.string.about_link_a, FCLPath.Prop.getProperty("about-link-a","null://")),
            LinkItem(R.string.about_link_b, FCLPath.Prop.getProperty("about-link-b","null://")),
            LinkItem(R.string.about_link_c, FCLPath.Prop.getProperty("about-link-c","null://")),
            LinkItem(R.string.about_link_d, FCLPath.Prop.getProperty("about-link-d","null://")),
            LinkItem(R.string.about_link_e, FCLPath.Prop.getProperty("about-link-e","null://"))
        )
    }
}