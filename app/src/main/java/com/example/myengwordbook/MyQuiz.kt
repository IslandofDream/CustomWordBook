package com.example.myengwordbook

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.myengwordbook.databinding.MyquizBinding
import java.util.Random
import java.util.regex.Pattern

class MyQuiz : Fragment(){

    lateinit var binding :MyquizBinding
    lateinit var mydbhelper : Mydbhelper
    lateinit var data: ArrayList<MyWordData>
    var number : Int = 0
    lateinit var quizlist : ArrayList<MyWordData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MyquizBinding.inflate(layoutInflater,container,false)
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
            while (true) { // ????????????
                data.shuffle() //????????? ?????? ??????
                var temp = data[Random().nextInt(data.size)] // ????????? ????????? ??????
                Log.d("asdf",temp.toString())
                if (!quizlist.contains(temp)){
                    quizlist.add(temp)
                    break
                    }
                }
            }

        number = Random().nextInt(4)
        binding.apply{
            toggleButton2.isChecked = quizlist[number].checked == 1 // ????????? ???????????? ??????
            hinttext.text = "??????" // ?????? ???????????? ?????????
            hinttext.background.setVisible(false,false)
            problem.text = quizlist[number].word // ?????? ??????
            select1.text = quizlist[0].meaning
            select1.setBackgroundColor(Color.parseColor("#338CB5"))
            select2.text = quizlist[1].meaning
            select2.setBackgroundColor(Color.parseColor("#338CB5"))
            select3.text = quizlist[2].meaning
            select3.setBackgroundColor(Color.parseColor("#338CB5"))
            select4.text = quizlist[3].meaning
            select4.setBackgroundColor(Color.parseColor("#338CB5"))
        }
    }

    fun correct(myWordData: MyWordData) {
        if(myWordData.meaning == quizlist[number].meaning){
            Toast.makeText(context,"???????????????!",Toast.LENGTH_SHORT).show()
            binding.imageView.setImageResource(R.drawable.ic_baseline_mood_24)
            quizset()
        }
        else{
            binding.imageView.setImageResource(R.drawable.ic_baseline_mood_bad_24)
            Toast.makeText(context,"???????????????!",Toast.LENGTH_SHORT).show()
        }
    }

    fun hint(myWordData: MyWordData){ //?????? ??????
        binding.hinttext.text = "" // ???????????? ?????????

        var hint = ""
        val string = myWordData.meaning
        if(string.length == 1)

        else {
            while (true) {
                hint = ""
                for (i in 0..string.length - 1) {
                    if (Pattern.matches("^[ !@#\$%^&*(),.?\\\":{}|~<>]+$", string[i].toString())) // ??????????????? ???????????? ?????? ??????
                        hint += string[i].toString()
                    else
                        if (Random().nextBoolean()) // ???????????? _ ??????
                            hint += "_"
                        else
                            hint += string[i].toString()
                }

                if (checkhint(hint)) // ?????? ???????????? ????????? ?????? ?????? ???????????? ??????
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
            if(Pattern.matches("^[???-??????-???a-zA-Z0-9]+\$",word.toString()))
               num++
        }
        return !hint.contains("_") || num == 0 //?????? ????????? _????????? _??? ????????? ?????? ??????
    }

}