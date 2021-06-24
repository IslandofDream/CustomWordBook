package com.example.myengwordbook

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myengwordbook.databinding.Myquiz2Binding
import com.example.myengwordbook.databinding.MyquizBinding

import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class MyQuiz2 :Fragment() {
    lateinit var binding : Myquiz2Binding
    lateinit var mydbhelper : Mydbhelper
    lateinit var data: ArrayList<MyWordData>
    var number : Int = 0
    lateinit var quizlist : ArrayList<MyWordData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = Myquiz2Binding.inflate(layoutInflater,container,false)
        return binding.root.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            mydbhelper = Mydbhelper(context)
            data = mydbhelper.getALLRecord()
            quizlist = mydbhelper.getALLRecord()
            quizset()

            select1.setOnClickListener {
                correct(quizlist[0])
            }
            select2.setOnClickListener {
                correct(quizlist[1])
            }
            select3.setOnClickListener {
                correct(quizlist[2])
            }
            select4.setOnClickListener {
                correct(quizlist[3])
            }

            openanwser.setOnClickListener {
                when(number){
                    0-> select1.setBackgroundColor(Color.parseColor("#ff0000"))
                    1-> select2.setBackgroundColor(Color.parseColor("#ff0000"))
                    2-> select3.setBackgroundColor(Color.parseColor("#ff0000"))
                    3-> select4.setBackgroundColor(Color.parseColor("#ff0000"))
                }
            }

            hintbtn.setOnClickListener {
                hint(quizlist[number])
            }

            toggleButton2.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    mydbhelper.updatebookmark(quizlist[number])
                }
                else{
                    mydbhelper.uncheckedbookmark(quizlist[number])
                }
            }
        }
    }

    fun quizset(){
        quizlist.clear()
        for(i in 1..4){
            while (true) { // 중복제거
                data.shuffle()
                var temp = data[Random().nextInt(data.size)]
                if (!quizlist.contains(temp)){
                    quizlist.add(temp)
                    break
                }
            }
        }

        number = Random().nextInt(4)
        binding.apply{
            toggleButton2.isChecked = quizlist[number].checked == 1 // 북마크 토글버튼 연동
            hinttext.text = "힌트" // 힌트 텍스트뷰 초기화
            hinttext.background.setVisible(false,false)
            problem.text = quizlist[number].meaning
            select1.text = quizlist[0].word
            select1.setBackgroundColor(Color.parseColor("#338CB5"))
            select2.text = quizlist[1].word
            select2.setBackgroundColor(Color.parseColor("#338CB5"))
            select3.text = quizlist[2].word
            select3.setBackgroundColor(Color.parseColor("#338CB5"))
            select4.text = quizlist[3].word
            select4.setBackgroundColor(Color.parseColor("#338CB5"))
        }
    }

    fun correct(myWordData: MyWordData) {
        if(myWordData.word == quizlist[number].word){
            Toast.makeText(context,"정답입니다!",Toast.LENGTH_SHORT).show()
            binding.imageView.setImageResource(R.drawable.ic_baseline_mood_24)
            quizset()
        }
        else{
            binding.imageView.setImageResource(R.drawable.ic_baseline_mood_bad_24)
            Toast.makeText(context,"오답입니다!",Toast.LENGTH_SHORT).show()
        }
    }

    fun hint(myWordData: MyWordData){ //힌트 출력
        binding.hinttext.text = "" // 텍스트뷰 초기화

        var hint = ""
        val string = myWordData.word
        if(string.length == 1)

        else {
            while (true) {
                hint = ""
                for (i in 0..string.length - 1) {
                    if (Pattern.matches("^[ !@#\$%^&*(),.?\\\":{}|~<>]+$", string[i].toString())) // 특수문자는 블라인드 처리 안함
                        hint += string[i].toString()
                    else
                        if (Random().nextBoolean())
                            hint += "_"
                        else
                            hint += string[i].toString()
                }

                if (checkhint(hint)) // 모두 블라인드 처리된 경우
                    continue
                else
                    break
            }
            binding.hinttext.append(hint)
        }
    }

    fun checkhint(hint :String): Boolean{
        var num = 0
        for(word in hint){
            if(Pattern.matches("^[ㄱ-ㅎ가-힣a-zA-Z0-9]+\$",word.toString()))
                num++
        }
        return !hint.contains("_") || num == 0
    }


}
