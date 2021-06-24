package com.example.myengwordbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.myengwordbook.databinding.QuizfragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class Quizfragment :Fragment() {

    val textarr = arrayListOf<String>("단어 해석 퀴즈","영단어 퀴즈")
    val iconarr = arrayListOf<Int>(R.drawable.ic_quiz_korean, R.drawable.ic_quiz_english)
    lateinit var binding: QuizfragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = QuizfragmentBinding.inflate(layoutInflater,container,false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = MyFragmentStatesApapter(this)
        TabLayoutMediator(binding.tablayout, binding.viewPager){
            tab, position ->
            tab.text= textarr[position]
            tab.setIcon(iconarr[position])
        }.attach()
    }


}