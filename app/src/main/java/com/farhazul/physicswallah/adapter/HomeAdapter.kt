package com.farhazul.physicswallah.adapter

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farhazul.physicswallah.R
import com.farhazul.physicswallah.model.Teacher


class HomeAdapter(): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    var teacherList = emptyList<Teacher>()

    // ViewHolder Class
    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view) {

        // initializing widgets.
        val imgView = view.findViewById<ImageView>(R.id.imgview_teacher)
        val tvTeacherName = view.findViewById<TextView>(R.id.tv_teacher_name)
        val tvQualification = view.findViewById<TextView>(R.id.tv_teacher_sub_qulification)
        val btnViewMore = view.findViewById<Button>(R.id.btn_view_more)

        val cardView = view.findViewById<CardView>(R.id.card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_teacher_layout, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentTeacher = teacherList[position]
        holder.apply{
            Glide.with(holder.itemView.context).load(currentTeacher.profileImg).into(imgView)

            tvTeacherName.text = currentTeacher.name
            val qualification = currentTeacher.qulificationsToString()
            val subject = currentTeacher.subjectsToString()
            val dot = getSeperator(" . ", holder.itemView.context)
            tvQualification.setText( qualification, TextView.BufferType.EDITABLE)
            tvQualification.append(dot)
            tvQualification.append(subject)




            when (holder.itemView.context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> {}
                Configuration.UI_MODE_NIGHT_NO -> {

                    // adding shadow color if andorid verision is greater than or equal to Pie and  light theme is enabled.
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P){
                        cardView.outlineAmbientShadowColor = holder.itemView.context.resources.getColor(R.color.shadow_color)
                        cardView.outlineSpotShadowColor = holder.itemView.context.resources.getColor(R.color.shadow_color)

                    }
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
            }
        }
    }

    // Seperator function to seperate Teacher qualification with subject.
    fun getSeperator(text: String, context: Context) : SpannableStringBuilder{
        val colorSpan = ForegroundColorSpan(
            ContextCompat.getColor(context, R.color.light_grey)
        )
        val ss1 = SpannableStringBuilder(text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ss1.setSpan(RelativeSizeSpan(2f), 1, text.length-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        ss1.setSpan(colorSpan, 1, text.length-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss1
    }

    override fun getItemCount(): Int = teacherList.size

    // fuction to set data in teacherList
    fun setTeacherData(list: ArrayList<Teacher>){
        teacherList = list
        notifyDataSetChanged()
    }
}