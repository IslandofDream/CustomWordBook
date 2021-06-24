package com.example.myengwordbook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myengwordbook.databinding.DicfragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class DicParsing :Fragment() {
    lateinit var binding: DicfragmentBinding
    lateinit var mydbhelper: Mydbhelper
    var url = "https://dic.daum.net/search.do?q="
    var urlcheck =  false
    val scope = CoroutineScope(Dispatchers.IO)
    var mean :String = ""


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = DicfragmentBinding.inflate(layoutInflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mydbhelper = Mydbhelper(context)
        binding.apply {
            searchbtn.setOnClickListener {

                if(searchmeaning.length() != 0 && searchword.length() == 0){ // 의미칸에만 적혀있는 경우 단어 파싱
                    parsemeaning()
                    mean = ""
                }
                else if(searchmeaning.length() == 0 && searchword.length() != 0){ // 단어칸에만 적혀있는 경우 의미 파싱
                    parseword()
                    mean = ""
                }
                else{
                    Toast.makeText(context, "입력창 하나만 입력되어 있어야 합니다.", Toast.LENGTH_SHORT).show()
                }


            }

            urlbtn.setOnClickListener {
                val intent =  Intent(Intent.ACTION_VIEW, Uri.parse(url)) //최근 검색 기록으로 이동
                view.context.startActivity(intent)
            }

            updatebtn.setOnClickListener { // 업데이트 혹은 단어 추가
                val word = searchword.text.toString().trim()
                val meaning = searchmeaning.text.toString().trim()
                if(word.isNotBlank() && meaning.isNotEmpty()) { // 공백만 남긴 경우 다른 특수문자는 상관없지만 공백의 경우에는 구분이 어렵기 때문에 앞뒤 공백은 모두 잘라줍니다.
                    if(mydbhelper.checkoverlap(word.trim())){
                        if(mydbhelper.insertword(word,meaning))
                        {Toast.makeText(context, "단어 추가 성공!", Toast.LENGTH_SHORT).show()
                            cleartext()
                        }
                        else
                            Toast.makeText(context, "단어 추가 실패", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        if (mydbhelper.upadate(word, meaning))
                            Toast.makeText(context, "단어 수정 실패", Toast.LENGTH_SHORT).show()
                        else{
                            Toast.makeText(context, "단어 수정 성공!", Toast.LENGTH_SHORT).show()
                            cleartext()
                        }
                    }
                }

                else{
                    Toast.makeText(context, "공백을 제외한 추가하거나 수정할 단어와 뜻을 모두 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    fun parseword(){
        scope.launch {
            var temp = ""
            url = "https://dic.daum.net/search.do?q=" + binding.searchword.text.toString()
            val doc = Jsoup.connect(url).get()
            val check = doc.select(".cleanword_type > .search_cleanword > .tit_cleansch > .txt_cleansch >span").first() // 검색결과가 올바른가
            if(check == null){
                withContext(Dispatchers.Main){
                    val intent =  Intent(Intent.ACTION_VIEW, Uri.parse(url)) //최근 검색 기록으로 이동
                    view!!.context!!.startActivity(intent)
                }
            }
            else{
                val result = doc.select(".cleanword_type > .list_search > li > .txt_search") // 검색결과 단어들 파싱
                val meanings = result.text().split(" ",",")
                for(meaning in meanings){
                    temp += meaning.toString() + " "
                }
                withContext(Dispatchers.Main){ // 잠시 메인스레드에 넘겨줄거 넘겨준다.
                    mean = temp // 검색결과 설정
                    binding.searchmeaning.setText(mean)
                }
            }
        }

    }

    fun parsemeaning(){
        scope.launch {
            var temp = ""
            url = "https://dic.daum.net/search.do?q=" + binding.searchmeaning.text.toString()   + "&dic=eng"
            val doc = Jsoup.connect(url).get()
            val check = doc.select(".cleanword_type > .search_cleanword > .tit_cleansch > .txt_cleansch >span").first()

            if(check == null){
                withContext(Dispatchers.Main){
                    val intent =  Intent(Intent.ACTION_VIEW, Uri.parse(url)) //최근 검색 기록으로 이동
                    view!!.context!!.startActivity(intent)
                }
            }
            else{
                Log.d("asdf", " 1")
                val result = doc.select( ".cleanword_type > .list_search > li > .txt_search") // 검색결과의 의미 파싱
                val meanings = result.text().split(" ",",")
                for(meaning in meanings){
                    temp += meaning.toString() + " "
                }
                withContext(Dispatchers.Main){ // 잠시 메인스레드에 넘겨줄거 넘겨준다.
                    mean = temp // 검색결과 설정
                    binding.searchword.setText(mean)
                }
            }

        }
    }


    fun cleartext(){
        binding.apply {
            searchmeaning.text.clear()
            searchword.text.clear()
        }
    }
}