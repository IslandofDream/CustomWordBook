package com.example.myengwordbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView


class MyItemRecyclerViewAdapter(
    private val values: ArrayList<MyWordData>
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    fun removeItem(pos:Int){ // 스와이프에 쓰일 아이
        values.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun refresh(){
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wordfragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.wordView.text = values[position].word
        holder.meaningView.text = values[position].meaning
        if(values[position].id == 0){ // 검색결과 혹은 북마크가없어서 리스트에 출력할게 없으면 버튼은 사라지게 한다.
            holder.button.visibility = View.GONE
        }
        else{
            holder.button.visibility = View.VISIBLE
        }

        holder.wordView.setOnClickListener {
            Toast.makeText(it.context,holder.wordView.text.toString(),Toast.LENGTH_SHORT).show()
            }

        holder.button.isChecked = values[position].checked == 1 // 토글버튼 속성 정하기

        holder.button.setOnCheckedChangeListener { buttonView, isChecked ->
            holder.mydbhelper = Mydbhelper(buttonView.context) // 토글 버튼 터치 이벤트
            if(isChecked){
                holder.mydbhelper.updatebookmark(values[position])
                values[position].checked = 1
            }
            else{
                holder.mydbhelper.uncheckedbookmark(values[position])
                values[position].checked = 0
            }

        }


        holder.meaningView.setOnClickListener {
            Toast.makeText(it.context,holder.meaningView.text.toString(),Toast.LENGTH_SHORT).show()
        }


    }



    override fun getItemCount(): Int = values.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordView: TextView = view.findViewById(R.id.word)
        val meaningView: TextView = view.findViewById(R.id.meaning)
        val button : ToggleButton = view.findViewById(R.id.toggleButton)
        lateinit var mydbhelper: Mydbhelper
        override fun toString(): String {
            return super.toString() + " '" + wordView.text + "'"
            return super.toString() + " '" + meaningView.text + "'"
        }

    }



}