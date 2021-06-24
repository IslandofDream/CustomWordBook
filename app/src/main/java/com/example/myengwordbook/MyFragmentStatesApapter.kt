package com.example.myengwordbook

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

class MyFragmentStatesApapter(frgamentActivity: Fragment) : FragmentStateAdapter(frgamentActivity) { // 뷰페이저를 위한 어댑터 클래스

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0-> MyQuiz()
            1-> MyQuiz2()
            else->MyQuiz()
        }

    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
    }
}